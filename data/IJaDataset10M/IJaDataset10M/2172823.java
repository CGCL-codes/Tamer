package javax.webbeans.exceptions.deployment;

public class InconsistentSpecializationException extends DeploymentException {

    public InconsistentSpecializationException(String message) {
        super(message);
    }

    public InconsistentSpecializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
