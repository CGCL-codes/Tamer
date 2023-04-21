package hu.sztaki.lpds.APP_SPEC.services.exceptions.FileManagerExceptions;

/**
*
* @author  balasko
*/
public class ReplicateException extends GeneralFileManagerException {

    public ReplicateException(Throwable cause) {
        super(cause);
    }

    public ReplicateException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReplicateException(String message) {
        super(message);
    }
}
