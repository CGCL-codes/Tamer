package uk.org.ogsadai.client.toolkit.exception;

import uk.org.ogsadai.exception.DAIUncheckedException;
import uk.org.ogsadai.exception.ErrorID;

/**
 * An internal server-side error occurred.
 * 
 * @author The OGSA-DAI Project Team
 */
public class ServerException extends DAIUncheckedException {

    /** Copyright statement */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2002 - 2007.";

    /**
     * Constructs an exception corresponding the given error ID.
     * 
     * @param errorID
     *            Error ID identifying the error type.
     * @param cause
     *            the ServiceException which caused of the exceoption.
     */
    public ServerException(final ErrorID errorID, Throwable cause) {
        super(errorID);
        initCause(cause);
    }

    /**
     * Constructs an exception corresponding the given error ID and parameters.
     * 
     * @param cause
     *            the ServiceException which caused of the exceoption.
     * @param errorID
     *            Error ID identifying the error type.
     * @param parameters
     *            Parameters associated with the error.
     */
    public ServerException(final ErrorID errorID, final Object[] parameters, Throwable cause) {
        super(errorID, parameters);
        initCause(cause);
    }
}
