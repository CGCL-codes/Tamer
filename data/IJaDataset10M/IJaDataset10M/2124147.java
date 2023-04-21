package metier.event.sort;

public abstract class Sort {

    private String nom;

    private int coutMana;

    public Sort(String nom, int coutMana) {
        this.nom = nom;
        this.coutMana = coutMana;
    }

    public String getNom() {
        return nom;
    }

    public int getCoutMana() {
        return coutMana;
    }

    public String toString() {
        return nom + " Mana : " + coutMana;
    }
}
