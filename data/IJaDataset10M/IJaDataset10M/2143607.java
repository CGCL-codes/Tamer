package com.l2jserver.gameserver.network.clientpackets;

import java.util.logging.Logger;
import com.l2jserver.Config;
import com.l2jserver.gameserver.model.BlockList;
import com.l2jserver.gameserver.model.L2Object;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.ActionFailed;
import com.l2jserver.gameserver.network.serverpackets.SendTradeRequest;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;
import com.l2jserver.gameserver.util.Util;

/**
 *
 * This class ...
 *
 * @version $Revision: 1.2.2.1.2.3 $ $Date: 2005/03/27 15:29:30 $
 */
public final class TradeRequest extends L2GameClientPacket {

    private static final String TRADEREQUEST__C__15 = "[C] 15 TradeRequest";

    private static Logger _log = Logger.getLogger(TradeRequest.class.getName());

    private int _objectId;

    @Override
    protected void readImpl() {
        _objectId = readD();
    }

    @Override
    protected void runImpl() {
        L2PcInstance player = getClient().getActiveChar();
        if (player == null) return;
        if (!player.getAccessLevel().allowTransaction()) {
            player.sendMessage("Transactions are disable for your Access Level");
            sendPacket(ActionFailed.STATIC_PACKET);
            return;
        }
        L2Object target = L2World.getInstance().findObject(_objectId);
        if (target == null || !player.getKnownList().knowsObject(target) || !(target instanceof L2PcInstance)) {
            player.sendPacket(new SystemMessage(SystemMessageId.INCORRECT_TARGET));
            return;
        }
        if (target.getObjectId() == player.getObjectId()) {
            player.sendPacket(new SystemMessage(SystemMessageId.TARGET_IS_INCORRECT));
            return;
        }
        L2PcInstance partner = (L2PcInstance) target;
        if (partner.getInstanceId() != player.getInstanceId() && player.getInstanceId() != -1) return;
        if (partner.isInOlympiadMode() || player.isInOlympiadMode()) {
            player.sendMessage("You or your target cant request trade in Olympiad mode");
            return;
        }
        if (!Config.ALT_GAME_KARMA_PLAYER_CAN_TRADE && (player.getKarma() > 0 || partner.getKarma() > 0)) {
            player.sendMessage("Chaotic players can't use Trade.");
            return;
        }
        if (Config.JAIL_DISABLE_TRANSACTION && (player.isInJail() || partner.isInJail())) {
            player.sendMessage("You cannot trade in Jail.");
            return;
        }
        if (player.getPrivateStoreType() != 0 || partner.getPrivateStoreType() != 0) {
            player.sendPacket(new SystemMessage(SystemMessageId.CANNOT_TRADE_DISCARD_DROP_ITEM_WHILE_IN_SHOPMODE));
            return;
        }
        if (player.isProcessingTransaction()) {
            if (Config.DEBUG) _log.fine("already trading with someone");
            player.sendPacket(new SystemMessage(SystemMessageId.ALREADY_TRADING));
            return;
        }
        if (partner.isProcessingRequest() || partner.isProcessingTransaction()) {
            if (Config.DEBUG) _log.info("transaction already in progress.");
            SystemMessage sm = new SystemMessage(SystemMessageId.C1_IS_BUSY_TRY_LATER);
            sm.addString(partner.getName());
            player.sendPacket(sm);
            return;
        }
        if (partner.getTradeRefusal()) {
            player.sendMessage("Target is in trade refusal mode");
            return;
        }
        if (BlockList.isBlocked(partner, player)) {
            SystemMessage sm = new SystemMessage(SystemMessageId.S1_HAS_ADDED_YOU_TO_IGNORE_LIST);
            sm.addCharName(partner);
            player.sendPacket(sm);
            return;
        }
        if (Util.calculateDistance(player, partner, true) > 150) {
            player.sendPacket(new SystemMessage(SystemMessageId.TARGET_TOO_FAR));
            return;
        }
        player.onTransactionRequest(partner);
        partner.sendPacket(new SendTradeRequest(player.getObjectId()));
        SystemMessage sm = new SystemMessage(SystemMessageId.REQUEST_C1_FOR_TRADE);
        sm.addString(partner.getName());
        player.sendPacket(sm);
    }

    @Override
    public String getType() {
        return TRADEREQUEST__C__15;
    }
}
