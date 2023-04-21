package org.jivesoftware.smack.filter;

import org.jivesoftware.smack.packet.Packet;

/**
 * Filters for packets with a particular type of packet extension.
 *
 * @author Matt Tucker
 */
public class PacketExtensionFilter implements PacketFilter {

    private String elementName;

    private String namespace;

    /**
     * Creates a new packet extension filter. Packets will pass the filter if
     * they have a packet extension that matches the specified element name
     * and namespace.
     *
     * @param elementName the XML element name of the packet extension.
     * @param namespace the XML namespace of the packet extension.
     */
    public PacketExtensionFilter(String elementName, String namespace) {
        this.elementName = elementName;
        this.namespace = namespace;
    }

    /**
     * Creates a new packet extension filter. Packets will pass the filter if they have a packet
     * extension that matches the specified namespace.
     *
     * @param namespace the XML namespace of the packet extension.
     */
    public PacketExtensionFilter(String namespace) {
        this(null, namespace);
    }

    public boolean accept(Packet packet) {
        return packet.getExtension(elementName, namespace) != null;
    }
}
