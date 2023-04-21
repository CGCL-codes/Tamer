package org.jcompany.commons;

import org.apache.log4j.Logger;

/**
 * Exce�ao disparada em servi�os de AOP.
 * @since jCompany 3.0
 */
public class PlcAopException extends PlcException {

    private static final long serialVersionUID = 721466570248697971L;

    /**
	 * 
	 * @since jCompany 3.0
	 */
    public PlcAopException(String messageKeyLoc, Object[] messageArgsLoc, Throwable cause, Logger logCause) {
        super(messageKeyLoc, messageArgsLoc, cause, logCause);
    }
}
