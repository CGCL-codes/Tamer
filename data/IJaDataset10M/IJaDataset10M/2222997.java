package org.internna.ossmoney;

public class OSSMoneyException extends RuntimeException {

    private static final long serialVersionUID = -2890660824037119324L;

    public OSSMoneyException(Exception ex) {
        super(ex);
    }
}
