package com.akivasoftware.comp.model;

import com.akivasoftware.comp.session.*;
import com.akivasoftware.comp.model.*;

/**
 * EzymTerm - Encapsulates an enzyme or 'catalyst' term
 */
public class EzymTerm extends KineticComponent implements StateChangedListener {

    /** Constructor - no arg */
    public EzymTerm() {
        super();
        this.doRegister();
        return;
    }

    /**
     * doRegister() - 
     */
    public void doRegister() {
        try {
            ((State) ADFOrb.getInstance().lookup("_STATE")).register(this);
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    /**
     * updateState() - 
     */
    public void updateState(StateChangedEvent evt) {
        try {
            debug("Update received in " + this);
            m_arrState = ((State) evt.getSource()).getState();
        } catch (Exception error) {
            error.printStackTrace();
        }
    }
}
