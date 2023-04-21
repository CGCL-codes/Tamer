package com.patientis.business.security;

/**
 * NullUserIdContextException is thrown when a BaseContext is accessed with a 0 user id
 *
 * <br/>Design Patterns: <a href="/functionality/rm/1000047.html">Exceptions</a>
 * <br/>  
 */
public class NullUserIdContextException extends RuntimeException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
}
