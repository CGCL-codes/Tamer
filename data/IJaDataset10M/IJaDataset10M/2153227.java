package org.dyno.visual.swing.base;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "org.dyno.visual.swing.base.messages";

    public static String ITEM_ENDEC_NO_SUCH_ELEMENT;

    public static String ITEMP_PROVIDER_CELL_EDITOR_VALIDATOR_CANNOT_FIND_SUCH_ITEM;

    public static String JAVA_UTIL_APPLYING_CHANGES;

    public static String JAVA_UTIL_FILE;

    public static String JAVA_UTIL_MODIFIED;

    public static String JAVA_UTIL_MODIFIED_RESOURCE;

    public static String RESOURCE_IMAGE_CANNOT_FIND_IMG_PATH;

    static {
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
