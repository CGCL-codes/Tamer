package uk.org.ogsadai.client.toolkit.exception;

import uk.org.ogsadai.exception.ErrorID;

/**
 * Exception thrown when activity inputs are overspecified.  Activity inputs are
 * overspecified when too many optional inputs are operational.  The simplest 
 * occurence is when two mutually exclusive optional inputs are operational.
 *
 * @author The OGSA-DAI Project Team
 */
public class ActivityInputsOverspecifiedException extends ActivityIOIllegalStateException {

    /** Copyright notice */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007.";

    /** The maximum number of inputs that can be operational */
    private final int mMaxCount;

    /**
     * Constructor.
     * 
     * @param maxCount    the maximum number of the specified inputs that are
     *                    can be operational.
     * @param inputNames  the names of the inputs that for the set that is
     *                    overspecified.
     */
    public ActivityInputsOverspecifiedException(final int maxCount, final String[] inputNames) {
        super(ErrorID.ACTIVITY_INPUTS_OVERSPECIFIED_ERROR, new Object[] { new Integer(maxCount), listStrings(inputNames) }, inputNames, new String[] {});
        mMaxCount = maxCount;
    }

    /**
     * Gets the maximum number of the input set that can be operational at one
     * time.
     *
     * @return maximum number of the input set that can be operational at one
     *         time.
     */
    public int getMaxCount() {
        return mMaxCount;
    }
}
