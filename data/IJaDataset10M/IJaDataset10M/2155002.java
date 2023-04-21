package gameserver.network.aion.clientpackets;

import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.player.DeniedStatus;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.RequestResponseHandler;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.SystemMessageId;
import gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.services.ExchangeService;
import gameserver.utils.PacketSendUtility;
import gameserver.world.World;

public class CM_EXCHANGE_REQUEST extends AionClientPacket {

    public Integer targetObjectId;

    public CM_EXCHANGE_REQUEST(int opcode) {
        super(opcode);
    }

    @Override
    protected void readImpl() {
        targetObjectId = readD();
    }

    @Override
    protected void runImpl() {
        final Player activePlayer = getConnection().getActivePlayer();
        final Player targetPlayer = World.getInstance().findPlayer(targetObjectId);
        if (activePlayer != targetPlayer) {
            if (targetPlayer != null) {
                if (targetPlayer.getPlayerSettings().isInDeniedStatus(DeniedStatus.TRADE)) {
                    sendPacket(SM_SYSTEM_MESSAGE.STR_MSG_REJECTED_TRADE(targetPlayer.getName()));
                    return;
                }
                sendPacket(SM_SYSTEM_MESSAGE.REQUEST_TRADE(targetPlayer.getName()));
                ExchangeService.getInstance().cancelExchange(activePlayer);
                ExchangeService.getInstance().cancelExchange(targetPlayer);
                RequestResponseHandler responseHandler = new RequestResponseHandler(activePlayer) {

                    @Override
                    public void acceptRequest(Creature requester, Player responder) {
                        ExchangeService.getInstance().registerExchange(activePlayer, targetPlayer);
                    }

                    @Override
                    public void denyRequest(Creature requester, Player responder) {
                        PacketSendUtility.sendPacket(activePlayer, new SM_SYSTEM_MESSAGE(SystemMessageId.EXCHANGE_HE_REJECTED_EXCHANGE, targetPlayer.getName()));
                    }
                };
                boolean requested = targetPlayer.getResponseRequester().putRequest(SM_QUESTION_WINDOW.STR_EXCHANGE_DO_YOU_ACCEPT_EXCHANGE, responseHandler);
                if (requested) {
                    PacketSendUtility.sendPacket(targetPlayer, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_EXCHANGE_DO_YOU_ACCEPT_EXCHANGE, 0, activePlayer.getName()));
                }
            }
        } else {
        }
    }
}
