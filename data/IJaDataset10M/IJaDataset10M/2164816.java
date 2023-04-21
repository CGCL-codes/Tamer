package com.alexmcchesney.poster.plugins.metaweblogapi;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Abstract base class for basic magic tree exceptions
 * @author AMCCHESNEY
 *
 */
public abstract class MetaWeblogAPIException extends Exception {

    /** Resource bundle containing localised exception strings */
    protected static final ResourceBundle m_resources = ResourceBundle.getBundle("com.alexmcchesney.poster.plugins.metaweblogapi.exceptions", Locale.getDefault());

    /**
	 * Constructor
	 * @param sMsg	Error message
	 * @param cause	Cause of the error
	 */
    public MetaWeblogAPIException(String sMsg, Throwable cause) {
        super(sMsg, cause);
    }

    /**
	 * Constructor
	 * @param sMsg	Error message
	 */
    public MetaWeblogAPIException(String sMsg) {
        super(sMsg);
    }
}
