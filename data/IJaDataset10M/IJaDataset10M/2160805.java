package uk.org.ogsadai.activity.extension;

import uk.org.ogsadai.activity.Activity;
import uk.org.ogsadai.resource.ResourceManager;

/**
 * Initialises activities that need access to a resource manager.
 *
 * @author The OGSA-DAI Project Team
 */
public class ResourceManagerActivityInitialiser implements ActivityInitialiser {

    /** Copyright statement */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007";

    /** Resource manager */
    private final ResourceManager mResourceManager;

    /**
     * Creates an initialiser that will provide the specified manager
     * to activities.
     * 
     * @param resourceManager
     *     Resource manager
     * @throws IllegalArgumentException
     *     If <code>resourceManager</code> is <code>null</code>.
     */
    public ResourceManagerActivityInitialiser(final ResourceManager resourceManager) {
        if (resourceManager == null) {
            throw new IllegalArgumentException("resourceManager must not be null");
        }
        mResourceManager = resourceManager;
    }

    public void initialise(Activity activity) throws ActivityInitialisationException {
        if (activity instanceof ResourceManagerActivity) {
            ((ResourceManagerActivity) activity).setResourceManager(mResourceManager);
        }
    }
}
