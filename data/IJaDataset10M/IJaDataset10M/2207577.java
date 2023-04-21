package com.xentelco.asterisk.agi.services;

import com.xentelco.asterisk.agi.AgiException;

/**
 * @author Ussama Baggili
 * 
 */
public interface ContextService {

    /**
     * Sets the context for continuation upon exiting the application.
     * 
     * @param context
     *            the context for continuation upon exiting the application.
     */
    void setContext(String context) throws AgiException;
}
