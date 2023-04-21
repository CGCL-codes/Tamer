package com.aionemu.gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;
import java.util.Map;
import com.aionemu.gameserver.model.items.ItemCooldown;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author ATracer
 */
public class SM_ITEM_COOLDOWN extends AionServerPacket {

    private Map<Integer, ItemCooldown> cooldowns;

    public SM_ITEM_COOLDOWN(Map<Integer, ItemCooldown> cooldowns) {
        this.cooldowns = cooldowns;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeH(buf, cooldowns.size());
        long currentTime = System.currentTimeMillis();
        for (Map.Entry<Integer, ItemCooldown> entry : cooldowns.entrySet()) {
            writeH(buf, entry.getKey());
            int left = Math.round((entry.getValue().getReuseTime() - currentTime) / 1000);
            writeD(buf, left > 0 ? left : 0);
            writeD(buf, entry.getValue().getUseDelay());
        }
    }
}
