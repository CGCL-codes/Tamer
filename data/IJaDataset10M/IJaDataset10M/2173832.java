package javax.webbeans.exceptions.deployment;

public class UnsatisfiedDependencyException extends DeploymentException {

    public UnsatisfiedDependencyException(String message) {
        super(message);
    }

    public UnsatisfiedDependencyException(String message, Throwable cause) {
        super(message, cause);
    }
}
