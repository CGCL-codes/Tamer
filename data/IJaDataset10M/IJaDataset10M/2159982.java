package uk.org.ogsadai.client.toolkit.exception;

import uk.org.ogsadai.exception.DAIException;
import uk.org.ogsadai.exception.ErrorID;

/**
 * Exception thrown wben a client toolkit connection to an
 * OGSA-DAI server times out.
 *
 * @author The OGSA-DAI Project Team.
 */
public class TimeoutException extends DAIException {

    /** Copyright notice */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh 2007.";

    /**
     * Constructor.
     */
    public TimeoutException() {
        super(ErrorID.CTK_TIMEOUT);
    }
}
