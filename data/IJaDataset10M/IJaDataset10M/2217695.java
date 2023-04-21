package net.sf.eBus.xml.parser;

import java.io.Reader;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import net.sf.eBus.text.Token;
import net.sf.eBus.xml.XmlDocument;
import net.sf.eBus.xml.XmlTag;

/**
 * Parses the XML document inside an input buffer and returns
 * the representative {@link net.sf.eBus.xml.XmlDocument} object.
 * If the parse fails for any reason, throws a
 * {@code java.text.ParseException} explaining the error.
 * <p>
 * This class is not designed to fulfill any XML API
 * specification. It is not a complete, full-featured XML parser.
 *
 * @author <a href="mailto:rapp@acm.org">Charles Rapp</a>
 */
@SuppressWarnings("unchecked")
public final class XmlParser {

    /**
     * Creates an XML parser for the given input.
     * @param input Read in the XML from this input.
     */
    public XmlParser(final Reader input) {
        _parserFSM = new XmlParserContext(this);
        _tagStack = new Stack<TagInfo>();
        _input = input;
        _docVersion = XmlDocument.DEFAULT_VERSION;
        _docEncoding = null;
        _docFlag = true;
        _rootTag = null;
        _tag = null;
        _attributeName = "";
        _reason = null;
        _parseStatus = true;
    }

    /**
     * Returns the {@link XmlDocument} by parsing the input
     * provided in
     * {@link net.sf.eBus.xml.parser.XmlParser#XmlParser(java.io.Reader)}.
     * @return the XML document represented by the buffer.
     * @exception ParseException
     * if {@code input} is not a well-formed XML document.
     */
    public XmlDocument parse() throws ParseException {
        final XmlLexer lexer = new XmlLexer(_input);
        Token token = null;
        Throwable cause = null;
        _continueFlag = true;
        while (_continueFlag == true && (token = lexer.nextToken(_rawTextMode)) != null) {
            try {
                _TransMethod[token.type()].invoke(_parserFSM, token);
            } catch (Exception jex) {
                _continueFlag = false;
                _parseStatus = false;
                _reason = jex.getMessage();
                cause = jex;
            }
        }
        if (_parseStatus == false) {
            final ParseException parsex = new ParseException(_reason, lexer.lineNumber());
            if (cause != null) {
                parsex.initCause(cause);
            }
            throw (parsex);
        }
        return (new XmlDocument(_docVersion, _docEncoding, _docFlag, _rootTag));
    }

    void setError(final String reason) {
        _reason = reason;
        _parseStatus = false;
        return;
    }

    void stopParsing() {
        _continueFlag = false;
        return;
    }

    void setRawTextMode(final boolean flag) {
        _rawTextMode = flag;
        return;
    }

    void clearDocument() {
        _docVersion = XmlDocument.DEFAULT_VERSION;
        _docEncoding = null;
        _docFlag = true;
        _rootTag = null;
        return;
    }

    void setDocumentVersion(final String version) {
        _docVersion = version.substring(1, (version.length() - 1));
        return;
    }

    void setDocumentEncoding(final String encoding) {
        _docEncoding = encoding.substring(1, (encoding.length() - 1));
        return;
    }

    void setDocumentStandalone(final String value) {
        final String flagStr = value.substring(1, (value.length() - 1));
        if (flagStr.equalsIgnoreCase("yes") == true) {
            _docFlag = true;
        } else if (flagStr.equalsIgnoreCase("no") == true) {
            _docFlag = false;
        } else {
            _reason = "invalid stand-alone value, " + value;
            _parseStatus = false;
            _continueFlag = false;
        }
        return;
    }

    void setDocumentRootTag(final XmlTag tag) {
        _rootTag = tag;
        return;
    }

    void createTag() {
        _tag = new TagInfo();
        return;
    }

    TagInfo getTag() {
        return (_tag);
    }

    void clearTag() {
        if (_tag != null) {
            _tag.clear();
            _tag = null;
        }
        return;
    }

    void setTagName(final String name) {
        _tag.name(name);
        return;
    }

    void addTagAttribute(final String key, final String value) {
        _tag.putAttribute(key, value.substring(1, (value.length() - 1)));
        return;
    }

    void addTagValue(final String text) {
        _tag.addValue(text);
        return;
    }

    void addTagValue(final XmlTag tag) {
        _tag.addTag(tag);
        return;
    }

    void pushTag() {
        if (_tag != null) {
            _tagStack.push(_tag);
            _tag = null;
        }
        return;
    }

    void popTag() {
        if (_tagStack.empty() == false) {
            _tag = _tagStack.pop();
        }
        return;
    }

    String getAttributeName() {
        return (_attributeName);
    }

    void setAttributeName(final String name) {
        _attributeName = name;
        return;
    }

    private final XmlParserContext _parserFSM;

    private final Stack<TagInfo> _tagStack;

    private final Reader _input;

    private boolean _rawTextMode;

    private String _docVersion;

    private String _docEncoding;

    private boolean _docFlag;

    private XmlTag _rootTag;

    private TagInfo _tag;

    private String _attributeName;

    private String _reason;

    private boolean _parseStatus;

    private boolean _continueFlag;

    private static Method[] _TransMethod;

    static {
        _TransMethod = new Method[XmlLexer.TOKEN_COUNT];
        try {
            final Class mapClass = XmlParserContext.class;
            _TransMethod[XmlLexer.COMMENT] = mapClass.getDeclaredMethod("COMMENT", Token.class);
            _TransMethod[XmlLexer.EMPTY_TAG_END] = mapClass.getDeclaredMethod("EMPTY_TAG_END", Token.class);
            _TransMethod[XmlLexer.END_TAG_START] = mapClass.getDeclaredMethod("END_TAG_START", Token.class);
            _TransMethod[XmlLexer.EOF] = mapClass.getDeclaredMethod("EOF", Token.class);
            _TransMethod[XmlLexer.ERROR] = mapClass.getDeclaredMethod("ERROR", Token.class);
            _TransMethod[XmlLexer.EQ] = mapClass.getDeclaredMethod("EQ", Token.class);
            _TransMethod[XmlLexer.NAME] = mapClass.getDeclaredMethod("NAME", Token.class);
            _TransMethod[XmlLexer.PI_TAG_START] = mapClass.getDeclaredMethod("PI_TAG_START", Token.class);
            _TransMethod[XmlLexer.PI_TAG_END] = mapClass.getDeclaredMethod("PI_TAG_END", Token.class);
            _TransMethod[XmlLexer.STRING] = mapClass.getDeclaredMethod("STRING", Token.class);
            _TransMethod[XmlLexer.TAG_END] = mapClass.getDeclaredMethod("TAG_END", Token.class);
            _TransMethod[XmlLexer.TAG_START] = mapClass.getDeclaredMethod("TAG_START", Token.class);
            _TransMethod[XmlLexer.TEXT] = mapClass.getDeclaredMethod("TEXT", Token.class);
        } catch (NoSuchMethodException ex1) {
            System.err.println("INITIALIZATION ERROR! " + ex1);
            System.exit(1);
        } catch (SecurityException ex2) {
            System.err.println("INITIALIZATION ERROR! " + ex2);
            System.exit(1);
        }
    }

    static final class TagInfo {

        private TagInfo() {
            _name = null;
            _attributeMap = new HashMap<String, String>();
            _values = new ArrayList<Object>();
        }

        String name() {
            return (_name);
        }

        void name(final String name) {
            _name = name;
            return;
        }

        String putAttribute(final String attribute, final String value) {
            return (_attributeMap.put(attribute, value));
        }

        void addTag(final XmlTag tag) {
            _values.add(tag);
            return;
        }

        void addValue(final String text) {
            _values.add(text);
            return;
        }

        void clear() {
            _name = null;
            _attributeMap.clear();
            _values.clear();
            return;
        }

        XmlTag generateTag() {
            return (new XmlTag(_name, _attributeMap, _values));
        }

        private String _name;

        private Map<String, String> _attributeMap;

        private List<Object> _values;
    }
}
