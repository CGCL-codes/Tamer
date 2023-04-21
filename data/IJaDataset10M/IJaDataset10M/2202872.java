package com.l2jserver.gameserver.model.actor.instance;

import java.util.StringTokenizer;
import com.l2jserver.Config;
import com.l2jserver.gameserver.TradeController;
import com.l2jserver.gameserver.ai.CtrlIntention;
import com.l2jserver.gameserver.ai.L2CharacterAI;
import com.l2jserver.gameserver.model.L2Party;
import com.l2jserver.gameserver.model.L2Skill;
import com.l2jserver.gameserver.model.L2TradeList;
import com.l2jserver.gameserver.model.L2WorldRegion;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.network.serverpackets.ActionFailed;
import com.l2jserver.gameserver.network.serverpackets.ExBuySellListPacket;
import com.l2jserver.gameserver.network.serverpackets.MyTargetSelected;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;
import com.l2jserver.gameserver.network.serverpackets.SellList;
import com.l2jserver.gameserver.network.serverpackets.ValidateLocation;
import com.l2jserver.gameserver.templates.chars.L2NpcTemplate;

/**
 * @author Kerberos
 */
public class L2MerchantSummonInstance extends L2SummonInstance {

    public L2MerchantSummonInstance(int objectId, L2NpcTemplate template, L2PcInstance owner, L2Skill skill) {
        super(objectId, template, owner, skill);
        setInstanceType(InstanceType.L2MerchantSummonInstance);
    }

    @Override
    public boolean hasAI() {
        return false;
    }

    @Override
    public L2CharacterAI getAI() {
        return null;
    }

    @Override
    public void deleteMe(L2PcInstance owner) {
    }

    @Override
    public void unSummon(L2PcInstance owner) {
        if (isVisible()) {
            stopAllEffects();
            L2WorldRegion oldRegion = getWorldRegion();
            decayMe();
            if (oldRegion != null) oldRegion.removeFromZones(this);
            getKnownList().removeAllKnownObjects();
            setTarget(null);
        }
    }

    @Override
    public void setFollowStatus(boolean state) {
    }

    @Override
    public boolean isAutoAttackable(L2Character attacker) {
        return false;
    }

    @Override
    public boolean isInvul() {
        return true;
    }

    @Override
    public L2Party getParty() {
        return null;
    }

    @Override
    public boolean isInParty() {
        return false;
    }

    @Override
    public void useMagic(L2Skill skill, boolean forceUse, boolean dontMove) {
    }

    @Override
    public void doCast(L2Skill skill) {
    }

    @Override
    public boolean isInCombat() {
        return false;
    }

    @Override
    public final void sendDamageMessage(L2Character target, int damage, boolean mcrit, boolean pcrit, boolean miss) {
    }

    @Override
    public void reduceCurrentHp(double i, L2Character attacker, boolean awake, boolean isDOT, L2Skill skill) {
    }

    @Override
    public void updateAndBroadcastStatus(int val) {
    }

    @Override
    public void onAction(L2PcInstance player, boolean interact) {
        if (player.isOutOfControl()) {
            player.sendPacket(ActionFailed.STATIC_PACKET);
            return;
        }
        if (this != player.getTarget()) {
            player.setTarget(this);
            final MyTargetSelected my = new MyTargetSelected(getObjectId(), 0);
            player.sendPacket(my);
            player.sendPacket(new ValidateLocation(this));
        } else if (interact) {
            if (!isInsideRadius(player, 150, false, false)) {
                player.getAI().setIntention(CtrlIntention.AI_INTENTION_INTERACT, this);
            } else showMessageWindow(player);
        }
        player.sendPacket(ActionFailed.STATIC_PACKET);
    }

    public void onBypassFeedback(L2PcInstance player, String command) {
        final StringTokenizer st = new StringTokenizer(command, " ");
        final String actualCommand = st.nextToken();
        if (actualCommand.equalsIgnoreCase("Buy")) {
            if (st.countTokens() < 1) return;
            final int val = Integer.parseInt(st.nextToken());
            showBuyWindow(player, val);
        } else if (actualCommand.equalsIgnoreCase("Sell")) {
            showSellWindow(player);
        }
    }

    protected final void showBuyWindow(L2PcInstance player, int val) {
        double taxRate = 50;
        player.tempInventoryDisable();
        if (Config.DEBUG) _log.fine("Showing buylist");
        L2TradeList list = TradeController.getInstance().getBuyList(val);
        if (list != null && list.getNpcId().equals(String.valueOf(getNpcId()))) {
            player.sendPacket(new ExBuySellListPacket(player, list, taxRate, false));
        } else {
            _log.warning("possible client hacker: " + player.getName() + " attempting to buy from GM shop! < Ban him!");
            _log.warning("buylist id:" + val);
        }
        player.sendPacket(ActionFailed.STATIC_PACKET);
    }

    protected final void showSellWindow(L2PcInstance player) {
        if (Config.DEBUG) _log.fine("Showing selllist");
        player.sendPacket(new SellList(player));
        if (Config.DEBUG) _log.fine("Showing sell window");
        player.sendPacket(ActionFailed.STATIC_PACKET);
    }

    private void showMessageWindow(L2PcInstance player) {
        player.sendPacket(ActionFailed.STATIC_PACKET);
        final String filename = "data/html/merchant/" + getNpcId() + ".htm";
        final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
        html.setFile(player.getHtmlPrefix(), filename);
        html.replace("%objectId%", String.valueOf(getObjectId()));
        player.sendPacket(html);
    }
}
