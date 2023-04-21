package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.services.LegionService;

/**
 * @author Simple
 * 
 */
public class CM_LEGION_UPLOAD_INFO extends AionClientPacket {

    /** Emblem related information **/
    private int totalSize;

    /**
	 * @param opcode
	 */
    public CM_LEGION_UPLOAD_INFO(int opcode) {
        super(opcode);
    }

    @Override
    protected void readImpl() {
        totalSize = readD();
        readC();
        readC();
        readC();
        readC();
    }

    @Override
    protected void runImpl() {
        LegionService.getInstance().uploadEmblemInfo(getConnection().getActivePlayer(), totalSize);
    }
}
