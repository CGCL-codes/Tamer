package net.community.chest.io.sax;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.Stack;
import net.community.chest.Triplet;
import net.community.chest.dom.DOMUtils;
import net.community.chest.io.EOLStyle;
import net.community.chest.io.xml.BaseIOTransformer;
import net.community.chest.lang.StringUtil;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;

/**
 * <P>Copyright 2010 as per GPLv2</P>
 *
 * @author Lyor G.
 * @since May 12, 2010 12:40:54 PM
 */
public class PrettyPrintHandler extends DefaultHandler2 {

    private Appendable _output;

    public Appendable getOutput() {
        return _output;
    }

    public void setOutput(Appendable a) {
        _output = a;
    }

    private int _tabLength;

    public int getTabLength() {
        return _tabLength;
    }

    public void setTabLength(int tabLength) {
        _tabLength = tabLength;
    }

    public PrettyPrintHandler(Appendable a, int tabLength) {
        _output = a;
        _tabLength = tabLength;
    }

    public PrettyPrintHandler(Appendable a) {
        this(a, BaseIOTransformer.DEFAULT_TAB_LENGTH);
    }

    public PrettyPrintHandler() {
        this(null);
    }

    private final StringBuilder _tabsBuilder = new StringBuilder();

    protected CharSequence prepareTabs(Boolean decreaseTabs) throws SAXException {
        if (decreaseTabs != null) {
            if (decreaseTabs.booleanValue()) {
                final int len = _tabsBuilder.length();
                if (len <= 0) throw new SAXException("Mismatched tabs state");
                _tabsBuilder.setLength(len - 1);
            } else _tabsBuilder.append('\t');
        }
        return _tabsBuilder;
    }

    protected <A extends Appendable> A appendTabs(A a) throws SAXException {
        final CharSequence sb = prepareTabs(null);
        final int sbLen = (null == sb) ? 0 : sb.length();
        if (sbLen > 0) {
            try {
                a.append(sb);
            } catch (IOException e) {
                throw new SAXException("appendTabs(" + e.getClass().getName() + "): " + e.getMessage(), e);
            }
        }
        return a;
    }

    protected <A extends Appendable> A appendEOL(A a) throws IOException {
        return EOLStyle.LOCAL.appendEOL(a);
    }

    protected Appendable prepareOutput(boolean startNewLine, Boolean decreaseTabs) throws IOException, SAXException {
        final Appendable a = getOutput();
        if (null == a) throw new SAXException("No " + Appendable.class.getSimpleName() + " output provided");
        if (startNewLine) appendEOL(a);
        if (decreaseTabs != null) {
            final CharSequence tabs = prepareTabs(decreaseTabs);
            final int sbLen = (null == tabs) ? 0 : tabs.length();
            if (sbLen > 0) a.append(tabs);
        }
        return a;
    }

    private final Stack<Triplet<String, Boolean, Boolean>> _elemsStack = new Stack<Triplet<String, Boolean, Boolean>>();

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        final int remElems = (null == _elemsStack) ? 0 : _elemsStack.size();
        if (remElems > 0) throw new SAXException("Pre-populated " + remElems + " elements");
    }

    @Override
    public void comment(char[] ch, int start, int length) throws SAXException {
        super.comment(ch, start, length);
        if (length <= 0) return;
        try {
            final Appendable a = prepareOutput(true, Boolean.FALSE);
            a.append(DOMUtils.XML_COMMENT_START).append(new String(ch, start, length)).append(DOMUtils.XML_COMMENT_END);
            appendEOL(a);
        } catch (IOException e) {
            throw new SAXException("startCDATA(" + e.getClass().getName() + "): " + e.getMessage(), e);
        }
    }

    @Override
    public void startCDATA() throws SAXException {
        super.startCDATA();
        try {
            final Appendable a = prepareOutput(true, Boolean.FALSE);
            a.append(DOMUtils.XML_CDATA_START);
            appendEOL(a);
        } catch (IOException e) {
            throw new SAXException("startCDATA(" + e.getClass().getName() + "): " + e.getMessage(), e);
        }
    }

    @Override
    public void endCDATA() throws SAXException {
        try {
            final Appendable a = prepareOutput(true, Boolean.TRUE);
            a.append(DOMUtils.XML_CDATA_END);
            appendEOL(a);
        } catch (IOException e) {
            throw new SAXException("startCDATA(" + e.getClass().getName() + "): " + e.getMessage(), e);
        }
        super.endCDATA();
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (length > 0) {
            final CharBuffer cb = CharBuffer.wrap(ch, start, length);
            final Triplet<String, Boolean, Boolean> elemState = ((null == _elemsStack) || _elemsStack.isEmpty()) ? null : _elemsStack.peek();
            final Boolean prevWrite = (null == elemState) ? null : elemState.getV3();
            if (null == prevWrite) throw new SAXException("characters(" + cb + ") no previous element state");
            try {
                final Appendable a = prepareOutput(false, null);
                if (!prevWrite.booleanValue()) {
                    a.append(DOMUtils.XML_ELEM_END_DELIM);
                    elemState.setV3(Boolean.TRUE);
                }
                a.append(cb);
            } catch (IOException e) {
                throw new SAXException("characters(" + cb + ") " + e.getClass().getName() + ": " + e.getMessage(), e);
            }
        }
        super.characters(ch, start, length);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        final Triplet<String, Boolean, Boolean> prevState = ((null == _elemsStack) || _elemsStack.isEmpty()) ? null : _elemsStack.peek();
        final Boolean prevSubClassed = (null == prevState) ? null : prevState.getV2();
        try {
            if ((prevSubClassed != null) && (!prevSubClassed.booleanValue())) {
                final Appendable a = getOutput();
                a.append(DOMUtils.XML_ELEM_END_DELIM);
                prevState.setV2(Boolean.TRUE);
                prevState.setV3(Boolean.TRUE);
            }
            final Appendable a = prepareOutput(true, Boolean.FALSE).append(DOMUtils.XML_ELEM_START_DELIM).append(qName);
            final int numAttrs = (null == attributes) ? 0 : attributes.getLength();
            for (int aIndex = 0; aIndex < numAttrs; aIndex++) {
                final String aName = attributes.getLocalName(aIndex), aValue = attributes.getValue(aIndex);
                DOMUtils.appendAttribute(a, true, aName, aValue);
            }
            _elemsStack.push(new Triplet<String, Boolean, Boolean>(qName, Boolean.FALSE, Boolean.FALSE));
        } catch (IOException e) {
            throw new SAXException("startElement(" + qName + ") " + e.getClass().getName() + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        final Triplet<String, Boolean, Boolean> elemState = ((null == _elemsStack) || _elemsStack.isEmpty()) ? null : _elemsStack.pop();
        final Boolean subClassed = (null == elemState) ? null : elemState.getV2(), withText = (null == elemState) ? null : elemState.getV3();
        if ((null == withText) || (null == subClassed)) throw new SAXException("endElement(" + qName + ") no previous element state");
        final String elemName = elemState.getV1();
        if (StringUtil.compareDataStrings(qName, elemName, true) != 0) throw new SAXException("endElement(" + qName + ") mismatched name - expected=" + elemName);
        try {
            final Appendable a = prepareOutput(subClassed.booleanValue(), null);
            if (subClassed.booleanValue()) appendTabs(a);
            if (withText.booleanValue() || subClassed.booleanValue()) {
                a.append(DOMUtils.XML_TAG_CLOSURE_SEQ).append(qName).append(DOMUtils.XML_ELEM_END_DELIM);
            } else a.append(DOMUtils.XML_TAG_INLINE_SEQ);
            prepareTabs(Boolean.TRUE);
        } catch (IOException e) {
            throw new SAXException("endElement(" + qName + ") " + e.getClass().getName() + ": " + e.getMessage(), e);
        }
        super.endElement(uri, localName, qName);
    }

    @Override
    public void endDocument() throws SAXException {
        final CharSequence tabs = prepareTabs(null);
        final int tabsLen = (null == tabs) ? 0 : tabs.length();
        if (tabsLen > 0) throw new SAXException("Remainder of " + tabsLen + " TABS");
        final int remElems = (null == _elemsStack) ? 0 : _elemsStack.size();
        if (remElems > 0) throw new SAXException("Remainder of " + remElems + " elements");
        super.endDocument();
    }
}
