package org.eclipse.mylyn.internal.eplanner.core;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "org.eclipse.mylyn.internal.eplanner.core.messages";

    static {
        reloadMessages();
    }

    public static void reloadMessages() {
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    public static String EPlannerRepositoryConnector_EXTREME_PLANNER_SUPPORTS_3_1_TO_3_2;
}
