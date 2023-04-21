package com.further.jaudit.http;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Vector;

/**
 * This is a freely available source formatter that does a simple (java/c)
 * to html conversion, and, at the same time, adds color and syntax highlighting
 * to the output html file. The code for this is a little long and procedural,
 * but it does the job and is just treated as a black box in the system.
 *
 * @author andcio@hotmail.com
 * @version $Id: SourceFormatter.java,v 1.1.1.1 2001/10/11 16:42:10 krisw Exp $
 * @since 1.0
 */
public class SourceFormatter {

    /** 
     * Create a new SourceFormatter, passing in the user selected color 
     * preferences.
     * 
     * @param colorPreferences The user selected color preferences for 
     *        formatting the source file.
     */
    public SourceFormatter(ColorPreferencesBean colorPreferences) {
        bgcolor = colorPreferences.getBackgroundColor();
        txcolor = colorPreferences.getForegroundColor();
        kwcolor = colorPreferences.getKeywordColor();
        cmcolor = colorPreferences.getCommentColor();
    }

    static final String keywords[] = { "abstract", "default", "if", "private", "throw", "boolean", "do", "implements", "protected", "throws", "break", "double", "import", "public", "transient", "byte", "else", "instanceof", "return", "try", "case", "extends", "int", "short", "void", "catch", "final", "interface", "static", "volatile", "char", "finally", "long", "super", "while", "class", "float", "native", "switch", "const", "for", "new", "synchronized", "continue", "goto", "package", "this" };

    static Vector keyw = new Vector(keywords.length);

    static {
        for (int i = 0; i < keywords.length; i++) keyw.addElement(keywords[i]);
    }

    static int tabsize = 4;

    static String bgcolor = "C0C0C0";

    static String txcolor = "000000";

    static String kwcolor = "0000F0";

    static String cmcolor = "A00000";

    /**
     * Convert the sourcecode contained in source to colorized html output. 
     * The source string is assumed to contain the entire contents of the source
     * file to be formatted, and the return string is assumed to contain a valid
     * html document.
     *
     * @param source The entire contents of the source file to be formatted
     * @return A valid html document that is the formatted sourccode.
     */
    public String convertSource(String source) throws IOException {
        StringReader in = new StringReader(source);
        StringWriter out = new StringWriter();
        out.write("<body ");
        out.write("bgcolor=\"" + bgcolor + "\" ");
        out.write("text=\"" + txcolor + "\">\r\n");
        out.write("<pre>\r\n");
        StringBuffer buf = new StringBuffer(2048);
        int c = 0, kwl = 0, bufl = 0;
        char ch = 0, lastch;
        int s_normal = 0;
        int s_string = 1;
        int s_char = 2;
        int s_comline = 3;
        int s_comment = 4;
        int state = s_normal;
        while (c != -1) {
            c = in.read();
            lastch = ch;
            ch = c >= 0 ? (char) c : 0;
            if (state == s_normal) if (kwl == 0 && Character.isJavaIdentifierStart(ch) && !Character.isJavaIdentifierPart(lastch) || kwl > 0 && Character.isJavaIdentifierPart(ch)) {
                buf.append(ch);
                bufl++;
                kwl++;
                continue;
            } else if (kwl > 0) {
                String kw = buf.toString().substring(buf.length() - kwl);
                if (keyw.contains(kw)) {
                    buf.insert(buf.length() - kwl, "<font color=\"" + kwcolor + "\">");
                    buf.append("</font>");
                }
                kwl = 0;
            }
            switch(ch) {
                case '&':
                    buf.append("&amp;");
                    bufl++;
                    break;
                case '\"':
                    buf.append("&quot;");
                    bufl++;
                    if (state == s_normal) state = s_string; else if (state == s_string && lastch != '\\') state = s_normal;
                    break;
                case '\'':
                    buf.append("\'");
                    bufl++;
                    if (state == s_normal) state = s_char; else if (state == s_char && lastch != '\\') state = s_normal;
                    break;
                case '\\':
                    buf.append("\\");
                    bufl++;
                    if (lastch == '\\' && (state == s_string || state == s_char)) lastch = 0;
                    break;
                case '/':
                    buf.append("/");
                    bufl++;
                    if (state == s_comment && lastch == '*') {
                        buf.append("</font>");
                        state = s_normal;
                    }
                    if (state == s_normal && lastch == '/') {
                        buf.insert(buf.length() - 2, "<font color=\"" + cmcolor + "\">");
                        state = s_comline;
                    }
                    break;
                case '*':
                    buf.append("*");
                    bufl++;
                    if (state == s_normal && lastch == '/') {
                        buf.insert(buf.length() - 2, "<font color=\"" + cmcolor + "\">");
                        state = s_comment;
                    }
                    break;
                case '<':
                    buf.append("&lt;");
                    bufl++;
                    break;
                case '>':
                    buf.append("&gt;");
                    bufl++;
                    break;
                case '\t':
                    int n = bufl / tabsize * tabsize + tabsize;
                    while (bufl < n) {
                        buf.append(' ');
                        bufl++;
                    }
                    break;
                case '\r':
                case '\n':
                    if (state == s_comline) {
                        buf.append("</font>");
                        state = s_normal;
                    }
                    buf.append(ch);
                    if (buf.length() >= 1024) {
                        out.write(buf.toString());
                        buf.setLength(0);
                    }
                    bufl = 0;
                    if (kwl != 0) kwl = 0;
                    if (state != s_normal && state != s_comment) state = s_normal;
                    break;
                case 0:
                    if (c < 0) {
                        if (state == s_comline) {
                            buf.append("</font>");
                            state = s_normal;
                        }
                        out.write(buf.toString());
                        buf.setLength(0);
                        bufl = 0;
                        if (state == s_comment) {
                            buf.append("</font>");
                            state = s_normal;
                        }
                        break;
                    }
                default:
                    bufl++;
                    buf.append(ch);
            }
        }
        out.write("</pre>\r\n</body>");
        in.close();
        out.close();
        return out.toString();
    }
}
