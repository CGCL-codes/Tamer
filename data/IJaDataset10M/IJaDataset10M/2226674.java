package net.jxta.impl.protocol;

import java.net.URI;
import java.util.Enumeration;
import java.net.URISyntaxException;
import java.util.logging.Level;
import net.jxta.logging.Logging;
import java.util.logging.Logger;
import net.jxta.document.Advertisement;
import net.jxta.document.AdvertisementFactory;
import net.jxta.document.Attribute;
import net.jxta.document.Document;
import net.jxta.document.Element;
import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocumentUtils;
import net.jxta.document.StructuredDocument;
import net.jxta.document.StructuredTextDocument;
import net.jxta.document.XMLElement;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;
import net.jxta.peergroup.PeerGroupID;
import net.jxta.protocol.RdvAdvertisement;
import net.jxta.protocol.RouteAdvertisement;

/**
 * This class implements the RdvAdvertisement.
 *
 * <p/><pre>
 *   <xs:complexType name="RdvAdvertisement">
 *     <xs:sequence>
 *       <xs:element name="RdvGroupId" type="jxta:JXTAID"/>
 *       <xs:element name="RdvPeerId" type="jxta:JXTAID"/>
 *       <xs:element name="RdvServiceName" type="xs:string"/>
 *       <xs:element name="Name" type="xs:string" minOccurs="0"/>
 *       <!-- This should be a route -->
 *       <xs:element name="RdvRoute" type="xs:anyType" minOccurs="0"/>
 *     </xs:sequence>
 *   </xs:complexType>
 * </pre>
 **/
public class RdvAdv extends RdvAdvertisement {

    /**
     *  Log4J Logger
     **/
    private static final transient Logger LOG = Logger.getLogger(RdvAdv.class.getName());

    private static final String[] INDEX_FIELDS = { PeerIDTag, ServiceNameTag, GroupIDTag };

    /**
     * Instantiator for our advertisement
     **/
    public static class Instantiator implements AdvertisementFactory.Instantiator {

        /**
         * {@inheritDoc}
         **/
        public String getAdvertisementType() {
            return RdvAdv.getAdvertisementType();
        }

        /**
         * {@inheritDoc}
         **/
        public Advertisement newInstance() {
            return new RdvAdv();
        }

        /**
         * {@inheritDoc}
         **/
        public Advertisement newInstance(Element root) {
            if (!XMLElement.class.isInstance(root)) {
                throw new IllegalArgumentException(getClass().getName() + " only supports XLMElement");
            }
            return new RdvAdv((XMLElement) root);
        }
    }

    /**
     *  Private constructor for new instances. Use the instantiator.
     */
    private RdvAdv() {
    }

    /**
     *  Private constructor for xml serialized instances. Use the instantiator.
     *  
     *  @param doc The XML serialization of the advertisement.
     */
    private RdvAdv(XMLElement doc) {
        String doctype = doc.getName();
        String typedoctype = "";
        Attribute itsType = doc.getAttribute("type");
        if (null != itsType) {
            typedoctype = itsType.getValue();
        }
        if (!doctype.equals(getAdvertisementType()) && !getAdvertisementType().equals(typedoctype)) {
            throw new IllegalArgumentException("Could not construct : " + getClass().getName() + "from doc containing a " + doc.getName());
        }
        Enumeration elements = doc.getChildren();
        while (elements.hasMoreElements()) {
            XMLElement elem = (XMLElement) elements.nextElement();
            if (!handleElement(elem)) {
                if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                    LOG.fine("Unhandled Element: " + elem.toString());
                }
            }
        }
        if (null == getGroupID()) {
            throw new IllegalArgumentException("Missing peer group ID");
        }
        if (null == getPeerID()) {
            throw new IllegalArgumentException("Missing peer ID");
        }
        if (null == getServiceName()) {
            throw new IllegalArgumentException("Missing service name");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAdvType() {
        return getAdvertisementType();
    }

    /**
     *  {@inheritDoc}
     **/
    @Override
    protected boolean handleElement(Element raw) {
        if (super.handleElement(raw)) {
            return true;
        }
        XMLElement elem = (XMLElement) raw;
        if (elem.getName().equals(RdvAdvertisement.GroupIDTag)) {
            try {
                URI groupID = new URI(elem.getTextValue().trim());
                setGroupID((PeerGroupID) IDFactory.fromURI(groupID));
            } catch (URISyntaxException badID) {
                throw new IllegalArgumentException("Bad group ID in advertisement");
            } catch (ClassCastException badID) {
                throw new IllegalArgumentException("ID is not a group ID");
            }
            return true;
        }
        if (elem.getName().equals(RdvAdvertisement.PeerIDTag)) {
            try {
                URI peerID = new URI(elem.getTextValue().trim());
                setPeerID((PeerID) IDFactory.fromURI(peerID));
            } catch (URISyntaxException badID) {
                throw new IllegalArgumentException("Bad group ID in advertisement");
            } catch (ClassCastException badID) {
                throw new IllegalArgumentException("ID is not a group ID");
            }
            return true;
        }
        if (elem.getName().equals(RdvAdvertisement.ServiceNameTag)) {
            setServiceName(elem.getTextValue());
            return true;
        }
        if (elem.getName().equals(RdvAdvertisement.RouteTag)) {
            for (Enumeration eachXpt = elem.getChildren(); eachXpt.hasMoreElements(); ) {
                XMLElement aXpt = (XMLElement) eachXpt.nextElement();
                RouteAdvertisement xptAdv = (RouteAdvertisement) AdvertisementFactory.newAdvertisement(aXpt);
                setRouteAdv(xptAdv);
            }
            return true;
        }
        if (elem.getName().equals(RdvAdvertisement.NameTag)) {
            setName(elem.getTextValue());
            return true;
        }
        return false;
    }

    /**
     *  {@inheritDoc}
     **/
    @Override
    public Document getDocument(MimeMediaType encodeAs) {
        if (null == getGroupID()) {
            throw new IllegalStateException("Missing peer group ID");
        }
        if (null == getPeerID()) {
            throw new IllegalStateException("Missing peer ID");
        }
        if (null == getServiceName()) {
            throw new IllegalStateException("Missing service name");
        }
        StructuredDocument adv = (StructuredDocument) super.getDocument(encodeAs);
        Element e = adv.createElement(RdvAdvertisement.GroupIDTag, getGroupID().toString());
        adv.appendChild(e);
        e = adv.createElement(RdvAdvertisement.PeerIDTag, getPeerID().toString());
        adv.appendChild(e);
        e = adv.createElement(RdvAdvertisement.ServiceNameTag, getServiceName());
        adv.appendChild(e);
        String peerName = getName();
        if (null != peerName) {
            e = adv.createElement(RdvAdvertisement.NameTag, getName());
            adv.appendChild(e);
        }
        if (getRouteAdv() != null) {
            Element el = adv.createElement(RdvAdvertisement.RouteTag);
            adv.appendChild(el);
            StructuredTextDocument xptDoc = (StructuredTextDocument) getRouteAdv().getDocument(encodeAs);
            StructuredDocumentUtils.copyElements(adv, el, xptDoc);
        }
        return adv;
    }

    /**
     *  {@inheritDoc}
     **/
    @Override
    public String[] getIndexFields() {
        return INDEX_FIELDS;
    }
}
