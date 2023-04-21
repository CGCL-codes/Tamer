package src;

public class Route {

    private String id, nom;

    private int vitesse;

    public Route(String id, String nom, int vitesse) {
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

    public int getVitesse() {
        return vitesse;
    }

    public void setVitesse(int vit) {
        vitesse = vit;
    }

    public String toString() {
        return id + ", " + nom + ", " + vitesse + "\n";
    }
}
