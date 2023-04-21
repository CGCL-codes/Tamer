package quest.sanctum;

import gameserver.model.PlayerClass;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.utils.PacketSendUtility;

public class _1989ASagesTeachings extends QuestHandler {

    private static final int questId = 1989;

    public _1989ASagesTeachings() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(203704).addOnQuestStart(questId);
        qe.setNpcQuestData(203705).addOnQuestStart(questId);
        qe.setNpcQuestData(203706).addOnQuestStart(questId);
        qe.setNpcQuestData(203707).addOnQuestStart(questId);
        qe.setNpcQuestData(203771).addOnQuestStart(questId);
        qe.setNpcQuestData(203771).addOnTalkEvent(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc) targetId = ((Npc) env.getVisibleObject()).getNpcId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 203771) {
                if (env.getDialogId() == 26) return sendQuestDialog(env, 1011); else return defaultQuestStartDialog(env);
            }
        }
        if (qs == null) return false;
        int var = qs.getQuestVarById(0);
        if (qs.getStatus() == QuestStatus.START) {
            PlayerClass playerClass = player.getCommonData().getPlayerClass();
            switch(targetId) {
                case 203704:
                    switch(env.getDialogId()) {
                        case 26:
                            if (playerClass == PlayerClass.GLADIATOR || playerClass == PlayerClass.TEMPLAR) return sendQuestDialog(env, 1352); else return sendQuestDialog(env, 1438);
                        case 10000:
                            qs.setQuestVarById(0, var + 1);
                            updateQuestStatus(env);
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                            return true;
                    }
                case 203705:
                    switch(env.getDialogId()) {
                        case 26:
                            if (playerClass == PlayerClass.ASSASSIN || playerClass == PlayerClass.RANGER) return sendQuestDialog(env, 1693); else return sendQuestDialog(env, 1779);
                        case 10000:
                            qs.setQuestVarById(0, var + 1);
                            updateQuestStatus(env);
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                            return true;
                    }
                case 203706:
                    switch(env.getDialogId()) {
                        case 26:
                            if (playerClass == PlayerClass.SORCERER || playerClass == PlayerClass.SPIRIT_MASTER) return sendQuestDialog(env, 2034); else return sendQuestDialog(env, 2120);
                        case 10000:
                            qs.setQuestVarById(0, var + 1);
                            updateQuestStatus(env);
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                            return true;
                    }
                case 203707:
                    switch(env.getDialogId()) {
                        case 26:
                            if (playerClass == PlayerClass.CLERIC || playerClass == PlayerClass.CHANTER) return sendQuestDialog(env, 2375); else return sendQuestDialog(env, 2461);
                        case 10000:
                            qs.setQuestVarById(0, var + 1);
                            updateQuestStatus(env);
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                            return true;
                    }
                case 203771:
                    switch(env.getDialogId()) {
                        case 26:
                            if (var == 1) return sendQuestDialog(env, 2716); else if (var == 2) return sendQuestDialog(env, 3057); else if (var == 3) {
                                if (player.getCommonData().getDp() < 4000) return sendQuestDialog(env, 3484); else return sendQuestDialog(env, 3398);
                            } else if (var == 4) {
                                if (player.getCommonData().getDp() < 4000) return sendQuestDialog(env, 3825); else return sendQuestDialog(env, 3739);
                            }
                        case 1009:
                            if (var == 3) {
                                PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 105));
                                player.getCommonData().setDp(0);
                                qs.setStatus(QuestStatus.REWARD);
                                updateQuestStatus(env);
                                return sendQuestDialog(env, 5);
                            } else if (var == 4) {
                                PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 105));
                                player.getCommonData().setDp(0);
                                qs.setStatus(QuestStatus.REWARD);
                                updateQuestStatus(env);
                                return sendQuestDialog(env, 5);
                            } else return this.defaultQuestEndDialog(env);
                        case 10001:
                            qs.setQuestVarById(0, var + 1);
                            updateQuestStatus(env);
                            return sendQuestDialog(env, 3057);
                        case 10003:
                            qs.setQuestVarById(0, 3);
                            updateQuestStatus(env);
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                            return true;
                        case 10004:
                            qs.setQuestVarById(0, 4);
                            updateQuestStatus(env);
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                            return true;
                    }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 203771) return defaultQuestEndDialog(env);
        }
        return false;
    }
}
