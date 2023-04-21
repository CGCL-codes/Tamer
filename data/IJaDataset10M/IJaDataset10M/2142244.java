package com.sun.jbi.internal.ant.tools;

import org.xml.sax.AttributeList;
import org.xml.sax.HandlerBase;

/**
 * This SAX handler converts given XML strings to a canonical format, as 
 * defined by James Clark {@link http://jclark.com/xml/canonxml.html}. This 
 * is based on Clark's XMLTest, available at 
 * {@link http://jclark.com/xml/XMLTest.java}.
 *
 * This implementation uses the deprecated AttributeList and HandlerBase
 * classes from the <code>org.xml.sax</code> package. These classes
 * have been deprecated only because they are not XML name space "aware." 
 * However, name space handling is not required for Clark's canonicalization.
 */
public class Canonicalizer extends HandlerBase {

    /** Buffer used to accumulate result */
    private StringBuffer mBuf;

    /** 
     * Construct a new instance of the canonicalizer.
     *
     * @param buf StringBuffer to place the result of the parse into.
     */
    Canonicalizer(StringBuffer buf) {
        mBuf = buf;
        return;
    }

    /** 
    * SAX handler callback, invoked when a start-tag is parsed.
    *
    * @param name tag name
    * @param atts attributes in the start-tag, in no particular order.
    */
    public void startElement(String name, AttributeList atts) {
        flushChars();
        write('<');
        write(name);
        int len = atts.getLength();
        if (len > 0) {
            int[] v = new int[len];
            for (int i = 0; i < len; i++) v[i] = i;
            for (int i = 1; i < len; i++) {
                int n = v[i];
                String s = atts.getName(n);
                int j;
                for (j = i - 1; j >= 0; j--) {
                    if (s.compareTo(atts.getName(v[j])) >= 0) break;
                    v[j + 1] = v[j];
                }
                v[j + 1] = n;
            }
            for (int i = 0; i < len; i++) {
                int n = v[i];
                write(' ');
                write(atts.getName(n));
                write("=\"");
                String value = atts.getValue(n);
                int valueLen = value.length();
                for (int j = 0; j < valueLen; j++) appendChar(value.charAt(j));
                flushChars();
                write('"');
            }
        }
        write('>');
    }

    /**
     * SAX handler call-back, invoked when ignorable whitespace is parsed.
     *
     * @param cbuf buffer holding characters from the source document
     * @param start buffer index for the first whitespace character
     * @param len number of whitespace characters in the buffer,
     *        beginning with the one at <code>start</code>.
     */
    public void ignorableWhitespace(char[] cbuf, int start, int len) {
        characters(cbuf, start, len);
    }

    /**
     * SAX handler call-back, invoked when PCHARS are parsed.
     *
     * @param cbuf buffer holding characters from the source document
     * @param start buffer index for the first character
     * @param len number of characters in the buffer,
     *        beginning with the one at <code>start</code>.
     */
    public void characters(char[] cbuf, int start, int len) {
        while (len-- > 0) appendChar(cbuf[start++]);
    }

    /**
     * Append the given character to the result. This method "escapes" out
     * mark-up characters.
     *
     * @param c the character to append to the result
     */
    private void appendChar(char c) {
        switch(c) {
            case '&':
                mBuf.append("&amp;");
                break;
            case '<':
                mBuf.append("&lt;");
                break;
            case '>':
                mBuf.append("&gt;");
                break;
            case '"':
                mBuf.append("&quot;");
                break;
            case '\t':
                mBuf.append("&#9;");
                break;
            case '\n':
                mBuf.append("&#10;");
                break;
            case '\r':
                mBuf.append("&#13;");
                break;
            default:
                mBuf.append(c);
                break;
        }
    }

    /**
     * SAX handler callback, invoked when an end-tag is parsed, or
     * when the end of a leaf tag is encountered.
     *
     * @param name name of end-tag
     */
    public void endElement(String name) {
        flushChars();
        write("</");
        write(name);
        write('>');
    }

    /**
     * SAX handler callback, invoked when a processing instruction is parsed.
     *
     * @param target name of the PI target
     * @param data data for te PI
     */
    public void processingInstruction(String target, String data) {
        flushChars();
        write("<?");
        write(target);
        write(' ');
        write(data);
        write("?>");
    }

    /**
     * SAX handler callback, invoked when the start of the source document
     * is encountered.
     */
    public void startDocument() {
    }

    /**
     * SAX handler callback, invoked when the end of the source document is
     * encountered.
     */
    public void endDocument() {
        flushChars();
    }

    /**
     * Flush the result.
     */
    private final void flushChars() {
    }

    /** 
     * Write the given string to the result. This methods does not "escape"
     * out mark-up characters.
     *
     * @param s the string to append to the result
     */
    private final void write(String s) {
        mBuf.append(s);
    }

    /**
     * Write the given character to the result. This method does not "escape"
     * out markup characters.
     */
    private final void write(char c) {
        mBuf.append(c);
    }
}
