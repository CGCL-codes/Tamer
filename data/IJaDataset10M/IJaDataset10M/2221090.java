package com.cubusmail.mail.exceptions;

import com.cubusmail.mail.IMailFolder;

/**
 * Server side execptions for mail folder operations.
 * 
 * @author Juergen Schlierf
 */
public class MailFolderException extends Exception {

    private static final long serialVersionUID = 1254290207185224330L;

    private IMailFolder folder;

    private String errorCode;

    /**
	 * @param message
	 * @param cause
	 */
    public MailFolderException(String errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    /**
	 * @param message
	 * @param cause
	 * @param folder
	 */
    public MailFolderException(String errorCode, Throwable cause, IMailFolder folder) {
        super(cause);
        this.errorCode = errorCode;
        this.folder = folder;
    }

    public IMailFolder getFolder() {
        return folder;
    }

    public void setFolder(IMailFolder folder) {
        this.folder = folder;
    }

    /**
	 * @return Returns the errorCode.
	 */
    public String getErrorCode() {
        return this.errorCode;
    }

    /**
	 * @param errorCode The errorCode to set.
	 */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public boolean hasErrorCode(String errorCode) {
        return errorCode.equals(this.errorCode);
    }
}
