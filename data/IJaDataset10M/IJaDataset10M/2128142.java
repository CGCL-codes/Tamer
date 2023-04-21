package gameserver.network.aion.serverpackets;

import gameserver.model.Race;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

public class SM_RIFT_ANNOUNCE extends AionServerPacket {

    private Race race;

    /**
	* Constructs new <tt>SM_RIFT_ANNOUNCE</tt> packet
	* 
	* @param player
	*/
    public SM_RIFT_ANNOUNCE(Race race) {
        this.race = race;
    }

    /**
	* {@inheritDoc}
	*/
    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeD(buf, 0);
        switch(race) {
            case ASMODIANS:
                writeD(buf, 1);
                writeD(buf, 0);
                break;
            case ELYOS:
                writeD(buf, 1);
                writeD(buf, 0);
                break;
        }
    }
}
