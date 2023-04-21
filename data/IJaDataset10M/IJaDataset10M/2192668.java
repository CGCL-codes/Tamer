package quest.morheim;

import gameserver.model.EmotionType;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;

/**
 * @author MrPoke remod By Nephis
 */
public class _2307IrresistibleSoup extends QuestHandler {

    private static final int questId = 2307;

    public _2307IrresistibleSoup() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(204378).addOnQuestStart(questId);
        qe.setNpcQuestData(204378).addOnTalkEvent(questId);
        qe.setNpcQuestData(204336).addOnTalkEvent(questId);
        qe.setNpcQuestData(700247).addOnTalkEvent(questId);
    }

    @Override
    public boolean onDialogEvent(final QuestCookie env) {
        final Player player = env.getPlayer();
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc) targetId = ((Npc) env.getVisibleObject()).getNpcId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (targetId == 204378) {
            if (qs == null || qs.getStatus() == QuestStatus.NONE) {
                if (env.getDialogId() == 26) return sendQuestDialog(env, 4762); else return defaultQuestStartDialog(env);
            } else if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
                return defaultQuestEndDialog(env);
            }
        } else if (targetId == 204336) {
            if (qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 1) {
                if (env.getDialogId() == 26) return sendQuestDialog(env, 1011); else if (env.getDialogId() == 10000) {
                    qs.setQuestVar(2);
                    qs.setStatus(QuestStatus.REWARD);
                    updateQuestStatus(env);
                    player.getInventory().removeFromBagByItemId(182204106, 1);
                    PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                    return true;
                } else if (env.getDialogId() == 1182) {
                    qs.setQuestVar(1);
                    qs.setStatus(QuestStatus.REWARD);
                    updateQuestStatus(env);
                    player.getInventory().removeFromBagByItemId(182204107, 1);
                    PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                    return true;
                } else if (env.getDialogId() == 1267) {
                    qs.setQuestVar(1);
                    qs.setStatus(QuestStatus.REWARD);
                    updateQuestStatus(env);
                    player.getInventory().removeFromBagByItemId(182204108, 1);
                    PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                    return true;
                } else return defaultQuestStartDialog(env);
            }
        } else if (qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 0) {
            switch(targetId) {
                case 700247:
                    {
                        if (qs.getQuestVarById(0) == 0 && env.getDialogId() == -1) {
                            final int targetObjectId = env.getVisibleObject().getObjectId();
                            PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 1));
                            PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0, targetObjectId), true);
                            ThreadPoolManager.getInstance().schedule(new Runnable() {

                                final QuestState qs = player.getQuestStateList().getQuestState(questId);

                                @Override
                                public void run() {
                                    if (player.getTarget() == null || player.getTarget().getObjectId() != targetObjectId) return;
                                    PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 0));
                                    PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_LOOT, 0, targetObjectId), true);
                                    qs.setQuestVar(1);
                                    updateQuestStatus(env);
                                }
                            }, 3000);
                        }
                    }
            }
        }
        return false;
    }
}
