package com.l2jserver.gameserver.network.clientpackets;

import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.ActionFailed;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;
import com.l2jserver.gameserver.network.serverpackets.TradeDone;

/**
 * This class ...
 *
 * @version $Revision: 1.5.4.2 $ $Date: 2005/03/27 15:29:30 $
 */
public final class AnswerTradeRequest extends L2GameClientPacket {

    private static final String _C__40_ANSWERTRADEREQUEST = "[C] 40 AnswerTradeRequest";

    private int _response;

    @Override
    protected void readImpl() {
        _response = readD();
    }

    @Override
    protected void runImpl() {
        L2PcInstance player = getClient().getActiveChar();
        if (player == null) return;
        if (!player.getAccessLevel().allowTransaction()) {
            player.sendPacket(new SystemMessage(SystemMessageId.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT));
            sendPacket(ActionFailed.STATIC_PACKET);
            return;
        }
        L2PcInstance partner = player.getActiveRequester();
        if (partner == null) {
            player.sendPacket(new TradeDone(0));
            SystemMessage msg = new SystemMessage(SystemMessageId.TARGET_IS_NOT_FOUND_IN_THE_GAME);
            player.sendPacket(msg);
            player.setActiveRequester(null);
            msg = null;
            return;
        } else if (L2World.getInstance().getPlayer(partner.getObjectId()) == null) {
            player.sendPacket(new TradeDone(0));
            SystemMessage msg = new SystemMessage(SystemMessageId.TARGET_IS_NOT_FOUND_IN_THE_GAME);
            player.sendPacket(msg);
            player.setActiveRequester(null);
            msg = null;
            return;
        }
        if (_response == 1 && !partner.isRequestExpired()) player.startTrade(partner); else {
            SystemMessage msg = new SystemMessage(SystemMessageId.C1_DENIED_TRADE_REQUEST);
            msg.addString(player.getName());
            partner.sendPacket(msg);
            msg = null;
        }
        player.setActiveRequester(null);
        partner.onTransactionResponse();
    }

    @Override
    public String getType() {
        return _C__40_ANSWERTRADEREQUEST;
    }
}
