package unbbayes.io.exception;

/**
 * Exception class when loading a network.
 * @author Rommel N. Carvalho
 * @author Michael S. Onishi
 * @version 1.0
 */
public class SaveException extends Exception {

    /** Serialization runtime version number */
    private static final long serialVersionUID = 0;

    public SaveException(String a) {
        super(a);
    }
}
