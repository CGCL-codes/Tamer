package uk.org.ogsadai.activity.extension;

import uk.org.ogsadai.activity.Activity;
import uk.org.ogsadai.activity.request.OGSADAIChildRequestConfiguration;
import uk.org.ogsadai.activity.request.RequestConfiguration;

/**
 * Initialises a pipeline spawning activity.
 * 
 * @author The OGSA-DAI Project Team
 */
public class RequestContextActivityInitialiser implements ActivityInitialiser {

    /** Copyright notice */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh 2007";

    /** Context of the request */
    private final RequestConfiguration mParentContext;

    /**
     * Create an initialiser for activities that spawn other activities within
     * a new request context.
     *
     * @param context the parent request context 
     */
    public RequestContextActivityInitialiser(RequestConfiguration context) {
        mParentContext = context;
    }

    /**
     * Creates a new context for creating a sub-workflow of activities and 
     * assigns it to the activity.
     */
    public void initialise(Activity activity) {
        if (activity instanceof RequestContextActivity) {
            OGSADAIChildRequestConfiguration context = new OGSADAIChildRequestConfiguration(mParentContext);
            ((RequestContextActivity) activity).setRequestContext(context);
        }
    }
}
