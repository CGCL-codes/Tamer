package org.yajul.xml;

import java.io.FilterWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * A FileWriter extension that writes XML fragments and XML documents.
 * @author josh Apr 8, 2004 7:11:36 AM
 */
public class XMLWriter extends FilterWriter {

    /** The default encoding (Java encoding name). */
    public static final String DEFAULT_ENCODING = "ISO8859_1";

    /**
     * Static table of XML_ENCODINGS, maps Java encoding names into XML encoding names (the 'encoding' attribute
     * in the XML prolog).
     */
    private static final Map XML_ENCODINGS = new HashMap();

    static {
        XML_ENCODINGS.put("UTF8", "UTF-8");
        XML_ENCODINGS.put("ISO8859_1", "ISO-8859-1");
    }

    /** The character encoding being used. */
    private String encoding;

    /**
     * Returns an XML prologue encoding name, given either a Java encoding
     * name, or an XML prologue encoding name.
     * @param enc The Java encoding name.
     * @return The XML encoding name.
     */
    public static String getEncoding(String enc) {
        String rv = (String) XML_ENCODINGS.get(enc);
        return rv == null ? enc : rv;
    }

    /** Creates a new XMLPrinter that will write to a string. */
    public XMLWriter() {
        this(new StringWriter());
    }

    /** Creates a new XMLPrinter that will write to the given writer. */
    public XMLWriter(Writer writer) {
        super(writer);
        encoding = DEFAULT_ENCODING;
    }

    /** Creates a new XMLPrinter that will write to the given
     * writer, with the given encoding. */
    public XMLWriter(Writer writer, String encoding) {
        super(writer);
        this.encoding = encoding;
    }

    public void writePrologue() throws java.io.IOException {
        String encName = getEncoding(encoding);
        write("<?xml version=\"1.0\" encoding=\"");
        write(encName);
        write("\"?>");
    }

    public void startElement(String elem) throws java.io.IOException {
        write("<");
        write(elem);
        write(">");
    }

    public void endElement(String elem) throws java.io.IOException {
        write("</");
        write(elem);
        write(">");
    }

    public void emptyElement(String elem) throws java.io.IOException {
        write("<");
        write(elem);
        write("/>");
    }

    public void chardata(String text) throws java.io.IOException {
        int l = text.length();
        for (int i = 0; i < l; i++) {
            char c = text.charAt(i);
            switch(c) {
                case '<':
                    write("&lt;");
                    break;
                case '&':
                    write("&amp;");
                    break;
                default:
                    write(c);
            }
        }
    }

    public String toString() {
        if (out instanceof StringWriter) {
            StringWriter sw = (StringWriter) out;
            return sw.toString();
        } else return null;
    }

    public byte[] getBytes() throws java.io.UnsupportedEncodingException {
        if (out instanceof StringWriter) return toString().getBytes(encoding); else return null;
    }
}
