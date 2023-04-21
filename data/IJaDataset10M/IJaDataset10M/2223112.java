package com.artofsolving.jodconverter.openoffice.connection;

import com.sun.star.beans.PropertyValue;
import com.sun.star.container.XNameAccess;
import com.sun.star.lang.XMultiServiceFactory;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XInterface;

/**
 * Utility class to access OpenOffice.org configuration properties at runtime
 */
public class OpenOfficeConfiguration {

    public static final String NODE_L10N = "org.openoffice.Setup/L10N";

    public static final String NODE_PRODUCT = "org.openoffice.Setup/Product";

    private OpenOfficeConnection connection;

    public OpenOfficeConfiguration(OpenOfficeConnection connection) {
        this.connection = connection;
    }

    public String getOpenOfficeProperty(String nodePath, String node) {
        if (!nodePath.startsWith("/")) {
            nodePath = "/" + nodePath;
        }
        String property = "";
        try {
            final String sProviderService = "com.sun.star.configuration.ConfigurationProvider";
            Object configProvider = connection.getRemoteServiceManager().createInstanceWithContext(sProviderService, connection.getComponentContext());
            XMultiServiceFactory xConfigProvider = (XMultiServiceFactory) UnoRuntime.queryInterface(com.sun.star.lang.XMultiServiceFactory.class, configProvider);
            final String sReadOnlyView = "com.sun.star.configuration.ConfigurationAccess";
            PropertyValue aPathArgument = new PropertyValue();
            aPathArgument.Name = "nodepath";
            aPathArgument.Value = nodePath;
            Object[] aArguments = new Object[1];
            aArguments[0] = aPathArgument;
            XInterface xElement = (XInterface) xConfigProvider.createInstanceWithArguments(sReadOnlyView, aArguments);
            XNameAccess xChildAccess = (XNameAccess) UnoRuntime.queryInterface(XNameAccess.class, xElement);
            property = (String) xChildAccess.getByName(node);
        } catch (Exception exception) {
            throw new OpenOfficeException("Could not retrieve property", exception);
        }
        return property;
    }

    public String getOpenOfficeVersion() {
        try {
            return getOpenOfficeProperty(NODE_PRODUCT, "ooSetupVersionAboutBox");
        } catch (OpenOfficeException noSuchElementException) {
            return getOpenOfficeProperty(NODE_PRODUCT, "ooSetupVersion");
        }
    }

    public String getOpenOfficeLocale() {
        return getOpenOfficeProperty(NODE_L10N, "ooLocale");
    }
}
