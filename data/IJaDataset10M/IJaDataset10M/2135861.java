package com.ail.core.persistence;

import com.ail.core.BaseError;
import com.ail.core.BaseException;

/**
 * Error indicating that persistance of an object could not be completed. The error indicates some form of
 * configuration problem.
 * @version $Revision: 1.2 $
 * @state $State: Exp $
 * @date $Date: 2005/07/16 10:23:26 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/persistence/UpdateException.java,v $
 */
public class UpdateException extends BaseException {

    /**
     * Constructor
     * @param description A description of the error.
     **/
    public UpdateException(String description) {
        super(description);
    }

    /**
     * Constructor
     * @param description A description of the error.
     * @param target The exception that caused this error to be thrown.
     */
    public UpdateException(String description, Throwable target) {
        super(description, target);
    }

    /**
     * Constructor
     * Turn a BaseError into a BaseException
     * @param e BaseException to convert.
     **/
    public UpdateException(BaseError e) {
        super(e);
    }
}
