package org.openaion.gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;
import org.openaion.gameserver.model.siege.Influence;
import org.openaion.gameserver.network.aion.AionConnection;
import org.openaion.gameserver.network.aion.AionServerPacket;
import org.openaion.gameserver.services.SiegeService;

/**
 * @author Nemiroff
 * Total Influence Ratio
 */
public class SM_INFLUENCE_RATIO extends AionServerPacket {

    /**
	 * {@inheritDoc}
	 */
    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        Influence inf = Influence.getInstance();
        writeD(buf, SiegeService.getInstance().getSiegeTime());
        writeF(buf, inf.getElyos());
        writeF(buf, inf.getAsmos());
        writeF(buf, inf.getBalaur());
        writeH(buf, 3);
        for (int i = 0; i < 3; i++) {
            switch(i) {
                case 0:
                    writeD(buf, 210050000);
                    writeF(buf, inf.getElyosInggison());
                    writeF(buf, inf.getAsmosInggison());
                    writeF(buf, inf.getBalaurInggison());
                    break;
                case 1:
                    writeD(buf, 220070000);
                    writeF(buf, inf.getElyosGelkmaros());
                    writeF(buf, inf.getAsmosGelkmaros());
                    writeF(buf, inf.getBalaurGelkmaros());
                    break;
                case 2:
                    writeD(buf, 400010000);
                    writeF(buf, inf.getElyosAbyss());
                    writeF(buf, inf.getAsmosAbyss());
                    writeF(buf, inf.getBalaurAbyss());
                    break;
            }
        }
    }
}
