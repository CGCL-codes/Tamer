package quest.verteron;

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.utils.PacketSendUtility;

public class _1022KrallDesecration extends QuestHandler {

    private static final int questId = 1022;

    private static final int[] mob_ids = { 210178, 216892 };

    public _1022KrallDesecration() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(203178).addOnTalkEvent(questId);
        qe.addQuestLvlUp(questId);
        for (int mob_id : mob_ids) qe.setNpcQuestData(mob_id).addOnKillEvent(questId);
    }

    @Override
    public boolean onLvlUpEvent(QuestCookie env) {
        return defaultQuestOnLvlUpEvent(env, 1017);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null) return false;
        int var = qs.getQuestVarById(0);
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc) targetId = ((Npc) env.getVisibleObject()).getNpcId();
        if (qs.getStatus() == QuestStatus.START) {
            if (targetId == 203178) {
                switch(env.getDialogId()) {
                    case 26:
                        if (var == 0) return sendQuestDialog(env, 1011);
                    case 10000:
                    case 10001:
                        if (var == 0) {
                            qs.setQuestVarById(0, var + 1);
                            updateQuestStatus(env);
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                            return true;
                        }
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 203178) return defaultQuestEndDialog(env);
        }
        return false;
    }

    @Override
    public boolean onKillEvent(QuestCookie env) {
        int[] mobs = { 210178, 216892 };
        if (defaultQuestOnKillEvent(env, mobs, 1, 5) || defaultQuestOnKillEvent(env, mobs, 5, true)) return true; else return false;
    }
}
