package model;

import java.util.*;

public class Entrepreneur extends Compte {

    private ArrayList<Restaurant> contrats = new ArrayList<Restaurant>();

    public ArrayList<Restaurant> getContrats() {
        return contrats;
    }

    public void setContrats(ArrayList<Restaurant> contrats) {
        this.contrats = contrats;
    }

    public Entrepreneur() {
        super();
    }

    public Entrepreneur(Compte c) {
        super(c.getPrenom(), c.getNom(), c.getCoordonee(), c.getUsername(), c.getPassword());
    }
}
