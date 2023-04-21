package org.gvsig.gpe.xml.writer;

import java.net.URI;
import java.util.Enumeration;
import org.gvsig.gpe.GPEDefaults;
import org.gvsig.gpe.writer.GPEWriterHandlerImplementor;
import org.gvsig.gpe.xml.XmlProperties;
import org.gvsig.gpe.xml.parser.GPEXmlParserFactory;
import org.gvsig.gpe.xml.stream.IXmlStreamWriter;
import org.gvsig.gpe.xml.stream.XmlStreamException;
import org.gvsig.xmlschema.som.IXSSchema;

/**
 * @author Jorge Piera LLodr� (jorge.piera@iver.es)
 */
public abstract class GPEXmlWriterHandlerImplementor extends GPEWriterHandlerImplementor {

    protected IXmlStreamWriter writer = null;

    private String targetNamespace = null;

    private String targetNamespacePrefix = null;

    public GPEXmlWriterHandlerImplementor() {
        super();
    }

    public void initialize() {
        super.initialize();
        try {
            writer = GPEXmlParserFactory.getWriter(getFormat(), getOutputStream());
        } catch (XmlStreamException e) {
            getErrorHandler().addError(e);
        } catch (IllegalArgumentException e) {
            getErrorHandler().addError(e);
        }
    }

    /**
	 * @return the schemaLocation tag
	 */
    protected String getSchemaLocations() {
        StringBuffer schemaLocation = new StringBuffer();
        Enumeration uris = getSchemaDocument().getURIs();
        int i = 0;
        while (uris.hasMoreElements()) {
            if (i > 0) {
                schemaLocation.append("\b");
            }
            URI uri = (URI) uris.nextElement();
            IXSSchema schema = getSchemaDocument().getSchema(uri);
            schemaLocation.append(schema.getTargetNamespace());
            schemaLocation.append(" ");
            schemaLocation.append(getSchemaDocument().getSchemaLocation(uri));
            i++;
        }
        return schemaLocation.toString();
    }

    /**
	 * Returns the selected target namespace prefix. If
	 * the namespace doesn't exists it returns the 
	 * default Namespace prefix; 
	 * @return the namespace prefix
	 */
    protected String getTargetNamespacePrefix() {
        if (targetNamespacePrefix == null) {
            String prefix = null;
            if (getSchemaDocument().getTargetNamespace() != null) {
                prefix = getSchemaDocument().getNamespacePrefix(getSchemaDocument().getTargetNamespace());
            } else {
                prefix = GPEDefaults.getStringProperty(XmlProperties.DEFAULT_NAMESPACE_PREFIX);
            }
            if (prefix != null) {
                targetNamespacePrefix = prefix + ":";
            } else {
                targetNamespacePrefix = "";
            }
        }
        return targetNamespacePrefix;
    }
}
