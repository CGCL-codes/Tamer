package gameserver.network.aion.serverpackets;

import gameserver.model.gameobjects.VisibleObject;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

public class SM_GATHERABLE_INFO extends AionServerPacket {

    private VisibleObject visibleObject;

    public SM_GATHERABLE_INFO(VisibleObject visibleObject) {
        super();
        this.visibleObject = visibleObject;
    }

    @Override
    public void writeImpl(AionConnection con, ByteBuffer buf) {
        writeF(buf, visibleObject.getX());
        writeF(buf, visibleObject.getY());
        writeF(buf, visibleObject.getZ());
        writeD(buf, visibleObject.getObjectId());
        writeD(buf, visibleObject.getSpawn().getStaticid());
        writeD(buf, visibleObject.getObjectTemplate().getTemplateId());
        writeH(buf, 1);
        writeC(buf, 0);
        writeD(buf, visibleObject.getObjectTemplate().getNameId());
        writeH(buf, 0);
        writeH(buf, 0);
        writeH(buf, 0);
        writeC(buf, 100);
    }
}
