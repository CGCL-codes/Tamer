package org.commCodypowered.spel.spel.voorwerp.element;

import java.util.ArrayList;
import org.commCodypowered.spel.spel.exceptions.VoorwerpValidationException;
import org.commCodypowered.spel.spel.voorwerp.Voorwerp;

public abstract class Element extends Voorwerp implements Cloneable {

    private String naam;

    protected Element() {
        this.naam = "generic";
    }

    protected Element(String naam) {
        this.naam = new String(naam);
    }

    public String getNaam() {
        return this.naam;
    }

    protected void setNaam(String naam) {
        this.naam = naam;
    }

    @Override
    public abstract Object clone() throws CloneNotSupportedException;

    @Override
    public abstract void validate(ArrayList<Persoon> werkers) throws VoorwerpValidationException, Exception;

    /**
	 * Is dit voorwerp gelijk aan een ander?
	 * 
	 * @param voorwerp Waaraan het gelijk moet zijn.
	 * 
	 * @return Is dit zo?
	 */
    @Override
    public boolean equals(Object obj) {
        return ((Element) obj).naam.equals(this.naam);
    }

    @Override
    public String toString() {
        return this.naam;
    }
}
