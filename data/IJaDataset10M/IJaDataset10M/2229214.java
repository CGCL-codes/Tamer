package uk.co.pointofcare.echobase.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import org.apache.log4j.Logger;
import org.apache.xerces.dom.DOMInputImpl;
import org.apache.xerces.xs.XSImplementation;
import org.apache.xerces.xs.XSLoader;
import org.apache.xerces.xs.XSModel;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;

/**
 * @author rchallen
 *
 */
public class XsdIO {

    static Logger log = Logger.getLogger(XsdIO.class);

    XSModel schema;

    public static XsdIO load(File f) throws FileNotFoundException {
        System.setProperty(DOMImplementationRegistry.PROPERTY, "org.apache.xerces.dom.DOMXSImplementationSourceImpl");
        DOMImplementationRegistry registry;
        try {
            registry = DOMImplementationRegistry.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        XSImplementation impl = (XSImplementation) registry.getDOMImplementation("XS-Loader");
        XSLoader schemaLoader = impl.createXSLoader(null);
        InputStream byteStream = new FileInputStream(f);
        XsdIO out = new XsdIO();
        out.schema = schemaLoader.load(new DOMInputImpl("publicId", "systemId", "baseSystemId", byteStream, "encoding"));
        return out;
    }
}
