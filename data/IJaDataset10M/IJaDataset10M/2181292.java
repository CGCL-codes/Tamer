package com.l2jserver.gameserver.network.serverpackets;

import java.util.Map;
import java.util.Map.Entry;
import com.l2jserver.gameserver.model.L2PremiumItem;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

/**
 * Structure:  "QdQdS"
 * 
 * @author Gnacik
 */
public class ExGetPremiumItemList extends L2GameServerPacket {

    private static final String _S__FE_86_EXGETPREMIUMITEMLIST = "[S] FE:86 ExGetPremiumItemList";

    private L2PcInstance _activeChar;

    private Map<Integer, L2PremiumItem> _map;

    public ExGetPremiumItemList(L2PcInstance activeChar) {
        _activeChar = activeChar;
        _map = _activeChar.getPremiumItemList();
    }

    @Override
    protected void writeImpl() {
        writeC(0xFE);
        writeH(0x86);
        if (!_map.isEmpty()) {
            writeD(_map.size());
            for (Entry<Integer, L2PremiumItem> entry : _map.entrySet()) {
                L2PremiumItem item = entry.getValue();
                writeD(entry.getKey());
                writeD(_activeChar.getObjectId());
                writeD(item.getItemId());
                writeQ(item.getCount());
                writeD(0);
                writeS(item.getSender());
            }
        } else writeD(0);
    }

    @Override
    public String getType() {
        return _S__FE_86_EXGETPREMIUMITEMLIST;
    }
}
