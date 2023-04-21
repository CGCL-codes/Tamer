package common.fourmi;

import java.util.ArrayList;
import java.util.List;
import common.planete.base.Case;
import common.planete.fractal.CaseFractal;

public class CaseFourmi extends CaseFractal {

    List<Pheromone> pheromones;

    int reserveNourriture;

    Fourmi fourmi = null;

    /** Creates a new instance of Case */
    public CaseFourmi(float x, float y, float z, int id) {
        super(x, y, z, id);
        pheromones = new ArrayList<Pheromone>();
    }

    public void tour() {
        List<Pheromone> aEvacuer = new ArrayList<Pheromone>();
        for (Pheromone ph : pheromones) {
            ph.tour();
            if (ph.getPuissance() == 0) aEvacuer.add(ph);
        }
        pheromones.removeAll(aEvacuer);
    }

    public boolean isPraticable() {
        return getAltitude() >= 0;
    }

    public Fourmi getFourmi() {
        return fourmi;
    }

    public void setFourmi(Fourmi fourmi) {
        this.fourmi = fourmi;
    }

    public List<Pheromone> getPheromones() {
        return pheromones;
    }

    public Pheromone getPheromone(Fourmi fourmi, PheromoneType type) {
        for (Pheromone ph : pheromones) {
            if (fourmi.idGroupe == ph.getFourmi().idGroupe && type.equals(ph.getType())) return ph;
        }
        return null;
    }

    public Pheromone getPheromone(int idGroupe, PheromoneType type) {
        for (Pheromone ph : pheromones) {
            if (idGroupe == ph.getFourmi().idGroupe && type.equals(ph.getType())) return ph;
        }
        return null;
    }

    public void addPheromone(Fourmi fourmi, PheromoneType type, int puissance) {
        Pheromone ph = getPheromone(fourmi.idGroupe, type);
        if (ph != null) {
            if (fourmi.getRebours(type) > ph.getPuissance()) ph.setPuissance(fourmi.getRebours(type));
        } else pheromones.add(new Pheromone(fourmi, fourmi.getRebours(type), type));
        for (Case c : this.adj) {
            ph = ((CaseFourmi) c).getPheromone(fourmi.idGroupe, type);
            if (ph != null) {
                if (fourmi.getRebours(type) - 2 > ph.getPuissance()) ph.setPuissance(fourmi.getRebours(type) - 2);
            } else ((CaseFourmi) c).pheromones.add(new Pheromone(fourmi, fourmi.getRebours(type) - 2, type));
        }
    }

    public int prelevement(int prelevement) {
        if (prelevement > reserveNourriture) {
            int tmp = reserveNourriture;
            reserveNourriture = 0;
            return tmp;
        } else {
            reserveNourriture -= prelevement;
            return prelevement;
        }
    }

    public int getReserveNourriture() {
        return reserveNourriture;
    }

    public void setReserveNourriture(int reserveNourriture) {
        this.reserveNourriture = reserveNourriture;
    }
}
