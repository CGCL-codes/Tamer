package ces.arch.query.parser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class TargetImpl extends DefaultHandler implements Cloneable, Unmarshallable, LexicalHandler, Target {

    private String as;

    private boolean zeus_AsSet;

    private String pojo;

    private boolean zeus_PojoSet;

    /** Any DOCTYPE reference/statements. */
    private String docTypeString;

    /** The encoding for the output document */
    private String outputEncoding;

    /** The current node in unmarshalling */
    private Unmarshallable zeus_currentUNode;

    /** The parent node in unmarshalling */
    private Unmarshallable zeus_parentUNode;

    /** Whether this node has been handled */
    private boolean zeus_thisNodeHandled = false;

    /** Whether a DTD exists for an unmarshal call */
    private boolean hasDTD;

    /** Whether validation is occurring */
    private boolean validate;

    /** The namespace mappings on this element */
    private Map namespaceMappings;

    /** The EntityResolver for SAX parsing to use */
    private static EntityResolver entityResolver;

    /** The ErrorHandler for SAX parsing to use */
    private static ErrorHandler errorHandler;

    private static TargetImpl prototype = null;

    public static void setPrototype(TargetImpl prototype) {
        TargetImpl.prototype = prototype;
    }

    public static TargetImpl newInstance() {
        try {
            return (prototype != null) ? (TargetImpl) prototype.clone() : new TargetImpl();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public TargetImpl() {
        zeus_AsSet = false;
        zeus_PojoSet = false;
        docTypeString = "";
        hasDTD = false;
        validate = false;
        namespaceMappings = new HashMap();
    }

    public String getAs() {
        return as;
    }

    public void setAs(String as) {
        this.as = as;
        zeus_AsSet = true;
    }

    public String getPojo() {
        return pojo;
    }

    public void setPojo(String pojo) {
        this.pojo = pojo;
        zeus_PojoSet = true;
    }

    public void setDocType(String name, String publicID, String systemID) {
        try {
            startDTD(name, publicID, systemID);
        } catch (SAXException neverHappens) {
        }
    }

    public void setOutputEncoding(String outputEncoding) {
        this.outputEncoding = outputEncoding;
    }

    public void marshal(File file) throws IOException {
        marshal(new FileWriter(file));
    }

    public void marshal(OutputStream outputStream) throws IOException {
        marshal(new OutputStreamWriter(outputStream));
    }

    public void marshal(Writer writer) throws IOException {
        writer.write("<?xml version=\"1.0\" ");
        if (outputEncoding != null) {
            writer.write("encoding=\"");
            writer.write(outputEncoding);
            writer.write("\"?>\n\n");
        } else {
            writer.write("encoding=\"UTF-8\"?>\n\n");
        }
        writer.write(docTypeString);
        writer.write("\n");
        writeXMLRepresentation(writer, "");
        writer.flush();
        writer.close();
    }

    protected void writeXMLRepresentation(Writer writer, String indent) throws IOException {
        writer.write(indent);
        writer.write("<target");
        for (Iterator i = namespaceMappings.keySet().iterator(); i.hasNext(); ) {
            String prefix = (String) i.next();
            String uri = (String) namespaceMappings.get(prefix);
            writer.write(" xmlns");
            if (!prefix.trim().equals("")) {
                writer.write(":");
                writer.write(prefix);
            }
            writer.write("=\"");
            writer.write(uri);
            writer.write("\"\n        ");
        }
        if (zeus_AsSet) {
            writer.write(" as=\"");
            writer.write(escapeAttributeValue(as));
            writer.write("\"");
        }
        if (zeus_PojoSet) {
            writer.write(" pojo=\"");
            writer.write(escapeAttributeValue(pojo));
            writer.write("\"");
        }
        writer.write("/>\n");
    }

    private String escapeAttributeValue(String attributeValue) {
        String returnValue = attributeValue;
        for (int i = 0; i < returnValue.length(); i++) {
            char ch = returnValue.charAt(i);
            if (ch == '"') {
                returnValue = new StringBuffer().append(returnValue.substring(0, i)).append("&quot;").append(returnValue.substring(i + 1)).toString();
            }
        }
        return returnValue;
    }

    private String escapeTextValue(String textValue) {
        String returnValue = textValue;
        for (int i = 0; i < returnValue.length(); i++) {
            char ch = returnValue.charAt(i);
            if (ch == '<') {
                returnValue = new StringBuffer().append(returnValue.substring(0, i)).append("&lt;").append(returnValue.substring(i + 1)).toString();
            } else if (ch == '>') {
                returnValue = new StringBuffer().append(returnValue.substring(0, i)).append("&gt;").append(returnValue.substring(i + 1)).toString();
            }
        }
        return returnValue;
    }

    /**
     * <p>
     *  This sets a SAX <code>EntityResolver</code> for this unmarshalling process.
     * </p>
     *
     * @param resolver the entity resolver to use.
     */
    public static void setEntityResolver(EntityResolver resolver) {
        entityResolver = resolver;
    }

    /**
     * <p>
     *  This sets a SAX <code>ErrorHandler</code> for this unmarshalling process.
     * </p>
     *
     * @param handler the entity resolver to use.
     */
    public static void setErrorHandler(ErrorHandler handler) {
        errorHandler = handler;
    }

    public static Target unmarshal(File file) throws IOException {
        return unmarshal(new FileReader(file));
    }

    public static Target unmarshal(File file, boolean validate) throws IOException {
        return unmarshal(new FileReader(file), validate);
    }

    public static Target unmarshal(InputStream inputStream) throws IOException {
        return unmarshal(new InputStreamReader(inputStream));
    }

    public static Target unmarshal(InputStream inputStream, boolean validate) throws IOException {
        return unmarshal(new InputStreamReader(inputStream), validate);
    }

    public static Target unmarshal(Reader reader) throws IOException {
        return unmarshal(reader, false);
    }

    public static Target unmarshal(Reader reader, boolean validate) throws IOException {
        TargetImpl target = TargetImpl.newInstance();
        target.setValidating(validate);
        target.setCurrentUNode(target);
        target.setParentUNode(null);
        XMLReader parser = null;
        String parserClass = System.getProperty("org.xml.sax.driver", "org.apache.xerces.parsers.SAXParser");
        try {
            parser = XMLReaderFactory.createXMLReader(parserClass);
            if (entityResolver != null) {
                parser.setEntityResolver(entityResolver);
            }
            parser.setErrorHandler(target);
            parser.setProperty("http://xml.org/sax/properties/lexical-handler", target);
            parser.setContentHandler(target);
        } catch (SAXException e) {
            throw new IOException("Could not load XML parser: " + e.getMessage());
        }
        InputSource inputSource = new InputSource(reader);
        try {
            parser.setFeature("http://xml.org/sax/features/validation", new Boolean(validate).booleanValue());
            parser.setFeature("http://xml.org/sax/features/namespaces", true);
            parser.setFeature("http://xml.org/sax/features/namespace-prefixes", false);
            parser.parse(inputSource);
        } catch (SAXException e) {
            throw new IOException("Error parsing XML document: " + e.getMessage());
        }
        return target;
    }

    public Unmarshallable getParentUNode() {
        return zeus_parentUNode;
    }

    public void setParentUNode(Unmarshallable parentUNode) {
        this.zeus_parentUNode = parentUNode;
    }

    public Unmarshallable getCurrentUNode() {
        return zeus_currentUNode;
    }

    public void setCurrentUNode(Unmarshallable currentUNode) {
        this.zeus_currentUNode = currentUNode;
    }

    public void setValidating(boolean validate) {
        this.validate = validate;
    }

    public void startDocument() throws SAXException {
    }

    public void setDocumentLocator(Locator locator) {
    }

    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        namespaceMappings.put(prefix, uri);
    }

    public void startElement(String namespaceURI, String localName, String qName, org.xml.sax.Attributes atts) throws SAXException {
        Unmarshallable current = getCurrentUNode();
        if (current != this) {
            current.startElement(namespaceURI, localName, qName, atts);
            return;
        }
        if ((localName.equals("target")) && (!zeus_thisNodeHandled)) {
            for (int i = 0, len = atts.getLength(); i < len; i++) {
                String attName = atts.getLocalName(i);
                String attValue = atts.getValue(i);
                if (attName.equals("as")) {
                    setAs(attValue);
                }
                if (attName.equals("pojo")) {
                    setPojo(attValue);
                }
            }
            zeus_thisNodeHandled = true;
            return;
        } else {
        }
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        Unmarshallable current = getCurrentUNode();
        if (current != this) {
            current.endElement(namespaceURI, localName, qName);
            return;
        }
        Unmarshallable parent = getCurrentUNode().getParentUNode();
        if (parent != null) {
            parent.setCurrentUNode(parent);
        }
    }

    public void characters(char[] ch, int start, int len) throws SAXException {
        Unmarshallable current = getCurrentUNode();
        if (current != this) {
            current.characters(ch, start, len);
            return;
        }
        String text = new String(ch, start, len);
        text = text.trim();
    }

    public void comment(char ch[], int start, int len) throws SAXException {
    }

    public void warning(SAXParseException e) throws SAXException {
        if (errorHandler != null) {
            errorHandler.warning(e);
        }
    }

    public void error(SAXParseException e) throws SAXException {
        if ((validate) && (!hasDTD)) {
            throw new SAXException("Validation is turned on, but no DTD has been specified in the input XML document. Please supply a DTD through a DOCTYPE reference.");
        }
        if (errorHandler != null) {
            errorHandler.error(e);
        }
    }

    public void fatalError(SAXParseException e) throws SAXException {
        if ((validate) && (!hasDTD)) {
            throw new SAXException("Validation is turned on, but no DTD has been specified in the input XML document. Please supply a DTD through a DOCTYPE reference.");
        }
        if (errorHandler != null) {
            errorHandler.fatalError(e);
        }
    }

    public void startDTD(String name, String publicID, String systemID) throws SAXException {
        if ((name == null) || (name.equals(""))) {
            docTypeString = "";
            return;
        }
        hasDTD = true;
        StringBuffer docTypeSB = new StringBuffer();
        boolean hasPublic = false;
        docTypeSB.append("<!DOCTYPE ").append(name);
        if ((publicID != null) && (!publicID.equals(""))) {
            docTypeSB.append(" PUBLIC \"").append(publicID).append("\"");
            hasPublic = true;
        }
        if ((systemID != null) && (!systemID.equals(""))) {
            if (!hasPublic) {
                docTypeSB.append(" SYSTEM");
            }
            docTypeSB.append(" \"").append(systemID).append("\"");
        }
        docTypeSB.append(">");
        docTypeString = docTypeSB.toString();
    }

    public void endDTD() throws SAXException {
    }

    public void startEntity(String name) throws SAXException {
    }

    public void endEntity(String name) throws SAXException {
    }

    public void startCDATA() throws SAXException {
    }

    public void endCDATA() throws SAXException {
    }
}
