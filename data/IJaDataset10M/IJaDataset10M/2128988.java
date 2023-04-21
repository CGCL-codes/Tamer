package com.google.code.cana.service;

import com.google.code.cana.CanaException;

/**
 * @author Taciano Pinheiro
 *
 */
public class AtributoNaoEditavelException extends CanaException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public AtributoNaoEditavelException() {
    }

    /**
	 * @param arg0
	 */
    public AtributoNaoEditavelException(String arg0) {
        super(arg0);
    }

    /**
	 * @param arg0
	 */
    public AtributoNaoEditavelException(Throwable arg0) {
        super(arg0);
    }

    /**
	 * @param arg0
	 * @param arg1
	 */
    public AtributoNaoEditavelException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }
}
