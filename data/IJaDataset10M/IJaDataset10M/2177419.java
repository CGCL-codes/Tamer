package ai.individual.raidboss;

import l2.universe.gameserver.datatables.SkillTable;
import l2.universe.gameserver.model.actor.L2Npc;
import l2.universe.gameserver.model.actor.instance.L2PcInstance;
import l2.universe.scripts.ai.L2AttackableAIScript;

/**
 * @author RosT, Synerge, ButterCup
 */
public class DemonPrince extends L2AttackableAIScript {

    private static final int PRINCE = 25540;

    private static final int PRINCE_MIN = 25541;

    public int princestatus;

    public DemonPrince(int id, String name, String descr) {
        super(id, name, descr);
        addSpawnId(PRINCE);
        addSpawnId(PRINCE_MIN);
        addAttackId(PRINCE);
    }

    @Override
    public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
        if (npc == null) return null;
        if (event.equalsIgnoreCase("time_to_suicide")) {
            npc.doCast(SkillTable.getInstance().getInfo(4529, 1));
            startQuestTimer("suicide", 1700, npc, null);
        } else if (event.equalsIgnoreCase("suicide")) npc.doDie(npc);
        return null;
    }

    @Override
    public String onSpawn(L2Npc npc) {
        switch(npc.getNpcId()) {
            case PRINCE_MIN:
                startQuestTimer("time_to_suicide", 20000, npc, null);
                break;
            case PRINCE:
                princestatus = 0;
                break;
        }
        return super.onSpawn(npc);
    }

    @Override
    public String onAttack(L2Npc npc, L2PcInstance attacker, int damage, boolean isPet) {
        if (npc.getNpcId() == PRINCE) {
            final int maxHp = npc.getMaxHp();
            final double nowHp = npc.getStatus().getCurrentHp();
            switch(princestatus) {
                case 0:
                    if (nowHp < maxHp * 0.55) {
                        princestatus = 1;
                        npc.doCast(SkillTable.getInstance().getInfo(5044, 2));
                        for (int i = 0; i < 3; i++) {
                            final int radius = 300;
                            final int x = (int) (radius * Math.cos(i * 0.518));
                            final int y = (int) (radius * Math.sin(i * 0.518));
                            addSpawn(PRINCE_MIN, npc.getX() + x, npc.getY() + y, npc.getZ(), 0, false, 0, false, npc.getInstanceId());
                        }
                    }
                    break;
                case 1:
                    if (nowHp < maxHp * 0.35) {
                        princestatus = 2;
                        npc.doCast(SkillTable.getInstance().getInfo(5044, 2));
                        for (int i = 0; i < 4; i++) {
                            final int radius = 300;
                            final int x = (int) (radius * Math.cos(i * 0.718));
                            final int y = (int) (radius * Math.sin(i * 0.718));
                            addSpawn(PRINCE_MIN, npc.getX() + x, npc.getY() + y, npc.getZ(), 0, false, 0, false, npc.getInstanceId());
                        }
                    }
                    break;
                case 2:
                    if (nowHp < maxHp * 0.15) {
                        princestatus = 3;
                        npc.doCast(SkillTable.getInstance().getInfo(5044, 2));
                        for (int i = 0; i < 5; i++) {
                            final int radius = 300;
                            final int x = (int) (radius * Math.cos(i * 0.918));
                            final int y = (int) (radius * Math.sin(i * 0.918));
                            addSpawn(PRINCE_MIN, npc.getX() + x, npc.getY() + y, npc.getZ(), 0, false, 0, false, npc.getInstanceId());
                        }
                    }
                    break;
                case 3:
                    if (nowHp < maxHp * 0.05) {
                        princestatus = 4;
                        npc.doCast(SkillTable.getInstance().getInfo(5044, 2));
                        for (int i = 0; i < 6; i++) {
                            final int radius = 300;
                            final int x = (int) (radius * Math.cos(i * 0.918));
                            final int y = (int) (radius * Math.sin(i * 0.918));
                            addSpawn(PRINCE_MIN, npc.getX() + x, npc.getY() + y, npc.getZ(), 0, false, 0, false, npc.getInstanceId());
                        }
                    }
                    break;
            }
        }
        return super.onAttack(npc, attacker, damage, isPet);
    }

    public static void main(String[] args) {
        new DemonPrince(-1, "DemonPrince", "ai");
    }
}
