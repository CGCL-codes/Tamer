package net.sf.ts2.pm.artifact.manager;

/**
 * @author chalumeau
 */
public class ArtifactManagerException extends Exception {

    /**
     * 
     */
    public ArtifactManagerException() {
        super();
    }

    /**
     * @param message
     */
    public ArtifactManagerException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public ArtifactManagerException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause
     */
    public ArtifactManagerException(Throwable cause) {
        super(cause);
    }
}
