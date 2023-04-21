package org.fulworx.core.io.streams.json;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jettison.AbstractXMLInputFactory;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.codehaus.jettison.json.JSONTokener;
import org.codehaus.jettison.mapped.Configuration;
import org.codehaus.jettison.mapped.MappedNamespaceConvention;
import org.codehaus.jettison.mapped.MappedXMLOutputFactory;
import org.codehaus.jettison.mapped.MappedXMLStreamReader;
import org.fulworx.core.io.streams.jaxb.JAXBContextMarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Use the jaxb streaming of the JAXBContextMarshaller to write to a JSON writer.  This is done by implementing
 * the getXmlStreamWriter and getXmlStreamReader methods.
 *
 * @version $Id: $
 */
public class JAXBJSONMarshaller extends JAXBContextMarshaller {

    private static final Log LOG = LogFactory.getLog(JAXBJSONMarshaller.class.getName());

    private List<String> namespaces;

    public JAXBJSONMarshaller() {
    }

    public JAXBJSONMarshaller(List<String> namespaces) {
        this.namespaces = namespaces;
    }

    public XMLStreamReader getXmlStreamReader(InputStream input) throws XMLStreamException {
        Map<String, String> nstojns = getNamespaceMapping();
        XMLInputFactory xif = new MyMappedXMLInputFactory(nstojns);
        return xif.createXMLStreamReader(input);
    }

    protected Map<String, String> getNamespaceMapping() {
        Map<String, String> mappings = new HashMap<String, String>();
        if (namespaces != null && !namespaces.isEmpty()) {
            populateMapping(mappings, namespaces);
        }
        mappings.put("http://www.w3.org/2001/XMLSchema-instance", "schema");
        return mappings;
    }

    public XMLStreamWriter getXmlStreamWriter(OutputStream output) throws XMLStreamException {
        Map<String, String> nstojns = getNamespaceMapping();
        XMLOutputFactory xof = new MappedXMLOutputFactory(nstojns);
        return xof.createXMLStreamWriter(output);
    }

    private void populateMapping(Map<String, String> mappings, List<String> namespaces) {
        for (String namespace : namespaces) {
            String prefix = getPrefixFromNS(namespace);
            if (prefix != null) {
                mappings.put(namespace, prefix);
            }
        }
    }

    private String getPrefixFromNS(String namespace) {
        int start = "http://".length();
        int end = namespace.indexOf('.', start);
        String result = null;
        if (start > -1 && end > -1 && end > start) {
            result = namespace.substring(start, end);
        }
        return result;
    }

    public static class MyMappedXMLInputFactory extends AbstractXMLInputFactory {

        private MappedNamespaceConvention convention;

        public MyMappedXMLInputFactory(Map nstojns) {
            this(new Configuration(nstojns));
        }

        public MyMappedXMLInputFactory(Configuration config) {
            super();
            this.convention = new MappedNamespaceConvention(config);
        }

        public XMLStreamReader createXMLStreamReader(JSONTokener tokener) throws XMLStreamException {
            try {
                JSONObject root = new JSONObject(tokener);
                return new MappedXMLStreamReader(root, convention) {

                    public int getTextLength() {
                        return super.getTextCharacters().length;
                    }
                };
            } catch (JSONException e) {
                LOG.info("Unable to create xml stream reader for JSON", e);
                throw new XMLStreamException(e);
            }
        }
    }
}
