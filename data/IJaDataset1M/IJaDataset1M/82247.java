package uk.ac.cam.caret.minibix.taggy;

import java.util.*;
import java.io.*;
import org.xml.sax.*;
import javax.xml.transform.*;
import javax.xml.transform.sax.*;
import javax.xml.transform.stream.*;
import org.apache.commons.io.*;

public class TaggySerializer {

    private TaggySchema schema;

    private SAXTransformerFactory factory = (SAXTransformerFactory) SAXTransformerFactory.newInstance();

    static final String TAGGY_PREFIX = "taggy";

    private List<byte[]> xslt = new ArrayList<byte[]>();

    private NamespaceStore ns = new NamespaceStore();

    public TaggySerializer(TaggySchema schema) {
        this.schema = schema;
        ns.registerNamespace(schema.getNamespace());
    }

    TaggySchema getSchema() {
        return schema;
    }

    String getPrefix(String namespace) {
        return ns.getPrefix(namespace);
    }

    public void addXSLT(InputStream in) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        IOUtils.copy(in, bytes);
        xslt.add(0, bytes.toByteArray());
    }

    private TransformerHandler createTerminalHandler(OutputStream out, boolean indent) throws SAXException, TransformerConfigurationException {
        TransformerHandler handler = factory.newTransformerHandler();
        Transformer tx = handler.getTransformer();
        if (indent) tx.setOutputProperty("indent", "yes");
        handler.setResult(new StreamResult(new OutputStreamWriter(out)));
        return handler;
    }

    public void addSubSerializer(TaggySerializer in) {
        schema = schema.duplicate();
        schema.addSubSchema(in.schema);
        ns.registerNamespaces(in.ns);
        xslt.addAll(in.xslt);
    }

    void serializeInner(Object in, ContentHandler handler, ObjectStack stack) throws Unserializable, SAXException {
        schema.serializeInner(in, handler, stack, this);
    }

    public void serialize(Object in, OutputStream stream, boolean indent) throws Unserializable {
        try {
            ObjectStack stack = new ObjectStack();
            TransformerHandler out = createTerminalHandler(stream, indent);
            TransformerHandler handler = out;
            for (byte[] sheet : xslt) {
                TransformerHandler style = factory.newTransformerHandler(new StreamSource(new ByteArrayInputStream(sheet)));
                style.setResult(new SAXResult(handler));
                handler = style;
            }
            handler.startDocument();
            for (String namespace : ns.getNamespaces()) handler.startPrefixMapping(ns.getPrefix(namespace), namespace);
            handler.startPrefixMapping("", schema.getNamespace());
            schema.serializeInner(in, handler, stack, this);
            handler.endPrefixMapping("");
            for (String namespace : ns.getNamespaces()) handler.endPrefixMapping(ns.getPrefix(namespace));
            handler.endDocument();
        } catch (TransformerConfigurationException x) {
            throw new Unserializable("Bad JAXP serializer", x, null);
        } catch (SAXException x) {
            throw new Unserializable("SAX exception during configuration", x, null);
        }
    }
}
