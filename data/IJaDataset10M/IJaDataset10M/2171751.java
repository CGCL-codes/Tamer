package net.sf.l2j.gameserver.clientpackets;

import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.QuestState;

public class RequestTutorialPassCmdToServer extends L2GameClientPacket {

    String _bypass = null;

    protected void readImpl() {
        _bypass = readS();
    }

    protected void runImpl() {
        L2PcInstance player = getClient().getActiveChar();
        if (player == null) return;
        QuestState qs = player.getQuestState("255_Tutorial");
        if (qs != null) qs.getQuest().notifyEvent(_bypass, null, player);
    }

    public String getType() {
        return "[C] 86 RequestTutorialPassCmdToServer";
    }
}
