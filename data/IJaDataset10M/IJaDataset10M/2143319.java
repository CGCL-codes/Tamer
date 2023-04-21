package org.rubypeople.rdt.refactoring.nodewrapper;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "org.rubypeople.rdt.refactoring.nodewrapper.messages";

    public static String SClassNodeWrapper_UnknownNode;

    static {
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
