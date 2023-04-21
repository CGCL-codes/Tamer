package ai.individual;

import static net.sf.l2j.gameserver.ai.CtrlIntention.AI_INTENTION_FOLLOW;
import static net.sf.l2j.gameserver.ai.CtrlIntention.AI_INTENTION_IDLE;
import java.util.Collection;
import javolution.util.FastList;
import ai.group_template.L2AttackableAIScript;
import net.sf.l2j.gameserver.GeoData;
import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.datatables.SkillTable;
import net.sf.l2j.gameserver.instancemanager.GrandBossManager;
import net.sf.l2j.gameserver.model.L2Attackable;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Effect;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2Summon;
import net.sf.l2j.gameserver.model.actor.instance.L2GrandBossInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.QuestTimer;
import net.sf.l2j.gameserver.model.zone.type.L2BossZone;
import net.sf.l2j.gameserver.network.serverpackets.Earthquake;
import net.sf.l2j.gameserver.network.serverpackets.PlaySound;
import net.sf.l2j.gameserver.network.serverpackets.NpcSay;
import net.sf.l2j.gameserver.network.serverpackets.SocialAction;
import net.sf.l2j.gameserver.templates.StatsSet;
import net.sf.l2j.gameserver.util.Util;
import net.sf.l2j.util.Rnd;

/**
 * Baium AI
 *
 * Note1: if the server gets rebooted while players are still fighting Baium, there is no lock, but
 *   players also lose their ability to wake baium up.  However, should another person
 *   enter the room and wake him up, the players who had stayed inside may join the raid.
 *   This can be helpful for players who became victims of a reboot (they only need 1 new player to
 *   enter and wake up baium) and is not too exploitable since any player wishing to exploit it
 *   would have to suffer 5 days of being parked in an empty room.
 * Note2: Neither version of Baium should be a permanent spawn.  This script is fully capable of
 *   spawning the statue-version when the lock expires and switching it to the mob version promptly.
 *
 * Additional notes ( source http://aleenaresron.blogspot.com/2006_08_01_archive.html ):
 *   * Baium only first respawns five days after his last death. And from those five days he will
 *       respawn within 1-8 hours of his last death. So, you have to know his last time of death.
 *   * If by some freak chance you are the only one in Baium's chamber and NO ONE comes in
 *       [ha, ha] you or someone else will have to wake Baium. There is a good chance that Baium
 *       will automatically kill whoever wakes him. There are some people that have been able to
 *       wake him and not die, however if you've already gone through the trouble of getting the
 *       bloody fabric and camped him out and researched his spawn time, are you willing to take that
 *       chance that you'll wake him and not be able to finish your quest? Doubtful.
 *       [ this powerful attack vs the player who wakes him up is NOT yet implemented here]
 *   * once someone starts attacking Baium no one else can port into the chamber where he is.
 *       Unlike with the other raid bosses, you can just show up at any time as long as you are there
 *       when they die. Not true with Baium. Once he gets attacked, the port to Baium closes. byebye,
 *       see you in 5 days.  If nobody attacks baium for 30 minutes, he auto-despawns and unlocks the
 *       vortex
 *
 * @author Fulminus version 0.1
 */
public class Baium extends L2AttackableAIScript {

    private L2Character _target;

    private L2Skill _skill;

    private static final int STONE_BAIUM = 29025;

    private static final int ANGELIC_VORTEX = 31862;

    private static final int LIVE_BAIUM = 29020;

    private static final int ARCHANGEL = 29021;

    private static FastList<L2Attackable> _Minions = new FastList<L2Attackable>();

    private static final int ANGEL_LOCATION[][] = { { 113004, 16209, 10076, 60242 }, { 114053, 16642, 10076, 4411 }, { 114563, 17184, 10076, 49241 }, { 116356, 16402, 10076, 31109 }, { 115015, 16393, 10076, 32760 }, { 115481, 15335, 10076, 16241 }, { 114680, 15407, 10051, 32485 }, { 114886, 14437, 10076, 16868 }, { 115391, 17593, 10076, 55346 }, { 115245, 17558, 10076, 35536 } };

    private static final byte ASLEEP = 0;

    private static final byte AWAKE = 1;

    private static final byte DEAD = 2;

    private static long _LastAttackVsBaiumTime = 0;

    private static L2BossZone _Zone;

    public Baium(int questId, String name, String descr) {
        super(questId, name, descr);
        int[] mob = { LIVE_BAIUM };
        this.registerMobs(mob);
        addStartNpc(STONE_BAIUM);
        addStartNpc(ANGELIC_VORTEX);
        addTalkId(STONE_BAIUM);
        addTalkId(ANGELIC_VORTEX);
        _Zone = GrandBossManager.getInstance().getZone(113100, 14500, 10077);
        StatsSet info = GrandBossManager.getInstance().getStatsSet(LIVE_BAIUM);
        int status = GrandBossManager.getInstance().getBossStatus(LIVE_BAIUM);
        if (status == DEAD) {
            long temp = info.getLong("respawn_time") - System.currentTimeMillis();
            if (temp > 0) {
                startQuestTimer("baium_unlock", temp, null, null);
            } else {
                addSpawn(STONE_BAIUM, 116006, 17402, 10104, 41740, false, 0);
                GrandBossManager.getInstance().setBossStatus(LIVE_BAIUM, ASLEEP);
            }
        } else if (status == AWAKE) {
            int loc_x = info.getInteger("loc_x");
            int loc_y = info.getInteger("loc_y");
            int loc_z = info.getInteger("loc_z");
            int heading = info.getInteger("heading");
            final int hp = info.getInteger("currentHP");
            final int mp = info.getInteger("currentMP");
            L2GrandBossInstance baium = (L2GrandBossInstance) addSpawn(LIVE_BAIUM, loc_x, loc_y, loc_z, heading, false, 0);
            GrandBossManager.getInstance().addBoss(baium);
            final L2NpcInstance _baium = baium;
            ThreadPoolManager.getInstance().scheduleGeneral(new Runnable() {

                public void run() {
                    try {
                        _baium.setCurrentHpMp(hp, mp);
                        _baium.setIsInvul(true);
                        _baium.setIsImmobilized(true);
                        _baium.setRunning();
                        _baium.broadcastPacket(new SocialAction(_baium.getObjectId(), 2));
                        startQuestTimer("baium_wakeup", 15000, _baium, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 100L);
        } else addSpawn(STONE_BAIUM, 116006, 17402, 10104, 41740, false, 0);
    }

    public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player) {
        if (event.equalsIgnoreCase("baium_unlock")) {
            GrandBossManager.getInstance().setBossStatus(LIVE_BAIUM, ASLEEP);
            addSpawn(STONE_BAIUM, 116006, 17402, 10104, 41740, false, 0);
        } else if (event.equalsIgnoreCase("skill_range") && npc != null) {
            callSkillAI(npc);
        } else if (event.equalsIgnoreCase("clean_player")) {
            _target = getRandomTarget(npc);
        } else if (event.equalsIgnoreCase("baium_wakeup") && npc != null) {
            if (npc.getNpcId() == LIVE_BAIUM) {
                npc.broadcastPacket(new NpcSay(npc.getObjectId(), 1, npc.getNpcId(), "Don't obstruct my sleep! Die!"));
                npc.broadcastPacket(new SocialAction(npc.getObjectId(), 1));
                npc.broadcastPacket(new Earthquake(npc.getX(), npc.getY(), npc.getZ(), 40, 5));
                _LastAttackVsBaiumTime = System.currentTimeMillis();
                startQuestTimer("baium_despawn", 60000, npc, null, true);
                startQuestTimer("skill_range", 500, npc, null, true);
                final L2NpcInstance baium = npc;
                ThreadPoolManager.getInstance().scheduleGeneral(new Runnable() {

                    public void run() {
                        try {
                            baium.setIsInvul(false);
                            baium.setIsImmobilized(false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 11100L);
                for (int i = 0; i < 5; i++) _Minions.add((L2Attackable) addSpawn(ARCHANGEL, ANGEL_LOCATION[i][0], ANGEL_LOCATION[i][1], ANGEL_LOCATION[i][2], ANGEL_LOCATION[i][3], false, 0));
            }
        } else if (event.equalsIgnoreCase("baium_despawn") && npc != null) {
            if (npc.getNpcId() == LIVE_BAIUM) {
                if (_Zone == null) _Zone = GrandBossManager.getInstance().getZone(113100, 14500, 10077);
                if (_LastAttackVsBaiumTime + 1800000 < System.currentTimeMillis()) {
                    npc.deleteMe();
                    addSpawn(STONE_BAIUM, 116006, 17402, 10104, 41740, false, 0);
                    GrandBossManager.getInstance().setBossStatus(LIVE_BAIUM, ASLEEP);
                    _Zone.oustAllPlayers();
                    cancelQuestTimer("baium_despawn", npc, null);
                    for (L2Attackable minion : _Minions) if (minion != null) minion.deleteMe();
                    _Minions.clear();
                } else if (!_Zone.isInsideZone(npc)) npc.teleToLocation(115213, 16623, 10080);
            }
        }
        return super.onAdvEvent(event, npc, player);
    }

    public String onTalk(L2NpcInstance npc, L2PcInstance player) {
        int npcId = npc.getNpcId();
        String htmltext = "";
        if (_Zone == null) _Zone = GrandBossManager.getInstance().getZone(113100, 14500, 10077);
        if (_Zone == null) return "<html><body>Angelic Vortex:<br>You may not enter while admin disabled this zone</body></html>";
        if (npcId == STONE_BAIUM && GrandBossManager.getInstance().getBossStatus(LIVE_BAIUM) == ASLEEP) {
            if (_Zone.isPlayerAllowed(player)) {
                GrandBossManager.getInstance().setBossStatus(LIVE_BAIUM, AWAKE);
                npc.deleteMe();
                L2GrandBossInstance baium = (L2GrandBossInstance) addSpawn(LIVE_BAIUM, npc);
                GrandBossManager.getInstance().addBoss(baium);
                player.reduceCurrentHp(player.getCurrentHp(), player);
                final L2NpcInstance _baium = baium;
                ThreadPoolManager.getInstance().scheduleGeneral(new Runnable() {

                    public void run() {
                        try {
                            _baium.setIsInvul(true);
                            _baium.setRunning();
                            _baium.broadcastPacket(new SocialAction(_baium.getObjectId(), 2));
                            startQuestTimer("baium_wakeup", 15000, _baium, null);
                        } catch (Throwable e) {
                        }
                    }
                }, 100L);
            } else htmltext = "Conditions are not right to wake up Baium";
        } else if (npcId == ANGELIC_VORTEX) {
            if (GrandBossManager.getInstance().getBossStatus(LIVE_BAIUM) == ASLEEP) {
                if (player.isFlying()) {
                    htmltext = "<html><body>Angelic Vortex:<br>You may not enter while flying a wyvern</body></html>";
                } else if (player.getQuestState("baium").getQuestItemsCount(4295) > 0) {
                    player.getQuestState("baium").takeItems(4295, 1);
                    _Zone.allowPlayerEntry(player, 30);
                    player.teleToLocation(113100, 14500, 10077);
                } else htmltext = "<html><body>Angelic Vortex:<br>You do not have enough items</body></html>";
            } else htmltext = "<html><body>Angelic Vortex:<br>You may not enter at this time</body></html>";
        }
        return htmltext;
    }

    public String onSpellFinished(L2NpcInstance npc, L2PcInstance player, L2Skill skill) {
        if (npc.isInvul()) {
            npc.getAI().setIntention(AI_INTENTION_IDLE);
            return null;
        } else if (npc.getNpcId() == LIVE_BAIUM && !npc.isInvul()) {
            callSkillAI(npc);
        }
        return super.onSpellFinished(npc, player, skill);
    }

    public String onAttack(L2NpcInstance npc, L2PcInstance attacker, int damage, boolean isPet) {
        if (!_Zone.isInsideZone(attacker)) {
            attacker.reduceCurrentHp(attacker.getCurrentHp(), attacker);
            return null;
        }
        if (npc.isInvul()) {
            npc.getAI().setIntention(AI_INTENTION_IDLE);
            return null;
        } else if (npc.getNpcId() == LIVE_BAIUM && !npc.isInvul()) {
            if (attacker.getMountType() == 1) {
                int sk_4258 = 0;
                L2Effect[] effects = attacker.getAllEffects();
                if (effects.length != 0 || effects != null) {
                    for (L2Effect e : effects) {
                        if (e.getSkill().getId() == 4258) sk_4258 = 1;
                    }
                }
                if (sk_4258 == 0) {
                    npc.setTarget(attacker);
                    npc.doCast(SkillTable.getInstance().getInfo(4258, 1));
                }
            }
            _LastAttackVsBaiumTime = System.currentTimeMillis();
            callSkillAI(npc);
        }
        return super.onAttack(npc, attacker, damage, isPet);
    }

    public String onKill(L2NpcInstance npc, L2PcInstance killer, boolean isPet) {
        cancelQuestTimer("baium_despawn", npc, null);
        npc.broadcastPacket(new PlaySound(1, "BS01_D", 1, npc.getObjectId(), npc.getX(), npc.getY(), npc.getZ()));
        addSpawn(29055, 115203, 16620, 10078, 0, false, 900000);
        long respawnTime = (121 + Rnd.get(8)) * 3600000;
        GrandBossManager.getInstance().setBossStatus(LIVE_BAIUM, DEAD);
        startQuestTimer("baium_unlock", respawnTime, null, null);
        StatsSet info = GrandBossManager.getInstance().getStatsSet(LIVE_BAIUM);
        info.set("respawn_time", System.currentTimeMillis() + respawnTime);
        GrandBossManager.getInstance().setStatsSet(LIVE_BAIUM, info);
        if (getQuestTimer("skill_range", npc, null) != null) getQuestTimer("skill_range", npc, null).cancel();
        for (L2Attackable minion : _Minions) if (minion != null) minion.deleteMe();
        _Minions.clear();
        return super.onKill(npc, killer, isPet);
    }

    public L2Character getRandomTarget(L2NpcInstance npc) {
        FastList<L2Character> result = new FastList<L2Character>();
        Collection<L2Object> objs = npc.getKnownList().getKnownObjects().values();
        {
            for (L2Object obj : objs) {
                if (obj instanceof L2Character) {
                    if (((L2Character) obj).getZ() < npc.getZ() - 100 && ((L2Character) obj).getZ() > npc.getZ() + 100 || !GeoData.getInstance().canSeeTarget(obj, npc)) continue;
                }
                if (obj instanceof L2PcInstance) {
                    if (Util.checkIfInRange(9000, npc, obj, true) && !((L2Character) obj).isDead()) result.add((L2PcInstance) obj);
                }
                if (obj instanceof L2Summon) {
                    if (Util.checkIfInRange(9000, npc, obj, true) && !((L2Character) obj).isDead()) result.add((L2Summon) obj);
                }
            }
        }
        if (!result.isEmpty() && result.size() != 0) {
            Object[] characters = result.toArray();
            QuestTimer timer = getQuestTimer("clean_player", npc, null);
            if (timer != null) timer.cancel();
            startQuestTimer("clean_player", 20000, npc, null);
            return (L2Character) characters[Rnd.get(characters.length)];
        }
        return null;
    }

    public synchronized void callSkillAI(L2NpcInstance npc) {
        if (npc.isInvul() || npc.isCastingNow()) {
            return;
        }
        if (_target == null || _target.isDead() || !_Zone.isInsideZone(_target)) {
            _target = getRandomTarget(npc);
            _skill = getRandomSkill(npc);
        }
        L2Character target = _target;
        L2Skill skill = _skill;
        if (target == null || target.isDead() || !_Zone.isInsideZone(target)) {
            return;
        }
        if (Util.checkIfInRange(skill.getCastRange(), npc, target, true)) {
            npc.getAI().setIntention(AI_INTENTION_IDLE);
            npc.setTarget(target);
            _target = null;
            npc.doCast(skill);
        } else {
            npc.getAI().setIntention(AI_INTENTION_FOLLOW, target, null);
        }
    }

    public L2Skill getRandomSkill(L2NpcInstance npc) {
        L2Skill skill;
        if (npc.getCurrentHp() > npc.getMaxHp() * 3 / 4) {
            if (Rnd.get(100) < 10) {
                skill = SkillTable.getInstance().getInfo(4128, 1);
            } else if (Rnd.get(100) < 10) {
                skill = SkillTable.getInstance().getInfo(4129, 1);
            } else {
                skill = SkillTable.getInstance().getInfo(4127, 1);
            }
        } else if (npc.getCurrentHp() > npc.getMaxHp() * 2 / 4) {
            if (Rnd.get(100) < 10) {
                skill = SkillTable.getInstance().getInfo(4131, 1);
            } else if (Rnd.get(100) < 10) {
                skill = SkillTable.getInstance().getInfo(4128, 1);
            } else if (Rnd.get(100) < 10) {
                skill = SkillTable.getInstance().getInfo(4129, 1);
            } else {
                skill = SkillTable.getInstance().getInfo(4127, 1);
            }
        } else if (npc.getCurrentHp() > npc.getMaxHp() * 1 / 4) {
            if (Rnd.get(100) < 10) {
                skill = SkillTable.getInstance().getInfo(4130, 1);
            } else if (Rnd.get(100) < 10) {
                skill = SkillTable.getInstance().getInfo(4131, 1);
            } else if (Rnd.get(100) < 10) {
                skill = SkillTable.getInstance().getInfo(4128, 1);
            } else if (Rnd.get(100) < 10) {
                skill = SkillTable.getInstance().getInfo(4129, 1);
            } else {
                skill = SkillTable.getInstance().getInfo(4127, 1);
            }
        } else if (Rnd.get(100) < 10) {
            skill = SkillTable.getInstance().getInfo(4130, 1);
        } else if (Rnd.get(100) < 10) {
            skill = SkillTable.getInstance().getInfo(4131, 1);
        } else if (Rnd.get(100) < 10) {
            skill = SkillTable.getInstance().getInfo(4128, 1);
        } else if (Rnd.get(100) < 10) {
            skill = SkillTable.getInstance().getInfo(4129, 1);
        } else {
            skill = SkillTable.getInstance().getInfo(4127, 1);
        }
        return skill;
    }

    public String onSkillSee(L2NpcInstance npc, L2PcInstance caster, L2Skill skill, L2Object[] targets, boolean isPet) {
        if (npc.isInvul()) {
            npc.getAI().setIntention(AI_INTENTION_IDLE);
            return null;
        }
        npc.setTarget(caster);
        return super.onSkillSee(npc, caster, skill, targets, isPet);
    }

    public static void main(String[] args) {
        new Baium(-1, "baium", "ai");
    }
}
