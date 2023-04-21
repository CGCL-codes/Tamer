package org.ithacasoft.smack.packet.extensions.xmlbeans;

import org.apache.xmlbeans.XmlObject;
import org.jivesoftware.smack.packet.PacketExtension;

/**
 * Smack packet extension class.
 * @version $Id: XmlBeansPacketExtension.java 9 2009-02-01 17:02:26Z akartashev $
 * @since 0.0.3
 * @author Andrey Kartashev &lt;kartashev@gmail.com&gt;
 * @param <C> XML Beans type.
 */
class XmlBeansPacketExtension<C extends XmlObject> implements PacketExtension {

    /** SimpleMapper instance which created this instance. */
    private SimpleMapper simpleMapper;

    /** XML Bean. */
    private C value;

    /**
     * Creates an instance of this class.
     * @param passedSimpleMapper SimpleMapper instance which created this
     * instance.
     * @param passedValue XML Bean to map to the Smack packet extension.
     */
    XmlBeansPacketExtension(final SimpleMapper passedSimpleMapper, final C passedValue) {
        this.simpleMapper = passedSimpleMapper;
        this.value = passedValue;
    }

    /**
     * Returns XML representation of the extension.
     * @return XML representation of the extension.
     */
    public String toXML() {
        return getValue().xmlText();
    }

    /**
     * Returns name of the XML element of the extension.
     * @return name of the XML element of the extension.
     */
    public String getElementName() {
        return simpleMapper.getElementName();
    }

    /**
     * Returns XML namespace of the extension.
     * @return XML namespace of the extension.
     */
    public String getNamespace() {
        return simpleMapper.getXmlBeansNamespace();
    }

    /**
     * Returns mapped XML Bean.
     * @return mapped XML Bean.
     */
    public C getValue() {
        return value;
    }
}
