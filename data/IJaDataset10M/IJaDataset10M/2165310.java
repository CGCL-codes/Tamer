package com.goodcodeisbeautiful.archtea.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import com.goodcodeisbeautiful.util.ReplaceCharReader;

/**
 * A simple utility to create an xml string.
 * @author hata
 *
 */
public class XMLUtil {

    /** a table to escape characters. */
    private static String[] ESCAPE_TABLE;

    static {
        ESCAPE_TABLE = new String[128];
        for (char c = 0; c < ESCAPE_TABLE.length; c++) {
            switch(c) {
                case '\r':
                case '\n':
                case '\t':
                    ESCAPE_TABLE[c] = "" + c;
                    break;
                case '<':
                    ESCAPE_TABLE[c] = "&lt;";
                    break;
                case '&':
                    ESCAPE_TABLE[c] = "&amp;";
                    break;
                case '>':
                    ESCAPE_TABLE[c] = "&gt;";
                    break;
                case '\"':
                    ESCAPE_TABLE[c] = "&quot;";
                    break;
                case '\'':
                    ESCAPE_TABLE[c] = "&apos;";
                    break;
                case 0x7f:
                    ESCAPE_TABLE[c] = "&#x7f;";
                    break;
                default:
                    ESCAPE_TABLE[c] = (c < 0x20) ? "?" : "" + c;
            }
        }
    }

    private static final Pattern TAG_PATTERN = Pattern.compile("<[^>]*>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

    /** an indent character. */
    public static final char INDENT_CHAR = ' ';

    /** Return code. */
    public static final String RET = "\n";

    /**
     * Escape characters and sandwitch with tag text.
     * @param indent is a number of white space characters.
     * @param tagName is a tag text to sandwitch.
     * @param number is a number to use in a xml text.
     * @return a part of xml text like &lt;tagName&gt;text&lt;/tagName&gt;
     */
    public static String buildXMLNodeText(int indent, String tagName, long number) {
        return buildXMLNodeText(indent, tagName, "" + number);
    }

    /**
     * Escape characters and sandwitch with tag text.
     * @param indent is a number of white space characters.
     * @param tagName is a tag text to sandwitch.
     * @param text is a xml text. If this value is null,
     * this method return only tag.
     * @return a part of xml text like &lt;tagName&gt;text&lt;/tagName&gt;
     */
    public static String buildXMLNodeText(int indent, String tagName, String text) {
        return buildXMLNodeText(indent, tagName, text, null);
    }

    /**
     * Create a XML node with text and attributes.
     * @param indent is a indent value like 4, 8, ....
     * @param tagName is used a tag name for an element.
     * @param text is a text string.
     * @param attrs are used to append some attributes to the created element.
     * @return a created XML text.
     */
    public static String buildXMLNodeText(int indent, String tagName, String text, Map attrs) {
        return buildXMLNodeNoEscapeText(indent, tagName, text != null ? escapeToXML(text) : null, attrs);
    }

    /**
     * Create a XML node with text and attributes.
     * @param indent is a indent value like 4, 8, ....
     * @param tagName is used a tag name for an element.
     * @param text is a text string.
     * @param attrs are used to append some attributes to the created element.
     * @return a created XML text.
     */
    public static String buildXMLNodeNoEscapeText(int indent, String tagName, String text, Map attrs) {
        StringBuffer buff = new StringBuffer();
        for (int i = 0; i < indent; i++) buff.append(INDENT_CHAR);
        StringBuffer attrString = new StringBuffer();
        if (attrs != null) {
            Set entries = attrs.entrySet();
            Iterator it = entries.iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                attrString.append(" ");
                attrString.append(entry.getKey() + "=\"" + escapeToXML(entry.getValue().toString()) + "\"");
            }
        }
        if (text != null) {
            buff.append("<" + tagName + attrString + ">");
            buff.append(text);
            buff.append("</" + tagName + ">");
        } else {
            buff.append("<" + tagName + attrString + "/>");
        }
        buff.append(RET);
        return new String(buff);
    }

    /**
     * Escape chars such as &gt;, &lt;, &amp;, &quot;, &apos; .
     * @param srcText is a source text to be checked and replaced
     * with escape characters if they found.
     * @return a text which is escaped to use in XML.
     */
    public static String escapeToXML(String srcText) {
        StringBuffer buff = new StringBuffer();
        for (int i = 0; i < srcText.length(); i++) {
            buff.append(escapeChar(srcText.charAt(i)));
        }
        return new String(buff);
    }

    /**
     * Replace Ctrl characters with a question characters '?'.
     * This method doesn't escape such as &gt; &lt; &amp; &quot; &apos; and
     * so on.
     * @param srcText is a src text.
     * @return text which doesn't contain wrong character for xml.
     */
    public static String removeCtrlChars(String srcText) {
        char[] cbuff = new char[srcText.length()];
        for (int i = 0; i < srcText.length(); i++) {
            char c = srcText.charAt(i);
            if (c < ReplaceCharReader.XML_REMOVE_CTRL_CODES.length) {
                cbuff[i] = ReplaceCharReader.XML_REMOVE_CTRL_CODES[c];
            } else {
                cbuff[i] = c;
            }
        }
        return new String(cbuff);
    }

    /**
     * Remove tags like &lt;b&gt;
     * @param src is a source text.
     * @return text which doesn't contain tags .
     */
    public static String removeTags(String src) {
        return TAG_PATTERN.matcher(src).replaceAll("");
    }

    /**
     * Escape a char.
     * @param c is a checked char and if the char should escape,
     * the char will replace with string.
     * @return a char or escaped string.
     */
    private static String escapeChar(char c) {
        return (0 <= c && c < ESCAPE_TABLE.length) ? ESCAPE_TABLE[c] : "" + c;
    }
}
