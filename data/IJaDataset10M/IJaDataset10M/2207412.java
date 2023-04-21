package org.nomicron.suber.validation;

/**
 * The base class for validations.
 */
public abstract class BaseValidation implements Validation {

    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
