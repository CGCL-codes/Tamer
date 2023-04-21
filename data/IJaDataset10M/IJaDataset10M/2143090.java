package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.services.ExchangeService;

/**
 * @author -Avol-
 * 
 */
public class CM_EXCHANGE_CANCEL extends AionClientPacket {

    public CM_EXCHANGE_CANCEL(int opcode) {
        super(opcode);
    }

    @Override
    protected void readImpl() {
    }

    @Override
    protected void runImpl() {
        final Player activePlayer = getConnection().getActivePlayer();
        ExchangeService.getInstance().cancelExchange(activePlayer);
    }
}
