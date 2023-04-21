package es.gavab.jmh.instancefile;

public class InstancesRepositoryException extends RuntimeException {

    public InstancesRepositoryException() {
    }

    public InstancesRepositoryException(String message) {
        super(message);
    }

    public InstancesRepositoryException(Throwable cause) {
        super(cause);
    }

    public InstancesRepositoryException(String message, Throwable cause) {
        super(message);
        this.initCause(cause);
    }
}
