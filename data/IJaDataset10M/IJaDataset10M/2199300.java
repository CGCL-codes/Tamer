package org.tranche.exceptions;

import java.io.IOException;

/**
 * <p>Special IOException that represents an invalid signature.</p>
 * @author Jayson Falkner - jfalkner@umich.edu
 */
public class CantVerifySignatureException extends IOException {

    /**
     * 
     * @param msg
     */
    public CantVerifySignatureException(String msg) {
        super(msg);
    }
}
