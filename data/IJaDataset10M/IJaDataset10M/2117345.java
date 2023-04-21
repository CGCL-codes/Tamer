package ai.zones.StakatoNest;

import java.util.Collection;
import java.util.concurrent.ScheduledFuture;
import l2.universe.scripts.ai.L2AttackableAIScript;
import l2.universe.gameserver.ThreadPoolManager;
import l2.universe.gameserver.datatables.SkillTable;
import l2.universe.gameserver.instancemanager.ZoneManager;
import l2.universe.gameserver.model.L2Skill;
import l2.universe.gameserver.model.actor.L2Character;
import l2.universe.gameserver.model.actor.L2Npc;
import l2.universe.gameserver.model.actor.instance.L2PcInstance;
import l2.universe.gameserver.model.actor.instance.L2RaidBossInstance;
import l2.universe.gameserver.model.zone.L2ZoneType;
import l2.universe.gameserver.network.serverpackets.ExSetCompassZoneCode;

public class QueenShyeed extends L2AttackableAIScript {

    private static final int QUEEN_SHYEED = 25514;

    private static final int STAKATO_NEST_ZONE = 12030;

    private long _QueenRespawn = 0;

    private long _QueenStatus = 0;

    protected ScheduledFuture<?> _zoneTask = null;

    public QueenShyeed(int questId, String name, String descr) {
        super(questId, name, descr);
        addKillId(QUEEN_SHYEED);
        addEnterZoneId(STAKATO_NEST_ZONE);
        addExitZoneId(STAKATO_NEST_ZONE);
        try {
            _QueenRespawn = Long.valueOf(loadGlobalQuestVar("_QueenRespawn"));
            _QueenStatus = Long.valueOf(loadGlobalQuestVar("_QueenStatus"));
        } catch (Exception e) {
        }
        saveGlobalQuestVar("_QueenRespawn", String.valueOf(_QueenRespawn));
        saveGlobalQuestVar("_QueenStatus", String.valueOf(_QueenStatus));
        if (_QueenStatus == 0 && !checkIfQueenSpawned()) {
            addSpawn(QUEEN_SHYEED, 79635, -55612, -5980, 0, false, 0);
            startQuestTimer("QueenDespawn", 10800000, null, null);
        } else startQuestTimer("QueenSpawn", _QueenRespawn - System.currentTimeMillis(), null, null);
        _zoneTask = ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(new stakatoBuffTask(), 30000, 30001);
    }

    @Override
    public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
        if (event.equalsIgnoreCase("QueenSpawn")) {
            if (!checkIfQueenSpawned()) {
                saveGlobalQuestVar("_QueenStatus", String.valueOf(0));
                _QueenStatus = 0;
                startQuestTimer("QueenDespawn", 10800000, null, null);
                addSpawn(QUEEN_SHYEED, 79635, -55612, -5980, 0, false, 0);
            }
        } else if (event.equalsIgnoreCase("QueenDespawn")) {
            final L2ZoneType zone = ZoneManager.getInstance().getZoneById(STAKATO_NEST_ZONE);
            final Collection<L2Character> charsZone = zone.getCharactersInside().values();
            for (L2Character c : charsZone) {
                if (!(c instanceof L2Npc)) continue;
                if (((L2Npc) c).getNpcId() == QUEEN_SHYEED) {
                    final long respawn = 86400000;
                    saveGlobalQuestVar("_QueenRespawn", String.valueOf(System.currentTimeMillis() + respawn));
                    saveGlobalQuestVar("_QueenStatus", String.valueOf(1));
                    _QueenStatus = 1;
                    startQuestTimer("QueenSpawn", respawn, null, null);
                    ((L2RaidBossInstance) c).deleteMe();
                }
            }
        } else if (event.equalsIgnoreCase("CompassON")) {
            player.sendPacket(new ExSetCompassZoneCode(ExSetCompassZoneCode.ALTEREDZONE));
        } else if (event.equalsIgnoreCase("CompassOFF")) {
            player.sendPacket(new ExSetCompassZoneCode(ExSetCompassZoneCode.GENERALZONE));
        }
        return null;
    }

    @Override
    public String onKill(L2Npc npc, L2PcInstance killer, boolean isPet) {
        final long respawn = 86400000;
        startQuestTimer("QueenSpawn", respawn, npc, null);
        saveGlobalQuestVar("_QueenRespawn", String.valueOf(System.currentTimeMillis() + respawn));
        saveGlobalQuestVar("_QueenStatus", String.valueOf(1));
        _QueenStatus = 1;
        return super.onKill(npc, killer, isPet);
    }

    @Override
    public String onEnterZone(L2Character character, L2ZoneType zone) {
        if (character instanceof L2PcInstance) {
            startQuestTimer("CompassON", 5000, null, (L2PcInstance) character);
            if (!checkIfPc(zone) && _zoneTask == null) _zoneTask = ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(new stakatoBuffTask(), 30000, 30001);
        }
        return super.onEnterZone(character, zone);
    }

    @Override
    public String onExitZone(L2Character character, L2ZoneType zone) {
        if (character instanceof L2PcInstance) {
            startQuestTimer("CompassOFF", 5000, null, (L2PcInstance) character);
            if (howManyPc(zone) == 1 && _zoneTask != null) {
                _zoneTask.cancel(true);
                _zoneTask = null;
            }
        }
        return super.onExitZone(character, zone);
    }

    private boolean checkIfQueenSpawned() {
        final L2ZoneType zone = ZoneManager.getInstance().getZoneById(STAKATO_NEST_ZONE);
        final Collection<L2Character> charsZone = zone.getCharactersInside().values();
        for (L2Character c : charsZone) {
            if (c instanceof L2Npc) {
                if (((L2Npc) c).getNpcId() == QUEEN_SHYEED) return true;
            }
        }
        return false;
    }

    private boolean checkIfPc(L2ZoneType zone) {
        final Collection<L2Character> inside = zone.getCharactersInside().values();
        for (L2Character c : inside) {
            if (c instanceof L2PcInstance) return true;
        }
        return false;
    }

    private int howManyPc(L2ZoneType zone) {
        int count = 0;
        final Collection<L2Character> inside = zone.getCharactersInside().values();
        for (L2Character c : inside) {
            if (c instanceof L2PcInstance) count++;
        }
        return count;
    }

    private class stakatoBuffTask implements Runnable {

        public void run() {
            final L2ZoneType zone = ZoneManager.getInstance().getZoneById(STAKATO_NEST_ZONE);
            if (howManyPc(zone) > 0) {
                int skillId = 0;
                final Collection<L2Character> inside = zone.getCharactersInside().values();
                for (L2Character c : inside) {
                    if (_QueenStatus == 0) {
                        if (c instanceof L2PcInstance) {
                            skillId = 6169;
                            handleNestBuff(c, skillId);
                        } else if (c instanceof L2Npc) {
                            skillId = 6170;
                            handleNestBuff(c, skillId);
                        }
                    } else {
                        if (c instanceof L2PcInstance) {
                            skillId = 6171;
                            handleNestBuff(c, skillId);
                        }
                    }
                }
            } else if (_zoneTask != null) {
                _zoneTask.cancel(true);
                _zoneTask = null;
            }
        }

        private void handleNestBuff(L2Character c, int skillId) {
            if (skillId == 6169) c.stopSkillEffects(6171); else c.stopSkillEffects(6169);
            if (c.getFirstEffect(skillId) == null) {
                L2Skill skill = SkillTable.getInstance().getInfo(skillId, 1);
                skill.getEffects(c, c);
            }
        }
    }

    public static void main(String[] args) {
        new QueenShyeed(-1, "QueenShyeed", "ai");
    }
}
