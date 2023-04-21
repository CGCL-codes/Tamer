package jaga.testing.pj;

import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import org.w3c.dom.*;
import java.io.*;

/**
 * This is a program to echo a DOM tree using DOM Level 2 interfaces.  Use
 * JAXP to load an XML file and create a DOM tree.  DOM currently does not
 * provide a method to do this.  (This is planned for Level 3.)  See the
 * method "main" for the three basic steps.  Once the application obtains a
 * DOM Document tree, it dumps out the nodes in the tree and associated
 * node attributes for each node.
 *
 * This program also shows how to validate a document along with using an
 * ErrorHandler to capture validation errors.
 *
 * Note: Program flags may be used to create non-conformant but possibly
 * useful DOM trees.  In some cases, particularly with element content
 * whitespace, applications may not want to rely on JAXP to filter out
 * these nodes but may want to skip the nodes themselves so the application
 * will be more robust.
 *
 * @author Edwin Goei
 */
public class LoadPopXML {

    /** All output will use this encoding */
    static final String outputEncoding = "UTF-8";

    /** Output goes here */
    private PrintWriter out;

    /** Indent level */
    private int indent = 0;

    /** Indentation will be in multiples of basicIndent  */
    private final String basicIndent = "  ";

    /** Constants used for JAXP 1.2 */
    static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";

    static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";

    LoadPopXML(PrintWriter out) {
        this.out = out;
    }

    protected int generation = 0;

    protected boolean inPop2Dump = false;

    protected boolean dumpNow = false;

    protected final int GENERATION_SKIP = 100;

    protected String lastSeenGenotype = null;

    /**
     * Echo common attributes of a DOM2 Node and terminate output with an
     * EOL character.
     */
    private void printlnCommon(Node n) {
        String narrator = "";
        out.print(" nodeName=\"" + n.getNodeName() + "\"");
        String val = n.getNamespaceURI();
        if (val != null) {
            out.print(" uri=\"" + val + "\"");
        }
        val = n.getPrefix();
        if (val != null) {
            out.print(" pre=\"" + val + "\"");
        }
        val = n.getLocalName();
        if (val != null) {
            out.print(" local=\"" + val + "\"");
        }
        val = n.getNodeValue();
        if (val != null) {
            out.print(" nodeValue=");
            if (val.trim().equals("")) {
            } else {
                out.print("\"" + n.getNodeValue() + "\"");
            }
        }
        out.println();
    }

    /**
     * Indent to the current level in multiples of basicIndent
     */
    private void outputIndentation() {
        for (int i = 0; i < indent; i++) {
            out.print(basicIndent);
        }
    }

    /**
     * Recursive routine to print out DOM tree nodes
     */
    private void echo(Node n) {
        outputIndentation();
        int type = n.getNodeType();
        switch(type) {
            case Node.ATTRIBUTE_NODE:
                out.print("ATTR:");
                printlnCommon(n);
                break;
            case Node.CDATA_SECTION_NODE:
                out.print("CDATA:");
                printlnCommon(n);
                break;
            case Node.COMMENT_NODE:
                out.print("COMM:");
                printlnCommon(n);
                break;
            case Node.DOCUMENT_FRAGMENT_NODE:
                out.print("DOC_FRAG:");
                printlnCommon(n);
                break;
            case Node.DOCUMENT_NODE:
                out.print("DOC:");
                printlnCommon(n);
                break;
            case Node.DOCUMENT_TYPE_NODE:
                out.print("DOC_TYPE:");
                printlnCommon(n);
                NamedNodeMap nodeMap = ((DocumentType) n).getEntities();
                indent += 2;
                for (int i = 0; i < nodeMap.getLength(); i++) {
                    Entity entity = (Entity) nodeMap.item(i);
                    echo(entity);
                }
                indent -= 2;
                break;
            case Node.ELEMENT_NODE:
                String name = n.getNodeName();
                if (name.equals("g")) {
                    dumpNow = true;
                }
                if (name.equals("pop")) {
                    if (lastSeenGenotype != null) {
                        out.println(lastSeenGenotype);
                        lastSeenGenotype = null;
                    }
                    generation++;
                    out.println("Generation: " + generation);
                }
                break;
            case Node.ENTITY_NODE:
                out.print("ENT:");
                printlnCommon(n);
                break;
            case Node.ENTITY_REFERENCE_NODE:
                out.print("ENT_REF:");
                printlnCommon(n);
                break;
            case Node.NOTATION_NODE:
                out.print("NOTATION:");
                printlnCommon(n);
                break;
            case Node.PROCESSING_INSTRUCTION_NODE:
                out.print("PROC_INST:");
                printlnCommon(n);
                break;
            case Node.TEXT_NODE:
                if (dumpNow) {
                    lastSeenGenotype = n.getNodeValue();
                    dumpNow = false;
                }
                break;
            default:
                out.print("UNSUPPORTED NODE: " + type);
                printlnCommon(n);
                break;
        }
        if ((generation % GENERATION_SKIP) == 0) {
            indent++;
            for (Node child = n.getFirstChild(); child != null; child = child.getNextSibling()) {
                echo(child);
            }
            indent--;
        }
    }

    private static void usage() {
        System.err.println("Usage: DOMEcho [-opts] <filename>");
        System.err.println("       -dtd = DTD validation");
        System.err.println("       -xsd = W3C XML Schema validation using xsi: hints in instance doc");
        System.err.println("       -ws = do not create element content whitespace nodes");
        System.err.println("       -co[mments] = do not create comment nodes");
        System.err.println("       -cd[ata] = put CDATA into Text nodes");
        System.err.println("       -e[ntity-ref] = create EntityReference nodes");
        System.err.println("       -usage or -help = this message");
        System.exit(1);
    }

    public static void main(String[] args) throws Exception {
        String filename = null;
        boolean dtdValidate = false;
        boolean xsdValidate = false;
        boolean ignoreWhitespace = false;
        boolean ignoreComments = false;
        boolean putCDATAIntoText = false;
        boolean createEntityRefs = false;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-dtd")) {
                dtdValidate = true;
            } else if (args[i].equals("-xsd")) {
                xsdValidate = true;
            } else if (args[i].equals("-ws")) {
                ignoreWhitespace = true;
                System.out.println("Ignoring ws");
            } else if (args[i].startsWith("-co")) {
                ignoreComments = true;
            } else if (args[i].startsWith("-cd")) {
                putCDATAIntoText = true;
            } else if (args[i].startsWith("-e")) {
                createEntityRefs = true;
            } else if (args[i].equals("-usage")) {
                usage();
            } else if (args[i].equals("-help")) {
                usage();
            } else {
                filename = args[i];
                if (i != args.length - 1) {
                    usage();
                }
            }
        }
        if (filename == null) {
            usage();
        }
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        dbf.setValidating(dtdValidate || xsdValidate);
        if (xsdValidate) {
            try {
                dbf.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
            } catch (IllegalArgumentException x) {
                System.err.println("Error: JAXP DocumentBuilderFactory attribute not recognized: " + JAXP_SCHEMA_LANGUAGE);
                System.err.println("Check to see if parser conforms to JAXP 1.2 spec.");
                System.exit(1);
            }
        }
        dbf.setIgnoringComments(ignoreComments);
        dbf.setIgnoringElementContentWhitespace(ignoreWhitespace);
        dbf.setCoalescing(putCDATAIntoText);
        dbf.setExpandEntityReferences(!createEntityRefs);
        DocumentBuilder db = dbf.newDocumentBuilder();
        OutputStreamWriter errorWriter = new OutputStreamWriter(System.err, outputEncoding);
        db.setErrorHandler(new MyErrorHandler(new PrintWriter(errorWriter, true)));
        Document doc = db.parse(new File(filename));
        OutputStreamWriter outWriter = new OutputStreamWriter(System.out, outputEncoding);
        new LoadPopXML(new PrintWriter(outWriter, true)).echo(doc);
    }

    private static class MyErrorHandler implements ErrorHandler {

        /** Error handler output goes here */
        private PrintWriter out;

        MyErrorHandler(PrintWriter out) {
            this.out = out;
        }

        /**
         * Returns a string describing parse exception details
         */
        private String getParseExceptionInfo(SAXParseException spe) {
            String systemId = spe.getSystemId();
            if (systemId == null) {
                systemId = "null";
            }
            String info = "URI=" + systemId + " Line=" + spe.getLineNumber() + ": " + spe.getMessage();
            return info;
        }

        public void warning(SAXParseException spe) throws SAXException {
            out.println("Warning: " + getParseExceptionInfo(spe));
        }

        public void error(SAXParseException spe) throws SAXException {
            String message = "Error: " + getParseExceptionInfo(spe);
            throw new SAXException(message);
        }

        public void fatalError(SAXParseException spe) throws SAXException {
            String message = "Fatal Error: " + getParseExceptionInfo(spe);
            throw new SAXException(message);
        }
    }
}
