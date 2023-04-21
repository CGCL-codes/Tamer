package gnu.xml.pipeline;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Vector;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Pipeline filter to remember XHTML links found in a document,
 * so they can later be crawled.  Fragments are not counted, and duplicates
 * are ignored.  Callers are responsible for filtering out URLs they aren't
 * interested in.  Events are passed through unmodified.
 *
 * <p> Input MUST include a setDocumentLocator() call, as it's used to
 * resolve relative links in the absence of a "base" element.  Input MUST
 * also include namespace identifiers, since it is the XHTML namespace
 * identifier which is used to identify the relevant elements.
 *
 * <p><em>FIXME:</em> handle xml:base attribute ... in association with
 * a stack of base URIs.  Similarly, recognize/support XLink data.
 *
 * @author David Brownell
 */
public class LinkFilter extends EventFilter {

    private Vector vector = new Vector();

    private String baseURI;

    private boolean siteRestricted = false;

    public LinkFilter() {
        super.setContentHandler(this);
    }

    public LinkFilter(EventConsumer next) {
        super(next);
        super.setContentHandler(this);
    }

    /**
     * Returns an enumeration of the links found since the filter
     * was constructed, or since removeAllLinks() was called.
     *
     * @return enumeration of strings.
     */
    public Enumeration getLinks() {
        return vector.elements();
    }

    /**
     * Removes records about all links reported to the event
     * stream, as if the filter were newly created.
     */
    public void removeAllLinks() {
        vector = new Vector();
    }

    /**
     * Collects URIs for (X)HTML content from elements which hold them.
     */
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        String link;
        if ("http://www.w3.org/1999/xhtml".equals(uri)) {
            if ("a".equals(localName) || "base".equals(localName) || "area".equals(localName)) link = atts.getValue("href"); else if ("iframe".equals(localName) || "frame".equals(localName)) link = atts.getValue("src"); else if ("blockquote".equals(localName) || "q".equals(localName) || "ins".equals(localName) || "del".equals(localName)) link = atts.getValue("cite"); else link = null;
            link = maybeAddLink(link);
            if ("base".equals(localName) && link != null) baseURI = link;
            if ("iframe".equals(localName) || "img".equals(localName)) maybeAddLink(atts.getValue("longdesc"));
        }
        super.startElement(uri, localName, qName, atts);
    }

    private String maybeAddLink(String link) {
        int index;
        if (link == null) return null;
        if ((index = link.indexOf("#")) >= 0) link = link.substring(0, index);
        if (link.equals("")) return null;
        try {
            URL base = new URL((baseURI != null) ? baseURI : getDocumentLocator().getSystemId());
            URL url = new URL(base, link);
            link = url.toString();
            if (vector.contains(link)) return link;
            if (siteRestricted) {
                if (!base.getProtocol().equals(url.getProtocol())) return link;
                if (base.getHost() != null && !base.getHost().equals(url.getHost())) return link;
            }
            vector.addElement(link);
            return link;
        } catch (IOException e) {
        }
        return null;
    }

    /**
     * Reports an error if no Locator has been made available.
     */
    public void startDocument() throws SAXException {
        if (getDocumentLocator() == null) throw new SAXException("no Locator!");
    }

    /**
     * Forgets about any base URI information that may be recorded.
     * Applications will often want to call removeAllLinks(), likely
     * after examining the links which were reported.
     */
    public void endDocument() throws SAXException {
        baseURI = null;
        super.endDocument();
    }
}
