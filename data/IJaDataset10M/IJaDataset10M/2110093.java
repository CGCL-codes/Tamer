package uk.org.ogsadai.resource.datasink;

import uk.org.ogsadai.exception.DAIException;
import uk.org.ogsadai.exception.ErrorID;

/**
 * Exception thrown when a string respresenting a data sink status does not 
 * contain a legal status value.
 *
 * @author The OGSA-DAI Project Team
 */
public class IllegalDataSinkStatusException extends DAIException {

    /** Copyright notice */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007.";

    /** Illegal state value. */
    private final String mIllegalStateValue;

    /**
     * Constructor.
     * 
     * @param illegalStateValue illegal state value.
     */
    public IllegalDataSinkStatusException(final String illegalStateValue) {
        super(ErrorID.ILLEGAL_DATA_SINK_STATUS_VALUE, new Object[] { illegalStateValue });
        mIllegalStateValue = illegalStateValue;
    }

    /**
     * Gets the illegal state value.
     * 
     * @return illegal state value.
     */
    public String getIllegalStateValue() {
        return mIllegalStateValue;
    }
}
