package com.sun.mail.util;

import java.io.IOException;
import javax.mail.Folder;

/**
 * A variant of FolderClosedException that can be thrown from methods
 * that only throw IOException.  The getContent method will catch this
 * exception and translate it back to FolderClosedException.
 *
 * @author Bill Shannon
 */
public class FolderClosedIOException extends IOException {

    private transient Folder folder;

    private static final long serialVersionUID = 4281122580365555735L;

    /**
     * Constructor
     * @param folder	the Folder
     */
    public FolderClosedIOException(Folder folder) {
        this(folder, null);
    }

    /**
     * Constructor
     * @param folder 	the Folder
     * @param message	the detailed error message
     */
    public FolderClosedIOException(Folder folder, String message) {
        super(message);
        this.folder = folder;
    }

    /**
     * Returns the dead Folder object
     */
    public Folder getFolder() {
        return folder;
    }
}
