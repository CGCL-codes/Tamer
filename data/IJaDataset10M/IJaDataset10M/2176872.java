package org.josef.web.jsf.beans.test;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * Burger Service Nummer Bean.
 * @author Kees Schotanus
 * @version 1.0 $Revision: 2507 $
 */
@ManagedBean
@SessionScoped
public class BurgerServiceNummerBean implements Serializable {

    /**
     * Universal version identifier for this serializable class.
     */
    private static final long serialVersionUID = 5730929063885560373L;

    /**
     * The burger service nummer (Dutch social security number).
     */
    private String bsn;

    /**
     * Gets the burger service nummer.
     * @return The burger service nummer.
     */
    public String getBsn() {
        return bsn;
    }

    /**
     * Sets the burger service nummer.
     * @param bsn The burger service nummer to set.
     */
    public void setBsn(final String bsn) {
        this.bsn = bsn;
    }
}
