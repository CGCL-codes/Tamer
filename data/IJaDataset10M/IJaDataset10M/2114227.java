package org.garret.ptl.template;

import org.garret.ptl.startup.localization.DictionaryReader;
import org.garret.ptl.startup.localization.LocalizationEmitter;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * A preprocessor that wraps around another ContentHandler and replaces
 * i18n:text tags and i18n:attr attributes with localized data before forwarding
 * a stream of tags to wrapped handler.
 *
 * @author Andrey Subbotin
 */
class LocalizingHandlerWrapper implements ContentHandler {

    private final String locale;

    private final ContentHandler wrapped;

    private int tagDepth = 0;

    private int namespaceDepth = 0;

    private String[] namespaces = new String[20];

    private int[] namespaceTag = new int[20];

    private boolean isInTextTag = false;

    private String i18nCase;

    private final StringBuffer versionBody = new StringBuffer();

    private final List<String> localizableAttrs = new ArrayList<String>(10);

    public LocalizingHandlerWrapper(String locale, ContentHandler wrapped) {
        this.locale = locale;
        this.wrapped = wrapped;
    }

    /**
     * if this in i18n:text element, enter special mode that does not allow subelements
     * and localizes text contents.
     *
     * for other nodes, check for i18n:attr attribute that contains space-separated names of
     * localizable attributes, localize those attributes and write others verbatim. Example:
     *   <img i18n:attr="alt title" src="hello.gif" alt="Hello!" title="Greeting"/>
     * is converted to
     *   <img src="hello.gif" alt="Privet" title="Pozdravlenie"/>
     *
     */
    public void startElement(String uri, String localName, String qname, Attributes attrs) throws SAXException {
        if (isInTextTag) throw new SAXException("'i18n:text' cannot have subelements, but it contains tag " + qname);
        if (LocalizationEmitter.NAMESPACE_URI.equals(uri) && localName.equals("text")) {
            isInTextTag = true;
            i18nCase = attrs.getValue("case");
        } else {
            String catalogue = attrs.getValue(LocalizationEmitter.NAMESPACE_URI, "catalogue");
            if (catalogue != null) {
                if (namespaceDepth >= namespaces.length) throw new SAXException("Too many i18n:catalogue levels");
                namespaceDepth++;
                namespaces[namespaceDepth] = catalogue;
                namespaceTag[namespaceDepth] = tagDepth;
            }
            Attributes a = convert(attrs);
            wrapped.startElement(uri, localName, qname, a);
        }
        tagDepth++;
    }

    private Attributes convert(Attributes attrs) throws SAXException {
        int catalogueIndex = attrs.getIndex(LocalizationEmitter.NAMESPACE_URI, "catalogue");
        int attrIndex = attrs.getIndex(LocalizationEmitter.NAMESPACE_URI, "attr");
        if (catalogueIndex == -1 && attrIndex == -1) return attrs;
        AttributesImpl a = new AttributesImpl(attrs);
        if (catalogueIndex >= 0) a.removeAttribute(catalogueIndex);
        if (attrIndex >= 0) {
            a.removeAttribute(a.getIndex(LocalizationEmitter.NAMESPACE_URI, "attr"));
            localizableAttrs.clear();
            String attlist = attrs.getValue(attrIndex);
            for (StringTokenizer tokenizer = new StringTokenizer(attlist, " "); tokenizer.hasMoreTokens(); ) {
                String aname = tokenizer.nextToken();
                localizableAttrs.add(aname);
            }
        }
        for (int i = 0; i < a.getLength(); i++) {
            if (localizableAttrs.contains(a.getLocalName(i))) {
                String localized = localize(a.getValue(i), null);
                a.setValue(i, localized);
            } else if (LocalizationEmitter.NAMESPACE_URI.equals(a.getURI(i))) {
                throw new SAXException("Illegal i18n attribute: " + a.getQName(i));
            }
        }
        return a;
    }

    public void endElement(String uri, String localName, String qname) throws SAXException {
        if (isInTextTag) {
            String text = versionBody.toString();
            String localized = localize(text, i18nCase);
            wrapped.characters(localized.toCharArray(), 0, localized.length());
            isInTextTag = false;
            versionBody.setLength(0);
        } else {
            wrapped.endElement(uri, localName, qname);
        }
        if (namespaceDepth >= 0 && namespaceTag[namespaceDepth] == tagDepth) namespaceDepth--;
        tagDepth--;
    }

    public void characters(char[] buffer, int pos, int len) throws SAXException {
        if (isInTextTag) {
            versionBody.append(buffer, pos, len);
        } else {
            wrapped.characters(buffer, pos, len);
        }
    }

    private String localize(String value, String valueCase) throws SAXException {
        if (namespaceDepth < 0) throw new SAXException("i18n:text or i18n:attr must be preceded by i18n:catalogue");
        return DictionaryReader.localize(namespaces[namespaceDepth], locale, value, valueCase);
    }

    public void startDocument() throws SAXException {
        wrapped.startDocument();
    }

    public void endDocument() throws SAXException {
        wrapped.endDocument();
    }

    public void processingInstruction(String target, String data) throws SAXException {
        wrapped.processingInstruction(target, data);
    }

    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        wrapped.startPrefixMapping(prefix, uri);
    }

    public void endPrefixMapping(String prefix) throws SAXException {
        wrapped.endPrefixMapping(prefix);
    }

    public void ignorableWhitespace(char ch[], int start, int length) throws SAXException {
        wrapped.ignorableWhitespace(ch, start, length);
    }

    public void skippedEntity(String name) throws SAXException {
        wrapped.skippedEntity(name);
    }

    public void setDocumentLocator(Locator locator) {
        wrapped.setDocumentLocator(locator);
    }
}
