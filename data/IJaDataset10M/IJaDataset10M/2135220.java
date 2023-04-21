package com.aptana.ide.core.io;

import org.eclipse.osgi.util.NLS;

/**
 * NLS
 */
public final class Messages extends NLS {

    private static final String BUNDLE_NAME = "com.aptana.ide.core.io.messages";

    private Messages() {
    }

    /**
	 * ProtocolManager_FileManagerNullError
	 */
    public static String ProtocolManager_FileManagerNullError;

    /**
	 * ProtocolManager_UnableToLoadProtocolManagerError
	 */
    public static String ProtocolManager_UnableToLoadProtocolManagerError;

    /**
	 * VirtualManagerBase_GetTimeOffsetError
	 */
    public static String VirtualManagerBase_GetTimeOffsetError;

    static {
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    /**
	 * VirtualManagerBase_UnableToAddPropertyListener
	 */
    public static String VirtualManagerBase_UnableToAddPropertyListener;
}
