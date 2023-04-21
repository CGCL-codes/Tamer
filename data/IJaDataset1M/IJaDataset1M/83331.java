package ai.individual.raidboss;

import l2.universe.gameserver.model.actor.instance.L2NpcInstance;
import l2.universe.gameserver.model.actor.instance.L2PcInstance;
import l2.universe.scripts.ai.L2AttackableAIScript;

public class Hallate extends L2AttackableAIScript {

    private static final int HALLATE = 25220;

    private static final int z1 = -2150;

    private static final int z2 = -1650;

    public Hallate(int questId, String name, String descr) {
        super(questId, name, descr);
        int[] mobs = { HALLATE };
        registerMobs(mobs);
    }

    public String onAttack(L2NpcInstance npc, L2PcInstance attacker, int damage, boolean isPet) {
        int npcId = npc.getNpcId();
        if (npcId == HALLATE) {
            int z = npc.getZ();
            if (z > z2 || z < z1) {
                npc.teleToLocation(113548, 17061, -2125);
                npc.getStatus().setCurrentHp(npc.getMaxHp());
            }
        }
        return super.onAttack(npc, attacker, damage, isPet);
    }

    public static void main(String[] args) {
        new Hallate(-1, "Hallate", "ai");
    }
}
