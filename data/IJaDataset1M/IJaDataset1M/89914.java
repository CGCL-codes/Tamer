package fr.gouv.defense.terre.esat.formathlon.vue.uc006;

import fr.gouv.defense.terre.esat.formathlon.entity.CelluleCours;
import fr.gouv.defense.terre.esat.formathlon.entity.Division;
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
public class ListerCoursBean implements Serializable {

    @EJB
    private UC006Metier uC006Metier;

    private List<CelluleCours> lstCours;

    private List<Division> lstDivisions;

    /**
     * Constructeur par défaut.
     */
    public ListerCoursBean() {
    }

    @PostConstruct
    public void init() {
        lstCours = uC006Metier.lstCellulesCours();
        lstDivisions = uC006Metier.lstDivisions();
    }

    public String supprimer(CelluleCours cours) {
        try {
            uC006Metier.supprimerCours(cours);
            JsfUtils.afficherMessage(FacesMessage.SEVERITY_INFO, "uc_006_modifierCours_SuppressionOk");
        } catch (Exception e) {
            JsfUtils.afficherException(e);
        }
        init();
        return null;
    }

    public String modifier(CelluleCours cours) {
        try {
            uC006Metier.modifierCours(cours);
            JsfUtils.afficherMessage(FacesMessage.SEVERITY_INFO, "uc_006_modifierCours_ModifOk");
        } catch (Exception e) {
            JsfUtils.afficherException(e);
        }
        return null;
    }

    public List<Division> getLstDivisions() {
        return lstDivisions;
    }

    public void setLstDivisions(List<Division> lstDivisions) {
        this.lstDivisions = lstDivisions;
    }

    public List<CelluleCours> getLstCours() {
        return lstCours;
    }

    public void setLstCours(List<CelluleCours> lstCours) {
        this.lstCours = lstCours;
    }

    public UC006Metier getuC006Metier() {
        return uC006Metier;
    }

    public void setuC006Metier(UC006Metier uC006Metier) {
        this.uC006Metier = uC006Metier;
    }
}
