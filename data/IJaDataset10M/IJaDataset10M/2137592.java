package g4mfs.impl.org.peertrust.exception;

/**
 * <p>
 * Exception raised if an error while configuring the system or a class.
 * </p><p>
 * $Id: ConfigurationException.java,v 1.1 2005/11/30 10:35:12 ionut_con Exp $
 * <br/>
 * Date: 05-Dec-2003
 * <br/>
 * Last changed: $Date: 2005/11/30 10:35:12 $
 * by $Author: ionut_con $
 * </p>
 * @author olmedilla 
 */
public class ConfigurationException extends PeertrustException {

    /**
	 * 
	 */
    public ConfigurationException() {
        super();
    }

    /**
	 * @param message
	 */
    public ConfigurationException(String message) {
        super(message);
    }

    /**
	 * @param arg0
	 */
    public ConfigurationException(Throwable arg0) {
        super(arg0);
    }

    /**
	 * @param arg0
	 * @param arg1
	 */
    public ConfigurationException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }
}
