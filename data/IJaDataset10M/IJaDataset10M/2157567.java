package org.apache.myfaces.trinidadinternal.io;

import java.io.IOException;
import java.io.Writer;

/**
 * Utility class for escaping XML text.
 * <p>
 */
public class XMLEscapes {

    public static void writeAttribute(Appendable out, CharSequence text) throws IOException {
        int length = text.length();
        for (int i = 0; i < length; i++) {
            char ch = text.charAt(i);
            if (ch <= 0x7f) {
                if (ch == '>') out.append("&gt;"); else if (ch == '<') out.append("&lt;"); else if (ch == '"') out.append("&quot;"); else if (ch == '&') out.append("&amp;"); else out.append(ch);
            } else {
                _writeHexRef(out, ch);
            }
        }
    }

    public static void writeText(Writer out, char[] text) throws IOException {
        writeText(out, text, 0, text.length);
    }

    public static void writeText(Writer out, char[] text, int start, int length) throws IOException {
        int end = start + length;
        for (int i = start; i < end; i++) {
            char ch = text[i];
            if (ch <= 0x7f) {
                if (ch == '>') out.write("&gt;"); else if (ch == '<') out.write("&lt;"); else if (ch == '"') out.write("&quot;"); else if (ch == '&') out.write("&amp;"); else {
                    out.write(ch);
                }
            } else {
                _writeHexRef(out, ch);
            }
        }
    }

    public static void writeAttribute(Writer out, char[] text) throws IOException {
        writeText(out, text);
    }

    public static void writeAttribute(Writer out, char[] text, int start, int length) throws IOException {
        writeText(out, text, start, length);
    }

    private static void _writeHexRef(Appendable out, char ch) throws IOException {
        out.append("&#x");
        out.append(Integer.toHexString(ch));
        out.append(';');
    }

    private XMLEscapes() {
    }
}
