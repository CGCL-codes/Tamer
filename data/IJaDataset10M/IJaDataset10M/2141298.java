package org.vrforcad.lib.network.beans;

import java.io.Serializable;

/**
 *Confirmation bean.
 * @version 1.0 
 * @author Daniel Cioi <dan.cioi@vrforcad.org>
 */
public class POConfirmation implements PassingObject, Serializable {

    private static final long serialVersionUID = 1L;

    private boolean successful;

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }
}
