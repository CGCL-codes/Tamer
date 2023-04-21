package com.jcorporate.expresso.ext.report;

import com.jcorporate.expresso.core.misc.StringUtil;
import com.jcorporate.expresso.kernel.exception.ExpressoRuntimeException;
import com.jcorporate.expresso.kernel.management.DOMWriter;
import com.jcorporate.expresso.kernel.util.FastStringBuffer;
import org.apache.log4j.Logger;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Entity;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.StringTokenizer;

/**
 * XMLPrinter is a simple (DOM) parser that outputs XML to a Writer stream.
 *
 * @author		David Lloyd
 */
public class XMLPrinter implements ErrorHandler, DOMWriter {

    protected static Logger log;

    public static final OutputStreamWriter SYSTEM_OUT = new OutputStreamWriter(System.out);

    public static final String DEFAULT_INDENT = "   ";

    /** The writer that will contain the XML written. */
    protected Writer _out = null;

    /** The indention prefix string. */
    protected String _indent = DEFAULT_INDENT;

    /** The character to use for indenting. */
    protected char _indentChar = ' ';

    /** The virtual area occupied by an indention. */
    protected int _indentLength = 0;

    /** The current indention level. */
    protected int _column = 0;

    /** Set to keep the &lt;?xml ... header from printing. */
    protected boolean _omitXmlDecl = false;

    /** The character sequence for a newline. */
    protected String _newline = System.getProperty("line.separator");

    public XMLPrinter() {
        log = Logger.getLogger(XMLPrinter.class);
        setWriter(SYSTEM_OUT);
    }

    /**
     * Override to actually save a DOM document to the output stream via whatever
     * method you desire
     * @param os The output stream to save to
     * @param document the DOM document representing the config.
     * @throws ExpressoRuntimeException if there's an error saving the file.
     */
    public void saveDocument(OutputStream os, Document document) throws ExpressoRuntimeException {
        try {
            setWriter(new OutputStreamWriter(os));
            outputDocument(document.getDocumentElement());
            getWriter().flush();
        } catch (IOException ioe) {
            throw new ExpressoRuntimeException(ioe);
        }
    }

    /**
     * Retrieve a class that must exist in the classpath for this to work.
     * Used as a sanity check to make sure the appropriate jars are installed.
     * @return java.lang.String a name of a class
     */
    public String getRequiredClass() {
        return "org.w3c.dom.Document";
    }

    /** Output the document whose root element is the specified node. */
    public void outputDocument(Node node) throws IOException {
        if (!_omitXmlDecl && node.getNodeType() == Node.ELEMENT_NODE) {
            println("<?xml version='1.0' ?>");
            println("");
        }
        printTree(node);
    }

    /** Set the output writer. */
    public void setWriter(Writer out) {
        this._out = out;
    }

    /** Get the output writer. */
    public Writer getWriter() {
        return this._out;
    }

    /** Set the indentation size to the number of characters specified using the
     *  current indent character.
     */
    public void setIndentSize(int indentSize) {
        _indent = "";
        _indentLength = 0;
        int inc;
        if (_indentChar == '\t') inc = 4; else inc = 1;
        for (int i = 0; i < indentSize; i++) {
            _indent += _indentChar;
            _indentLength += inc;
        }
    }

    /** Set the indent semantics.
     * @param indentChar The character to use for indentions.
     * @param size       The number of characters to use for each indention level.
     */
    public void setIndent(char indentChar, int size) {
        _indentChar = indentChar;
        setIndentSize(size);
    }

    /** Set whether the xml declaration should be output at the start of a document. */
    public void setOmitXmlDecl(boolean b) {
        _omitXmlDecl = b;
    }

    /** Closes the output writer ignoring any errors recieved. You are encouraged
     *  to close the writer yourself to recieve any errors encountered.
     */
    public void closeWriter() {
        try {
            this._out.close();
        } catch (IOException ioe) {
        }
    }

    /** Creates an output writer that will create the file specified and any
     *  parent directories needed to do so.
     */
    public void setFile(File file) throws IOException {
        File parentDirectory = file.getParentFile();
        if (parentDirectory != null && !parentDirectory.exists()) {
            parentDirectory.mkdirs();
        }
        setWriter(new BufferedWriter(new FileWriter(file)));
    }

    /** Creates an output writer that will create the file specified and any
     *  parent directories needed to do so.
     */
    public void setFile(String filename) throws IOException {
        setFile(new File(filename));
    }

    /**
     * Return the given node as a string.  If any error occurs
     * in processing (likely an io exception from outputDocument()),
     * null will be returned.
     **/
    public static String nodeToString(Node node) {
        try {
            XMLPrinter printer = new XMLPrinter();
            StringWriter writer = new StringWriter();
            printer.setWriter(writer);
            printer.outputDocument(node);
            return writer.getBuffer().toString();
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Parse an string and convert it to xml style
     * @param html The string to be parsed
     * @return The resulting xml string
     */
    public static String toXML(String html) {
        return toXML(html, true);
    }

    /**
     * Parse an string and convert it to xml style
     * @param html The string to be parsed
     * @param escapeAll If false, the semicolon, apostrophe, and quote are left alone (useful for text nodes).
     * @return The resulting xml string
     */
    public static String toXML(String html, boolean escapeAll) {
        if (html == null) return "";
        FastStringBuffer parsedString = new FastStringBuffer(html.length());
        String delim;
        delim = "&;><'\"";
        StringTokenizer st;
        st = new StringTokenizer(html, delim, true);
        String token;
        while (st.hasMoreTokens()) {
            token = st.nextToken();
            if (token.equals("&")) token = "&amp;"; else if (token.equals(">")) token = "&gt;"; else if (token.equals("<")) token = "&lt;"; else if (escapeAll) {
                if (token.equals(";")) token = "&semi;"; else if (token.equals("'")) token = "&apos;"; else if (token.equals("\"")) token = "&quot;";
            }
            parsedString.append(token);
        }
        return parsedString.toString();
    }

    /** Prints the string to the output at an indentation.
     * @param string The string to print.
     * @param indent The indentation to prefix the string.
     */
    public void print(String string, String indent) throws IOException {
        print(indent);
        if (string != null) print(string.trim());
    }

    /** Prints the string to the output at an indentation following it with a newline.
     * @param string The string to print.
     * @param indent The indentation to prefix the string.
     */
    public void println(String string, String indent) throws IOException {
        print(indent);
        if (string != null) println(string.trim()); else println("");
    }

    /** Prints the string to the output.
     * @param string The string to print.
     */
    public void print(String string) throws IOException {
        this._out.write(string);
    }

    /** Prints the string to the output following it with a newline.
     * @param string The string to print.
     */
    public void println(String string) throws IOException {
        print(string);
        print(_newline);
    }

    /** Get the normal indentation for a level.
     * @param col The level of indentation.
     */
    protected String getIndent(int col) {
        String indent = "";
        String indent2x = this._indent + this._indent;
        for (int i = (col & 0x7ffffe); i > 0; i -= 2) {
            indent += indent2x;
        }
        if ((col & 0x01) == 1) indent += this._indent;
        return indent;
    }

    /** Output the node (and children) at the specified indentation level. */
    protected void printTree(Node node, int col) throws IOException {
        int old = _column;
        _column = col;
        printTree(node);
        _column = old;
    }

    /** Output the node (and children) at the current indentation level. */
    protected void printTree(Node node) throws IOException {
        int nodeType = -1;
        if (node != null) {
            nodeType = node.getNodeType();
            switch(nodeType) {
                case Node.DOCUMENT_NODE:
                    {
                        NodeList nodes = node.getChildNodes();
                        if (nodes != null) {
                            for (int i = 0; i < nodes.getLength(); i++) {
                                printTree(nodes.item(i));
                            }
                        }
                        break;
                    }
                case Node.ELEMENT_NODE:
                    {
                        String name = node.getNodeName();
                        this.print("<" + name, getIndent(this._column));
                        NamedNodeMap attributes = node.getAttributes();
                        for (int i = 0; i < attributes.getLength(); i++) {
                            Attr current = (Attr) attributes.item(i);
                            if (current.getSpecified() == true) {
                                this.print(" " + current.getNodeName() + "='" + current.getNodeValue() + "'");
                            }
                        }
                        if (!node.hasChildNodes()) {
                            this.println(" />");
                        } else {
                            this.print(">");
                            NodeList children = node.getChildNodes();
                            boolean hasChildElements = false;
                            if (children != null) {
                                int len = children.getLength();
                                for (int i = 0; i < len; i++) {
                                    if (children.item(i).getNodeType() != Node.TEXT_NODE) {
                                        hasChildElements = true;
                                        break;
                                    }
                                }
                            }
                            if (hasChildElements) this.println("");
                            this._column++;
                            for (int i = 0; i < children.getLength(); i++) {
                                printTree(children.item(i));
                            }
                            this._column--;
                            if (hasChildElements) this.println("</" + name + ">", getIndent(this._column)); else this.println("</" + name + ">");
                        }
                        break;
                    }
                case Node.TEXT_NODE:
                    {
                        String nodeValue = node.getNodeValue().trim();
                        if (!nodeValue.equals("")) {
                            this.print(toXML(nodeValue));
                        }
                        break;
                    }
                case Node.CDATA_SECTION_NODE:
                    {
                        this.print("<![CDATA[", getIndent(this._column));
                        this.print(convertNewline(node.getNodeValue()));
                        this.println("]]>");
                        break;
                    }
                case Node.PROCESSING_INSTRUCTION_NODE:
                    {
                        if (node.getNodeName() != null) {
                            if (!_omitXmlDecl && (false == node.getNodeName().startsWith("xml")) && (false == node.getNodeName().startsWith("xsl"))) {
                                this.println("<?xml " + node.getNodeName() + "=\"" + node.getNodeValue() + "\"?>");
                            } else {
                                if (!_omitXmlDecl || !"xml".equals(node.getNodeName())) this.println("<?" + node.getNodeName() + " " + node.getNodeValue() + " ?>");
                            }
                        }
                        break;
                    }
                case Node.ENTITY_REFERENCE_NODE:
                    {
                        this.println("&" + node.getNodeName() + ";");
                        break;
                    }
                case Node.DOCUMENT_TYPE_NODE:
                    {
                        DocumentType docType = (DocumentType) node;
                        this.print("<!DOCTYPE " + docType.getName());
                        if (docType.getPublicId() != null) {
                            this.print(" PUBLIC ");
                        } else if (docType.getSystemId() != null) {
                            this.print(" SYSTEM ");
                        }
                        if (docType.getPublicId() != null) {
                            this.print("\"" + docType.getPublicId() + "\" ");
                        }
                        if (docType.getSystemId() != null) {
                            this.print("\"" + docType.getSystemId() + "\" ");
                        }
                        NamedNodeMap nodes = docType.getEntities();
                        for (int i = 0; i < nodes.getLength(); i++) {
                            this.println("");
                            Entity entity = (Entity) nodes.item(i);
                            this.print(" [<!ENTITY " + entity.getNodeName() + " ");
                            NodeList children = entity.getChildNodes();
                            if (children != null && children.getLength() > 0) this.print("\"" + XMLPrinter.nodeToString(children.item(0)) + "\">]"); else this.print("\"" + entity.getNodeValue() + "\">]");
                        }
                        this.println("");
                        this.println(">");
                        break;
                    }
            }
        }
        this._out.flush();
    }

    /** Convert newlines to what we want. */
    protected String convertNewline(String text) {
        text = StringUtil.replace(text, "\r\n", "\n");
        text = StringUtil.replace(text, "\n", _newline);
        return text;
    }

    public static int run(String[] args, XMLPrinter printer) {
        String filename = null;
        String outputname = null;
        File tempOut = null;
        try {
            if (System.getProperty("log4j.configuration") == null || System.getProperty("log4j.configuration").trim().length() == 0) {
                System.err.println("ERROR: Logging will not work - 'log4j.configuration' must be defined.");
            } else {
                org.apache.log4j.PropertyConfigurator.configureAndWatch(System.getProperty("log4j.configuration"));
            }
            for (int i = 0; i < args.length; i++) {
                if (args[i].toLowerCase().startsWith("-omitxml=")) {
                    printer.setOmitXmlDecl(StringUtil.toBoolean(args[i].substring(9)));
                } else if (args[i].toLowerCase().equals("-out") && args.length > i + 1) {
                    outputname = args[++i];
                } else if (args[i].toLowerCase().equals("-in") && args.length > i + 1) {
                    filename = args[++i];
                }
            }
            if (filename == null) {
                log.error("No input file specified.");
                return (2);
            }
            File inputfile = new File(filename);
            Document doc = null;
            java.io.Reader inputReader = null;
            try {
                inputReader = new DocBookFilterReader(new java.io.FileReader(inputfile));
                InputSource inputSource = new InputSource(inputReader);
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                dbf.setNamespaceAware(true);
                dbf.setValidating(false);
                DocumentBuilder db = dbf.newDocumentBuilder();
                db.setErrorHandler(printer);
                doc = db.parse(inputSource);
            } catch (Exception e) {
                log.error("Error Parsing XML Document.", e);
                return (1);
            } finally {
                try {
                    inputReader.close();
                } catch (Throwable t) {
                }
            }
            File outputfile = null;
            if (outputname != null) outputfile = new File(outputname); else outputfile = inputfile;
            tempOut = File.createTempFile(outputfile.getName() + "-", ".xmlpp", outputfile.getParentFile());
            try {
                Writer outfilewriter = new java.io.FileWriter(tempOut);
                printer.setWriter(new DocBookFilterWriter(outfilewriter));
                printer.outputDocument(doc.getDocumentElement());
            } catch (Exception e) {
                log.error("Error formatting XML Document.", e);
                return (1);
            } finally {
                try {
                    printer.getWriter().close();
                } catch (Throwable t) {
                }
            }
            if (outputname == null) {
                File bak = new File(inputfile.getParentFile(), inputfile.getName() + ".bak");
                if (!inputfile.renameTo(bak) || inputfile.exists()) {
                    bak = File.createTempFile(inputfile.getName() + "-", ".xmlpp", inputfile.getParentFile());
                    log.warn("Can't rename input to *.bak  -  trying copy to " + bak.getName());
                    try {
                        copyFile(inputfile, bak);
                        log.info("input file backed up");
                    } catch (IOException ioe) {
                        tempOut = null;
                        log.error("Unable to backup the input file. The output was left in " + tempOut.getAbsolutePath(), ioe);
                        return 1;
                    }
                }
            }
            try {
                copyFile(tempOut, outputfile);
            } catch (IOException ioe) {
                log.error("Unable to overwrite output file " + outputfile.getAbsolutePath());
                return 1;
            }
        } catch (Exception e) {
            log.error("Error prettying XML Document.", e);
            return (1);
        } finally {
            if (tempOut != null) {
                try {
                    if (tempOut.delete()) return 0;
                } catch (Throwable t) {
                }
                try {
                    log.warn("A temporary file was left on disk - " + tempOut.getAbsolutePath());
                } catch (Throwable t) {
                    log.warn("A temporary file was left on disk - ");
                }
            }
        }
        return 0;
    }

    public static int run(String[] args) {
        org.apache.log4j.BasicConfigurator.configure();
        XMLPrinter printer = new XMLPrinter();
        return run(args, printer);
    }

    public static void main(String[] args) {
        System.exit(run(args));
    }

    protected static void copyFile(File in, File out) throws IOException {
        java.io.FileWriter filewriter = null;
        java.io.FileReader filereader = null;
        try {
            filewriter = new java.io.FileWriter(out);
            filereader = new java.io.FileReader(in);
            char[] buf = new char[4096];
            int nread = filereader.read(buf, 0, 4096);
            while (nread >= 0) {
                filewriter.write(buf, 0, nread);
                nread = filereader.read(buf, 0, 4096);
            }
            buf = null;
        } finally {
            try {
                filereader.close();
            } catch (Throwable t) {
            }
            try {
                filewriter.close();
            } catch (Throwable t) {
            }
        }
    }

    /**
     * Issue a warning on parsing errors
     * @param ex A Sax Parse Exception event
     */
    public void warning(SAXParseException ex) {
        log.warn(getLocationString(ex) + ": " + ex.getMessage());
    }

    /**
     * Issue an error
     * @param ex A Sax Parse Exception event
     */
    public void error(SAXParseException ex) {
        log.error(getLocationString(ex) + ": " + ex.getMessage());
    }

    /**
     * Fatal error. Used Internally for parsing only
     * @param ex A Sax Parse Exception event
     * @throws SAXException after logging the Parsing Exception
     */
    public void fatalError(SAXParseException ex) throws SAXException {
        log.error(getLocationString(ex) + ": " + ex.getMessage());
        throw ex;
    }

    /**
     * Returns a string of the location. Used Internally For Parsing Only
     * @param ex A Sax Parse Exception event
     * @return java.lang.String
     */
    private String getLocationString(SAXParseException ex) {
        FastStringBuffer str = new FastStringBuffer(128);
        String systemId = ex.getSystemId();
        if (systemId != null) {
            int index = systemId.lastIndexOf('/');
            if (index != -1) {
                systemId = systemId.substring(index + 1);
            }
            str.append(systemId);
        }
        str.append(':');
        str.append(ex.getLineNumber());
        str.append(':');
        str.append(ex.getColumnNumber());
        return str.toString();
    }
}
