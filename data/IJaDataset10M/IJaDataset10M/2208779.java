package com.sri.emo.wizard;

public interface WizardFactorySPI {

    /**
     * To be set by the factory.
     *
     * @param newId int
     */
    public void setWizardId(int newId);
}
