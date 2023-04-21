package com.blogspot.qbeukes.addp.packets;

/**
 *
 * @author quintin
 */
public class DhcpConfigurationResponsePacket extends ConfigurationResponsePacket {

    public DhcpConfigurationResponsePacket(BufferParser parser) {
        super(PacketType.DhcpConfigResponse, parser);
    }
}
