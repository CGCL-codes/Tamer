package fr.aston.gestionconges.metiers;

import java.util.Date;

public class DemandeConges {

    private int idDemande;

    private Date dateDebut;

    private Date dateFin;

    private String typeConge;

    private String etat;

    public DemandeConges() {
    }

    public DemandeConges(int idDemande) {
        this.idDemande = idDemande;
    }

    public DemandeConges(int idDemande, Date dateDebut, Date dateFin, String typeConge, String etat) {
        this.idDemande = idDemande;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.typeConge = typeConge;
        this.etat = etat;
    }

    public int getIdDemande() {
        return idDemande;
    }

    public void setIdDemande(int idDemande) {
        this.idDemande = idDemande;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public String getTypeConge() {
        return typeConge;
    }

    public void setTypeConge(String typeConge) {
        this.typeConge = typeConge;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String toString() {
        String affichageAttributsDemandeConge = "Le id demande : " + idDemande + "\n" + "La dateDebut : " + dateDebut + "\n" + "La date Fin : " + dateFin + "\n" + "Le type de Conge : " + typeConge + "\n" + "L'�tat : " + etat;
        return affichageAttributsDemandeConge;
    }
}
