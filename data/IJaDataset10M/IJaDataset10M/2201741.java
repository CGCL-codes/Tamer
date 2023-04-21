package org.dreamspeak.lib.protocol.packets.inbound.reliablecontent;

import java.nio.ByteBuffer;
import org.dreamspeak.lib.data.Channel;
import org.dreamspeak.lib.data.ChannelList;
import org.dreamspeak.lib.protocol.ProtocolException;
import org.dreamspeak.lib.protocol.packets.ResponsibleForPacketTypes;

/**
 * TODO: Proper documentation
 * 
 * @author avithan
 */
@ResponsibleForPacketTypes(0x006F)
public class ChannelNameChange extends ChannelListUpdate {

    public ChannelNameChange(short packetType, ByteBuffer data) throws ProtocolException {
        super(packetType, data);
        byte[] raw = data.array();
        channelId = data.getInt(0x00);
        userId = data.getInt(0x04);
        int pos = 0x08;
        StringBuilder rawNewName = new StringBuilder();
        while (pos < raw.length && raw[pos] != 0x00) {
            rawNewName.append((char) raw[pos]);
            pos++;
        }
        newName = rawNewName.toString();
    }

    final int userId;

    final int channelId;

    final String newName;

    public int getChannelId() {
        return channelId;
    }

    public int getUserId() {
        return userId;
    }

    public String getNewName() {
        return newName;
    }

    /**
	 * Applies changes in this update to the channelList
	 * 
	 * @param channelList
	 */
    public void processUpdate(ChannelList channelList) throws ProtocolException {
        Channel channel = channelList.getChannelById(getChannelId());
        if (channel == null) {
            throw new ProtocolException("ChannelNameChange for unknown channel with id " + getChannelId());
        }
        channel.setTopic(getNewName());
    }
}
