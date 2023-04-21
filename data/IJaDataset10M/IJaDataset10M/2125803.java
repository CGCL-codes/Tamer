package handlers.voicedcommandhandlers;

import l2.universe.Config;
import l2.universe.ExternalConfig;
import l2.universe.gameserver.datatables.ItemTable;
import l2.universe.gameserver.handler.IVoicedCommandHandler;
import l2.universe.gameserver.instancemanager.CastleManager;
import l2.universe.gameserver.instancemanager.InstanceManager;
import l2.universe.gameserver.model.L2World;
import l2.universe.gameserver.model.actor.L2Character;
import l2.universe.gameserver.model.actor.instance.L2PcInstance;
import l2.universe.gameserver.model.entity.Instance;
import l2.universe.gameserver.model.entity.events.TvTEvent;
import l2.universe.gameserver.network.SystemMessageId;
import l2.universe.gameserver.network.serverpackets.SystemMessage;

public class cl implements IVoicedCommandHandler {

    private static final String[] VOICED_COMMANDS = { "cl", "tocl" };

    @Override
    public boolean useVoicedCommand(final String command, final L2PcInstance activeChar, final String target) {
        if (command.equalsIgnoreCase("cl")) {
            if (activeChar.getClan() == null) return false;
            L2PcInstance leader;
            leader = (L2PcInstance) L2World.getInstance().findObject(activeChar.getClan().getLeaderId());
            if (!CheckAllowAction(leader, activeChar)) return false;
            if (!CheckAllowAction(activeChar, activeChar)) return false;
            if (activeChar == leader) {
                activeChar.sendMessage("You cannot teleport to yourself.");
                return false;
            }
            if (activeChar.getInventory().getItemByItemId(ExternalConfig.ITEM_ID_CLAN_LIDER_TELEPORT) == null) {
                activeChar.sendMessage("You need one or more " + ItemTable.getInstance().getTemplate(ExternalConfig.ITEM_ID_CLAN_LIDER_TELEPORT).getName() + " to use the cl-teleport system.");
                return false;
            }
            activeChar.teleToLocation(leader.getX(), leader.getY(), leader.getZ());
            activeChar.sendMessage("You have been teleported to your leader!");
            activeChar.getInventory().destroyItemByItemId("RessSystem", ExternalConfig.ITEM_ID_CLAN_LIDER_TELEPORT, ExternalConfig.COUNT_ITEM_CLAN_LIDER_TELEPORT, activeChar, activeChar.getTarget());
            activeChar.sendMessage("Item has dissapeared! Thank you!");
        } else if (command.equalsIgnoreCase("tocl")) {
            if (!activeChar.isClanLeader()) return false;
            if (!CheckAllowAction(activeChar, activeChar)) return false;
            for (final L2PcInstance member : activeChar.getClan().getOnlineMembers(0)) {
                if (!CheckAllowAction(member, activeChar)) continue;
                if (!member.isClanLeader()) {
                    member.teleToLocation(activeChar.getX(), activeChar.getY(), activeChar.getZ());
                    member.sendMessage("You have been teleported to your leader!");
                    activeChar.getInventory().destroyItemByItemId("RessSystem", ExternalConfig.ITEM_ID_CLAN_LIDER_TELEPORT_TO, ExternalConfig.COUNT_ITEM_CLAN_LIDER_TELEPORT_TO, activeChar, activeChar.getTarget());
                    activeChar.sendMessage("Item has dissapeared! Thank you!");
                }
            }
        }
        return true;
    }

    public static boolean CheckAllowAction(final L2PcInstance player_check, final L2PcInstance player_message) {
        if (player_message == null) return false;
        if (player_check == null) {
            player_message.sendMessage("Your partner is not online.");
            return false;
        }
        if (player_check.isInOlympiadMode()) {
            player_message.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.THIS_ITEM_IS_NOT_AVAILABLE_FOR_THE_OLYMPIAD_EVENT));
            return false;
        }
        if (!TvTEvent.onEscapeUse(player_check.getObjectId())) {
            player_message.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOUR_TARGET_IS_IN_AN_AREA_WHICH_BLOCKS_SUMMONING));
            return false;
        }
        if (player_check.isInsideZone(L2Character.ZONE_NOSUMMONFRIEND) || player_check.isFlyingMounted()) {
            player_message.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOUR_TARGET_IS_IN_AN_AREA_WHICH_BLOCKS_SUMMONING));
            return false;
        }
        if (player_check.isAlikeDead()) {
            SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.C1_IS_DEAD_AT_THE_MOMENT_AND_CANNOT_BE_SUMMONED);
            sm.addPcName(player_check);
            player_message.sendPacket(sm);
            sm = null;
            return false;
        }
        if (player_check.isInStoreMode()) {
            SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.C1_CURRENTLY_TRADING_OR_OPERATING_PRIVATE_STORE_AND_CANNOT_BE_SUMMONED);
            sm.addPcName(player_check);
            player_message.sendPacket(sm);
            sm = null;
            return false;
        }
        if (player_check.isRooted() || player_check.isInCombat()) {
            SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.C1_IS_ENGAGED_IN_COMBAT_AND_CANNOT_BE_SUMMONED);
            sm.addPcName(player_check);
            player_message.sendPacket(sm);
            sm = null;
            return false;
        }
        if (player_check.isFestivalParticipant() || player_check.isFlyingMounted()) {
            player_message.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOUR_TARGET_IS_IN_AN_AREA_WHICH_BLOCKS_SUMMONING));
            return false;
        }
        if (player_check.inObserverMode()) {
            player_message.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOUR_TARGET_IS_IN_AN_AREA_WHICH_BLOCKS_SUMMONING));
            return false;
        }
        if (!TvTEvent.onEscapeUse(player_check.getObjectId())) {
            player_message.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOUR_TARGET_IS_IN_AN_AREA_WHICH_BLOCKS_SUMMONING));
            return false;
        }
        if (player_check.isInsideZone(L2Character.ZONE_NOSUMMONFRIEND)) {
            SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.C1_IN_SUMMON_BLOCKING_AREA);
            sm.addString(player_check.getName());
            player_message.sendPacket(sm);
            sm = null;
            return false;
        }
        if (player_check.getInstanceId() > 0) {
            final Instance summonerInstance = InstanceManager.getInstance().getInstance(player_check.getInstanceId());
            if (!Config.ALLOW_SUMMON_TO_INSTANCE || !summonerInstance.isSummonAllowed()) {
                player_message.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_MAY_NOT_SUMMON_FROM_YOUR_CURRENT_LOCATION));
                return false;
            }
        }
        if (player_check.isInJail()) {
            player_message.sendMessage("The player " + player_check.getName() + " is in jail.");
            return false;
        }
        if (player_check.atEvent) {
            player_message.sendMessage("You are in an event.");
            return false;
        }
        if (player_check.isInDuel()) {
            player_message.sendMessage("You are in a duel!");
            return false;
        }
        if (player_check.getClan() != null && CastleManager.getInstance().getCastleByOwner(player_check.getClan()) != null && CastleManager.getInstance().getCastleByOwner(player_check.getClan()).getSiege().getIsInProgress()) {
            player_message.sendMessage("Your leader is in siege, you can't go to your leader.");
            return false;
        }
        if (player_check.isInParty() && player_check.getParty().isInDimensionalRift()) {
            player_message.sendMessage("You are in the dimensional rift.");
            return false;
        }
        return true;
    }

    @Override
    public String[] getVoicedCommandList() {
        return VOICED_COMMANDS;
    }
}
