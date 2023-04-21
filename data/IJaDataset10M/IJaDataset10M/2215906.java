package admincommands;

import nakayo.gameserver.configs.administration.AdminConfig;
import nakayo.gameserver.model.gameobjects.VisibleObject;
import nakayo.gameserver.model.gameobjects.player.Player;
import nakayo.gameserver.network.aion.serverpackets.SM_QUEST_ACCEPTED;
import nakayo.gameserver.questEngine.model.QuestCookie;
import nakayo.gameserver.questEngine.model.QuestState;
import nakayo.gameserver.questEngine.model.QuestStatus;
import nakayo.gameserver.services.QuestService;
import nakayo.gameserver.utils.PacketSendUtility;
import nakayo.gameserver.utils.chathandlers.AdminCommand;

/**
 * @author MrPoke
 */
public class QuestCommand extends AdminCommand {

    public QuestCommand() {
        super("quest");
    }

    @Override
    public void executeCommand(Player admin, String[] params) {
        if (admin.getAccessLevel() >= AdminConfig.COMMAND_QUESTCOMMAND) {
            if (params == null || params.length < 1) {
                PacketSendUtility.sendMessage(admin, "syntax //quest <start | set | vars>");
                return;
            }
            Player target = null;
            VisibleObject creature = admin.getTarget();
            if (admin.getTarget() instanceof Player) {
                target = (Player) creature;
            }
            if (target == null) {
                PacketSendUtility.sendMessage(admin, "Incorrect target!");
                return;
            }
            if (params[0].equals("start")) {
                if (params.length != 2) {
                    PacketSendUtility.sendMessage(admin, "syntax //quest start <quest id>");
                    return;
                }
                int id;
                try {
                    id = Integer.valueOf(params[1]);
                } catch (NumberFormatException e) {
                    PacketSendUtility.sendMessage(admin, "syntax //quest start <quest id>");
                    return;
                }
                QuestCookie env = new QuestCookie(null, target, id, 0);
                if (QuestService.startQuest(env, QuestStatus.START)) {
                    PacketSendUtility.sendMessage(admin, "Quest started.");
                } else {
                    PacketSendUtility.sendMessage(admin, "Quest not started.");
                }
            } else if (params[0].equals("set") && params.length == 4) {
                int questId, var;
                QuestStatus questStatus;
                try {
                    questId = Integer.valueOf(params[1]);
                    questStatus = QuestStatus.valueOf(params[2]);
                    var = Integer.valueOf(params[3]);
                } catch (NumberFormatException e) {
                    PacketSendUtility.sendMessage(admin, "syntax //quest set <quest id status var>");
                    return;
                } catch (IllegalArgumentException e) {
                    PacketSendUtility.sendMessage(admin, "syntax //quest set <quest id status var>");
                    return;
                }
                QuestState qs = target.getQuestStateList().getQuestState(questId);
                if (qs == null) {
                    PacketSendUtility.sendMessage(admin, "syntax //quest set <quest id status var>");
                    return;
                }
                qs.setStatus(questStatus);
                qs.setQuestVar(var);
                PacketSendUtility.sendPacket(target, new SM_QUEST_ACCEPTED(questId, qs.getStatus(), qs.getQuestVars().getQuestVars()));
            } else if (params[0].equals("set") && params.length == 5) {
                int questId, varId, var;
                QuestStatus questStatus;
                try {
                    questId = Integer.valueOf(params[1]);
                    questStatus = QuestStatus.valueOf(params[2]);
                    varId = Integer.valueOf(params[3]);
                    var = Integer.valueOf(params[4]);
                } catch (NumberFormatException e) {
                    PacketSendUtility.sendMessage(admin, "syntax //quest set <questId status varId var>");
                    return;
                }
                QuestState qs = target.getQuestStateList().getQuestState(questId);
                if (qs == null) {
                    PacketSendUtility.sendMessage(admin, "syntax //quest set <questId status varId var>");
                    return;
                }
                qs.setStatus(questStatus);
                qs.setQuestVarById(varId, var);
                PacketSendUtility.sendPacket(target, new SM_QUEST_ACCEPTED(questId, qs.getStatus(), qs.getQuestVars().getQuestVars()));
            } else if (params[0].equals("vars") && params.length == 2) {
                int questId, varId, var;
                QuestStatus questStatus;
                try {
                    questId = Integer.valueOf(params[1]);
                } catch (NumberFormatException e) {
                    PacketSendUtility.sendMessage(admin, "syntax //quest vars questId");
                    return;
                }
                QuestState qs = target.getQuestStateList().getQuestState(questId);
                if (qs == null) {
                    PacketSendUtility.sendMessage(admin, "syntax //quest vars questId");
                    return;
                }
                PacketSendUtility.sendMessage(admin, "vars: " + qs.getQuestVarById(0));
            } else PacketSendUtility.sendMessage(admin, "syntax //quest <start | set | vars>");
            return;
        } else if (admin.getAccessLevel() >= AdminConfig.COMMAND_QUESTCOMMANDPLAYERS) {
            if (params == null || params.length < 1) {
                PacketSendUtility.sendMessage(admin, "syntax //quest <restart>");
                return;
            }
            if (params[0].equals("restart")) {
                if (params.length != 2) {
                    PacketSendUtility.sendMessage(admin, "syntax //quest restart <quest id>");
                    return;
                }
                int id;
                try {
                    id = Integer.valueOf(params[1]);
                } catch (NumberFormatException e) {
                    PacketSendUtility.sendMessage(admin, "syntax //quest restart <quest id>");
                    return;
                }
                QuestState qs = admin.getQuestStateList().getQuestState(id);
                if (qs == null || id == 1006 || id == 2008) {
                    PacketSendUtility.sendMessage(admin, "Quest " + id + " can't be restarted");
                    return;
                }
                if (qs.getStatus() == QuestStatus.START || qs.getStatus() == QuestStatus.REWARD) {
                    if (qs.getQuestVarById(0) != 0) {
                        qs.setStatus(QuestStatus.START);
                        qs.setQuestVar(0);
                        PacketSendUtility.sendPacket(admin, new SM_QUEST_ACCEPTED(id, qs.getStatus(), qs.getQuestVars().getQuestVars()));
                        PacketSendUtility.sendMessage(admin, "Quest " + id + " restarted");
                    } else PacketSendUtility.sendMessage(admin, "Quest " + id + " can't be restarted");
                } else {
                    PacketSendUtility.sendMessage(admin, "Quest " + id + " can't be restarted");
                }
            } else PacketSendUtility.sendMessage(admin, "syntax //quest <restart>");
            return;
        } else {
            PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
            return;
        }
    }
}
