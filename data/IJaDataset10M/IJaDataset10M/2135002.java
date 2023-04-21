package org.baselinetest.comparator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import org.baselinetest.TestComparator;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * A class for comparing XML files for differences. The XMLComparator will pretty-print the expected and actual test
 * output, and do a textual comparison of the pretty-printed output. It does not attempt to determine any type of
 * logical equivalency.
 * @author jwan
 */
public class XMLComparator implements TestComparator {

    /**
     * Namespaces feature id (http://xml.org/sax/features/namespaces).
     */
    protected static final String NAMESPACES_FEATURE_ID = "http://xml.org/sax/features/namespaces";

    /**
     * Namespace prefixes feature id (http://xml.org/sax/features/namespace-prefixes).
     */
    protected static final String NAMESPACE_PREFIXES_FEATURE_ID = "http://xml.org/sax/features/namespace-prefixes";

    /**
     * Validation feature id (http://xml.org/sax/features/validation).
     */
    protected static final String VALIDATION_FEATURE_ID = "http://xml.org/sax/features/validation";

    /**
     * Schema validation feature id (http://apache.org/xml/features/validation/schema).
     */
    protected static final String SCHEMA_VALIDATION_FEATURE_ID = "http://apache.org/xml/features/validation/schema";

    /**
     * Schema full checking feature id (http://apache.org/xml/features/validation/schema-full-checking).
     */
    protected static final String SCHEMA_FULL_CHECKING_FEATURE_ID = "http://apache.org/xml/features/validation/schema-full-checking";

    /**
     * Validate schema annotations feature id (http://apache.org/xml/features/validate-annotations)
     */
    protected static final String VALIDATE_ANNOTATIONS_ID = "http://apache.org/xml/features/validate-annotations";

    /**
     * Generate synthetic schema annotations feature id (http://apache.org/xml/features/generate-synthetic-annotations).
     */
    protected static final String GENERATE_SYNTHETIC_ANNOTATIONS_ID = "http://apache.org/xml/features/generate-synthetic-annotations";

    /**
     * Dynamic validation feature id (http://apache.org/xml/features/validation/dynamic).
     */
    protected static final String DYNAMIC_VALIDATION_FEATURE_ID = "http://apache.org/xml/features/validation/dynamic";

    /**
     * Load external DTD feature id (http://apache.org/xml/features/nonvalidating/load-external-dtd).
     */
    protected static final String LOAD_EXTERNAL_DTD_FEATURE_ID = "http://apache.org/xml/features/nonvalidating/load-external-dtd";

    /**
     * XInclude feature id (http://apache.org/xml/features/xinclude).
     */
    protected static final String XINCLUDE_FEATURE_ID = "http://apache.org/xml/features/xinclude";

    /**
     * XInclude fixup base URIs feature id (http://apache.org/xml/features/xinclude/fixup-base-uris).
     */
    protected static final String XINCLUDE_FIXUP_BASE_URIS_FEATURE_ID = "http://apache.org/xml/features/xinclude/fixup-base-uris";

    /**
     * XInclude fixup language feature id (http://apache.org/xml/features/xinclude/fixup-language).
     */
    protected static final String XINCLUDE_FIXUP_LANGUAGE_FEATURE_ID = "http://apache.org/xml/features/xinclude/fixup-language";

    /**
     * Lexical handler property id (http://xml.org/sax/properties/lexical-handler).
     */
    protected static final String LEXICAL_HANDLER_PROPERTY_ID = "http://xml.org/sax/properties/lexical-handler";

    /**
     * Default parser name.
     */
    protected static final String DEFAULT_PARSER_NAME = "org.apache.xerces.parsers.SAXParser";

    /**
     * Default namespaces support (true).
     */
    protected static final boolean DEFAULT_NAMESPACES = true;

    /**
     * Default namespace prefixes (false).
     */
    protected static final boolean DEFAULT_NAMESPACE_PREFIXES = false;

    /**
     * Default validation support (false).
     */
    protected static final boolean DEFAULT_VALIDATION = false;

    /**
     * Default load external DTD (true).
     */
    protected static final boolean DEFAULT_LOAD_EXTERNAL_DTD = true;

    /**
     * Default Schema validation support (false).
     */
    protected static final boolean DEFAULT_SCHEMA_VALIDATION = false;

    /**
     * Default Schema full checking support (false).
     */
    protected static final boolean DEFAULT_SCHEMA_FULL_CHECKING = false;

    /**
     * Default validate schema annotations (false).
     */
    protected static final boolean DEFAULT_VALIDATE_ANNOTATIONS = false;

    /**
     * Default generate synthetic schema annotations (false).
     */
    protected static final boolean DEFAULT_GENERATE_SYNTHETIC_ANNOTATIONS = false;

    /**
     * Default dynamic validation support (false).
     */
    protected static final boolean DEFAULT_DYNAMIC_VALIDATION = false;

    /**
     * Default XInclude processing support (false).
     */
    protected static final boolean DEFAULT_XINCLUDE = false;

    /**
     * Default XInclude fixup base URIs support (true).
     */
    protected static final boolean DEFAULT_XINCLUDE_FIXUP_BASE_URIS = true;

    /**
     * Default XInclude fixup language support (true).
     */
    protected static final boolean DEFAULT_XINCLUDE_FIXUP_LANGUAGE = true;

    /**
     * Default canonical output (false).
     */
    protected static final boolean DEFAULT_CANONICAL = false;

    public XMLComparator() {
    }

    /**
     * Perform a comparison of two XML files.
     * @param expectedOutputFile the expected test output.
     * @param actualOutputFile the test output.
     * @return true if the files represent equivalent XML data structures.
     */
    public boolean compare(File expectedOutputFile, File actualOutputFile) {
        boolean match = false;
        try {
            File actualOutputDirectory = actualOutputFile.getParentFile();
            String expectedOutputFilename = expectedOutputFile.getName();
            File newExpectedOutputFile = new File(actualOutputDirectory, "cleanedexpected." + expectedOutputFilename + ".xml");
            createPrettyPrintedFile(expectedOutputFile, newExpectedOutputFile);
            String actualOutputFilename = actualOutputFile.getName();
            File newActualOutputFile = new File(actualOutputDirectory, "cleanedactual." + actualOutputFilename + ".xml");
            createPrettyPrintedFile(actualOutputFile, newActualOutputFile);
            TextComparator textComparator = new TextComparator();
            match = textComparator.compare(newExpectedOutputFile, newActualOutputFile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return match;
    }

    public void createPrettyPrintedFile(File inputFile, File outputFile) throws IOException, SAXException {
        Writer writer = new Writer();
        FileWriter fileWriter = new FileWriter(outputFile);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        try {
            writer.setOutput(bufferedWriter);
            XMLReader parser = null;
            parser = XMLReaderFactory.createXMLReader(DEFAULT_PARSER_NAME);
            parser.setContentHandler(writer);
            parser.setErrorHandler(writer);
            try {
                parser.setProperty(LEXICAL_HANDLER_PROPERTY_ID, writer);
            } catch (SAXException e) {
            }
            writer.setCanonical(DEFAULT_CANONICAL);
            FileReader fileReader = new FileReader(inputFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            try {
                InputSource inputSource = new InputSource(bufferedReader);
                parser.parse(inputSource);
            } finally {
                bufferedReader.close();
            }
        } finally {
            bufferedWriter.close();
        }
    }

    static class Writer extends DefaultHandler implements LexicalHandler {

        /**
         * Print writer.
         */
        protected PrintWriter fOut;

        /**
         * Canonical output.
         */
        protected boolean fCanonical;

        /**
         * Element depth.
         */
        protected int fElementDepth;

        /**
         * Document locator.
         */
        protected Locator fLocator;

        /**
         * Processing XML 1.1 document.
         */
        protected boolean fXML11;

        /**
         * In CDATA section.
         */
        protected boolean fInCDATA;

        /**
         * Default constructor.
         */
        public Writer() {
        }

        /**
         * Sets whether output is canonical.
         */
        public void setCanonical(boolean canonical) {
            fCanonical = canonical;
        }

        /**
         * Sets the output stream for printing.
         */
        public void setOutput(OutputStream stream, String encoding) throws UnsupportedEncodingException {
            if (encoding == null) {
                encoding = "UTF8";
            }
            java.io.Writer writer = new OutputStreamWriter(stream, encoding);
            fOut = new PrintWriter(writer);
        }

        /**
         * Sets the output writer.
         */
        public void setOutput(java.io.Writer writer) {
            fOut = writer instanceof PrintWriter ? (PrintWriter) writer : new PrintWriter(writer);
        }

        /**
         * Set Document Locator.
         */
        public void setDocumentLocator(Locator locator) {
            fLocator = locator;
        }

        /**
         * Start document.
         */
        public void startDocument() throws SAXException {
            fElementDepth = 0;
            fXML11 = false;
            fInCDATA = false;
        }

        /**
         * Processing instruction.
         */
        public void processingInstruction(String target, String data) throws SAXException {
            if (fElementDepth > 0) {
                fOut.print("<?");
                fOut.print(target);
                if (data != null && data.length() > 0) {
                    fOut.print(' ');
                    fOut.print(data);
                }
                fOut.print("?>");
                fOut.flush();
            }
        }

        /**
         * Start element.
         */
        public void startElement(String uri, String local, String raw, Attributes attrs) throws SAXException {
            if (fElementDepth == 0) {
                if (fLocator != null) {
                    fXML11 = "1.1".equals(getVersion());
                    fLocator = null;
                }
                if (!fCanonical) {
                    if (fXML11) {
                        fOut.println("<?xml version=\"1.1\" encoding=\"UTF-8\"?>");
                    } else {
                        fOut.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                    }
                    fOut.flush();
                }
            }
            for (int pad = 0; pad < fElementDepth; pad++) {
                fOut.print("    ");
            }
            fElementDepth++;
            fOut.print('<');
            fOut.print(raw);
            if (attrs != null) {
                int len = attrs.getLength();
                for (int i = 0; i < len; i++) {
                    fOut.print(' ');
                    fOut.print(attrs.getQName(i));
                    fOut.print("=\"");
                    normalizeAndPrint(attrs.getValue(i), true);
                    fOut.print('"');
                }
            }
            fOut.print('>');
            fOut.println();
            fOut.flush();
        }

        /**
         * Characters.
         */
        public void characters(char ch[], int start, int length) throws SAXException {
            if (!fInCDATA) {
                int end = start + length;
                while (start < end) {
                    if (Character.isWhitespace(ch[start])) {
                        start++;
                    } else {
                        break;
                    }
                }
                while (start < end) {
                    if (Character.isWhitespace(ch[end - 1])) {
                        end--;
                    } else {
                        break;
                    }
                }
                length = end - start;
                normalizeAndPrint(ch, start, length, false);
            } else {
                for (int i = 0; i < length; ++i) {
                    fOut.print(ch[start + i]);
                }
            }
            fOut.flush();
        }

        /**
         * Ignorable whitespace.
         */
        public void ignorableWhitespace(char ch[], int start, int length) throws SAXException {
        }

        /**
         * End element.
         */
        public void endElement(String uri, String local, String raw) throws SAXException {
            fElementDepth--;
            for (int pad = 0; pad < fElementDepth; pad++) {
                fOut.print("    ");
            }
            fOut.print("</");
            fOut.print(raw);
            fOut.print('>');
            fOut.println();
            fOut.flush();
        }

        /**
         * Warning.
         */
        public void warning(SAXParseException ex) throws SAXException {
            printError("Warning", ex);
        }

        /**
         * Error.
         */
        public void error(SAXParseException ex) throws SAXException {
            printError("Error", ex);
        }

        /**
         * Fatal error.
         */
        public void fatalError(SAXParseException ex) throws SAXException {
            printError("Fatal Error", ex);
            throw ex;
        }

        /**
         * Start DTD.
         */
        public void startDTD(String name, String publicId, String systemId) throws SAXException {
        }

        /**
         * End DTD.
         */
        public void endDTD() throws SAXException {
        }

        /**
         * Start entity.
         */
        public void startEntity(String name) throws SAXException {
        }

        /**
         * End entity.
         */
        public void endEntity(String name) throws SAXException {
        }

        /**
         * Start CDATA section.
         */
        public void startCDATA() throws SAXException {
            if (!fCanonical) {
                fOut.print("<![CDATA[");
                fInCDATA = true;
            }
        }

        /**
         * End CDATA section.
         */
        public void endCDATA() throws SAXException {
            if (!fCanonical) {
                fInCDATA = false;
                fOut.print("]]>");
            }
        }

        /**
         * Comment.
         */
        public void comment(char ch[], int start, int length) throws SAXException {
            if (!fCanonical && fElementDepth > 0) {
                fOut.print("<!--");
                for (int i = 0; i < length; ++i) {
                    fOut.print(ch[start + i]);
                }
                fOut.print("-->");
                fOut.flush();
            }
        }

        /**
         * Returns a sorted list of attributes.
         */
        protected Attributes sortAttributes(Attributes attrs) {
            AttributesImpl attributes = new AttributesImpl();
            int len = (attrs != null) ? attrs.getLength() : 0;
            for (int i = 0; i < len; i++) {
                String name = attrs.getQName(i);
                int count = attributes.getLength();
                int j = 0;
                while (j < count) {
                    if (name.compareTo(attributes.getQName(j)) < 0) {
                        break;
                    }
                    j++;
                }
                attributes.insertAttributeAt(j, name, attrs.getType(i), attrs.getValue(i));
            }
            return attributes;
        }

        /**
         * Normalizes and prints the given string.
         */
        protected void normalizeAndPrint(String s, boolean isAttValue) {
            int len = (s != null) ? s.length() : 0;
            for (int i = 0; i < len; i++) {
                char c = s.charAt(i);
                normalizeAndPrint(c, isAttValue);
            }
        }

        /**
         * Normalizes and prints the given array of characters.
         */
        protected void normalizeAndPrint(char[] ch, int offset, int length, boolean isAttValue) {
            for (int i = 0; i < length; i++) {
                normalizeAndPrint(ch[offset + i], isAttValue);
            }
        }

        /**
         * Normalizes and print the given character.
         */
        protected void normalizeAndPrint(char c, boolean isAttValue) {
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
                        if (isAttValue) {
                            fOut.print("&quot;");
                        } else {
                            fOut.print("\"");
                        }
                        break;
                    }
                case '\r':
                    {
                        fOut.print("&#xD;");
                        break;
                    }
                case '\n':
                    {
                        if (fCanonical) {
                            fOut.print("&#xA;");
                            break;
                        }
                    }
                default:
                    {
                        if (fXML11 && ((c >= 0x01 && c <= 0x1F && c != 0x09 && c != 0x0A) || (c >= 0x7F && c <= 0x9F) || c == 0x2028) || isAttValue && (c == 0x09 || c == 0x0A)) {
                            fOut.print("&#x");
                            fOut.print(Integer.toHexString(c).toUpperCase());
                            fOut.print(";");
                        } else {
                            fOut.print(c);
                        }
                    }
            }
        }

        /**
         * Prints the error message.
         */
        protected void printError(String type, SAXParseException ex) {
            System.err.print("[");
            System.err.print(type);
            System.err.print("] ");
            String systemId = ex.getSystemId();
            if (systemId != null) {
                int index = systemId.lastIndexOf('/');
                if (index != -1) {
                    systemId = systemId.substring(index + 1);
                }
                System.err.print(systemId);
            }
            System.err.print(':');
            System.err.print(ex.getLineNumber());
            System.err.print(':');
            System.err.print(ex.getColumnNumber());
            System.err.print(": ");
            System.err.print(ex.getMessage());
            System.err.println();
            System.err.flush();
        }

        /**
         * Extracts the XML version from the Locator.
         */
        protected String getVersion() {
            if (fLocator == null) {
                return null;
            }
            String version = null;
            Method getXMLVersion = null;
            try {
                getXMLVersion = fLocator.getClass().getMethod("getXMLVersion", new Class[] {});
                if (getXMLVersion != null) {
                    version = (String) getXMLVersion.invoke(fLocator, (Object[]) null);
                }
            } catch (Exception e) {
            }
            return version;
        }
    }

    /**
     * An Attributes implementation that can perform more operations than the attribute list helper supplied with the
     * standard SAX2 distribution.
     */
    static class AttributesImpl implements Attributes {

        /**
         * Head node.
         */
        private ListNode head;

        /**
         * Tail node.
         */
        private ListNode tail;

        /**
         * Length.
         */
        private int length;

        /**
         * Returns the number of attributes.
         */
        public int getLength() {
            return length;
        }

        /**
         * Returns the index of the specified attribute.
         */
        public int getIndex(String raw) {
            ListNode place = head;
            int index = 0;
            while (place != null) {
                if (place.raw.equals(raw)) {
                    return index;
                }
                index++;
                place = place.next;
            }
            return -1;
        }

        /**
         * Returns the index of the specified attribute.
         */
        public int getIndex(String uri, String local) {
            ListNode place = head;
            int index = 0;
            while (place != null) {
                if (place.uri.equals(uri) && place.local.equals(local)) {
                    return index;
                }
                index++;
                place = place.next;
            }
            return -1;
        }

        /**
         * Returns the attribute URI by index.
         */
        public String getURI(int index) {
            ListNode node = getListNodeAt(index);
            return node != null ? node.uri : null;
        }

        /**
         * Returns the attribute local name by index.
         */
        public String getLocalName(int index) {
            ListNode node = getListNodeAt(index);
            return node != null ? node.local : null;
        }

        /**
         * Returns the attribute raw name by index.
         */
        public String getQName(int index) {
            ListNode node = getListNodeAt(index);
            return node != null ? node.raw : null;
        }

        /**
         * Returns the attribute type by index.
         */
        public String getType(int index) {
            ListNode node = getListNodeAt(index);
            return (node != null) ? node.type : null;
        }

        /**
         * Returns the attribute type by uri and local.
         */
        public String getType(String uri, String local) {
            ListNode node = getListNode(uri, local);
            return (node != null) ? node.type : null;
        }

        /**
         * Returns the attribute type by raw name.
         */
        public String getType(String raw) {
            ListNode node = getListNode(raw);
            return (node != null) ? node.type : null;
        }

        /**
         * Returns the attribute value by index.
         */
        public String getValue(int index) {
            ListNode node = getListNodeAt(index);
            return (node != null) ? node.value : null;
        }

        /**
         * Returns the attribute value by uri and local.
         */
        public String getValue(String uri, String local) {
            ListNode node = getListNode(uri, local);
            return (node != null) ? node.value : null;
        }

        /**
         * Returns the attribute value by raw name.
         */
        public String getValue(String raw) {
            ListNode node = getListNode(raw);
            return (node != null) ? node.value : null;
        }

        /**
         * Adds an attribute.
         */
        public void addAttribute(String raw, String type, String value) {
            addAttribute(null, null, raw, type, value);
        }

        /**
         * Adds an attribute.
         */
        public void addAttribute(String uri, String local, String raw, String type, String value) {
            ListNode node = new ListNode(uri, local, raw, type, value);
            if (length == 0) {
                head = node;
            } else {
                tail.next = node;
            }
            tail = node;
            length++;
        }

        /**
         * Inserts an attribute.
         */
        public void insertAttributeAt(int index, String raw, String type, String value) {
            insertAttributeAt(index, null, null, raw, type, value);
        }

        /**
         * Inserts an attribute.
         */
        public void insertAttributeAt(int index, String uri, String local, String raw, String type, String value) {
            if (length == 0 || index >= length) {
                addAttribute(uri, local, raw, type, value);
                return;
            }
            ListNode node = new ListNode(uri, local, raw, type, value);
            if (index < 1) {
                node.next = head;
                head = node;
            } else {
                ListNode prev = getListNodeAt(index - 1);
                node.next = prev.next;
                prev.next = node;
            }
            length++;
        }

        /**
         * Removes an attribute.
         */
        public void removeAttributeAt(int index) {
            if (length == 0) {
                return;
            }
            if (index == 0) {
                head = head.next;
                if (head == null) {
                    tail = null;
                }
                length--;
            } else {
                ListNode prev = getListNodeAt(index - 1);
                ListNode node = getListNodeAt(index);
                if (node != null) {
                    prev.next = node.next;
                    if (node == tail) {
                        tail = prev;
                    }
                    length--;
                }
            }
        }

        /**
         * Removes the specified attribute.
         */
        public void removeAttribute(String raw) {
            removeAttributeAt(getIndex(raw));
        }

        /**
         * Removes the specified attribute.
         */
        public void removeAttribute(String uri, String local) {
            removeAttributeAt(getIndex(uri, local));
        }

        /**
         * Returns the node at the specified index.
         */
        private ListNode getListNodeAt(int i) {
            for (ListNode place = head; place != null; place = place.next) {
                if (--i == -1) {
                    return place;
                }
            }
            return null;
        }

        /**
         * Returns the first node with the specified uri and local.
         */
        public ListNode getListNode(String uri, String local) {
            if (uri != null && local != null) {
                ListNode place = head;
                while (place != null) {
                    if (place.uri != null && place.local != null && place.uri.equals(uri) && place.local.equals(local)) {
                        return place;
                    }
                    place = place.next;
                }
            }
            return null;
        }

        /**
         * Returns the first node with the specified raw name.
         */
        private ListNode getListNode(String raw) {
            if (raw != null) {
                for (ListNode place = head; place != null; place = place.next) {
                    if (place.raw != null && place.raw.equals(raw)) {
                        return place;
                    }
                }
            }
            return null;
        }

        /**
         * Returns a string representation of this object.
         */
        public String toString() {
            StringBuffer str = new StringBuffer();
            str.append('[');
            str.append("len=");
            str.append(length);
            str.append(", {");
            for (ListNode place = head; place != null; place = place.next) {
                str.append(place.toString());
                if (place.next != null) {
                    str.append(", ");
                }
            }
            str.append("}]");
            return str.toString();
        }

        /**
         * An attribute node.
         */
        static class ListNode {

            /**
             * Attribute uri.
             */
            public String uri;

            /**
             * Attribute local.
             */
            public String local;

            /**
             * Attribute raw.
             */
            public String raw;

            /**
             * Attribute type.
             */
            public String type;

            /**
             * Attribute value.
             */
            public String value;

            /**
             * Next node.
             */
            public ListNode next;

            /**
             * Constructs a list node.
             */
            public ListNode(String uri, String local, String raw, String type, String value) {
                this.uri = uri;
                this.local = local;
                this.raw = raw;
                this.type = type;
                this.value = value;
            }

            /**
             * Returns string representation of this object.
             */
            public String toString() {
                return raw != null ? raw : local;
            }
        }
    }
}
