package net.jxta.impl.pipe;

import net.jxta.document.Attribute;
import net.jxta.document.Document;
import net.jxta.document.Element;
import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocumentFactory;
import net.jxta.document.StructuredTextDocument;
import net.jxta.document.XMLDocument;
import net.jxta.document.XMLElement;
import net.jxta.id.ID;
import net.jxta.id.IDFactory;
import net.jxta.logging.Logging;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class implements a JXTA-WIRE header.
 */
public class WireHeader {

    /**
     * Logger
     */
    private static final Logger LOG = Logger.getLogger(WireHeader.class.getName());

    private static final String Name = "JxtaWire";

    private static final String MsgIdTag = "MsgId";

    private static final String PipeIdTag = "PipeId";

    private static final String SrcTag = "SrcPeer";

    private static final String TTLTag = "TTL";

    private static final String PeerTag = "VisitedPeer";

    private ID srcPeer = ID.nullID;

    private ID pipeID = ID.nullID;

    private String msgId = null;

    private int TTL = Integer.MIN_VALUE;

    public WireHeader() {
    }

    public WireHeader(Element root) {
        initialize(root);
    }

    public void setSrcPeer(ID p) {
        srcPeer = p;
    }

    public ID getSrcPeer() {
        return srcPeer;
    }

    public void setTTL(int t) {
        TTL = t;
    }

    public int getTTL() {
        return TTL;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String id) {
        this.msgId = id;
    }

    public ID getPipeID() {
        return pipeID;
    }

    public void setPipeID(ID id) {
        this.pipeID = id;
    }

    /**
     * Called to handle elements during parsing.
     *
     * @param elem Element to parse
     * @return true if element was handled, otherwise false.
     */
    protected boolean handleElement(XMLElement elem) {
        if (elem.getName().equals(SrcTag)) {
            try {
                URI pID = new URI(elem.getTextValue());
                setSrcPeer(IDFactory.fromURI(pID));
            } catch (URISyntaxException badID) {
                throw new IllegalArgumentException("Bad PeerID ID in header: " + elem.getTextValue());
            }
            return true;
        }
        if (elem.getName().equals(MsgIdTag)) {
            msgId = elem.getTextValue();
            return true;
        }
        if (elem.getName().equals(PipeIdTag)) {
            try {
                URI pipeID = new URI(elem.getTextValue());
                setPipeID(IDFactory.fromURI(pipeID));
            } catch (URISyntaxException badID) {
                throw new IllegalArgumentException("Bad pipe ID in header");
            }
            return true;
        }
        if (elem.getName().equals(TTLTag)) {
            TTL = Integer.parseInt(elem.getTextValue());
            return true;
        }
        return elem.getName().equals(PeerTag);
    }

    /**
     * internal method to process a document into a header.
     *
     * @param root where to start.
     */
    protected void initialize(Element root) {
        if (!XMLElement.class.isInstance(root)) {
            throw new IllegalArgumentException(getClass().getName() + " only supports XLMElement");
        }
        XMLElement doc = (XMLElement) root;
        String doctype = doc.getName();
        String typedoctype = "";
        Attribute itsType = doc.getAttribute("type");
        if (null != itsType) {
            typedoctype = itsType.getValue();
        }
        if (!doctype.equals(Name) && !Name.equals(typedoctype)) {
            throw new IllegalArgumentException("Could not construct : " + getClass().getName() + "from doc containing a " + doc.getName());
        }
        Enumeration elements = doc.getChildren();
        while (elements.hasMoreElements()) {
            XMLElement elem = (XMLElement) elements.nextElement();
            if (!handleElement(elem)) {
                if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                    LOG.fine("Unhandled Element: " + elem.getName());
                }
            }
        }
        if (null == getMsgId()) {
            throw new IllegalArgumentException("Header does not contain a message id");
        }
        if (ID.nullID == getPipeID()) {
            throw new IllegalArgumentException("Header does not contain a pipe id");
        }
        if (TTL < 1) {
            throw new IllegalArgumentException("TTL must be >= 1");
        }
    }

    /**
     * Returns the docment for this header
     *
     * @param encodeAs mime type encoding
     * @return the docment for this header
     */
    public Document getDocument(MimeMediaType encodeAs) {
        StructuredTextDocument doc = (StructuredTextDocument) StructuredDocumentFactory.newStructuredDocument(encodeAs, Name);
        if (doc instanceof XMLDocument) {
            ((XMLDocument) doc).addAttribute("xmlns:jxta", "http://jxta.org");
        }
        if (null == getMsgId()) {
            throw new IllegalStateException("Message id is not initialized");
        }
        if (ID.nullID == getPipeID()) {
            throw new IllegalStateException("PipeID is not initialized");
        }
        if (TTL < 1) {
            throw new IllegalStateException("TTL must be >= 1");
        }
        Element e;
        if ((srcPeer != null) && (srcPeer != ID.nullID)) {
            e = doc.createElement(SrcTag, srcPeer.toString());
            doc.appendChild(e);
        }
        e = doc.createElement(PipeIdTag, getPipeID().toString());
        doc.appendChild(e);
        e = doc.createElement(MsgIdTag, getMsgId());
        doc.appendChild(e);
        e = doc.createElement(TTLTag, Integer.toString(TTL));
        doc.appendChild(e);
        return doc;
    }
}
