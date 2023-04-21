package org.dbe.composer.wfengine.bpel.impl;

import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.log4j.Logger;
import org.dbe.composer.wfengine.bpel.SdlBusinessProcessException;
import org.dbe.composer.wfengine.bpel.SdlMessageDataFactory;
import org.dbe.composer.wfengine.bpel.message.ISdlMessageData;
import org.dbe.composer.wfengine.util.SdlUtil;
import org.dbe.composer.wfengine.util.SdlXmlUtil;
import org.dbe.composer.wfengine.xml.XMLParserBase;
import org.jaxen.JaxenException;
import org.jaxen.XPath;
import org.jaxen.dom.DOMXPath;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Constructs a message data object from serialized message data.
 * @todo should pass type mapper even though this is just for transient fault messages
 */
public class SdlMessageDataDeserializer implements ISdlImplStateNames {

    private static final Logger logger = Logger.getLogger(SdlMessageDataDeserializer.class.getName());

    /** The XML <code>Element</code> containing the serialized message data. */
    private Element mMessageDataElement;

    /** The message type specifying the message data to produce. */
    private QName mMessageType;

    /** The resulting message data object. */
    private ISdlMessageData mMessageData;

    private boolean mIsSdl = true;

    /**
     * Default constructor.
     */
    public SdlMessageDataDeserializer(boolean isSdl) {
        logger.debug("SdlMessageDataDeserializer() isSdl=" + isSdl);
        mIsSdl = isSdl;
    }

    /**
     * Constructs a message data object from the specified serialized message
     * data and message type.
     */
    protected ISdlMessageData createMessageData(Element aMessageDataElement, QName aMessageType) throws SdlBusinessProcessException {
        logger.debug("createMessageData() msg_type=" + aMessageType);
        List partElements = selectNodes(aMessageDataElement, "./" + STATE_PART);
        ISdlMessageData messageData = SdlMessageDataFactory.instance().createMessageData(aMessageType);
        for (Iterator i = partElements.iterator(); i.hasNext(); ) {
            Element partElement = (Element) i.next();
            String name = partElement.getAttribute(STATE_NAME);
            Object value;
            Element data = SdlXmlUtil.getFirstSubElement(partElement);
            if (data != null) {
                value = createPartDocument(name, data);
            } else {
                value = SdlXmlUtil.getText(partElement);
            }
            messageData.setData(name, value);
        }
        return messageData;
    }

    /**
     * Returns a new part <code>Document</code> with the specified data.
     *
     * @param aPartName The name of the part to create.
     * @param aPartData The root of the part data.
     */
    protected Document createPartDocument(String aPartName, Element aPartData) {
        logger.debug("createPartDocument() " + aPartName);
        XMLParserBase parser = new XMLParserBase();
        parser.setValidating(false);
        parser.setNamespaceAware(true);
        Document document = parser.createDocument();
        String namespaceURI = aPartData.getNamespaceURI();
        String dataName = aPartData.getLocalName();
        String qualifiedName;
        if (!SdlUtil.isNullOrEmpty(namespaceURI) && dataName.equals(aPartName)) {
            namespaceURI = null;
            qualifiedName = dataName;
        } else {
            qualifiedName = aPartData.getNodeName();
        }
        Element root = document.createElementNS(namespaceURI, qualifiedName);
        document.appendChild(root);
        SdlXmlUtil.copyNodeContents(aPartData, root);
        return document;
    }

    /**
     * Returns a message data object constructed from the serialized message
     * data and message type specified with <code>setMessageDataElement</code>
     * and <code>setMessageType</code>.
     */
    public ISdlMessageData getMessageData() throws SdlBusinessProcessException {
        if (mMessageData == null) {
            if (mMessageDataElement == null) {
                logger.error("No parts data to deserialize!");
                throw new SdlBusinessProcessException("No parts data to deserialize!");
            }
            mMessageData = createMessageData(mMessageDataElement, getMessageType());
        }
        return mMessageData;
    }

    /**
     * Returns the message type specifying the message data to produce.
     */
    protected QName getMessageType() {
        if (mMessageType == null) {
            String localPart = mMessageDataElement.getAttribute(STATE_NAME);
            String namespace = mMessageDataElement.getAttribute(STATE_NAMESPACEURI);
            mMessageType = new QName(namespace, localPart);
        }
        return mMessageType;
    }

    /**
     * Selects nodes by XPath.
     *
     * @param aNode The node to select from.
     * @param aPath The XPath expression.
     * @return List The list of matching nodes.
     * @throws SdlBusinessProcessException
     */
    protected static List selectNodes(Node aNode, String aPath) throws SdlBusinessProcessException {
        try {
            XPath xpath = new DOMXPath(aPath);
            return xpath.selectNodes(aNode);
        } catch (JaxenException e) {
            logger.error("XPath query failed for " + aPath);
            throw new SdlBusinessProcessException("XPath query failed for " + aPath, e);
        }
    }

    /**
     * Sets the XML <code>Element</code> containing the serialized message data.
     *
     * @param aMessageDataElement
     */
    public void setMessageDataElement(Element aMessageDataElement) {
        mMessageDataElement = aMessageDataElement;
        mMessageData = null;
    }

    /**
     * Sets the message type specifying the message data to produce.
     *
     * @param aMessageType
     */
    public void setMessageType(QName aMessageType) {
        mMessageType = aMessageType;
        mMessageData = null;
    }
}
