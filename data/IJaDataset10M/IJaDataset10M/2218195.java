package wrm.saferJava.oval.exception;

/**
 * @author Sebastian Thomschke
 * @version $Revision: 1.0 $
 */
public class InvalidConfigurationException extends OValException {

    private static final long serialVersionUID = 1L;

    public InvalidConfigurationException(final String message) {
        super(message);
    }

    public InvalidConfigurationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
