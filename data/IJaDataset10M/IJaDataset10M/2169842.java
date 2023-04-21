package delphorm.web.validateur;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import delphorm.entite.personne.GroupeCollectif;

public class AjouterGroupeCollectifValidateur implements Validator {

    public boolean supports(Class classe) {
        return classe.isAssignableFrom(GroupeCollectif.class);
    }

    public void validate(Object obj, Errors errors) {
        GroupeCollectif groupe = (GroupeCollectif) obj;
        if (groupe.getCreerQuestionnaires() == null) {
            errors.rejectValue("creerQuestionnaires", "AjouterGroupeCollectif.obligatoire");
        }
        if (groupe.getAjouterUtilisateursTousGroupes() == null) {
            errors.rejectValue("ajouterUtilisateursTousGroupes", "AjouterGroupeCollectif.obligatoire");
        }
        if (groupe.getVoirTousUtilisateurs() == null) {
            errors.rejectValue("voirTousUtilisateurs", "AjouterGroupeCollectif.obligatoire");
        }
        if (groupe.getCreerGroupe() == null) {
            errors.rejectValue("creerGroupe", "AjouterGroupeCollectif.obligatoire");
        }
        if (groupe.getCreerToutesInstances() == null) {
            errors.rejectValue("creerToutesInstances", "AjouterGroupeCollectif.obligatoire");
        }
        if (groupe.getModifierTousGroupes() == null) {
            errors.rejectValue("modifierTousGroupes", "AjouterGroupeCollectif.obligatoire");
        }
        if (groupe.getModifierTousQuestionnaires() == null) {
            errors.rejectValue("modifierTousQuestionnaires", "AjouterGroupeCollectif.obligatoire");
        }
        if (groupe.getModifierTousUtilisateurs() == null) {
            errors.rejectValue("modifierTousUtilisateurs", "AjouterGroupeCollectif.obligatoire");
        }
        if (groupe.getModifierToutesInstances() == null) {
            errors.rejectValue("modifierToutesInstances", "AjouterGroupeCollectif.obligatoire");
        }
        if (groupe.getSupprimerTousGroupes() == null) {
            errors.rejectValue("supprimerTousGroupes", "AjouterGroupeCollectif.obligatoire");
        }
        if (groupe.getSupprimerTousQuestionnaires() == null) {
            errors.rejectValue("supprimerTousQuestionnaires", "AjouterGroupeCollectif.obligatoire");
        }
        if (groupe.getSupprimerTousUtilisateurs() == null) {
            errors.rejectValue("supprimerTousUtilisateurs", "AjouterGroupeCollectif.obligatoire");
        }
        if (groupe.getSupprimerToutesInstances() == null) {
            errors.rejectValue("supprimerToutesInstances", "AjouterGroupeCollectif.obligatoire");
        }
        if (groupe.getSupprimerUtilisateursTousGroupes() == null) {
            errors.rejectValue("supprimerUtilisateursTousGroupes", "AjouterGroupeCollectif.obligatoire");
        }
        if (groupe.getValiderToutesInstances() == null) {
            errors.rejectValue("validerToutesInstances", "AjouterGroupeCollectif.obligatoire");
        }
        if (groupe.getVoirTousQuestionnaires() == null) {
            errors.rejectValue("voirTousQuestionnaires", "AjouterGroupeCollectif.obligatoire");
        }
        if (groupe.getVoirToutesInstances() == null) {
            errors.rejectValue("voirToutesInstances", "AjouterGroupeCollectif.obligatoire");
        }
    }
}
