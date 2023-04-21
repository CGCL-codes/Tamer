package com.ontotext.ordi.sar;

/**
 * Used in errors caused by missing/ambiguous or in some other way invalid data.
 * 
 * @author atanas
 *
 */
public class SARDataIntegrityException extends SARException {

    static final long serialVersionUID = 1L;

    public SARDataIntegrityException(String msg) {
        super(msg);
    }

    public SARDataIntegrityException(String msg, Throwable t) {
        super(msg, t);
    }

    public SARDataIntegrityException(Throwable t) {
        super(t);
    }
}
