package org.deved.antlride.internal.core.model;

import org.eclipse.osgi.util.NLS;

class ModelMessages extends NLS {

    public static String ModelMessageInvalidGrammarTitle;

    public static String ModelMessageInvalidGrammarMessage;

    private ModelMessages() {
    }

    static {
        String BUNDLE_NAME = ModelMessages.class.getName();
        NLS.initializeMessages(BUNDLE_NAME, ModelMessages.class);
    }
}
