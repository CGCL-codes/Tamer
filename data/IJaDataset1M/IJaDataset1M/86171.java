package gameserver.network.aion.clientpackets;

import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionClientPacket;
import gameserver.services.ExchangeService;

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
