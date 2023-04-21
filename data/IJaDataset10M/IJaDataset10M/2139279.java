package uk.org.ogsadai.client.toolkit.exception;

import uk.org.ogsadai.exception.ErrorID;

/**
 * Exception thrown when a data stream ends unexpectedly.  For example, if a 
 * data stream ends with a list opened but not closed.
 *
 * @author The OGSA-DAI Project Team
 */
public class UnexpectedDataStreamEndException extends DataException {

    /** Copyright notice */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007.";

    /**
     * Constructor.
     */
    public UnexpectedDataStreamEndException() {
        super(ErrorID.CTK_UNEXPECTED_DATA_STREAM_END_ERROR);
    }
}
