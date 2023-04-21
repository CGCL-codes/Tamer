package com.ibm.celldt.environment.launcher.variables;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "com.ibm.celldt.environment.launcher.variables.messages";

    public static String invalidEclipseVariableErrorMessage;

    static {
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
