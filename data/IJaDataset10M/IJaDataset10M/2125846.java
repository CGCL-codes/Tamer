package handlers.voicedcommandhandlers;

import l2.universe.ExternalConfig;
import l2.universe.gameserver.datatables.SkillTable;
import l2.universe.gameserver.handler.IVoicedCommandHandler;
import l2.universe.gameserver.model.actor.instance.L2PcInstance;

/**
 * Voice commands .fbuff , .mbuff , .fullbuff, .cancel
 * AutoBuff team
 * 
 * @author Open-Team
 */
public class VoiceBuff implements IVoicedCommandHandler {

    private final String[] _voicedCommands = { "fbuff", "mbuff", "fullbuff", "cancel" };

    @Override
    public boolean useVoicedCommand(final String command, final L2PcInstance activeChar, final String target) {
        if (ExternalConfig.VOICED_BUFF_ONLY_PREMIUM && activeChar.getPremiumService() == 0) {
            activeChar.sendMessage(ExternalConfig.VOICED_BUFF_NOTPREMIUM_MESSAGE);
            return true;
        }
        if (command.equalsIgnoreCase("fbuff")) {
            if (activeChar.isInCombat() || activeChar.isInOlympiadMode() || activeChar.isInDuel() || activeChar.isInSiege()) activeChar.sendMessage("You can't use this command in PvP, Duel, Olympiad or Siege mods."); else {
                activeChar.sendMessage("You get a Fighter-buff complect.");
                SkillTable.getInstance().getInfo(275, 1).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(271, 1).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(274, 1).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(264, 1).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(304, 1).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(267, 1).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(1240, 3).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(1035, 4).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(1068, 3).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(1045, 6).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(1048, 6).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(1077, 3).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(1086, 2).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(1036, 2).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(1040, 3).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(1242, 3).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(1062, 2).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(1388, 3).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(1268, 4).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(1259, 4).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(1243, 6).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(1087, 3).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(1204, 2).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(349, 1).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(364, 1).getEffects(activeChar, activeChar);
                activeChar.broadcastUserInfo();
            }
        } else if (command.equalsIgnoreCase("mbuff")) {
            if (activeChar.isInCombat() || activeChar.isInOlympiadMode() || activeChar.isInDuel() || activeChar.isInSiege()) activeChar.sendMessage("You can't use this command in PvP, Duel, Olympiad or Siege mods."); else {
                activeChar.sendMessage("You get a Mage-buff complect.");
                SkillTable.getInstance().getInfo(276, 1).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(273, 1).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(264, 1).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(304, 1).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(267, 1).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(1085, 3).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(1062, 2).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(1078, 6).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(1059, 3).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(1303, 2).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(1204, 2).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(1036, 2).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(1040, 3).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(1389, 3).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(1045, 6).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(1048, 6).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(1397, 3).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(349, 1).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(363, 1).getEffects(activeChar, activeChar);
                activeChar.broadcastUserInfo();
            }
        } else if (command.equalsIgnoreCase("fullbuff")) {
            if (activeChar.isInCombat() || activeChar.isInOlympiadMode() || activeChar.isInDuel() || activeChar.isInSiege()) activeChar.sendMessage("You can't use this command in PvP, Duel, Olympiad or Siege mods."); else {
                activeChar.sendMessage("You get a Full-buff complect.");
                SkillTable.getInstance().getInfo(4342, 2).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(4343, 3).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(4344, 3).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(4345, 3).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(4346, 4).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(4347, 6).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(4348, 6).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(4349, 2).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(1087, 3).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(4151, 6).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(4352, 2).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(4353, 6).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(4354, 4).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(4355, 3).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(4356, 3).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(4357, 2).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(4358, 3).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(4359, 3).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(4360, 3).getEffects(activeChar, activeChar);
                SkillTable.getInstance().getInfo(1044, 3).getEffects(activeChar, activeChar);
                activeChar.broadcastUserInfo();
            }
        } else if (command.equalsIgnoreCase("petbuff")) {
            if (activeChar.isInCombat() || activeChar.isInOlympiadMode() || activeChar.isInDuel() || activeChar.isInSiege()) activeChar.sendMessage("You can't use this command in PvP, Duel, Olympiad or Siege mods."); else {
                activeChar.sendMessage("You get a Pet-buff complect.");
                SkillTable.getInstance().getInfo(4342, 2).getEffects(activeChar.getPet(), activeChar.getPet());
                SkillTable.getInstance().getInfo(4343, 3).getEffects(activeChar.getPet(), activeChar.getPet());
                SkillTable.getInstance().getInfo(4344, 3).getEffects(activeChar.getPet(), activeChar.getPet());
                SkillTable.getInstance().getInfo(4345, 3).getEffects(activeChar.getPet(), activeChar.getPet());
                SkillTable.getInstance().getInfo(4346, 4).getEffects(activeChar.getPet(), activeChar.getPet());
                SkillTable.getInstance().getInfo(4347, 6).getEffects(activeChar.getPet(), activeChar.getPet());
                SkillTable.getInstance().getInfo(4348, 6).getEffects(activeChar.getPet(), activeChar.getPet());
                SkillTable.getInstance().getInfo(4349, 2).getEffects(activeChar.getPet(), activeChar.getPet());
                SkillTable.getInstance().getInfo(1087, 3).getEffects(activeChar.getPet(), activeChar.getPet());
                SkillTable.getInstance().getInfo(4151, 6).getEffects(activeChar.getPet(), activeChar.getPet());
                SkillTable.getInstance().getInfo(4352, 2).getEffects(activeChar.getPet(), activeChar.getPet());
                SkillTable.getInstance().getInfo(4353, 6).getEffects(activeChar.getPet(), activeChar.getPet());
                SkillTable.getInstance().getInfo(4354, 4).getEffects(activeChar.getPet(), activeChar.getPet());
                SkillTable.getInstance().getInfo(4355, 3).getEffects(activeChar.getPet(), activeChar.getPet());
                SkillTable.getInstance().getInfo(4356, 3).getEffects(activeChar.getPet(), activeChar.getPet());
                SkillTable.getInstance().getInfo(4357, 2).getEffects(activeChar.getPet(), activeChar.getPet());
                SkillTable.getInstance().getInfo(4358, 3).getEffects(activeChar.getPet(), activeChar.getPet());
                SkillTable.getInstance().getInfo(4359, 3).getEffects(activeChar.getPet(), activeChar.getPet());
                SkillTable.getInstance().getInfo(4360, 3).getEffects(activeChar.getPet(), activeChar.getPet());
                SkillTable.getInstance().getInfo(1044, 3).getEffects(activeChar, activeChar);
                activeChar.broadcastUserInfo();
            }
        } else if (command.equalsIgnoreCase("cancel")) if (activeChar.isInCombat() || activeChar.isInOlympiadMode() || activeChar.isInDuel() || activeChar.isInSiege()) activeChar.sendMessage("You can't use this command in PvP, Duel, Olympiad or Siege mods."); else {
            activeChar.sendMessage("You have canceled all your buffs.");
            activeChar.stopAllEffectsExceptThoseThatLastThroughDeath();
            activeChar.broadcastUserInfo();
        }
        return true;
    }

    @Override
    public String[] getVoicedCommandList() {
        return _voicedCommands;
    }
}
