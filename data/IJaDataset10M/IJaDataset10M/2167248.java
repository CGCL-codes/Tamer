package org.commCodypowered.spel.spel.voorwerp.element;

import java.util.ArrayList;
import org.commCodypowered.spel.spel.exceptions.VoorwerpValidationException;

/**
 * Write a description of class Grondstoffen here.
 * 
 * @author  Joris Goovaerts
 * @version 0.1
 */
public class Grondstof extends Goed implements Cloneable {

    private String grondstofNaam;

    private short moeilijkheidsgraad;

    private boolean isBovengronds;

    private boolean isOndergronds;

    private int restPercentage;

    public Grondstof() {
        super();
        this.grondstofNaam = "generic";
        this.moeilijkheidsgraad = 50;
        this.isBovengronds = true;
        this.isOndergronds = true;
        this.restPercentage = 100;
    }

    public Grondstof(String grondstofNaam) {
        super(grondstofNaam);
        this.grondstofNaam = grondstofNaam;
        this.moeilijkheidsgraad = 50;
        this.isBovengronds = true;
        this.isOndergronds = true;
        this.restPercentage = 100;
    }

    public Grondstof(String grondstofNaam, Goed goed, short moeilijkheidsgraad, boolean isBovengronds, boolean isOndergronds) {
        super(goed);
        this.grondstofNaam = grondstofNaam;
        this.moeilijkheidsgraad = moeilijkheidsgraad;
        this.isBovengronds = isBovengronds;
        this.isOndergronds = isOndergronds;
        this.restPercentage = 100;
    }

    @Override
    public void validate(ArrayList<Persoon> werkers) throws VoorwerpValidationException, Exception {
        for (Persoon persoon : werkers) inning(persoon.getKracht());
    }

    public int inning(int kracht) {
        int aanwezig = getHoeveelheid();
        if (aanwezig == 0) return 0;
        int oorsprong = (int) ((double) aanwezig / this.restPercentage * 100);
        int geind = (int) (aanwezig * ((double) kracht / this.moeilijkheidsgraad));
        if (!neemWeg(geind)) {
            this.restPercentage = 0;
            neemWeg(aanwezig);
            geind = aanwezig;
        } else this.restPercentage = (short) ((double) getHoeveelheid() / oorsprong * 100);
        return geind;
    }

    @Override
    public Object clone() {
        Grondstof nieuw = new Grondstof(new String(this.getNaam()), new Goed((Goed) super.clone()), this.moeilijkheidsgraad, this.isBovengronds, this.isOndergronds);
        nieuw.restPercentage = this.restPercentage;
        return nieuw;
    }

    @Override
    public String toString() {
        return "Grondstof:\nNaam: " + this.grondstofNaam + "\nMoeilijkheidsgraad: " + this.moeilijkheidsgraad + "\nBovengronds? " + this.isBovengronds + "\nOndergronds? " + this.isOndergronds + "\nRestpercentage: " + this.restPercentage;
    }
}
