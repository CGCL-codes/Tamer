package com.ldap.obi;

/**
 * File : ObiDataException.java 
 * Component : Version : 1.0 
 * Creation date : 2010-03-09 
 * Modification date : 2010-03-09
 */
public class ObiDataException extends Exception {

    private static final long serialVersionUID = 5609473694658991590L;

    /**
	 * Exception qui n'affiche aucun message.
	 */
    public ObiDataException() {
        super();
    }

    /**
	 * Exception qui affiche le message pass� en param�tre.
	 * 
	 * @param message
	 *            Message � afficher.
	 */
    public ObiDataException(String message) {
        super(message);
    }
}
