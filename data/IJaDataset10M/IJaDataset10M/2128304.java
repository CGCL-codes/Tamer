package dom;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * A sample DOM writer. This sample program illustrates how to
 * traverse a DOM tree in order to print a document that is parsed.
 *
 * @author Andy Clark, IBM
 *
 * @version $Id: Writer.java,v 1.1.1.1 2002/10/31 15:40:27 pettys Exp $
 */
public class Writer {

    /** Namespaces feature id (http://xml.org/sax/features/namespaces). */
    protected static final String NAMESPACES_FEATURE_ID = "http://xml.org/sax/features/namespaces";

    /** Validation feature id (http://xml.org/sax/features/validation). */
    protected static final String VALIDATION_FEATURE_ID = "http://xml.org/sax/features/validation";

    /** Schema validation feature id (http://apache.org/xml/features/validation/schema). */
    protected static final String SCHEMA_VALIDATION_FEATURE_ID = "http://apache.org/xml/features/validation/schema";

    /** Schema full checking feature id (http://apache.org/xml/features/validation/schema-full-checking). */
    protected static final String SCHEMA_FULL_CHECKING_FEATURE_ID = "http://apache.org/xml/features/validation/schema-full-checking";

    /** Lexical handler property id (http://xml.org/sax/properties/lexical-handler). */
    protected static final String LEXICAL_HANDLER_PROPERTY_ID = "http://xml.org/sax/properties/lexical-handler";

    /** Default parser name. */
    protected static final String DEFAULT_PARSER_NAME = "dom.wrappers.Xerces";

    /** Default namespaces support (true). */
    protected static final boolean DEFAULT_NAMESPACES = true;

    /** Default validation support (false). */
    protected static final boolean DEFAULT_VALIDATION = false;

    /** Default Schema validation support (false). */
    protected static final boolean DEFAULT_SCHEMA_VALIDATION = false;

    /** Default Schema full checking support (false). */
    protected static final boolean DEFAULT_SCHEMA_FULL_CHECKING = false;

    /** Default canonical output (false). */
    protected static final boolean DEFAULT_CANONICAL = false;

    /** Print writer. */
    protected PrintWriter fOut;

    /** Canonical output. */
    protected boolean fCanonical;

    /** Default constructor. */
    public Writer() {
    }

    public Writer(boolean canonical) {
        fCanonical = canonical;
    }

    /** Sets whether output is canonical. */
    public void setCanonical(boolean canonical) {
        fCanonical = canonical;
    }

    /** Sets the output stream for printing. */
    public void setOutput(OutputStream stream, String encoding) throws UnsupportedEncodingException {
        if (encoding == null) {
            encoding = "UTF8";
        }
        java.io.Writer writer = new OutputStreamWriter(stream, encoding);
        fOut = new PrintWriter(writer);
    }

    /** Sets the output writer. */
    public void setOutput(java.io.Writer writer) {
        fOut = writer instanceof PrintWriter ? (PrintWriter) writer : new PrintWriter(writer);
    }

    /** Writes the specified node, recursively. */
    public void write(Node node) {
        if (node == null) {
            return;
        }
        short type = node.getNodeType();
        switch(type) {
            case Node.DOCUMENT_NODE:
                {
                    Document document = (Document) node;
                    if (!fCanonical) {
                        fOut.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                        fOut.flush();
                        write(document.getDoctype());
                    }
                    write(document.getDocumentElement());
                    break;
                }
            case Node.DOCUMENT_TYPE_NODE:
                {
                    DocumentType doctype = (DocumentType) node;
                    fOut.print("<!DOCTYPE ");
                    fOut.print(doctype.getName());
                    String publicId = doctype.getPublicId();
                    String systemId = doctype.getSystemId();
                    if (publicId != null) {
                        fOut.print(" PUBLIC '");
                        fOut.print(publicId);
                        fOut.print("' '");
                        fOut.print(systemId);
                        fOut.print('\'');
                    } else {
                        fOut.print(" SYSTEM '");
                        fOut.print(systemId);
                        fOut.print('\'');
                    }
                    String internalSubset = doctype.getInternalSubset();
                    if (internalSubset != null) {
                        fOut.println(" [");
                        fOut.print(internalSubset);
                        fOut.print(']');
                    }
                    fOut.println('>');
                    break;
                }
            case Node.ELEMENT_NODE:
                {
                    fOut.print('<');
                    fOut.print(node.getNodeName());
                    Attr attrs[] = sortAttributes(node.getAttributes());
                    for (int i = 0; i < attrs.length; i++) {
                        Attr attr = attrs[i];
                        fOut.print(' ');
                        fOut.print(attr.getNodeName());
                        fOut.print("=\"");
                        normalizeAndPrint(attr.getNodeValue());
                        fOut.print('"');
                    }
                    fOut.print('>');
                    fOut.flush();
                    Node child = node.getFirstChild();
                    while (child != null) {
                        write(child);
                        child = child.getNextSibling();
                    }
                    break;
                }
            case Node.ENTITY_REFERENCE_NODE:
                {
                    if (fCanonical) {
                        Node child = node.getFirstChild();
                        while (child != null) {
                            write(child);
                            child = child.getNextSibling();
                        }
                    } else {
                        fOut.print('&');
                        fOut.print(node.getNodeName());
                        fOut.print(';');
                        fOut.flush();
                    }
                    break;
                }
            case Node.CDATA_SECTION_NODE:
                {
                    if (fCanonical) {
                        normalizeAndPrint(node.getNodeValue());
                    } else {
                        fOut.print("<![CDATA[");
                        fOut.print(node.getNodeValue());
                        fOut.print("]]>");
                    }
                    fOut.flush();
                    break;
                }
            case Node.TEXT_NODE:
                {
                    normalizeAndPrint(node.getNodeValue());
                    fOut.flush();
                    break;
                }
            case Node.PROCESSING_INSTRUCTION_NODE:
                {
                    fOut.print("<?");
                    fOut.print(node.getNodeName());
                    String data = node.getNodeValue();
                    if (data != null && data.length() > 0) {
                        fOut.print(' ');
                        fOut.print(data);
                    }
                    fOut.println("?>");
                    fOut.flush();
                    break;
                }
        }
        if (type == Node.ELEMENT_NODE) {
            fOut.print("</");
            fOut.print(node.getNodeName());
            fOut.print('>');
            fOut.flush();
        }
    }

    /** Returns a sorted list of attributes. */
    protected Attr[] sortAttributes(NamedNodeMap attrs) {
        int len = (attrs != null) ? attrs.getLength() : 0;
        Attr array[] = new Attr[len];
        for (int i = 0; i < len; i++) {
            array[i] = (Attr) attrs.item(i);
        }
        for (int i = 0; i < len - 1; i++) {
            String name = array[i].getNodeName();
            int index = i;
            for (int j = i + 1; j < len; j++) {
                String curName = array[j].getNodeName();
                if (curName.compareTo(name) < 0) {
                    name = curName;
                    index = j;
                }
            }
            if (index != i) {
                Attr temp = array[i];
                array[i] = array[index];
                array[index] = temp;
            }
        }
        return array;
    }

    /** Normalizes and prints the given string. */
    protected void normalizeAndPrint(String s) {
        int len = (s != null) ? s.length() : 0;
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            normalizeAndPrint(c);
        }
    }

    /** Normalizes and print the given character. */
    protected void normalizeAndPrint(char c) {
        switch(c) {
            case '<':
                {
                    fOut.print("&lt;");
                    break;
                }
            case '>':
                {
                    fOut.print("&gt;");
                    break;
                }
            case '&':
                {
                    fOut.print("&amp;");
                    break;
                }
            case '"':
                {
                    fOut.print("&quot;");
                    break;
                }
            case '\r':
            case '\n':
                {
                    if (fCanonical) {
                        fOut.print("&#");
                        fOut.print(Integer.toString(c));
                        fOut.print(';');
                        break;
                    }
                }
            default:
                {
                    fOut.print(c);
                }
        }
    }

    /** Main program entry point. */
    public static void main(String argv[]) {
        if (argv.length == 0) {
            printUsage();
            System.exit(1);
        }
        Writer writer = null;
        ParserWrapper parser = null;
        boolean namespaces = DEFAULT_NAMESPACES;
        boolean validation = DEFAULT_VALIDATION;
        boolean schemaValidation = DEFAULT_SCHEMA_VALIDATION;
        boolean schemaFullChecking = DEFAULT_SCHEMA_FULL_CHECKING;
        boolean canonical = DEFAULT_CANONICAL;
        for (int i = 0; i < argv.length; i++) {
            String arg = argv[i];
            if (arg.startsWith("-")) {
                String option = arg.substring(1);
                if (option.equals("p")) {
                    if (++i == argv.length) {
                        System.err.println("error: Missing argument to -p option.");
                    }
                    String parserName = argv[i];
                    try {
                        parser = (ParserWrapper) Class.forName(parserName).newInstance();
                    } catch (Exception e) {
                        parser = null;
                        System.err.println("error: Unable to instantiate parser (" + parserName + ")");
                    }
                    continue;
                }
                if (option.equalsIgnoreCase("n")) {
                    namespaces = option.equals("n");
                    continue;
                }
                if (option.equalsIgnoreCase("v")) {
                    validation = option.equals("v");
                    continue;
                }
                if (option.equalsIgnoreCase("s")) {
                    schemaValidation = option.equals("s");
                    continue;
                }
                if (option.equalsIgnoreCase("f")) {
                    schemaFullChecking = option.equals("f");
                    continue;
                }
                if (option.equalsIgnoreCase("c")) {
                    canonical = option.equals("c");
                    continue;
                }
                if (option.equals("h")) {
                    printUsage();
                    continue;
                }
            }
            if (parser == null) {
                try {
                    parser = (ParserWrapper) Class.forName(DEFAULT_PARSER_NAME).newInstance();
                } catch (Exception e) {
                    System.err.println("error: Unable to instantiate parser (" + DEFAULT_PARSER_NAME + ")");
                    continue;
                }
            }
            try {
                parser.setFeature(NAMESPACES_FEATURE_ID, namespaces);
            } catch (SAXException e) {
                System.err.println("warning: Parser does not support feature (" + NAMESPACES_FEATURE_ID + ")");
            }
            try {
                parser.setFeature(VALIDATION_FEATURE_ID, validation);
            } catch (SAXException e) {
                System.err.println("warning: Parser does not support feature (" + VALIDATION_FEATURE_ID + ")");
            }
            try {
                parser.setFeature(SCHEMA_VALIDATION_FEATURE_ID, schemaValidation);
            } catch (SAXException e) {
                System.err.println("warning: Parser does not support feature (" + SCHEMA_VALIDATION_FEATURE_ID + ")");
            }
            try {
                parser.setFeature(SCHEMA_FULL_CHECKING_FEATURE_ID, schemaFullChecking);
            } catch (SAXException e) {
                System.err.println("warning: Parser does not support feature (" + SCHEMA_FULL_CHECKING_FEATURE_ID + ")");
            }
            if (writer == null) {
                writer = new Writer();
                try {
                    writer.setOutput(System.out, "UTF8");
                } catch (UnsupportedEncodingException e) {
                    System.err.println("error: Unable to set output. Exiting.");
                    System.exit(1);
                }
            }
            writer.setCanonical(canonical);
            try {
                Document document = parser.parse(arg);
                writer.write(document);
            } catch (SAXParseException e) {
            } catch (Exception e) {
                System.err.println("error: Parse error occurred - " + e.getMessage());
                if (e instanceof SAXException) {
                    e = ((SAXException) e).getException();
                }
                e.printStackTrace(System.err);
            }
        }
    }

    /** Prints the usage. */
    private static void printUsage() {
        System.err.println("usage: java dom.Writer (options) uri ...");
        System.err.println();
        System.err.println("options:");
        System.err.println("  -p name  Select parser by name.");
        System.err.println("  -n | -N  Turn on/off namespace processing.");
        System.err.println("  -v | -V  Turn on/off validation.");
        System.err.println("  -s | -S  Turn on/off Schema validation support.");
        System.err.println("           NOTE: Not supported by all parsers.");
        System.err.println("  -f  | -F Turn on/off Schema full checking.");
        System.err.println("           NOTE: Requires use of -s and not supported by all parsers.");
        System.err.println("  -c | -C  Turn on/off Canonical XML output.");
        System.err.println("           NOTE: This is not W3C canonical output.");
        System.err.println("  -h       This help screen.");
        System.err.println();
        System.err.println("defaults:");
        System.err.println("  Parser:     " + DEFAULT_PARSER_NAME);
        System.err.print("  Namespaces: ");
        System.err.println(DEFAULT_NAMESPACES ? "on" : "off");
        System.err.print("  Validation: ");
        System.err.println(DEFAULT_VALIDATION ? "on" : "off");
        System.err.print("  Schema:     ");
        System.err.println(DEFAULT_SCHEMA_VALIDATION ? "on" : "off");
        System.err.print("  Schema full checking:     ");
        System.err.println(DEFAULT_SCHEMA_FULL_CHECKING ? "on" : "off");
        System.err.print("  Canonical:  ");
        System.err.println(DEFAULT_CANONICAL ? "on" : "off");
    }
}
