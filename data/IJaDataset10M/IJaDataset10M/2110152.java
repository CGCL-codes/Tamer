package com.aptana.ide.editor.html.parsing;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.aptana.ide.editor.html.parsing.nodes.HTMLParseNodeFactory;
import com.aptana.ide.parsing.IParseState;
import com.aptana.ide.parsing.ParseStateChild;
import com.aptana.ide.parsing.nodes.IParseNodeFactory;

/**
 * @author Kevin Lindsey
 */
public class HTMLParseState extends ParseStateChild {

    private static final String HTML_2_0 = "-//IETF//DTD HTML//EN";

    private static final String HTML_3_2 = "-//W3C//DTD HTML 3.2 Final//EN";

    private static final String HTML_4_0_1_STRICT = "-//W3C//DTD HTML 4.01//EN";

    private static final String HTML_4_0_1_TRANSITIONAL = "-//W3C//DTD HTML 4.01 Transitional//EN";

    private static final String HTML_4_0_1_FRAMESET = "-//W3C//DTD HTML 4.01 Frameset//EN";

    private static final String XHTML_1_0_STRICT = "-//W3C//DTD XHTML 1.0 Strict//EN";

    private static final String XHTML_1_0_TRANSITIONAL = "-//W3C//DTD XHTML 1.0 Transitional//EN";

    private static final String XHTML_1_0_FRAMESET = "-//W3C//DTD XHTML 1.0 Frameset//EN";

    private static final String XHTML_1_1_STRICT = "-//W3C//DTD XHTML 1.1//EN";

    private static Pattern _docTypeSniffer;

    private static HashMap _docTypeIndex;

    private static HashMap _endTagInfo;

    private String _rootElement;

    private String _pubId;

    private String _system;

    private int _documentType;

    /**
	 * getDocumentType
	 * 
	 * @return int;
	 */
    public int getDocumentType() {
        return this._documentType;
    }

    /**
	 * Returns the language mime type for this parseState
	 * 
	 * @see com.aptana.ide.parsing.IParseState#getLanguage()
	 */
    public String getLanguage() {
        return HTMLMimeType.MimeType;
    }

    /**
	 * getRootElement
	 * 
	 * @return String
	 */
    public String getRootElement() {
        return this._rootElement;
    }

    /**
	 * getPubId
	 * 
	 * @return String
	 */
    public String getPubId() {
        return this._pubId;
    }

    /**
	 * getSystem
	 * 
	 * @return String
	 */
    public String getSystem() {
        return this._system;
    }

    /**
	 * static constructor
	 */
    static {
        _docTypeSniffer = Pattern.compile("<!DOCTYPE\\s+(\\S+)\\s+PUBLIC\\s+((?:'[^']+')|(?:\"[^\"]+\"))(?:\\s+((?:'[^']+')|(?:\"[^\"]+\")))?");
        _docTypeIndex = new HashMap();
        _docTypeIndex.put(HTML_2_0, new Integer(HTMLDocumentType.HTML_2_0));
        _docTypeIndex.put(HTML_3_2, new Integer(HTMLDocumentType.HTML_3_2));
        _docTypeIndex.put(HTML_4_0_1_STRICT, new Integer(HTMLDocumentType.HTML_4_0_1_STRICT));
        _docTypeIndex.put(HTML_4_0_1_TRANSITIONAL, new Integer(HTMLDocumentType.HTML_4_0_1_TRANSITIONAL));
        _docTypeIndex.put(HTML_4_0_1_FRAMESET, new Integer(HTMLDocumentType.HTML_4_0_1_FRAMESET));
        _docTypeIndex.put(XHTML_1_0_STRICT, new Integer(HTMLDocumentType.XHTML_1_0_STRICT));
        _docTypeIndex.put(XHTML_1_0_TRANSITIONAL, new Integer(HTMLDocumentType.XHTML_1_0_TRANSITIONAL));
        _docTypeIndex.put(XHTML_1_0_FRAMESET, new Integer(HTMLDocumentType.XHTML_1_0_FRAMESET));
        _docTypeIndex.put(XHTML_1_1_STRICT, new Integer(HTMLDocumentType.XHTML_1_1_STRICT));
        _endTagInfo = new HashMap();
        _endTagInfo.put("area", new Integer(HTMLTagInfo.END_FORBIDDEN | HTMLTagInfo.EMPTY));
        _endTagInfo.put("base", new Integer(HTMLTagInfo.END_FORBIDDEN | HTMLTagInfo.EMPTY));
        _endTagInfo.put("basefont", new Integer(HTMLTagInfo.END_FORBIDDEN | HTMLTagInfo.EMPTY));
        _endTagInfo.put("body", new Integer(HTMLTagInfo.END_OPTIONAL));
        _endTagInfo.put("br", new Integer(HTMLTagInfo.END_FORBIDDEN | HTMLTagInfo.EMPTY));
        _endTagInfo.put("col", new Integer(HTMLTagInfo.END_FORBIDDEN | HTMLTagInfo.EMPTY));
        _endTagInfo.put("colgroup", new Integer(HTMLTagInfo.END_OPTIONAL));
        _endTagInfo.put("dd", new Integer(HTMLTagInfo.END_OPTIONAL));
        _endTagInfo.put("dt", new Integer(HTMLTagInfo.END_OPTIONAL));
        _endTagInfo.put("frame", new Integer(HTMLTagInfo.END_FORBIDDEN | HTMLTagInfo.EMPTY));
        _endTagInfo.put("area", new Integer(HTMLTagInfo.END_OPTIONAL));
        _endTagInfo.put("hr", new Integer(HTMLTagInfo.END_FORBIDDEN | HTMLTagInfo.EMPTY));
        _endTagInfo.put("html", new Integer(HTMLTagInfo.END_OPTIONAL));
        _endTagInfo.put("img", new Integer(HTMLTagInfo.END_FORBIDDEN | HTMLTagInfo.EMPTY));
        _endTagInfo.put("input", new Integer(HTMLTagInfo.END_FORBIDDEN | HTMLTagInfo.EMPTY));
        _endTagInfo.put("isindex", new Integer(HTMLTagInfo.END_FORBIDDEN | HTMLTagInfo.EMPTY));
        _endTagInfo.put("li", new Integer(HTMLTagInfo.END_OPTIONAL));
        _endTagInfo.put("link", new Integer(HTMLTagInfo.END_FORBIDDEN | HTMLTagInfo.EMPTY));
        _endTagInfo.put("meta", new Integer(HTMLTagInfo.END_FORBIDDEN | HTMLTagInfo.EMPTY));
        _endTagInfo.put("option", new Integer(HTMLTagInfo.END_OPTIONAL));
        _endTagInfo.put("p", new Integer(HTMLTagInfo.END_OPTIONAL));
        _endTagInfo.put("param", new Integer(HTMLTagInfo.END_FORBIDDEN | HTMLTagInfo.EMPTY));
        _endTagInfo.put("tbody", new Integer(HTMLTagInfo.END_OPTIONAL));
        _endTagInfo.put("td", new Integer(HTMLTagInfo.END_OPTIONAL));
        _endTagInfo.put("tfoot", new Integer(HTMLTagInfo.END_OPTIONAL));
        _endTagInfo.put("th", new Integer(HTMLTagInfo.END_OPTIONAL));
        _endTagInfo.put("thead", new Integer(HTMLTagInfo.END_OPTIONAL));
        _endTagInfo.put("tr", new Integer(HTMLTagInfo.END_OPTIONAL));
    }

    /**
	 * Create a new instance of HTMLParseState
	 */
    public HTMLParseState() {
        super();
    }

    /**
	 * Create a new instance of HTMLParseState
	 * 
	 * @param parent
	 *            The parent IParseState
	 */
    public HTMLParseState(IParseState parent) {
        super(parent);
    }

    /**
	 * @see com.aptana.ide.parsing.ParseStateChild#createParseNodeFactory()
	 */
    protected IParseNodeFactory createParseNodeFactory() {
        return new HTMLParseNodeFactory(this);
    }

    /**
	 * getCloseTagType
	 *
	 * @param tagName
	 * @return close tag type
	 */
    public int getCloseTagType(String tagName) {
        int result = HTMLTagInfo.END_REQUIRED;
        if (this._documentType < HTMLDocumentType.XHTML_1_0_STRICT) {
            String key = tagName.toLowerCase();
            if (_endTagInfo.containsKey(key)) {
                result = ((Integer) _endTagInfo.get(key)).intValue();
                result = (result & HTMLTagInfo.END_MASK);
            }
        }
        return result;
    }

    /**
	 * isEmptyTagType
	 *
	 * @param tagName
	 * @return empty tag type
	 */
    public boolean isEmptyTagType(String tagName) {
        boolean result = false;
        String key = tagName.toLowerCase();
        if (_endTagInfo.containsKey(key)) {
            int flags = ((Integer) _endTagInfo.get(key)).intValue();
            result = (flags & HTMLTagInfo.EMPTY) == HTMLTagInfo.EMPTY;
        }
        return result;
    }

    /**
	 * @see com.aptana.ide.parsing.ParseStateChild#setEditState(java.lang.String, java.lang.String, int, int)
	 */
    public void setEditState(String source, String insertedSource, int offset, int removeLength) {
        super.setEditState(source, insertedSource, offset, removeLength);
        int documentType = HTMLDocumentType.UNKNOWN;
        Matcher match = _docTypeSniffer.matcher(source);
        if (match.find()) {
            this._rootElement = match.group(1);
            this._pubId = match.group(2);
            this._system = match.group(3);
            this._pubId = this._pubId.substring(1, this._pubId.length() - 1);
            if (this._system != null && this._system.length() > 0) {
                this._system = this._system.substring(1, this._system.length() - 1);
            }
            if (this._rootElement.equals("html") || this._rootElement.equals("HTML")) {
                if (_docTypeIndex.containsKey(this._pubId)) {
                    documentType = ((Integer) _docTypeIndex.get(this._pubId)).intValue();
                }
            }
        }
        this._documentType = documentType;
    }
}
