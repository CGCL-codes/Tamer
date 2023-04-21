package com.bpd.error.handlers;

/**
 *
 * @author u19730
 */
public class ErrorSavingBeneficiario extends Exception {

    @Override
    public String getMessage() {
        return "_ERROR_INESPERADO_";
    }
}
