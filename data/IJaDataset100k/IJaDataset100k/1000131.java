package net.sf.ninjakore.packetdata;

public class MapServerLoginBuilder extends AbstractPacketBuilder {

    public MapServerLoginBuilder(ClientVersion version, int id) {
        super(version, id);
    }

    @Override
    protected void initializeTemplates() {
    }

    @Override
    protected String[] keys() {
        return null;
    }

    @Override
    public Class<? extends PacketData> packetClass() {
        return MapServerLogin.class;
    }
}
