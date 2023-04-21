package fr.kb.frais.modele.date;

import fr.kb.frais.vue.util.Constant;

public class MoisAnnee {

    private int mois;

    private String name;

    public MoisAnnee() {
    }

    public MoisAnnee(int mois) {
        this.mois = mois;
        this.name = Constant.getMonth(mois);
    }

    public String toString() {
        return name;
    }

    public int getMois() {
        return mois;
    }

    public void setMois(int mois) {
        this.mois = mois;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
