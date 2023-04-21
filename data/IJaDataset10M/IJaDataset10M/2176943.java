package org.rob.confjsflistener.core.exception;

import java.text.MessageFormat;

/**
 * @author Roberto
 *
 */
public class XMLConfigurationException extends LoadConfigurationException {

    private static final String DEFAULT_MESSAGE = "An error happened loading configuration from file ''{0}''";

    /**
	 * XML file where the error happened
	 */
    private String XMLFile = null;

    /**
	 * 
	 * @param XMLFile
	 */
    public XMLConfigurationException(String XMLFile) {
        super(MessageFormat.format(DEFAULT_MESSAGE, XMLFile));
        this.XMLFile = XMLFile;
    }

    /**
	 * 
	 * @param XMLFile
	 * @param message
	 * @param cause
	 */
    protected XMLConfigurationException(String XMLFile, String message, Throwable cause) {
        super(message, cause);
        this.XMLFile = XMLFile;
    }

    /**
	 * 
	 * @param XMLFile
	 * @param message
	 */
    protected XMLConfigurationException(String XMLFile, String message) {
        super(message);
        this.XMLFile = XMLFile;
    }

    /**
	 * 
	 * @param XMLFile
	 * @param cause
	 */
    public XMLConfigurationException(String XMLFile, Throwable cause) {
        super(MessageFormat.format(DEFAULT_MESSAGE, XMLFile), cause);
        this.XMLFile = XMLFile;
    }

    /**
	 * @return the xMLFile
	 */
    public String getXMLFile() {
        return this.XMLFile;
    }
}
