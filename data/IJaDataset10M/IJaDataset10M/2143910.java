package net.disy.ogc.wpspd.v_1_0_0;

import java.net.URL;
import java.text.MessageFormat;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.xml.sax.SAXException;

public class WpspdUtils {

    private static ObjectFactory OBJECT_FACTORY;

    public static ObjectFactory createObjectFactory() {
        if (OBJECT_FACTORY == null) {
            OBJECT_FACTORY = new ObjectFactory();
        }
        return OBJECT_FACTORY;
    }

    private static Schema SCHEMA;

    public static Schema createSchema() throws SAXException {
        if (SCHEMA != null) {
            return SCHEMA;
        } else {
            final SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            final URL schemaResource = WpspdUtils.class.getClassLoader().getResource("wps-pd/1.0.0/wps-pd.xsd");
            SCHEMA = schemaFactory.newSchema(new StreamSource(schemaResource.toString()));
            return SCHEMA;
        }
    }

    private static JAXBContext CONTEXT = null;

    public static JAXBContext createJaxbContext() {
        if (CONTEXT == null) {
            try {
                CONTEXT = JAXBContext.newInstance(WpspdConstants.CONTEXT_PATH);
            } catch (JAXBException jaxbex) {
                throw new ExceptionInInitializerError(MessageFormat.format("Could not create a JAXB context for the context path [{0}].", WpspdConstants.CONTEXT_PATH));
            }
        }
        return CONTEXT;
    }
}
