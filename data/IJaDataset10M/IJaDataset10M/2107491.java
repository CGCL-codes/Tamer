package donnee;

import java.util.Date;
import fabrique.FabEmploye;
import fabrique.FabMission;

public class PlanificationMission {

    private int id;

    private Date dateDebut;

    private int nbJours;

    public PlanificationMission(int id, Date dateDebut, int nbJours) {
        this.id = id;
        this.dateDebut = dateDebut;
        this.nbJours = nbJours;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public Date getDateFinPlanificationMission() {
        return utilitaires.Utilitaires.getDatefin(this.dateDebut, this.nbJours);
    }

    public int getNbJours() {
        return nbJours;
    }

    public Mission getLaMission() {
        return FabMission.getInstance().rechercherMissiondelaPlanificationMission(id);
    }

    public Employe getLEmploye() {
        return FabEmploye.getInstance().rechercherlEmployedelaPlanificationMission(id);
    }
}
