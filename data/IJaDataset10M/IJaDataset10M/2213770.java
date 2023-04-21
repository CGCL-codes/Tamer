package orcajo.azada.discoverer.views;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "orcajo.azada.discoverer.views.messages";

    public static String DiscovererView_Browser;

    public static String Roles_roles;

    public static String SetsHandle_sets;

    public static String ViewLabelProvider_dataSources;

    public static String SharedDimension_sharedDimensions;

    public static String ShowXmlAction_Open_whith_XML_editor;

    static {
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }
}
