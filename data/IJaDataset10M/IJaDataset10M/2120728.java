package tpac.lib.wcs.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.stream.*;
import javax.xml.stream.events.*;
import java.util.Iterator;
import tpac.lib.wcs.adapter.WcsAdapter;
import tpac.lib.wcs.adapter.WcsAdapter_100;
import tpac.lib.DAPSpider.DapAuthenticator;

public class WcsAdapterFactoryImpl implements WcsAdapterFactory {

    private String lastError;

    /**
     * Creates a WCS adapter.
     * 
     * @param urlString
     *            the url string
     * @param auth
     *            the auth
     * 
     * @return an appropriately versioned WcsAdapter object
     */
    public WcsAdapter createWcsAdapter(String urlString, DapAuthenticator auth) {
        try {
            String version = null;
            Authenticator.setDefault(auth);
            URL url = new URL(urlString + "?request=GetCapabilities&version=1.0.0&service=WCS");
            InputStream stream = url.openStream();
            XMLEventReader eventReader = XMLInputFactory.newInstance().createXMLEventReader(stream);
            while (eventReader.hasNext() && version == null) {
                XMLEvent event = (XMLEvent) eventReader.next();
                if (event instanceof StartElement && (((StartElement) event).getName().getLocalPart().equals("WCS_Capabilities"))) {
                    Iterator<Attribute> iter = ((StartElement) event).getAttributes();
                    while (iter.hasNext()) {
                        version = iter.next().getValue();
                    }
                }
            }
            if (version == null) {
                return null;
            } else if (version.equals("1.0.0")) {
                return new WcsAdapter_100(eventReader);
            } else if (version.equals("1.1.1")) {
                return null;
            }
        } catch (MalformedURLException e) {
            lastError = "Error connecting to server address: " + urlString + " exception message: " + e.toString();
            e.printStackTrace(System.out);
        } catch (IOException e) {
            lastError = "Error opening connection to server address:" + urlString + " exception message: " + e.toString();
            e.printStackTrace(System.out);
        } catch (TransformerFactoryConfigurationError e) {
            lastError = "Error with transformer factory configuration.  Exception message: " + e.toString();
            e.printStackTrace();
        } catch (XMLStreamException e) {
            lastError = "Error with XML stream.  Exception message: " + e.toString();
            e.printStackTrace();
        } catch (FactoryConfigurationError e) {
            lastError = "Error with factory.  Exception message: " + e.toString();
            e.printStackTrace();
        }
        return null;
    }
}
