package com.germinus.xpression.cms.directory;

/**
 *
 * User: Eduardo
 * Date: 07-ago-2009
 * Time: 13:46:33
 *
 */
public class DuplicatedFolderNameException extends Exception {

    private static final long serialVersionUID = -8754778838574518462L;

    public DuplicatedFolderNameException() {
        super();
    }

    public DuplicatedFolderNameException(String message) {
        super(message);
    }

    public DuplicatedFolderNameException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicatedFolderNameException(Throwable cause) {
        super(cause);
    }
}
