package uk.org.ogsadai.activity;

import uk.org.ogsadai.activity.pipeline.ActivityDescriptor;
import uk.org.ogsadai.exception.DAIException;
import uk.org.ogsadai.exception.ErrorID;

/**
 * Raised if a problem that is not due to a user mistake prevents the successful
 * creation of an activity instance.
 *  
 * @author The OGSA-DAI Project Team
 */
public class ActivityCreationException extends DAIException {

    /** Copyright statement */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007.";

    /**
     * Creates a new exception.
     * 
     * @param cause
     *            cause of the exception
     */
    public ActivityCreationException(Throwable cause) {
        super(ErrorID.ACTIVITY_CREATION_EXCEPTION);
        initCause(cause);
    }

    /**
     * Creates a new exception.
     * 
     * @param descriptor
     *            activity to be created
     * @param cause
     *            cause of the exception
     */
    public ActivityCreationException(ActivityDescriptor descriptor, Throwable cause) {
        super(ErrorID.ACTIVITY_CREATION_EXCEPTION, new Object[] { descriptor.getActivityName(), descriptor.getInstanceName() });
        initCause(cause);
    }
}
