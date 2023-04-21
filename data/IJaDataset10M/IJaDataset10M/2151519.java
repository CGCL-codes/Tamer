package com.versusoft.packages.ooo.acronymabbr;

/**
 *
 * @author Vincent Spiewak
 */
public class Acronym {

    private String fullForm = null;

    private boolean pronounce = false;

    public Acronym(String fullForm, boolean pronounce) {
        this.fullForm = fullForm;
        this.pronounce = pronounce;
    }

    /**
     * @return the fullForm
     */
    public String getFullForm() {
        return fullForm;
    }

    /**
     * @return the pronounce
     */
    public boolean isPronounce() {
        return pronounce;
    }

    @Override
    public String toString() {
        if (pronounce) {
            return fullForm + " [*]";
        }
        return fullForm;
    }
}
