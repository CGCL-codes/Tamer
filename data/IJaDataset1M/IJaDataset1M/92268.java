package unbbayes.prs.oobn.exception;

import java.util.ResourceBundle;

/**
 * @author Shou Matsumoto
 *
 */
public class OOBNException extends Exception {

    /** Load resource file from this package */
    private static ResourceBundle resource = unbbayes.util.ResourceController.newInstance().getBundle(unbbayes.prs.oobn.resources.Resources.class.getName());

    /**
	 * 
	 */
    public OOBNException() {
        super(resource.getString("OOBNExceptionMessage"));
    }

    /**
	 * @param message
	 */
    public OOBNException(String message) {
        super(message);
    }

    /**
	 * @param cause
	 */
    public OOBNException(Throwable cause) {
        super(resource.getString("OOBNExceptionMessage"), cause);
    }

    /**
	 * @param message
	 * @param cause
	 */
    public OOBNException(String message, Throwable cause) {
        super(message, cause);
    }
}
