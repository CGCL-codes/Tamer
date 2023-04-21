package net.sf.crispy.properties;

/**
 * Exception by loading the property file.
 * 
 * @author Linke
 *
 */
public class PropertiesLoadException extends RuntimeException {

    private static final long serialVersionUID = 1313877982424L;

    public PropertiesLoadException(String pvMsg, Throwable pvThrowable) {
        super(pvMsg, pvThrowable);
    }
}
