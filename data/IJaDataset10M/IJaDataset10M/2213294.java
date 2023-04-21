package de.searchworkorange.lib.acl;

/**
 *
 * @author Sascha Kriegesmann kriegesmann at vaxnet.de
 */
public class UnableToRemoveDirectoryFromAclDirectoryStorageException extends Exception {

    /**
     * 
     * @param cause
     */
    public UnableToRemoveDirectoryFromAclDirectoryStorageException(Throwable cause) {
        super(cause);
    }

    /**
     *
     * @param message
     * @param cause
     */
    public UnableToRemoveDirectoryFromAclDirectoryStorageException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 
     * @param message
     */
    public UnableToRemoveDirectoryFromAclDirectoryStorageException(String message) {
        super(message);
    }

    public UnableToRemoveDirectoryFromAclDirectoryStorageException() {
    }
}
