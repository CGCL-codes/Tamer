package com.hadeslee.audiotag.audio.exceptions;

import java.io.File;

/**
 * This exception is thrown if a
 * {@link entagged.audioformats.generic.AudioFileModificationListener} wants to
 * prevent the &quote;entagged audio library&quote; from actually finishing its
 * operation.<br>
 * This exception can be used in all methods but
 * {@link entagged.audioformats.generic.AudioFileModificationListener#fileOperationFinished(File)}.
 * 
 * @author Christian Laireiter
 */
public class ModifyVetoException extends Exception {

    /**
	 * (overridden)
	 */
    public ModifyVetoException() {
        super();
    }

    /**
	 * (overridden)
	 * 
	 * @see Exception#Exception(java.lang.String)
	 */
    public ModifyVetoException(String message) {
        super(message);
    }

    /**
	 * (overridden)
	 * 
	 * @see Exception#Exception(java.lang.String, java.lang.Throwable)
	 */
    public ModifyVetoException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
	 * (overridden)
	 * 
	 * @see Exception#Exception(java.lang.Throwable)
	 */
    public ModifyVetoException(Throwable cause) {
        super(cause);
    }
}
