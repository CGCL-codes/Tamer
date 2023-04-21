package com.sun.java.help.impl;

import java.io.CharConversionException;
import java.io.*;
import java.util.Hashtable;
import java.net.*;

public abstract class DocumentParser {

    static final char EOF = (char) -1;

    protected Reader source;

    int readOffset;

    ScanBuffer buffer;

    ScanBuffer escapeBuffer;

    ScanBuffer documentSource;

    boolean shouldCacheSource;

    /************************************************************************
    *****								*****
    *****		Public interface 				*****
    *****								*****
    ************************************************************************/
    public DocumentParser(InputStream in) {
        source = new MyBufferedReader(new InputStreamReader(in));
        init();
    }

    public DocumentParser(Reader src) {
        if (src instanceof MyBufferedReader) {
            source = src;
        } else {
            source = new MyBufferedReader(src);
        }
        init();
    }

    public void setInput(Reader src) {
        if (src instanceof MyBufferedReader) {
            source = src;
        } else {
            source = new MyBufferedReader(src);
        }
    }

    public void setInput(InputStream i, String encoding) throws UnsupportedEncodingException {
        if (i == null) {
            source = null;
            return;
        }
        source = new MyBufferedReader(new InputStreamReader(i, encoding));
    }

    public void setShouldCacheSource(boolean state) {
        shouldCacheSource = state;
    }

    public String getDocumentSource() {
        if (!shouldCacheSource) return null; else {
            int offset = (0 == documentSource.length()) ? 0 : documentSource.length() - 1;
            return new String(documentSource.buf, 0, offset) + "\n";
        }
    }

    public void parse() throws IOException {
        char c = EOF;
        buffer.clear();
        if (source != null) {
            c = readChar();
        }
        for (; ; ) {
            if (c == EOF) break;
            if (c == DocPConst.AMPERSAND) c = parseEscape(); else if (c == DocPConst.LANGLE) {
                buffer.flush(this);
                c = parseTag();
            } else {
                if (buffer.buflen >= buffer.buf.length) {
                    char[] x = new char[buffer.buf.length * buffer.scale];
                    System.arraycopy(buffer.buf, 0, x, 0, buffer.buf.length);
                    buffer.buf = x;
                }
                if (c != DocPConst.RETURN) buffer.buf[buffer.buflen++] = c;
                if (myCount >= mySize) {
                    try {
                        mySize = source.read(cb, 0, defaultCharBufferSize);
                        if (mySize < 0) {
                            break;
                        }
                        if (mySize == 0) {
                            System.err.println(" DocumentParser::parse() !!!ERROR !!! source.read(...) == 0");
                            break;
                        }
                        myCount = 0;
                    } catch (CharConversionException e) {
                        throw e;
                    } catch (IOException e) {
                        break;
                    }
                }
                if (shouldCacheSource) documentSource.add(cb[myCount]);
                c = cb[myCount++];
            }
        }
        buffer.flush(this);
    }

    public void parseText() throws IOException {
        char c;
        tag("PRE", null, false, false);
        buffer.clear();
        c = readChar();
        for (; ; ) {
            if (c == EOF) break;
            buffer.add(c);
            c = readChar();
        }
        buffer.flush(this);
    }

    /**
     * Invokes flush(). This method is provided so that flush() can 
     * be protected
     */
    protected void callFlush(char[] buf, int offset, int length) {
        flush(buf, offset, length);
    }

    /**
     * This method creates a block of text for a document.
     *
     * It should be overridden by the subclass
     */
    protected abstract void flush(char[] buf, int offset, int length);

    /**
     * This method inserts a comment
     *
     * It should be overridden by the subclass
     */
    protected abstract void comment(String s);

    /**
     * This method emits a tag
     *
     * It should be overridden by the subclass
     */
    protected abstract void tag(String name, TagProperties atts, boolean endTag, boolean emptyTag);

    /**
     * This method emits a pi
     *
     * It should be overridden by the subclass
     */
    protected abstract void pi(String target, String data);

    /**
     * This method emits a doctype.  Internal subset information is discarded
     *
     * It should be overridden by the subclass
     */
    protected abstract void doctype(String root, String publicId, String systemId);

    /**
     * This method looks up a &xxx; sequence in the document
     * properties (this is used for templates).  A return of null
     * means the proerty is undefined.
     *
     * It should be overridden by the subclass
     */
    protected abstract String documentAttribute(String name);

    /**
     * This method inserts a parse error string into the document
     *
     * It should be overridden by the subclass
     */
    protected abstract void errorString(String s);

    /************************************************************************
    *****								*****
    *****		Internal methods				*****
    *****								*****
    ************************************************************************/
    private void init() {
        buffer = new ScanBuffer(8192, 4);
        escapeBuffer = new ScanBuffer(8192, 4);
        documentSource = new ScanBuffer(8192, 4);
        readOffset = 0;
    }

    protected void findCloseAngleForComment(char c) throws IOException {
        buffer.add(c);
        for (; ; ) {
            c = readChar();
            if (c == DocPConst.RANGLE) break;
            buffer.add(c);
        }
        buffer.add(c);
        comment(buffer.extract(0));
        buffer.clear();
    }

    protected char handleCommentOrDoctype(char c) throws IOException {
        buffer.add(c);
        int offset = buffer.length();
        c = scanIdentifier(c);
        String name = buffer.extract(offset);
        if (!name.equals("DOCTYPE")) {
            findCloseAngleForComment(c);
            return readChar();
        }
        c = skipWhite(c);
        offset = buffer.length();
        c = scanIdentifier(c);
        String root = buffer.extract(offset);
        c = skipWhite(c);
        if (c == DocPConst.RANGLE) {
            buffer.clear();
            return readChar();
        }
        offset = buffer.length();
        c = scanIdentifier(c);
        name = buffer.extract(offset);
        String publicId = null;
        String systemId = null;
        if (name.equals("SYSTEM")) {
            c = skipWhite(c);
            offset = buffer.length();
            c = scanQuotedString(c);
            systemId = buffer.extract(offset);
            doctype(root, null, systemId);
            if (c != DocPConst.RANGLE) {
                findCloseAngleForComment(c);
            }
            buffer.clear();
            return readChar();
        } else if (name.equals("PUBLIC")) {
            c = skipWhite(c);
            offset = buffer.length();
            c = scanQuotedString(c);
            publicId = buffer.extract(offset);
            c = skipWhite(c);
            offset = buffer.length();
            c = scanQuotedString(c);
            systemId = buffer.extract(offset);
            doctype(root, publicId, systemId);
            if (c != DocPConst.RANGLE) {
                findCloseAngleForComment(c);
            }
            buffer.clear();
            return readChar();
        } else {
            if (c != DocPConst.RANGLE) {
                findCloseAngleForComment(c);
            }
            findCloseAngleForComment(c);
            doctype(root, null, null);
            buffer.clear();
            return readChar();
        }
    }

    protected void setXmlEntities(TagProperties attr) {
    }

    protected char parseTag() throws IOException {
        char c;
        String name;
        TagProperties attributes;
        int offset;
        int tagStartOffset;
        boolean endTag;
        boolean emptyTag;
        buffer.clear();
        tagStartOffset = 0;
        c = DocPConst.LANGLE;
        buffer.add(DocPConst.LANGLE);
        c = readChar();
        if (c == DocPConst.EXCLAIM) {
            int resetPos = 0;
            buffer.add(DocPConst.EXCLAIM);
            c = readChar();
            if (c != DocPConst.MINUS) {
                return handleCommentOrDoctype(c);
            }
            buffer.add(c);
            c = readChar();
            if (c != DocPConst.MINUS) {
                findCloseAngleForComment(c);
                return readChar();
            }
            buffer.add(c);
            resetPos = buffer.length();
            int nDash = 0;
            for (; ; ) {
                c = readChar();
                if (c == EOF) {
                    commentEOFError(resetPos);
                    break;
                }
                if (c != DocPConst.MINUS) {
                    buffer.add(c);
                    continue;
                }
                while (c == DocPConst.MINUS) {
                    buffer.add(c);
                    nDash++;
                    c = readChar();
                }
                if (c == EOF) {
                    commentEOFError(resetPos);
                    break;
                }
                buffer.add(c);
                if ((nDash >= 2) && (c == DocPConst.RANGLE)) {
                    comment(buffer.extract(0));
                    buffer.clear();
                    return readChar();
                }
                nDash = 0;
            }
        }
        if (c == DocPConst.QUESTION) {
            int resetPos = 0;
            StringBuffer target = new StringBuffer();
            buffer.add(DocPConst.QUESTION);
            while (((c = readChar()) != DocPConst.DQUOTE) && c != DocPConst.SPACE && c != DocPConst.TAB && c != DocPConst.NEWLINE && c != DocPConst.RANGLE) {
                buffer.add(c);
                target.append(c);
            }
            if (!target.toString().equals("xml")) {
                buffer.clear();
                while (true) {
                    while ((c = readChar()) != DocPConst.QUESTION && c != EOF) {
                        buffer.add(c);
                    }
                    if (c == EOF) {
                        eofError();
                        return readChar();
                    }
                    c = readChar();
                    if (c != DocPConst.RANGLE && c != EOF) {
                        buffer.add(DocPConst.QUESTION);
                        buffer.add(c);
                    }
                    if (c == EOF) {
                        eofError();
                        return readChar();
                    } else {
                        pi(target.toString(), buffer.extract(0));
                        return readChar();
                    }
                }
            }
            target = null;
            c = readChar();
            attributes = null;
            for (; ; ) {
                c = skipWhite(c);
                if (c == EOF) {
                    eofError();
                    return c;
                }
                if (c == DocPConst.QUESTION) break;
                offset = buffer.length();
                c = scanIdentifier(c);
                if (offset == buffer.length()) {
                    error("Expecting an attribute");
                    skipToCloseAngle(c);
                    return readChar();
                }
                String attname = buffer.extract(offset);
                c = skipWhite(c);
                if (attributes == null) attributes = new TagProperties();
                String attvalue;
                if (c == DocPConst.EQUALS) {
                    buffer.add(c);
                    c = readChar();
                    int voff;
                    c = skipWhite(c);
                    if (c == DocPConst.QUESTION || c == DocPConst.LANGLE) {
                        attvalue = "";
                    } else if (c == DocPConst.DQUOTE) {
                        buffer.add(c);
                        voff = buffer.length();
                        for (; ; ) {
                            c = readChar();
                            if (c == EOF) {
                                eofError();
                                return c;
                            }
                            if (c == DocPConst.DQUOTE) break;
                            if (c == DocPConst.AMPERSAND) c = parseEscape();
                            buffer.add(c);
                        }
                        attvalue = buffer.extract(voff);
                        buffer.add(c);
                        c = readChar();
                    } else {
                        voff = buffer.length();
                        buffer.add(c);
                        for (; ; ) {
                            c = readChar();
                            if (c == EOF) {
                                eofError();
                                return c;
                            }
                            if (c == DocPConst.DQUOTE || c == DocPConst.SPACE || c == DocPConst.TAB || c == DocPConst.NEWLINE || c == DocPConst.QUESTION) break;
                            if (c == DocPConst.AMPERSAND) c = parseEscape();
                            buffer.add(c);
                        }
                        attvalue = buffer.extract(voff);
                    }
                } else {
                    attvalue = "true";
                }
                attributes.put(attname, attvalue);
            }
            c = readChar();
            if (c == EOF) {
                eofError();
                return readChar();
            }
            c = skipWhite(c);
            buffer.add(c);
            if ((c == DocPConst.RANGLE)) {
                setXmlEntities(attributes);
                buffer.clear();
                return readChar();
            } else {
                error("Expecting ?>");
                skipToCloseAngle(c);
                return readChar();
            }
        }
        c = skipWhite(c);
        if (c == EOF) {
            eofError();
            return c;
        }
        if (c == DocPConst.SLASH) {
            buffer.add(c);
            c = skipWhite(readChar());
            endTag = true;
            emptyTag = false;
        } else {
            endTag = false;
            emptyTag = false;
        }
        offset = buffer.length();
        c = scanIdentifier(c);
        name = buffer.extract(offset);
        attributes = null;
        for (; ; ) {
            c = skipWhite(c);
            if (c == EOF) {
                eofError();
                return c;
            }
            if (c == DocPConst.RANGLE) break;
            if (c == DocPConst.SLASH) {
                buffer.add(c);
                c = readChar();
                if (c == DocPConst.RANGLE) {
                    endTag = true;
                    emptyTag = true;
                    break;
                } else {
                    error("Expecting />");
                    skipToCloseAngle(c);
                    return readChar();
                }
            }
            if (c == DocPConst.LANGLE) {
                tag(name, attributes, endTag, false);
                buffer.clear();
                return (DocPConst.LANGLE);
            }
            offset = buffer.length();
            c = scanIdentifier(c);
            if (offset == buffer.length()) {
                error("Expecting an attribute (2)");
                skipToCloseAngle(c);
                return readChar();
            }
            String attname = buffer.extract(offset);
            c = skipWhite(c);
            if (attributes == null) attributes = new TagProperties();
            String attvalue;
            if (c == DocPConst.EQUALS) {
                buffer.add(c);
                c = readChar();
                int voff;
                c = skipWhite(c);
                if (c == DocPConst.RANGLE || c == DocPConst.LANGLE) {
                    attvalue = "";
                } else if (c == DocPConst.DQUOTE) {
                    buffer.add(c);
                    voff = buffer.length();
                    for (; ; ) {
                        c = readChar();
                        if (c == EOF) {
                            eofError();
                            return c;
                        }
                        if (c == DocPConst.DQUOTE) break;
                        if (c == DocPConst.AMPERSAND) c = parseEscape();
                        buffer.add(c);
                    }
                    attvalue = buffer.extract(voff);
                    buffer.add(c);
                    c = readChar();
                } else {
                    voff = buffer.length();
                    buffer.add(c);
                    for (; ; ) {
                        c = readChar();
                        if (c == EOF) {
                            eofError();
                            return c;
                        }
                        if (c == DocPConst.DQUOTE || c == DocPConst.SPACE || c == DocPConst.TAB || c == DocPConst.NEWLINE || c == DocPConst.RANGLE) break;
                        if (c == DocPConst.AMPERSAND) c = parseEscape();
                        buffer.add(c);
                    }
                    attvalue = buffer.extract(voff);
                }
            } else {
                attvalue = "true";
            }
            attributes.put(attname, attvalue);
        }
        tag(name, attributes, endTag, emptyTag);
        buffer.clear();
        return readChar();
    }

    protected char parseEscape() throws IOException {
        char c;
        int offset;
        offset = buffer.length();
        buffer.add(DocPConst.AMPERSAND);
        c = readChar();
        if (c == EOF) {
            generateError(offset);
            return c;
        }
        if (c == DocPConst.HASH) {
            int x;
            x = 0;
            for (; ; ) {
                c = readChar();
                if (c == EOF) {
                    generateError(offset);
                    return c;
                } else if (c == DocPConst.SEMICOLON) {
                    c = DocPConst.NULL;
                    break;
                } else if (!Character.isDigit(c)) {
                    if (x > 0) {
                        break;
                    } else {
                        error("Expecting a digit");
                        generateError(offset);
                        return c;
                    }
                }
                buffer.add(c);
                x = x * 10 + Character.digit(c, 10);
            }
            buffer.reset(offset);
            buffer.add((char) x);
        } else if (Character.isLowerCase(c) || Character.isUpperCase(c)) {
            if (entities == null) initEntities();
            escapeBuffer.clear();
            escapeBuffer.add(c);
            for (; ; ) {
                buffer.add(c);
                c = readChar();
                if (c == EOF) {
                    generateError(offset);
                    return c;
                }
                if (Character.isLowerCase(c) || Character.isUpperCase(c)) {
                    escapeBuffer.add(c);
                    if (entities.get(escapeBuffer.extract(0)) != null) {
                        c = readChar();
                        if (c == DocPConst.SEMICOLON) c = DocPConst.NULL;
                        break;
                    }
                } else if (c == DocPConst.SEMICOLON) {
                    c = DocPConst.NULL;
                    break;
                } else {
                    error("Expecting a letter");
                    generateError(offset);
                    return c;
                }
            }
            String s = escapeBuffer.extract(0);
            buffer.reset(offset);
            Character x = (Character) entities.get(s);
            if (x != null) {
                return x.charValue();
            } else {
                String a = documentAttribute(s);
                if (a != null) {
                    int i;
                    for (i = 0; i < a.length(); i++) buffer.add(a.charAt(i));
                }
            }
        } else {
            error("Expecting a letter o");
            generateError(offset);
            return c;
        }
        if (c != DocPConst.NULL) return c; else return readChar();
    }

    Hashtable entities;

    protected void initEntities() {
        entities = new Hashtable();
        entities.put("quot", new Character(DocPConst.DQUOTE));
        entities.put("amp", new Character(DocPConst.AMPERSAND));
        entities.put("gt", new Character(DocPConst.RANGLE));
        entities.put("lt", new Character(DocPConst.LANGLE));
        entities.put("nbsp", new Character((char) 160));
        entities.put("copy", new Character((char) 169));
        entities.put("Agrave", new Character((char) 192));
        entities.put("Aacute", new Character((char) 193));
        entities.put("Acirc", new Character((char) 194));
        entities.put("Atilde", new Character((char) 195));
        entities.put("Auml", new Character((char) 196));
        entities.put("Aring", new Character((char) 197));
        entities.put("AElig", new Character((char) 198));
        entities.put("Ccedil", new Character((char) 199));
        entities.put("Egrave", new Character((char) 200));
        entities.put("Eacute", new Character((char) 201));
        entities.put("Ecirc", new Character((char) 202));
        entities.put("Euml", new Character((char) 203));
        entities.put("Igrave", new Character((char) 204));
        entities.put("Iacute", new Character((char) 205));
        entities.put("Icirc", new Character((char) 206));
        entities.put("Iuml", new Character((char) 207));
        entities.put("Ntilde", new Character((char) 209));
        entities.put("Ograve", new Character((char) 210));
        entities.put("Oacute", new Character((char) 211));
        entities.put("Ocirc", new Character((char) 212));
        entities.put("Otilde", new Character((char) 213));
        entities.put("Ouml", new Character((char) 214));
        entities.put("Oslash", new Character((char) 216));
        entities.put("Ugrave", new Character((char) 217));
        entities.put("Uacute", new Character((char) 218));
        entities.put("Ucirc", new Character((char) 219));
        entities.put("Uuml", new Character((char) 220));
        entities.put("Yacute", new Character((char) 221));
        entities.put("THORN", new Character((char) 222));
        entities.put("szlig", new Character((char) 223));
        entities.put("agrave", new Character((char) 224));
        entities.put("aacute", new Character((char) 225));
        entities.put("acirc", new Character((char) 226));
        entities.put("atilde", new Character((char) 227));
        entities.put("auml", new Character((char) 228));
        entities.put("aring", new Character((char) 229));
        entities.put("aelig", new Character((char) 230));
        entities.put("ccedil", new Character((char) 231));
        entities.put("egrave", new Character((char) 232));
        entities.put("eacute", new Character((char) 233));
        entities.put("ecirc", new Character((char) 234));
        entities.put("euml", new Character((char) 235));
        entities.put("igrave", new Character((char) 236));
        entities.put("iacute", new Character((char) 237));
        entities.put("icirc", new Character((char) 238));
        entities.put("iuml", new Character((char) 239));
        entities.put("eth", new Character((char) 240));
        entities.put("ntilde", new Character((char) 241));
        entities.put("ograve", new Character((char) 242));
        entities.put("oacute", new Character((char) 243));
        entities.put("ocirc", new Character((char) 244));
        entities.put("otilde", new Character((char) 245));
        entities.put("ouml", new Character((char) 246));
        entities.put("oslash", new Character((char) 248));
        entities.put("ugrave", new Character((char) 249));
        entities.put("uacute", new Character((char) 250));
        entities.put("ucirc", new Character((char) 251));
        entities.put("uuml", new Character((char) 252));
        entities.put("yacute", new Character((char) 253));
        entities.put("thorn", new Character((char) 254));
        entities.put("yuml", new Character((char) 255));
    }

    protected char scanIdentifier(char c) throws IOException {
        for (; ; ) {
            if (!(c == DocPConst.HORIZBAR || c == DocPConst.COLON || (c >= DocPConst.ZERO && c <= DocPConst.NINE) || Character.isLetter(c))) {
                break;
            }
            if (buffer.buflen >= buffer.buf.length) {
                char[] x = new char[buffer.buf.length * buffer.scale];
                System.arraycopy(buffer.buf, 0, x, 0, buffer.buf.length);
                buffer.buf = x;
            }
            buffer.buf[buffer.buflen++] = c;
            if (myCount >= mySize) {
                try {
                    mySize = source.read(cb, 0, defaultCharBufferSize);
                    if (mySize < 0) {
                        break;
                    }
                    if (mySize == 0) {
                        System.err.println(" DocumentParser::scanIdentifier() !!!ERROR !!! source.read(...) == 0");
                        break;
                    }
                    myCount = 0;
                } catch (CharConversionException e) {
                    throw e;
                } catch (IOException e) {
                    break;
                }
            }
            if (shouldCacheSource) documentSource.add(cb[myCount]);
            c = cb[myCount++];
        }
        return c;
    }

    protected char scanQuotedString(char c) throws IOException {
        c = skipWhite(c);
        if (c == DocPConst.DQUOTE) {
            for (; ; ) {
                c = readChar();
                if (c == DocPConst.DQUOTE || c == DocPConst.RANGLE) {
                    break;
                }
                buffer.add(c);
            }
            return readChar();
        } else if (c == DocPConst.QUOTE) {
            for (; ; ) {
                c = readChar();
                if (c == DocPConst.QUOTE || c == DocPConst.RANGLE) {
                    break;
                }
                buffer.add(c);
            }
            return readChar();
        } else {
            return c;
        }
    }

    protected char skipWhite(char c) throws IOException {
        while (c == DocPConst.SPACE || c == DocPConst.RETURN || c == DocPConst.TAB || c == DocPConst.NEWLINE) {
            if (buffer.buflen >= buffer.buf.length) {
                char[] x = new char[buffer.buf.length * buffer.scale];
                System.arraycopy(buffer.buf, 0, x, 0, buffer.buf.length);
                buffer.buf = x;
            }
            buffer.buf[buffer.buflen++] = c;
            if (myCount >= mySize) {
                try {
                    mySize = source.read(cb, 0, defaultCharBufferSize);
                    if (mySize < 0) {
                        break;
                    }
                    if (mySize == 0) {
                        System.err.println(" DocumentParser::parse() !!!ERROR !!! source.read(...) == 0");
                        break;
                    }
                    myCount = 0;
                } catch (CharConversionException e) {
                    throw e;
                } catch (IOException e) {
                    break;
                }
            }
            if (shouldCacheSource) documentSource.add(cb[myCount]);
            c = cb[myCount++];
        }
        return c;
    }

    int defaultCharBufferSize = 8192;

    char cb[] = new char[defaultCharBufferSize];

    int mySize = 0;

    int myCount = 0;

    protected char readChar() throws IOException {
        if (myCount >= mySize) {
            try {
                mySize = source.read(cb, 0, defaultCharBufferSize);
                if (mySize < 0) {
                    return EOF;
                }
                if (mySize == 0) {
                    System.err.println(" DocumentParser::readChar() !!!ERROR !!! source.read(...) == 0");
                    return EOF;
                }
                myCount = 0;
            } catch (CharConversionException e) {
                throw e;
            } catch (IOException e) {
                return EOF;
            }
        }
        if (shouldCacheSource) documentSource.add(cb[myCount]);
        return cb[myCount++];
    }

    protected void skipToCloseAngle(char c) throws IOException {
        for (; ; ) {
            if (buffer.buflen >= buffer.buf.length) {
                char[] x = new char[buffer.buf.length * buffer.scale];
                System.arraycopy(buffer.buf, 0, x, 0, buffer.buf.length);
                buffer.buf = x;
            }
            buffer.buf[buffer.buflen++] = c;
            if (c == DocPConst.RANGLE) {
                break;
            }
            if (myCount >= mySize) {
                try {
                    mySize = source.read(cb, 0, defaultCharBufferSize);
                    if (mySize < 0) {
                        break;
                    }
                    if (mySize == 0) {
                        System.err.println(" DocumentParser::skipToCloseAngle() !!!ERROR !!! source.read(...) == 0");
                        break;
                    }
                    myCount = 0;
                } catch (CharConversionException e) {
                    throw e;
                } catch (IOException e) {
                    break;
                }
            }
            c = cb[myCount++];
        }
        generateError(0);
    }

    protected void generateError(int offset) {
        String s = buffer.extract(offset);
        buffer.reset(offset);
        buffer.flush(this);
        errorString(s);
    }

    protected void commentEOFError(int resetTo) {
        eofError();
    }

    protected void eofError() {
        error("Unexpected end of file");
        generateError(0);
    }

    void error(String s) {
        System.err.println("DocumentParser Error: " + s);
    }
}

class ScanBuffer {

    char[] buf;

    int buflen;

    int scale = 2;

    ScanBuffer() {
        buf = new char[256];
    }

    ScanBuffer(int l, int s) {
        buf = new char[l];
        scale = s;
    }

    protected void clear() {
        buflen = 0;
    }

    protected void reset(int offset) {
        buflen = offset;
    }

    protected void flush(DocumentParser owner) {
        if (buflen > 0) {
            owner.callFlush(buf, 0, buflen);
            buflen = 0;
        }
    }

    protected void add(char c) {
        if (buflen >= buf.length) {
            char[] x = new char[buf.length * scale];
            System.arraycopy(buf, 0, x, 0, buf.length);
            buf = x;
        }
        buf[buflen++] = c;
    }

    protected int length() {
        return buflen;
    }

    public String toString() {
        return "ScanBuffer, buf = " + buf + ", buflen = " + buflen;
    }

    protected String extract(int offset) {
        return new String(buf, offset, buflen - offset);
    }
}

class MyBufferedReader extends BufferedReader {

    public MyBufferedReader(Reader in, int sz) {
        super(in, sz);
    }

    public MyBufferedReader(Reader in) {
        super(in);
    }

    public int read(char cbuf[], int off, int len) throws IOException {
        if (lock == null) {
            return -1;
        }
        return super.read(cbuf, off, len);
    }
}
