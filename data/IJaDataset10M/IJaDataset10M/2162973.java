package net.virtualhockey.utils.xml;

import java.io.IOException;
import java.io.InputStream;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * <p>
 * Shall be used to load XML schemata directly from the classpath. In order to be able to
 * load them, their path must be specified as shown next:
 * </p>
 * 
 * <p>
 * <code>file://SomeClassPath/SchemaFile.xsd</code>
 * </p>
 * 
 * <p>
 * If the Schema resides in the packages net.virtualhockey.config.xsd, then the correct
 * schema location would be
 * <code>file:///net/virtualhockey/config/xsd/SchemaFile.xsd</code>
 * </p>
 * 
 * @version $Id: XMLSchemaLoader.java 71 2005-08-27 16:31:58Z jankejan $
 * @author jankejan
 */
public final class XMLSchemaLoader implements EntityResolver {

    private static final String FILE_PREFIX = "file://";

    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        if (systemId.startsWith(FILE_PREFIX)) {
            String fileName = systemId.substring(FILE_PREFIX.length());
            ClassLoader cl = XMLSchemaLoader.class.getClassLoader();
            if (cl == null) cl = ClassLoader.getSystemClassLoader();
            InputStream stream = cl.getResourceAsStream(fileName);
            return new InputSource(stream);
        }
        return null;
    }
}
