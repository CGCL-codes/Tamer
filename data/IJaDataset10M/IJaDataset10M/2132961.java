package alice.cartago;

/**
 * Exception raised when trying to create an artifact whose template is not known
 * @author aricci
 *
 */
public class UnknownArtifactTemplateException extends CartagoException {

    public UnknownArtifactTemplateException() {
    }

    public UnknownArtifactTemplateException(String arg) {
        super(arg);
    }
}
