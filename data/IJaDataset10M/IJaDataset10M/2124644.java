package org.eclipse.misc.internal;

import org.eclipse.osgi.util.NLS;

public final class Messages extends NLS {

    private Messages() {
    }

    static {
        NLS.initializeMessages("org.eclipse.misc.internal.messages", Messages.class);
    }

    public static String Startup_infoMessage;
}
