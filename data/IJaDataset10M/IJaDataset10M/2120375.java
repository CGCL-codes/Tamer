package org.eclipse.swordfish.tooling.ui;

import java.util.ResourceBundle;
import org.eclipse.osgi.util.NLS;

/**
 * class Messages
 * 
 * @author atelesh
 */
public class Messages {

    private static final String BUNDLE_NAME = "org.eclipse.swordfish.tooling.ui.messages";

    static {
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    private Messages() {
    }

    /** Tile New CXF Endpoint */
    public static String UI_TITLE_NEW_CXF_ENDPOINT;

    /** Tile New CXF Endpoint File */
    public static String UI_TITLE_NEW_CXF_ENDPOINT_FILE;

    /** Description new CXF Endpoint File */
    public static String UI_DESCRIPTION_NEW_CXF_ENDPOINT_FILE;

    /** Help */
    public static String UI_HELP;

    /** Tile new CXF Endpoint Options */
    public static String UI_TITLE_NEW_CXF_ENDPOINT_OPTIONS;

    /** Tile Select Input file */
    public static String UI_TITLE_SELECT_INPUT_FILE;

    /** Tile Select Input resource */
    public static String UI_TITLE_SELECT_INPUT_RESOURCE;

    /** Description new CXF Endpoint Options */
    public static String UI_DESCRIPTION_NEW_CXF_ENDPOINT_OPTIONS;

    /** Label Service name */
    public static String UI_LABEL_SERVICE_NAME;

    /** Label Service implementator */
    public static String UI_LABEL_SERVICE_IMPLEMENTOR;

    /** Label Service location */
    public static String UI_LABEL_SERVICE_LOCATION;

    /** Button implementator */
    public static String UI_BUTTON_IMPLEMENTOR;

    /** Dialog select implementator title*/
    public static String UI_DIALOG_SELECT_IMPLEMENTOR_TITLE;

    /** Dialog select implementator message*/
    public static String UI_DIALOG_SELECT_IMPLEMENTOR_MESSAGE;

    /** Title java project from WSDL*/
    public static String UI_TITLE_JAVA_PROJECT_FROM_WSDL;

    /** Description java project from WSDL*/
    public static String UI_DESCRIPTION_JAVA_PROJECT_FROM_WSDL;

    /** Error message*/
    public static String ERROR_NO_INTERFACES;

    /** Error message*/
    public static String ERROR_NO_SERVICE_CLASSES;

    /** Warning message*/
    public static String WARNING_MORE_THAN_ONE_INTERFACE;

    /** Warning message*/
    public static String WARNING_MORE_THAN_ONE_SERVICE_CLASS;

    /** Warning message*/
    public static String WARNING_NO_IMPLEMENTOR_FOUND;

    /** enter path*/
    public static String ODE_PACKAGING_PATH_TO_BPEL;

    /**enter path*/
    public static String ODE_PACKAGING_PATH_TO_DEPLOY_XML;

    /** specify artifacts*/
    public static String ODE_PACKAGING_SPECIFY_ARTIFACTS;

    /** title*/
    public static String ODE_PACKAGING_UI_TITLE_ODE_PACKAGING_GENERATE_PROJECT;

    /** jax-ws consumer generation error message */
    public static String ERROR_JAXWS_CONSUMER_GENERATION;

    /** jax-ws consumer generation error message */
    public static String ERROR_JAXWS_PROVIDER_GENERATION;
}
