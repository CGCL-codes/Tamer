package com.jclark.xsl.sax2;

import com.jclark.xsl.sax.CommentHandler;
import com.jclark.xsl.sax.Destination;
import org.xml.sax.*;
import java.io.Writer;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Properties;

/**
 * A OutputContentHandler that writes an HTML representation
 * to a Destination
 */
public class HTMLOutputHandler implements OutputContentHandler, CommentHandler, RawCharactersHandler {

    private Writer writer;

    private static final int DEFAULT_BUF_LENGTH = 4 * 1024;

    private char[] buf = new char[DEFAULT_BUF_LENGTH];

    private int bufUsed = 0;

    private boolean inCdata = false;

    private boolean inPcdataChunk = true;

    private boolean inBlock = false;

    private final String lineSeparator = System.getProperty("line.separator");

    private boolean indent = true;

    private char maxRepresentableChar = '￿';

    private boolean keepOpen;

    private String encoding;

    private String doctypeSystem;

    private String doctypePublic;

    private static final int NORMAL_CONTENT = 0;

    private static final int EMPTY_CONTENT = 1;

    private static final int CDATA_CONTENT = 2;

    private static final int CONTENT_TYPE = 3;

    private static final int BLOCK_ELEMENT = 4;

    private static final int HEAD_ELEMENT = 8;

    private static final int PCDATA_ELEMENT = 16;

    /**
     * Constructor with output destination TBD
     */
    public HTMLOutputHandler() {
    }

    /**
     * Constructor with a Writer 
     */
    public HTMLOutputHandler(Writer writer) {
        this.writer = writer;
    }

    /**
     * The Properties represent output serialization options
     */
    public ContentHandler init(Destination dest, Properties props) throws IOException {
        String mediaType = props.getProperty("media-type");
        if (mediaType == null) {
            mediaType = "text/html";
        }
        encoding = props.getProperty("encoding");
        if (encoding == null) {
            writer = dest.getWriter(mediaType, "iso-8859-1");
            maxRepresentableChar = '';
        } else {
            writer = dest.getWriter(mediaType, encoding);
            encoding = dest.getEncoding();
            if (encoding.equalsIgnoreCase("iso-8859-1")) {
                maxRepresentableChar = 'ÿ';
            } else if (encoding.equalsIgnoreCase("us-ascii")) {
                maxRepresentableChar = '';
            }
        }
        doctypeSystem = props.getProperty("doctype-system");
        doctypePublic = props.getProperty("doctype-public");
        keepOpen = dest.keepOpen();
        if ("no".equals(props.getProperty("indent"))) {
            indent = false;
        }
        return this;
    }

    public void setWriter(Writer writer) {
        this.writer = writer;
    }

    public void startDocument() throws SAXException {
    }

    public void characters(char[] ch, int off, int len) throws SAXException {
        if (len == 0) {
            return;
        }
        inPcdataChunk = true;
        if (inCdata) {
            writeUnquoted(new String(ch, off, len));
        } else {
            for (; len > 0; len--, off++) {
                char c = ch[off];
                switch(c) {
                    case '\n':
                        write(lineSeparator);
                        break;
                    case '&':
                        write("&amp;");
                        break;
                    case '<':
                        write("&lt;");
                        break;
                    case '>':
                        write("&gt;");
                        break;
                    case ' ':
                        write("&nbsp;");
                        break;
                    default:
                        if (c <= maxRepresentableChar) {
                            write(c);
                        } else {
                            write(getCharString(c));
                        }
                        break;
                }
            }
        }
    }

    public void ignorableWhitespace(char[] ch, int off, int len) throws SAXException {
        characters(ch, off, len);
    }

    public void startPrefixMapping(String prefix, String namespaceURI) {
    }

    public void endPrefixMapping(String prefix) {
    }

    public void skippedEntity(String name) {
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        if (inCdata) {
            return;
        }
        if (doctypeSystem != null || doctypePublic != null) {
            write("<!DOCTYPE ");
            write(qName.equals("HTML") ? "HTML" : "html");
            if (doctypePublic != null) {
                write(" PUBLIC ");
                char lit = doctypePublic.indexOf('"') >= 0 ? '\'' : '"';
                write(lit);
                write(doctypePublic);
                write(lit);
            } else {
                write(" SYSTEM");
            }
            if (doctypeSystem != null) {
                char lit = doctypeSystem.indexOf('"') >= 0 ? '\'' : '"';
                write(' ');
                write(lit);
                write(doctypeSystem);
                write(lit);
            }
            write('>');
            doctypeSystem = null;
            doctypePublic = null;
            write(lineSeparator);
        }
        int flags = getElementTypeFlags(qName);
        int contentType = (flags & CONTENT_TYPE);
        boolean isBlockElement = (flags & BLOCK_ELEMENT) != 0;
        if (inPcdataChunk) {
            inPcdataChunk = false;
        } else if (indent && (!inBlock || isBlockElement)) {
            write(lineSeparator);
        }
        inBlock = !isBlockElement;
        write('<');
        write(qName);
        int nAtts = atts.getLength();
        for (int i = 0; i < nAtts; i++) {
            attribute(atts.getQName(i), atts.getValue(i));
        }
        if (contentType == CDATA_CONTENT) {
            inCdata = true;
        }
        write('>');
        if (encoding != null && (flags & HEAD_ELEMENT) != 0) {
            write(lineSeparator + "<META http-equiv=\"Content-Type\" content=\"text/html; charset=" + encoding + "\">");
        }
    }

    public void rawCharacters(String chars) throws SAXException {
        if (chars.length() != 0) {
            writeUnquoted(chars);
            inPcdataChunk = true;
        }
    }

    private void writeUnquoted(String str) throws SAXException {
        int start = 0;
        for (; ; ) {
            int i = str.indexOf('\n', start);
            if (i < 0) {
                break;
            }
            if (i > start) {
                write(str.substring(start, i));
            }
            write(lineSeparator);
            start = i + 1;
        }
        write(start == 0 ? str : str.substring(start));
    }

    private void attribute(String name, String value) throws SAXException {
        write(' ');
        write(name);
        if (!isBooleanAttribute(name, value)) {
            write('=');
            write('"');
            int len = value.length();
            for (int i = 0; i < len; i++) {
                char c = value.charAt(i);
                switch(c) {
                    case '\n':
                        write(lineSeparator);
                        break;
                    case '&':
                        if (i + 1 < len && value.charAt(i + 1) == '{') {
                            write(c);
                        } else {
                            write("&amp;");
                        }
                        break;
                    case '"':
                        write("&quot;");
                        break;
                    case ' ':
                        write("&nbsp;");
                        break;
                    default:
                        if (c <= maxRepresentableChar) {
                            write(c);
                        } else {
                            write(getCharString(c));
                        }
                        break;
                }
            }
            write('"');
        }
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        int flags = getElementTypeFlags(qName);
        int contentType = (flags & CONTENT_TYPE);
        boolean isBlockElement = (flags & BLOCK_ELEMENT) != 0;
        if (contentType != EMPTY_CONTENT) {
            if (inPcdataChunk) {
                inPcdataChunk = false;
            } else if (indent && (!inBlock || isBlockElement)) {
                write(lineSeparator);
            }
            inBlock = !isBlockElement;
            write('<');
            write('/');
            write(qName);
            write('>');
        }
        if ((flags & PCDATA_ELEMENT) != 0) {
            inPcdataChunk = true;
        }
        inCdata = false;
    }

    public void comment(String str) throws SAXException {
        write("<!--");
        writeUnquoted(str);
        write("-->");
    }

    public void processingInstruction(String target, String data) throws SAXException {
        if (target == null) {
            comment(data);
            return;
        }
        write("<?");
        write(target);
        if (data.length() != 0) {
            write(' ');
            writeUnquoted(data);
        }
        write('>');
    }

    private static final String emptyElements[] = { "area", "base", "basefont", "br", "col", "frame", "hr", "img", "input", "isindex", "link", "meta", "param" };

    private static final String cdataElements[] = { "script", "style" };

    private static final String blockElements[] = { "address", "area", "base", "blockquote", "body", "br", "caption", "center", "col", "colgroup", "dd", "dir", "div", "dl", "dt", "fieldset", "form", "frame", "frameset", "h1", "h2", "h3", "h4", "h5", "h6", "head", "hr", "html", "isindex", "li", "link", "map", "menu", "meta", "noframes", "noscript", "ol", "p", "pre", "style", "table", "tbody", "tfoot", "thead", "title", "tr", "ul" };

    private static final String pcdataElements[] = { "applet", "img", "object" };

    private static final String booleanAttributes[] = { "checked", "compact", "declare", "defer", "disabled", "ismap", "multiple", "nohref", "noresize", "noshade", "nowrap", "readonly", "selected" };

    private static final String charEntities[] = { " nbsp", "¡iexcl", "¢cent", "£pound", "¤curren", "¥yen", "¦brvbar", "§sect", "¨uml", "©copy", "ªordf", "«laquo", "¬not", "­shy", "®reg", "¯macr", "°deg", "±plusmn", "²sup2", "³sup3", "´acute", "µmicro", "¶para", "·middot", "¸cedil", "¹sup1", "ºordm", "»raquo", "¼frac14", "½frac12", "¾frac34", "¿iquest", "ÀAgrave", "ÁAacute", "ÂAcirc", "ÃAtilde", "ÄAuml", "ÅAring", "ÆAElig", "ÇCcedil", "ÈEgrave", "ÉEacute", "ÊEcirc", "ËEuml", "ÌIgrave", "ÍIacute", "ÎIcirc", "ÏIuml", "ÐETH", "ÑNtilde", "ÒOgrave", "ÓOacute", "ÔOcirc", "ÕOtilde", "ÖOuml", "×times", "ØOslash", "ÙUgrave", "ÚUacute", "ÛUcirc", "ÜUuml", "ÝYacute", "ÞTHORN", "ßszlig", "àagrave", "áaacute", "âacirc", "ãatilde", "äauml", "åaring", "æaelig", "çccedil", "èegrave", "éeacute", "êecirc", "ëeuml", "ìigrave", "íiacute", "îicirc", "ïiuml", "ðeth", "ñntilde", "òograve", "óoacute", "ôocirc", "õotilde", "öouml", "÷divide", "øoslash", "ùugrave", "úuacute", "ûucirc", "üuuml", "ýyacute", "þthorn", "ÿyuml", "ŒOElig", "œoelig", "ŠScaron", "šscaron", "ŸYuml", "ƒfnof", "ˆcirc", "˜tilde", "ΑAlpha", "ΒBeta", "ΓGamma", "ΔDelta", "ΕEpsilon", "ΖZeta", "ΗEta", "ΘTheta", "ΙIota", "ΚKappa", "ΛLambda", "ΜMu", "ΝNu", "ΞXi", "ΟOmicron", "ΠPi", "ΡRho", "ΣSigma", "ΤTau", "ΥUpsilon", "ΦPhi", "ΧChi", "ΨPsi", "ΩOmega", "αalpha", "βbeta", "γgamma", "δdelta", "εepsilon", "ζzeta", "ηeta", "θtheta", "ιiota", "κkappa", "λlambda", "μmu", "νnu", "ξxi", "οomicron", "πpi", "ρrho", "ςsigmaf", "σsigma", "τtau", "υupsilon", "φphi", "χchi", "ψpsi", "ωomega", "ϑthetasym", "ϒupsih", "ϖpiv", " ensp", " emsp", " thinsp", "‌zwnj", "‍zwj", "‎lrm", "‏rlm", "–ndash", "—mdash", "‘lsquo", "’rsquo", "‚sbquo", "“ldquo", "”rdquo", "„bdquo", "†dagger", "‡Dagger", "•bull", "…hellip", "‰permil", "′prime", "″Prime", "‹lsaquo", "›rsaquo", "‾oline", "⁄frasl", "€euro", "ℑimage", "℘weierp", "ℜreal", "™trade", "ℵalefsym", "←larr", "↑uarr", "→rarr", "↓darr", "↔harr", "↵crarr", "⇐lArr", "⇑uArr", "⇒rArr", "⇓dArr", "⇔hArr", "∀forall", "∂part", "∃exist", "∅empty", "∇nabla", "∈isin", "∉notin", "∋ni", "∏prod", "∑sum", "−minus", "∗lowast", "√radic", "∝prop", "∞infin", "∠ang", "∧and", "∨or", "∩cap", "∪cup", "∫int", "∴there4", "∼sim", "≅cong", "≈asymp", "≠ne", "≡equiv", "≤le", "≥ge", "⊂sub", "⊃sup", "⊄nsub", "⊆sube", "⊇supe", "⊕oplus", "⊗otimes", "⊥perp", "⋅sdot", "⌈lceil", "⌉rceil", "⌊lfloor", "⌋rfloor", "〈lang", "〉rang", "◊loz", "♠spades", "♣clubs", "♥hearts", "♦diams" };

    private static String[][] charMap = new String[256][];

    private static Hashtable elementTypeTable = new Hashtable();

    private static Hashtable booleanAttributesTable = new Hashtable();

    static {
        for (int i = 0; i < charEntities.length; i++) {
            int c = charEntities[i].charAt(0);
            int lo = c & 0xff;
            int hi = c >> 8;
            if (charMap[hi] == null) charMap[hi] = new String[256];
            charMap[hi][lo] = "&" + charEntities[i].substring(1) + ";";
        }
        char[] charBuf = new char[1];
        for (int i = 0; i < 128; i++) {
            if (charMap[0][i] == null) {
                charBuf[0] = (char) i;
                charMap[0][i] = new String(charBuf);
            }
        }
        Integer type = new Integer(BLOCK_ELEMENT);
        for (int i = 0; i < blockElements.length; i++) elementTypeTable.put(blockElements[i], type);
        Integer blockType = new Integer(EMPTY_CONTENT | BLOCK_ELEMENT);
        Integer inlineType = new Integer(EMPTY_CONTENT);
        for (int i = 0; i < emptyElements.length; i++) {
            if (elementTypeTable.get(emptyElements[i]) == null) type = inlineType; else type = blockType;
            elementTypeTable.put(emptyElements[i], type);
        }
        blockType = new Integer(CDATA_CONTENT | BLOCK_ELEMENT);
        inlineType = new Integer(CDATA_CONTENT);
        for (int i = 0; i < cdataElements.length; i++) {
            if (elementTypeTable.get(cdataElements[i]) == null) type = inlineType; else type = blockType;
            elementTypeTable.put(cdataElements[i], type);
        }
        for (int i = 0; i < pcdataElements.length; i++) {
            type = (Integer) elementTypeTable.get(pcdataElements[i]);
            if (type == null) type = new Integer(PCDATA_ELEMENT); else type = new Integer(PCDATA_ELEMENT | type.intValue());
            elementTypeTable.put(pcdataElements[i], type);
        }
        for (int i = 0; i < booleanAttributes.length; i++) booleanAttributesTable.put(booleanAttributes[i], booleanAttributes[i]);
        elementTypeTable.put("head", new Integer(BLOCK_ELEMENT | HEAD_ELEMENT));
    }

    private static String getCharString(char c) {
        String[] v = charMap[c >> 8];
        if (v == null) {
            v = new String[256];
            charMap[c >> 8] = v;
        }
        String name = v[c & 0xFF];
        if (name == null) {
            name = "&#" + Integer.toString(c) + ";";
            v[c & 0xFF] = name;
        }
        return name;
    }

    private static int getElementTypeFlags(String name) {
        Integer type = (Integer) elementTypeTable.get(name.toLowerCase());
        if (type == null) {
            return 0;
        }
        return type.intValue();
    }

    private static boolean isBooleanAttribute(String name, String value) {
        if (!name.equalsIgnoreCase(value)) return false;
        return booleanAttributesTable.get(name.toLowerCase()) != null;
    }

    private final void write(String s) throws SAXException {
        int start = 0;
        int len = s.length();
        int avail = buf.length - bufUsed;
        while (avail < len) {
            s.getChars(start, start + avail, buf, bufUsed);
            bufUsed = buf.length;
            flushBuf();
            start += avail;
            len -= avail;
            avail = buf.length;
        }
        s.getChars(start, start + len, buf, bufUsed);
        bufUsed += len;
    }

    private final void write(char b) throws SAXException {
        if (bufUsed == buf.length) {
            flushBuf();
        }
        buf[bufUsed++] = b;
    }

    private final void flushBuf() throws SAXException {
        try {
            writer.write(buf, 0, bufUsed);
        } catch (IOException e) {
            throw new SAXException(e);
        }
        bufUsed = 0;
    }

    public void endDocument() throws SAXException {
        write(lineSeparator);
        if (bufUsed != 0) {
            flushBuf();
        }
        try {
            if (keepOpen) {
                writer.flush();
            } else {
                writer.close();
            }
        } catch (IOException e) {
            throw new SAXException(e);
        }
        writer = null;
        buf = null;
    }

    public void setDocumentLocator(org.xml.sax.Locator loc) {
    }
}
