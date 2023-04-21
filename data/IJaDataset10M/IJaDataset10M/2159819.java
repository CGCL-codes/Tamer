package com.l2jserver.gameserver.network.serverpackets;

import javolution.util.FastList;
import com.l2jserver.gameserver.instancemanager.FortSiegeManager;
import com.l2jserver.gameserver.instancemanager.FortSiegeManager.SiegeSpawn;
import com.l2jserver.gameserver.model.L2Spawn;
import com.l2jserver.gameserver.model.entity.Fort;

/**
 *
 * @author  KenM
 */
public class ExShowFortressSiegeInfo extends L2GameServerPacket {

    private int _fortId;

    private int _size;

    private Fort _fort;

    private int _csize;

    private int _csize2;

    /**
	 * @param fortId
	 */
    public ExShowFortressSiegeInfo(Fort fort) {
        _fort = fort;
        _fortId = fort.getFortId();
        _size = fort.getFortSize();
        FastList<SiegeSpawn> commanders = FortSiegeManager.getInstance().getCommanderSpawnList(_fortId);
        if (commanders != null) _csize = commanders.size();
        FastList<L2Spawn> list = _fort.getSiege().getCommanders().get(_fortId);
        if (list != null) _csize2 = list.size();
    }

    /**
	 * @see com.l2jserver.gameserver.network.serverpackets.L2GameServerPacket#getType()
	 */
    @Override
    public String getType() {
        return "[S] FE:17 ExShowFortressSiegeInfo";
    }

    /**
	 * @see com.l2jserver.gameserver.network.serverpackets.L2GameServerPacket#writeImpl()
	 */
    @Override
    protected void writeImpl() {
        writeC(0xfe);
        writeH(0x17);
        writeD(_fortId);
        writeD(_size);
        if (_csize > 0) {
            switch(_csize) {
                case 3:
                    switch(_csize2) {
                        case 0:
                            writeD(0x03);
                            break;
                        case 1:
                            writeD(0x02);
                            break;
                        case 2:
                            writeD(0x01);
                            break;
                        case 3:
                            writeD(0x00);
                            break;
                    }
                    break;
                case 4:
                    switch(_csize2) {
                        case 0:
                            writeD(0x05);
                            break;
                        case 1:
                            writeD(0x04);
                            break;
                        case 2:
                            writeD(0x03);
                            break;
                        case 3:
                            writeD(0x02);
                            break;
                        case 4:
                            writeD(0x01);
                            break;
                    }
                    break;
            }
        } else {
            for (int i = 0; i < _size; i++) writeD(0x00);
        }
    }
}
