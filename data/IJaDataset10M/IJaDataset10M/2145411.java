package org.openaion.gameserver.network.aion.clientpackets;

import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.dataholders.QuestsData;
import org.openaion.gameserver.model.TaskId;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.AionClientPacket;
import org.openaion.gameserver.network.aion.serverpackets.SM_QUEST_ACCEPTED;
import org.openaion.gameserver.quest.QuestEngine;
import org.openaion.gameserver.services.GuildService;

public class CM_DELETE_QUEST extends AionClientPacket {

    static QuestsData questsData = DataManager.QUEST_DATA;

    public int questId;

    public CM_DELETE_QUEST(int opcode) {
        super(opcode);
    }

    @Override
    protected void readImpl() {
        questId = readH();
    }

    @Override
    protected void runImpl() {
        Player player = getConnection().getActivePlayer();
        if (questsData.getQuestById(questId).isTimer()) {
            player.getController().cancelTask(TaskId.QUEST_TIMER);
            sendPacket(new SM_QUEST_ACCEPTED(4, questId, 0));
        }
        if (!QuestEngine.getInstance().deleteQuest(player, questId)) return;
        sendPacket(new SM_QUEST_ACCEPTED(questId));
        GuildService.getInstance().deleteDaily(player, questId);
        player.getController().updateNearbyQuests();
    }
}
