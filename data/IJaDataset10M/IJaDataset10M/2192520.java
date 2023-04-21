package edu.toronto.jscoop;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "edu.toronto.jscoop.messages";

    public static String AWAIT_SYNTAX_ERROR;

    public static String ARG_SEPARATE_ERROR;

    public static String ARRAY_DECLARATION_ERROR;

    public static String ERROR_DIALOG_DELETE_FILE_TRY_AGAIN_MESSAGE;

    public static String ERROR_DIALOG_TITLE;

    public static String JSCOOPProjectWizard_JSCOOP_PROJECT_WIZARD_TITLE;

    public static String QUERY_SEPARATE_ERROR;

    public static String SEPARATE_ERROR;

    public static String STATUS_ERROR_MESSAGE_FILE_EXISTS;

    public static String TYPE_SEPARATE_ERROR;

    public static String SEPARATE_CALL_ERROR;

    public static String BUSINESS_CARD_ERROR;

    public static String PRIMITIVE_TYPE_ERROR;

    static {
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
