package jssh;

/**
 * When a channel is closed at one side of the connection, that
 * side sends this message. Upon receiving this message, the channel
 * should be closed.
 */
class MSG_CHANNEL_CLOSE extends Packet implements IInteractivePacket {

    /** Use this constructor when receiving a packet from the network.
     */
    MSG_CHANNEL_CLOSE(byte[] data_) {
        super(data_);
    }

    /** Use this constructor when creating a packet to send on the
     * network.
     */
    MSG_CHANNEL_CLOSE(int remote_channel_) {
        super();
        int block_length = 1 + 4;
        super._data = new byte[block_length];
        int offset = 0;
        super._data[offset++] = SSH_MSG_CHANNEL_CLOSE;
        SSHOutputStream.insertInteger(remote_channel_, offset, super._data);
    }

    int getRemoteChannelNumber() {
        int offset = 1;
        return SSHInputStream.getInteger(offset, super._data);
    }

    /** Implements the IInteractivePacket interface.
     */
    public void processPacket(IProtocolHandler handler_) {
        OpenChannel channel = handler_.findOpenChannel(getRemoteChannelNumber());
        if (channel != null) channel.enqueue(this);
    }
}
