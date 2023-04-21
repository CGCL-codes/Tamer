package uk.org.ogsadai.activity.transform;

import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.converters.tuple.TupleHandlerException;
import uk.org.ogsadai.exception.ErrorID;

/**
 * Raised when an activity encounters a problem while converting data from one
 * format to another.
 * 
 * @author The OGSA-DAI Project Team
 */
public class ActivityDataConversionException extends ActivityProcessingException {

    /** Copyright statement */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007.";

    /**
     * Create a new exception.
     * 
     * @param cause
     *            the <code>ODTupleHandlerException</code> that caused the
     *            problem
     */
    public ActivityDataConversionException(TupleHandlerException cause) {
        super(ErrorID.TRANSFORM_ACTIVITY_CODING_EXCEPTION);
        initCause(cause);
    }
}
