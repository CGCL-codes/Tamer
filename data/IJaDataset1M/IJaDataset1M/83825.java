package br.gov.frameworkdemoiselle.internal.security;

import br.gov.frameworkdemoiselle.exception.DemoiselleException;

public class SecurityException extends DemoiselleException {

    private static final long serialVersionUID = 1L;

    public SecurityException(String message) {
        super(message);
    }
}
