package com.example.gestionvaccinationtestfirst.service.UserService;

import com.example.gestionvaccinationtestfirst.DTos.CentreVaccinationDTO;
import com.example.gestionvaccinationtestfirst.DTos.UtilisateurDTO;
import com.example.gestionvaccinationtestfirst.Excepyion.CentreNotFoundException;
import com.example.gestionvaccinationtestfirst.Excepyion.UtilisateurNotFoundException;
import com.example.gestionvaccinationtestfirst.dtoMapper.Utilisateur.UtilisateurMapper;
import com.example.gestionvaccinationtestfirst.dtoMapper.centreMapper.CentreVaccMapper;
import com.example.gestionvaccinationtestfirst.model.CentreVaccination;
import com.example.gestionvaccinationtestfirst.model.Utilisateur;
import com.example.gestionvaccinationtestfirst.repository.CentreVaccinationRepository;
import com.example.gestionvaccinationtestfirst.repository.UtilisateurRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor

public class UtilisateurServiceImpl implements UtilisateurService{
    private CentreVaccMapper centreVaccMapper;

    private UtilisateurRepository utilisateurRepository;

    private CentreVaccinationRepository centreVaccinationRepository;

    private  UtilisateurMapper utilisateurMapper;
    @Override
    public List<UtilisateurDTO> getAllUsers(){
        List<Utilisateur> listUtilisateurs=utilisateurRepository.findAll();
        List<UtilisateurDTO> usersDtos=listUtilisateurs.stream().
                map(user->utilisateurMapper.fromUtilisateur(user)).toList();
        return usersDtos;
    }

    @Override
    public UtilisateurDTO getUserBYId(Long userId) throws UtilisateurNotFoundException {
       Utilisateur user= utilisateurRepository.findById(userId)
               .orElseThrow(()->new UtilisateurNotFoundException("Utilisateur not found"));
        return utilisateurMapper.fromUtilisateur(user);
    }

    @Override
    public void deleteUser(Long userId) {
        log.info("Supprimer un utilisateur");
        utilisateurRepository.deleteById(userId);


    }

    @Override
    public UtilisateurDTO saveUser(UtilisateurDTO utilisateurDTO,Long centreId) throws CentreNotFoundException{
      CentreVaccination centre=centreVaccinationRepository.findById(centreId)
              .orElseThrow(()->new CentreNotFoundException("Centre Introuvable"));

      CentreVaccinationDTO centreVaccinationDTO=centreVaccMapper.fromCentreVaccination(centre);


         utilisateurDTO.setCentreVaccinationDTO(centreVaccinationDTO);

        //centreVaccinationDTO.getUtilisateurDTOListDTO().add(utilisateurDTO);

      Utilisateur user=utilisateurMapper.fromUtilisateurDTO(utilisateurDTO);

      centre.getUtilisateurs().add(user);

      Utilisateur user1=utilisateurRepository.save(user);


      return utilisateurMapper.fromUtilisateur(user1);

    }

    @Override
    public UtilisateurDTO updateUser(UtilisateurDTO utilisateurDTO){
        log.info("Modifier utilisateur");

        Utilisateur utilisateur=utilisateurMapper.fromUtilisateurDTO(utilisateurDTO);
        Utilisateur updateUser=utilisateurRepository.save(utilisateur);

        return utilisateurMapper.fromUtilisateur(updateUser);
    }

    public List<UtilisateurDTO> getUserSearch(String email,String telephone){
        List<Utilisateur> utilisateurList=utilisateurRepository.findByTelephoneIgnoreCaseAndEmailIgnoreCase(telephone,email);
        List<UtilisateurDTO> utilisateurDTOS=utilisateurList.stream().map(user->utilisateurMapper.fromUtilisateur(user)).collect(Collectors.toList());

        return utilisateurDTOS;
    }


}
