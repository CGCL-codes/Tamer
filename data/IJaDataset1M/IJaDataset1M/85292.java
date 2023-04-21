package fr.gouv.defense.terre.esat.formathlon.vue.uc006;

import fr.gouv.defense.terre.esat.formathlon.entity.Formation;
import fr.gouv.defense.terre.esat.formathlon.entity.GroupeEnum;
import fr.gouv.defense.terre.esat.formathlon.entity.Utilisateur;
import fr.gouv.defense.terre.esat.formathlon.metier.uc006.UC006Metier;
import fr.gouv.defense.terre.esat.formathlon.vue.utils.JsfUtils;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 * Bean managé par JSF servant à ...
 * @author aRemplacer.
 */
@ManagedBean
@ViewScoped
public class AssocierFormateurAvanceAFormationsBean implements Serializable {

    @EJB
    private UC006Metier uC006Metier;

    private List<Utilisateur> lstUtilisateurs;

    private String utilisateur;

    private List<Formation> lstFormationsNonDirige;

    private List<Formation> lstFormationsDirige;

    /**
     * Constructeur par défaut.
     */
    public AssocierFormateurAvanceAFormationsBean() {
    }

    public String ajouter(Formation formaAssociee) {
        try {
            uC006Metier.ajouterFormationAFormateurAvance(utilisateur, formaAssociee);
            selectionFormateurAvance();
        } catch (Exception e) {
            JsfUtils.afficherException(e);
        }
        return null;
    }

    public String retirer(Formation formaAssociee) {
        try {
            uC006Metier.retirerFormationAFormateurAvance(utilisateur, formaAssociee);
            selectionFormateurAvance();
        } catch (Exception e) {
            JsfUtils.afficherException(e);
        }
        return null;
    }

    public String selectionFormateurAvance() {
        if (utilisateur != null) {
            lstFormationsDirige = uC006Metier.listerFormationDirigeParFormateurAvance(utilisateur);
            lstFormationsNonDirige = uC006Metier.listerFormationNonDirigeParFormateurAvance(utilisateur);
        } else {
            init();
        }
        return null;
    }

    @PostConstruct
    public void init() {
        lstUtilisateurs = uC006Metier.lstUtilisateurs(GroupeEnum.FORMATEUR_AVANCE);
        utilisateur = null;
        lstFormationsDirige = null;
        lstFormationsNonDirige = null;
    }

    public String associer() {
        try {
            JsfUtils.afficherMessage(FacesMessage.SEVERITY_INFO, "uc_006_AssocFormateurFormations_MiseAJourOk");
            init();
        } catch (Exception e) {
            JsfUtils.afficherException(e);
        }
        return null;
    }

    public List<Utilisateur> getLstUtilisateurs() {
        return lstUtilisateurs;
    }

    public void setLstUtilisateurs(List<Utilisateur> lstUtilisateurs) {
        this.lstUtilisateurs = lstUtilisateurs;
    }

    public String getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(String utilisateur) {
        this.utilisateur = utilisateur;
    }

    public List<Formation> getLstFormationsDirige() {
        return lstFormationsDirige;
    }

    public void setLstFormationsDirige(List<Formation> lstFormationsDirige) {
        this.lstFormationsDirige = lstFormationsDirige;
    }

    public List<Formation> getLstFormationsNonDirige() {
        return lstFormationsNonDirige;
    }

    public void setLstFormationsNonDirige(List<Formation> lstFormationsNonDirige) {
        this.lstFormationsNonDirige = lstFormationsNonDirige;
    }
}
