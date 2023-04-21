package com.jclark.xsl.expr;

import com.jclark.xsl.om.*;
import java.io.ByteArrayOutputStream;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;

/**
 * Represents the EXSL str:encode-uri function
 * For more information consult exsl specification at: 
 * <A HREF="http://www.exslt.org/str/functions/encode-uri/str.encode-uri.html">Specification</A>. 
 */
class EncodeURIFunction implements Function {

    /**
     *
     */
    public ConvertibleExpr makeCallExpr(ConvertibleExpr[] args, Node exprNode) throws ParseException {
        if (args.length < 2 || args.length > 3) {
            throw new ParseException("expected 2 or 3 arguments");
        }
        final StringExpr se = args[0].makeStringExpr();
        final BooleanExpr se2 = args[1].makeBooleanExpr();
        final StringExpr se3 = (args.length == 2 ? new LiteralExpr("") : args[2].makeStringExpr());
        return new ConvertibleStringExpr() {

            public String eval(Node node, ExprContext context) throws XSLException {
                return encodeURI(se.eval(node, context), se2.eval(node, context), se3.eval(node, context));
            }
        };
    }

    static BitSet dontNeedEncodingRFC2396;

    static BitSet dontNeedEncodingRFC2732;

    static final int caseDiff = ('a' - 'A');

    static String dfltEncName = "UTF-8";

    static {
        dontNeedEncodingRFC2396 = new BitSet(256);
        int i;
        for (i = 'a'; i <= 'z'; i++) {
            dontNeedEncodingRFC2396.set(i);
        }
        for (i = 'A'; i <= 'Z'; i++) {
            dontNeedEncodingRFC2396.set(i);
        }
        for (i = '0'; i <= '9'; i++) {
            dontNeedEncodingRFC2396.set(i);
        }
        dontNeedEncodingRFC2396.set(' ');
        dontNeedEncodingRFC2396.set('-');
        dontNeedEncodingRFC2396.set('_');
        dontNeedEncodingRFC2396.set('.');
        dontNeedEncodingRFC2396.set('*');
        dontNeedEncodingRFC2732 = new BitSet(256);
        dontNeedEncodingRFC2732.set(';');
        dontNeedEncodingRFC2732.set('/');
        dontNeedEncodingRFC2732.set('?');
        dontNeedEncodingRFC2732.set(':');
        dontNeedEncodingRFC2732.set('@');
        dontNeedEncodingRFC2732.set('&');
        dontNeedEncodingRFC2732.set('=');
        dontNeedEncodingRFC2732.set('+');
        dontNeedEncodingRFC2732.set('$');
        dontNeedEncodingRFC2732.set(',');
        dontNeedEncodingRFC2732.set('[');
        dontNeedEncodingRFC2732.set(']');
    }

    /**
     * Translates a string into <code>application/x-www-form-urlencoded</code>
     * format using a specific encoding scheme. This method uses the
     * supplied encoding scheme to obtain the bytes for unsafe
     * characters.
     * <p>
     * <em><strong>Note:</strong> The <a href=
     * "http://www.w3.org/TR/html40/appendix/notes.html#non-ascii-chars">
     * World Wide Web Consortium Recommendation</a> states that
     * UTF-8 should be used. Not doing so may introduce
     * incompatibilites.</em>
     *
     * @param   s   <code>String</code> to be translated.
     * @param   enc   The name of a supported 
     *    <a href="../lang/package-summary.html#charenc">character encoding</a>.
     * @return  the translated <code>String</code>.
     */
    private static final String encodeURI(String s, boolean encodeURISubset, String enc) {
        try {
            if ("".equals(enc)) {
                enc = dfltEncName;
            }
            boolean needToChange = false;
            boolean wroteUnencodedChar = false;
            int maxBytesPerChar = 10;
            StringBuffer out = new StringBuffer(s.length());
            ByteArrayOutputStream buf = new ByteArrayOutputStream(maxBytesPerChar);
            OutputStreamWriter writer = new OutputStreamWriter(buf, enc);
            for (int i = 0; i < s.length(); i++) {
                int c = (int) s.charAt(i);
                if (dontNeedEncoding(c, encodeURISubset)) {
                    if (c == ' ') {
                        c = '+';
                        needToChange = true;
                    }
                    out.append((char) c);
                    wroteUnencodedChar = true;
                } else {
                    try {
                        if (wroteUnencodedChar) {
                            writer = new OutputStreamWriter(buf, enc);
                            wroteUnencodedChar = false;
                        }
                        writer.write(c);
                        if (c >= 0xD800 && c <= 0xDBFF) {
                            if ((i + 1) < s.length()) {
                                int d = (int) s.charAt(i + 1);
                                if (d >= 0xDC00 && d <= 0xDFFF) {
                                    writer.write(d);
                                    i++;
                                }
                            }
                        }
                        writer.flush();
                    } catch (IOException e) {
                        buf.reset();
                        continue;
                    }
                    byte[] ba = buf.toByteArray();
                    for (int j = 0; j < ba.length; j++) {
                        out.append('%');
                        char ch = Character.forDigit((ba[j] >> 4) & 0xF, 16);
                        if (Character.isLetter(ch)) {
                            ch -= caseDiff;
                        }
                        out.append(ch);
                        ch = Character.forDigit(ba[j] & 0xF, 16);
                        if (Character.isLetter(ch)) {
                            ch -= caseDiff;
                        }
                        out.append(ch);
                    }
                    buf.reset();
                    needToChange = true;
                }
            }
            return (needToChange ? out.toString() : s);
        } catch (UnsupportedEncodingException uex) {
            return "";
        } catch (Exception ex) {
            return "";
        }
    }

    private static final boolean dontNeedEncoding(int c, boolean encodeURISubset) {
        return (encodeURISubset) ? dontNeedEncodingRFC2396.get(c) : dontNeedEncodingRFC2396.get(c) || dontNeedEncodingRFC2732.get(c);
    }
}
