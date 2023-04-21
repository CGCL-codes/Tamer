package edu.toronto.jscoop.translator;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "edu.toronto.jscoop.translator.messages";

    public static String SOURCE_FOLDER_NOT_FOUND_STATUS_MESSAGE;

    public static String SOURCE_FOLDER_NOT_FOUND_DIALOG_TITLE;

    public static String SOURCE_FOLDER_NOT_FOUND_DIALOG_MESSAGE;

    static {
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
