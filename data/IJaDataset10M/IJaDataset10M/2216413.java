package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.services.LegionService;

/**
 * @author Simple
 * 
 */
public class CM_LEGION_MODIFY_EMBLEM extends AionClientPacket {

    /** Emblem related information **/
    private int legionId;

    private int emblemId;

    private int red;

    private int green;

    private int blue;

    /**
	 * @param opcode
	 */
    public CM_LEGION_MODIFY_EMBLEM(int opcode) {
        super(opcode);
    }

    @Override
    protected void readImpl() {
        legionId = readD();
        emblemId = readH();
        readC();
        red = readC();
        green = readC();
        blue = readC();
    }

    @Override
    protected void runImpl() {
        Player activePlayer = getConnection().getActivePlayer();
        if (activePlayer.isLegionMember()) LegionService.getInstance().storeLegionEmblem(activePlayer, legionId, emblemId, red, green, blue);
    }
}
