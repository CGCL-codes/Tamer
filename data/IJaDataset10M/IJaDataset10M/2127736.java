package org.openaion.gameserver.network.aion.clientpackets;

import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.AionClientPacket;
import org.openaion.gameserver.services.ExchangeService;

/**
 * @author -Avol-
 * 
 */
public class CM_EXCHANGE_OK extends AionClientPacket {

    public CM_EXCHANGE_OK(int opcode) {
        super(opcode);
    }

    @Override
    protected void readImpl() {
    }

    @Override
    protected void runImpl() {
        final Player activePlayer = getConnection().getActivePlayer();
        ExchangeService.getInstance().confirmExchange(activePlayer);
    }
}
