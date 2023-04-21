package jbreport.xrl.dom;

import java.io.InputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Properties;
import org.w3c.dom.Element;
import org.apache.xerces.dom.DocumentImpl;
import jbreport.xrl.XRLDocument;

/**
 * 
 * @author Grant Finnemore
 * @version $Revision: 1.1 $
 */
public class XRLDocumentImpl extends DocumentImpl implements XRLDocument {

    /** The properties that will be queried to translate names to classes */
    private static Properties properties;

    /** The builder that constructed this instance */
    private XRLBuilder builder;

    /**
    * The static initializer that will load the appropriate properties when
    * this class is first initialized.
    */
    static {
        InputStream is = XRLDocumentImpl.class.getResourceAsStream("builder.properties");
        try {
            properties = new Properties();
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.toString());
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                System.err.println("Problem during close : " + e.getMessage());
            }
        }
    }

    XRLDocumentImpl(XRLBuilder builder) {
        this.builder = builder;
    }

    public Element createElementNS(String namespaceURI, String qName) {
        String className = properties.getProperty("elem." + qName);
        try {
            Class cls = Class.forName(className);
            Constructor cons = cls.getConstructor(new Class[] { XRLDocumentImpl.class, String.class, String.class });
            return (XRLElementImpl) cons.newInstance(new Object[] { this, namespaceURI, qName });
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.toString());
        }
    }
}
