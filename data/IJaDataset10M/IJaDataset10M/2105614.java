package org.xmlcml.cml.tools;

import org.xmlcml.cml.base.CMLElement.FormalChargeControl;

/**
 * manage counting and management of double bonding. manages the strategy for
 * adding multiple bonds and charges. still evolving
 * 
 * @author pmr
 * 
 */
public class PiSystemControls {

    boolean updateBonds;

    boolean guessUnpaired;

    boolean distributeCharge;

    int knownUnpaired;

    int knownCharge;

    FormalChargeControl fcd;

    /**
     * default manager. updateBonds = false; guessUnpaired = false;
     * knownUnpaired = 0; fcd = FormalChargeControl.DEFAULT;
     */
    public PiSystemControls() {
        updateBonds = false;
        guessUnpaired = false;
        knownUnpaired = 0;
        fcd = FormalChargeControl.DEFAULT;
        distributeCharge = false;
    }

    /**
     * copy constructor.
     * 
     * @param piSystemManager
     */
    public PiSystemControls(PiSystemControls piSystemManager) {
        updateBonds = piSystemManager.updateBonds;
        guessUnpaired = piSystemManager.guessUnpaired;
        knownUnpaired = piSystemManager.knownUnpaired;
        fcd = piSystemManager.fcd;
        distributeCharge = piSystemManager.distributeCharge;
    }

    /**
     * are we guessing the number of unpaired electrons?
     * 
     * @return true if so
     */
    public boolean isGuessUnpaired() {
        return guessUnpaired;
    }

    /**
     * set whether we guess the unpaired electrons.
     * 
     * @param guessUnpaired
     *            default false.
     */
    public void setGuessUnpaired(boolean guessUnpaired) {
        this.guessUnpaired = guessUnpaired;
    }

    /**
     * are we distributing charge over unpaired electrons?
     * 
     * @return true if so
     */
    public boolean isDistributeCharge() {
        return distributeCharge;
    }

    /**
     * set whether distributing charge over unpaired electrons?
     * 
     * @param distributeCharge
     *            default false.
     */
    public void setDistributeCharge(boolean distributeCharge) {
        this.distributeCharge = distributeCharge;
    }

    /**
     * @return Returns the knownUnpaired.
     */
    public int getKnownUnpaired() {
        return knownUnpaired;
    }

    /**
     * set number of known unpaired electrons.
     * 
     * @param knownUnpaired
     *            default 0.
     */
    public void setKnownUnpaired(int knownUnpaired) {
        this.knownUnpaired = knownUnpaired;
    }

    /**
     * get the knownCharge.
     * 
     * @return the knownCharge.
     */
    public int getKnownCharge() {
        return knownCharge;
    }

    /**
     * set known charge.
     * 
     * @param knownCharge
     *            default 0.
     */
    public void setKnownCharge(int knownCharge) {
        this.knownCharge = knownCharge;
    }

    /**
     * are bonds to be updated.
     * 
     * @return true if so
     */
    public boolean isUpdateBonds() {
        return updateBonds;
    }

    /**
     * use pi analysis to change unknown bond orders to multiple.
     * 
     * @param updateBonds
     *            if true change bonds
     */
    public void setUpdateBonds(boolean updateBonds) {
        this.updateBonds = updateBonds;
    }

    /**
     * sets formal charge control.
     * 
     * @param fcd
     */
    public void setFormalChargeControl(FormalChargeControl fcd) {
        this.fcd = fcd;
    }

    /**
     * get formal charge control.
     * 
     * @return fcd
     */
    public FormalChargeControl getFormalChargeControl() {
        return fcd;
    }
}
