package net.community.chest.dom.impl;

import org.w3c.dom.DOMException;
import org.w3c.dom.Text;

/**
 * <P>Copyright 2008 as per GPLv2</P>
 *
 * @author Lyor G.
 * @since Feb 12, 2009 11:28:10 AM
 */
public class StandaloneTextImpl extends BaseCharacterDataImpl<Text> implements Text {

    public StandaloneTextImpl(String baseURI, String name, String value) {
        super(Text.class, baseURI, name, value);
    }

    public StandaloneTextImpl(String name, String value) {
        this(null, name, value);
    }

    public static final String DEFAULT_TEXT_NODE_NAME = "#text";

    public StandaloneTextImpl(String value) {
        this(DEFAULT_TEXT_NODE_NAME, value);
    }

    public StandaloneTextImpl() {
        this(null);
    }

    @Override
    public String getWholeText() {
        return getData();
    }

    @Override
    public boolean isElementContentWhitespace() {
        final CharSequence cs = getWholeText();
        final int csLen = (null == cs) ? 0 : cs.length();
        if (csLen <= 0) return false;
        for (int cIndex = 0; cIndex < csLen; cIndex++) {
            final char c = cs.charAt(cIndex);
            if ((' ' <= c) && (c <= (char) 0x7E)) return false;
        }
        return true;
    }

    @Override
    public Text replaceWholeText(String content) throws DOMException {
        try {
            final StandaloneTextImpl impl = (StandaloneTextImpl) clone();
            impl.setData(content);
            return impl;
        } catch (CloneNotSupportedException e) {
            throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "replaceWholeText(" + content + ") " + e.getMessage());
        }
    }

    @Override
    public Text splitText(int offset) throws DOMException {
        try {
            final StandaloneTextImpl impl = (StandaloneTextImpl) clone();
            final String d = getData(), t = d.substring(0, offset), n = d.substring(offset);
            setData(t);
            impl.setData(n);
            return impl;
        } catch (CloneNotSupportedException e) {
            throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "splitText(" + offset + ") " + e.getMessage());
        }
    }

    @Override
    public final short getNodeType() {
        return TEXT_NODE;
    }
}
