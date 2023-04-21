package src.TestCVS;

public class Route {

    private String id, nom;

    private String vitesse;

    public Route(String id, String nom, String vitesse) {
        this.id = id;
        this.nom = nom;
        this.vitesse = vitesse;
    }

    public String getID() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getVitesse() {
        return vitesse;
    }

    public String toString() {
        return id + ", " + nom + ", " + vitesse + "\n";
    }
}
