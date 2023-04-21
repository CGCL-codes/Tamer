package com.testonica.kickelhahn.core.formats.svf;

/**
 * Default logger implementation of logging mechanism.
 * Throws <code>SVFFormatException</code> in case
 * of error or warning.
 * 
 * @author Sergei Devadze
 */
class DefaultLogger implements SVFLogger {

    /** 
     * Throws <code>SVFFormatException</code> 
     * with given message */
    public void error(String s) throws SVFFormatException {
        throw new SVFFormatException(s);
    }

    /** 
     * Throws <code>SVFFormatException</code> 
     * with given message */
    public void warning(String s) throws SVFFormatException {
        throw new SVFFormatException(s);
    }
}
