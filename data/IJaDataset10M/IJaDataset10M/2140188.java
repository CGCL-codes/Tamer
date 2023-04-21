package flattree.xml.sax;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import flattree.TreeReader;
import flattree.flat.FlatReader;
import flattree.tree.Node;

/**
 * An {@link XMLReader} reading from flat files.
 */
public class FlatXMLReader implements XMLReader {

    private Stack<String> started = new Stack<String>();

    private ContentHandler contentHandler;

    private DTDHandler dtdHandler;

    private EntityResolver entityResolver;

    private ErrorHandler errorHandler;

    private Map<String, Object> properties = new HashMap<String, Object>();

    private Node root;

    /**
	 * Define the tree structure.
	 * 
	 * @param root
	 *            root of tree
	 */
    public FlatXMLReader(Node root) {
        this.root = root;
    }

    public ContentHandler getContentHandler() {
        return contentHandler;
    }

    public DTDHandler getDTDHandler() {
        return dtdHandler;
    }

    public EntityResolver getEntityResolver() {
        return entityResolver;
    }

    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        return false;
    }

    public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        return properties.get(name);
    }

    public void setContentHandler(ContentHandler handler) {
        this.contentHandler = handler;
    }

    public void setDTDHandler(DTDHandler handler) {
        this.dtdHandler = handler;
    }

    public void setEntityResolver(EntityResolver resolver) {
        this.entityResolver = resolver;
    }

    public void setErrorHandler(ErrorHandler handler) {
        this.errorHandler = handler;
    }

    /**
	 * Not supported.
	 * 
	 * @throws SAXException
	 *             always
	 */
    public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
        throw new SAXNotSupportedException();
    }

    public void setProperty(String name, Object value) throws SAXNotRecognizedException, SAXNotSupportedException {
        properties.put(name, value);
    }

    /**
	 * Not supported.
	 * 
	 * @throws SAXNotSupportedException
	 *             always
	 */
    public void parse(String systemId) throws IOException, SAXException {
        throw new SAXNotSupportedException("parse from systemId not supported");
    }

    public void parse(InputSource input) throws IOException, SAXException {
        Reader reader = input.getCharacterStream();
        if (reader == null) {
            reader = new InputStreamReader(input.getByteStream());
        }
        TreeReader treeReader = createTreeReader(reader);
        contentHandler.startDocument();
        while (true) {
            int state = treeReader.read();
            if (state == TreeReader.START) {
                String name = treeReader.getName();
                started.push(name);
                contentHandler.startElement("", name, name, new AttributesImpl(treeReader));
            } else {
                String name = started.pop();
                contentHandler.endElement("", name, name);
                if (started.isEmpty()) {
                    break;
                }
            }
        }
        contentHandler.endDocument();
    }

    protected TreeReader createTreeReader(Reader reader) {
        return new TreeReader(root, new FlatReader(reader));
    }

    private class AttributesImpl implements Attributes {

        private List<String> names;

        private Map<String, String> values;

        public AttributesImpl(TreeReader reader) {
            this.values = reader.getValues();
            this.names = new ArrayList<String>(values.keySet());
        }

        public int getLength() {
            return values.size();
        }

        public String getType(int index) {
            return "CDATA";
        }

        public String getType(String qName) {
            return "CDATA";
        }

        public String getType(String uri, String localName) {
            return "CDATA";
        }

        public String getURI(int index) {
            return "";
        }

        public String getLocalName(int index) {
            return "";
        }

        public String getQName(int index) {
            return names.get(index);
        }

        public int getIndex(String qName) {
            throw new UnsupportedOperationException();
        }

        public int getIndex(String uri, String localName) {
            throw new UnsupportedOperationException();
        }

        public String getValue(int index) {
            return values.get(names.get(index));
        }

        public String getValue(String qName) {
            return values.get(qName);
        }

        public String getValue(String uri, String localName) {
            return values.get(localName);
        }
    }
}
