package teleports.ToiVortexRed;

import l2.universe.gameserver.model.actor.L2Npc;
import l2.universe.gameserver.model.actor.instance.L2PcInstance;
import l2.universe.gameserver.model.quest.Quest;
import l2.universe.gameserver.model.quest.QuestState;

public class ToiVortexRed extends Quest {

    private static final String qn = "ToiVortexRed";

    private static final int DIMENSION_VORTEX_1 = 30952;

    private static final int DIMENSION_VORTEX_2 = 30953;

    private static final int RED_DIMENSION_STONE = 4403;

    public ToiVortexRed(int questId, String name, String descr) {
        super(questId, name, descr);
        addStartNpc(DIMENSION_VORTEX_1);
        addStartNpc(DIMENSION_VORTEX_2);
        addTalkId(DIMENSION_VORTEX_1);
        addTalkId(DIMENSION_VORTEX_2);
    }

    @Override
    public String onTalk(L2Npc npc, L2PcInstance player) {
        String htmltext = "";
        QuestState st = player.getQuestState(getName());
        int npcId = npc.getNpcId();
        if (npcId == DIMENSION_VORTEX_1 || npcId == DIMENSION_VORTEX_2) {
            if (st.getQuestItemsCount(RED_DIMENSION_STONE) >= 1) {
                st.takeItems(RED_DIMENSION_STONE, 1);
                player.teleToLocation(118558, 16659, 5987);
            } else htmltext = "1.htm";
        }
        st.exitQuest(true);
        return htmltext;
    }

    public static void main(String[] args) {
        new ToiVortexRed(-1, qn, "teleports");
    }
}
