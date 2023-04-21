package fr.cpbrennestt.presentation.display.frontal.calculateur;

import java.util.List;

public class DbJoueurResultat {

    private String nom = "";

    private String prenom = "";

    private String classement = "";

    private int points;

    private String pointsTotal;

    private List<DbAdversaireResultat> adversaires;

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getClassement() {
        return classement;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setClassement(String classement) {
        this.classement = classement;
    }

    public List<DbAdversaireResultat> getAdversaires() {
        return adversaires;
    }

    public void setAdversaires(List<DbAdversaireResultat> adversaires) {
        this.adversaires = adversaires;
    }

    public String getPointsTotal() {
        return pointsTotal;
    }

    public void setPointsTotal(String total) {
        this.pointsTotal = total;
    }
}
