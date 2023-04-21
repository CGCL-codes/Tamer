package org.apache.axis2.jaxws.description;

import org.apache.axis2.jaxws.description.builder.DescriptionBuilderComposite;
import org.apache.axis2.jaxws.spi.ServiceDelegate;
import org.apache.axis2.jaxws.unitTest.TestLogger;
import javax.wsdl.Definition;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.xml.ws.Service;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.io.File;

/**
 * 
 */
public class DescriptionTestUtils2 {

    public static URL getWSDLURL() {
        return getWSDLURL("WSDLTests.wsdl");
    }

    public static String getWSDLLocation(String wsdlFileName) {
        String basedir = System.getProperty("basedir", ".");
        return basedir + "/test-resources/wsdl/" + wsdlFileName;
    }

    public static URL getWSDLURL(String wsdlFileName) {
        URL wsdlURL = null;
        String urlString = getWSDLLocation(wsdlFileName);
        try {
            wsdlURL = new File(urlString).getAbsoluteFile().toURL();
        } catch (Exception e) {
            TestLogger.logger.debug("Caught exception creating WSDL URL :" + urlString + "; exception: " + e.toString());
        }
        return wsdlURL;
    }

    static Definition createWSDLDefinition(URL wsdlURL) {
        Definition wsdlDefinition = null;
        try {
            WSDLFactory factory = WSDLFactory.newInstance();
            WSDLReader reader = factory.newWSDLReader();
            wsdlDefinition = reader.readWSDL(wsdlURL.toString());
        } catch (Exception e) {
            TestLogger.logger.debug("*** ERROR ***: Caught exception trying to create WSDL Definition: " + e);
            e.printStackTrace();
        }
        return wsdlDefinition;
    }

    public static ServiceDelegate getServiceDelegate(Service service) {
        ServiceDelegate returnServiceDelegate = null;
        try {
            try {
                Field serviceDelgateField = service.getClass().getDeclaredFields()[0];
                serviceDelgateField.setAccessible(true);
                returnServiceDelegate = (ServiceDelegate) serviceDelgateField.get(service);
            } catch (ArrayIndexOutOfBoundsException e) {
                Field serviceDelegateField = service.getClass().getSuperclass().getDeclaredFields()[0];
                serviceDelegateField.setAccessible(true);
                returnServiceDelegate = (ServiceDelegate) serviceDelegateField.get(service);
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return returnServiceDelegate;
    }

    public static DescriptionBuilderComposite getServiceDescriptionComposite(ServiceDescription svcDesc) {
        DescriptionBuilderComposite returnComposite = null;
        try {
            Method getComposite = svcDesc.getClass().getDeclaredMethod("getDescriptionBuilderComposite");
            getComposite.setAccessible(true);
            returnComposite = (DescriptionBuilderComposite) getComposite.invoke(svcDesc, null);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return returnComposite;
    }
}
