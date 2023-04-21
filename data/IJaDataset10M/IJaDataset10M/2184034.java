package instances.ChamberOfDelusionTower;

import java.util.Calendar;
import l2.universe.gameserver.ai.CtrlIntention;
import l2.universe.gameserver.instancemanager.InstanceManager;
import l2.universe.gameserver.instancemanager.InstanceManager.InstanceWorld;
import l2.universe.gameserver.model.L2Party;
import l2.universe.gameserver.model.L2World;
import l2.universe.gameserver.model.actor.L2Npc;
import l2.universe.gameserver.model.actor.L2Summon;
import l2.universe.gameserver.model.actor.instance.L2PcInstance;
import l2.universe.gameserver.model.quest.Quest;
import l2.universe.gameserver.model.quest.QuestState;
import l2.universe.gameserver.network.SystemMessageId;
import l2.universe.gameserver.network.serverpackets.SystemMessage;
import l2.universe.gameserver.util.Util;
import l2.universe.util.Rnd;

/**
 * @author d0s
 *
 */
public class ChamberOfDelusionTower extends Quest {

    private class CDWorld extends InstanceWorld {

        private L2Npc manager, managera, managerb, managerc, managerd, managere, managerf, managerg, managerh, chesta, chestb, chestc, chestd, _aenkinel;

        public CDWorld() {
        }
    }

    private static final String qn = "ChamberOfDelusionTower";

    private static final int INSTANCEID = 132;

    private static final int RESET_HOUR = 6;

    private static final int RESET_MIN = 30;

    private static final int GKSTART = 32663;

    private static final int GKFINISH = 32669;

    private static final int AENKINEL = 25695;

    private static final int PRIZ = 18820;

    private static final int FAIL1 = 18819;

    private static final int FAIL2 = 18819;

    private static final int FAIL3 = 18819;

    private static final int ROOM1 = 0;

    private static final int ROOM2 = 1;

    private static final int ROOM3 = 2;

    private static final int ROOM4 = 3;

    private static final int ROOM5 = 4;

    private static final int ROOM6 = 5;

    private static final int ROOM7 = 6;

    private static final int ROOM8 = 7;

    private int a;

    public int instId = 0;

    private int b;

    private int h = 0;

    private int g = 0;

    private int c;

    private int tp;

    private int m;

    private int r1 = 0;

    private int r2 = 0;

    private int r3 = 0;

    private int r4 = 0;

    private int r5 = 0;

    private int r6 = 0;

    private int r7 = 0;

    private int r8 = 0;

    private class teleCoord {

        int instanceId;

        int x;

        int y;

        int z;
    }

    private static final int[][] TELEPORT = { { -108992, -152624, -6752 }, { -108992, -153504, -6752 }, { -107120, -154304, -6752 }, { -107120, -155184, -6752 }, { -108064, -151328, -6752 }, { -107120, -153008, -6752 }, { -108992, -154800, -6752 }, { -108064, -153008, -6752 } };

    private boolean checkConditions(L2PcInstance player) {
        final L2Party party = player.getParty();
        if (party == null) {
            player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.NOT_IN_PARTY_CANT_ENTER));
            return false;
        }
        if (party.getLeader() != player) {
            player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.ONLY_PARTY_LEADER_CAN_ENTER));
            return false;
        }
        for (L2PcInstance partyMember : party.getPartyMembers()) {
            if (partyMember.getLevel() < 80) {
                SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.C1_LEVEL_REQUIREMENT_NOT_SUFFICIENT);
                sm.addPcName(partyMember);
                player.sendPacket(sm);
                sm = null;
                return false;
            }
            if (!Util.checkIfInRange(1000, player, partyMember, true)) {
                SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.C1_IS_IN_LOCATION_THAT_CANNOT_BE_ENTERED);
                sm.addPcName(partyMember);
                player.sendPacket(sm);
                sm = null;
                return false;
            }
            Long reentertime = InstanceManager.getInstance().getInstanceTime(partyMember.getObjectId(), INSTANCEID);
            if (System.currentTimeMillis() < reentertime) {
                SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.C1_MAY_NOT_REENTER_YET);
                sm.addPcName(partyMember);
                player.sendPacket(sm);
                sm = null;
                return false;
            }
        }
        return true;
    }

    private void teleportplayer(L2PcInstance player, teleCoord teleto) {
        player.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
        player.setInstanceId(teleto.instanceId);
        player.teleToLocation(teleto.x, teleto.y, teleto.z);
    }

    public void penalty(InstanceWorld world) {
        if (world instanceof CDWorld) {
            Calendar reenter = Calendar.getInstance();
            reenter.add(Calendar.MINUTE, RESET_MIN);
            reenter.add(Calendar.HOUR_OF_DAY, RESET_HOUR);
            SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.INSTANT_ZONE_S1_RESTRICTED);
            sm.addString(InstanceManager.getInstance().getInstanceIdName(world.templateId));
            for (int objectId : world.allowed) {
                final L2PcInstance player = L2World.getInstance().getPlayer(objectId);
                if (player != null && player.isOnline()) {
                    InstanceManager.getInstance().setInstanceTime(objectId, world.templateId, reenter.getTimeInMillis());
                    player.sendPacket(sm);
                }
            }
            sm = null;
        }
    }

    private void teleportrnd(L2PcInstance player) {
        tp = Rnd.get(TELEPORT.length);
        m = player.getParty().getMemberCount();
        switch(tp) {
            case ROOM1:
                if (r1 == m) return;
                for (int i = 0; i < TELEPORT.length; i++) {
                    if (i != tp) continue;
                    for (L2PcInstance partyMember : player.getParty().getPartyMembers()) {
                        r1++;
                        partyMember.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
                        partyMember.setInstanceId(instId);
                        partyMember.teleToLocation(TELEPORT[i][0], TELEPORT[i][1], TELEPORT[i][2]);
                    }
                }
                a = player.getX();
                b = player.getY();
                c = player.getZ();
                break;
            case ROOM2:
                if (r2 == m) return;
                for (int i = 0; i < TELEPORT.length; i++) {
                    if (i != tp) continue;
                    for (L2PcInstance partyMember : player.getParty().getPartyMembers()) {
                        r2++;
                        partyMember.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
                        partyMember.setInstanceId(instId);
                        partyMember.teleToLocation(TELEPORT[i][0], TELEPORT[i][1], TELEPORT[i][2]);
                    }
                }
                a = player.getX();
                b = player.getY();
                c = player.getZ();
                break;
            case ROOM3:
                if (r3 == m) return;
                for (int i = 0; i < TELEPORT.length; i++) {
                    if (i != tp) continue;
                    for (L2PcInstance partyMember : player.getParty().getPartyMembers()) {
                        r3++;
                        partyMember.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
                        partyMember.setInstanceId(instId);
                        partyMember.teleToLocation(TELEPORT[i][0], TELEPORT[i][1], TELEPORT[i][2]);
                    }
                }
                a = player.getX();
                b = player.getY();
                c = player.getZ();
                break;
            case ROOM4:
                if (r4 == m) return;
                for (int i = 0; i < TELEPORT.length; i++) {
                    if (i != tp) continue;
                    for (L2PcInstance partyMember : player.getParty().getPartyMembers()) {
                        r4++;
                        partyMember.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
                        partyMember.setInstanceId(instId);
                        partyMember.teleToLocation(TELEPORT[i][0], TELEPORT[i][1], TELEPORT[i][2]);
                    }
                }
                a = player.getX();
                b = player.getY();
                c = player.getZ();
                break;
            case ROOM5:
                if (r5 == m) return;
                for (int i = 0; i < TELEPORT.length; i++) {
                    if (i != tp) continue;
                    for (L2PcInstance partyMember : player.getParty().getPartyMembers()) {
                        r5++;
                        partyMember.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
                        partyMember.setInstanceId(instId);
                        partyMember.teleToLocation(TELEPORT[i][0], TELEPORT[i][1], TELEPORT[i][2]);
                    }
                }
                a = player.getX();
                b = player.getY();
                c = player.getZ();
                break;
            case ROOM6:
                if (r6 == m) return;
                for (int i = 0; i < TELEPORT.length; i++) {
                    if (i != tp) continue;
                    for (L2PcInstance partyMember : player.getParty().getPartyMembers()) {
                        r6++;
                        partyMember.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
                        partyMember.setInstanceId(instId);
                        partyMember.teleToLocation(TELEPORT[i][0], TELEPORT[i][1], TELEPORT[i][2]);
                    }
                }
                a = player.getX();
                b = player.getY();
                c = player.getZ();
                break;
            case ROOM7:
                if (r7 == m) return;
                for (int i = 0; i < TELEPORT.length; i++) {
                    if (i != tp) continue;
                    for (L2PcInstance partyMember : player.getParty().getPartyMembers()) {
                        r7++;
                        partyMember.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
                        partyMember.setInstanceId(instId);
                        partyMember.teleToLocation(TELEPORT[i][0], TELEPORT[i][1], TELEPORT[i][2]);
                    }
                }
                a = player.getX();
                b = player.getY();
                c = player.getZ();
                break;
            case ROOM8:
                if (r8 == m) return;
                for (int i = 0; i < TELEPORT.length; i++) {
                    if (i != tp) continue;
                    for (L2PcInstance partyMember : player.getParty().getPartyMembers()) {
                        r8++;
                        partyMember.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
                        partyMember.setInstanceId(instId);
                        partyMember.teleToLocation(TELEPORT[i][0], TELEPORT[i][1], TELEPORT[i][2]);
                    }
                }
                a = player.getX();
                b = player.getY();
                c = player.getZ();
                break;
        }
    }

    protected void spawnState(CDWorld world) {
        world._aenkinel = addSpawn(AENKINEL, -108064, -154688, -6752, 0, false, 0, false, world.instanceId);
        world._aenkinel.setIsNoRndWalk(false);
        world.manager = addSpawn(32669, -108064, -154688, -6752, 0, false, 0, false, world.instanceId);
        world.manager.setIsNoRndWalk(true);
        world.managerb = addSpawn(32669, -108992, -152624, -6752, 0, false, 0, false, world.instanceId);
        world.managerb.setIsNoRndWalk(true);
        world.managerc = addSpawn(32669, -108992, -153504, -6752, 0, false, 0, false, world.instanceId);
        world.managerc.setIsNoRndWalk(true);
        world.managerd = addSpawn(32669, -107120, -154304, -6752, 0, false, 0, false, world.instanceId);
        world.managerd.setIsNoRndWalk(true);
        world.managere = addSpawn(32669, -107120, -155184, -6752, 0, false, 0, false, world.instanceId);
        world.managere.setIsNoRndWalk(true);
        world.managerf = addSpawn(32669, -108064, -151328, -6752, 0, false, 0, false, world.instanceId);
        world.managerf.setIsNoRndWalk(true);
        world.managerg = addSpawn(32669, -107120, -153008, -6752, 0, false, 0, false, world.instanceId);
        world.managerg.setIsNoRndWalk(true);
        world.managerh = addSpawn(32669, -108992, -154800, -6752, 0, false, 0, false, world.instanceId);
        world.managerh.setIsNoRndWalk(true);
        world.managera = addSpawn(32669, -108064, -153008, -6752, 0, false, 0, false, world.instanceId);
        world.managera.setIsNoRndWalk(true);
    }

    protected int enterInstance(L2PcInstance player, String template) {
        InstanceWorld world = InstanceManager.getInstance().getPlayerWorld(player);
        if (world != null) {
            if (!(world instanceof CDWorld)) {
                player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.ALREADY_ENTERED_ANOTHER_INSTANCE_CANT_ENTER));
                return 0;
            }
            teleCoord tele = new teleCoord();
            tele.x = a;
            tele.y = b;
            tele.z = c;
            tele.instanceId = world.instanceId;
            teleportplayer(player, tele);
            return tele.instanceId;
        } else {
            if (!checkConditions(player)) return 0;
            int instanceId = InstanceManager.getInstance().createDynamicInstance(template);
            world = new CDWorld();
            world.instanceId = instanceId;
            world.templateId = INSTANCEID;
            world.status = 0;
            InstanceManager.getInstance().addWorld(world);
            _log.info("Chamber Of Delusion started " + template + " Instance: " + instanceId + " created by player: " + player.getName());
            spawnState((CDWorld) world);
            instId = world.instanceId;
            for (L2PcInstance partyMember : player.getParty().getPartyMembers()) {
                teleportrnd(partyMember);
                world.allowed.add(partyMember.getObjectId());
            }
            return instanceId;
        }
    }

    protected void exitInstance(L2PcInstance player, teleCoord tele) {
        player.setInstanceId(0);
        player.teleToLocation(tele.x, tele.y, tele.z);
        final L2Summon pet = player.getPet();
        if (pet != null) {
            pet.setInstanceId(0);
            pet.teleToLocation(tele.x, tele.y, tele.z);
        }
    }

    @Override
    public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
        if (player == null) return "";
        final L2Party party = player.getParty();
        if (party == null) return "";
        InstanceManager.getInstance().getPlayerWorld(player);
        instId = player.getInstanceId();
        if (event.equalsIgnoreCase("tproom")) {
            for (L2PcInstance partyMember : party.getPartyMembers()) {
                teleportrnd(partyMember);
            }
            startQuestTimer("tproom1", 480000, null, player);
            h++;
        } else if (event.equalsIgnoreCase("tproom1")) {
            for (L2PcInstance partyMember : party.getPartyMembers()) {
                teleportrnd(partyMember);
            }
            startQuestTimer("tproom2", 480000, null, player);
            h++;
        } else if (event.equalsIgnoreCase("tproom2")) {
            for (L2PcInstance partyMember : party.getPartyMembers()) {
                teleportrnd(partyMember);
            }
            startQuestTimer("tproom3", 480000, null, player);
            h++;
        } else if (event.equalsIgnoreCase("tproom3")) {
            teleCoord tele = new teleCoord();
            tele.instanceId = player.getInstanceId();
            tele.x = -108064;
            tele.y = -154688;
            tele.z = -6752;
            for (L2PcInstance partyMember : party.getPartyMembers()) {
                teleportplayer(partyMember, tele);
            }
        } else if ("7".equalsIgnoreCase(event)) {
            if (g != 0) return null;
            switch(h) {
                case 0:
                    cancelQuestTimers("tproom");
                    for (L2PcInstance partyMember : party.getPartyMembers()) {
                        teleportrnd(partyMember);
                    }
                    startQuestTimer("tproom1", 480000, null, player);
                    g = 1;
                    break;
                case 1:
                    cancelQuestTimers("tproom1");
                    for (L2PcInstance partyMember : party.getPartyMembers()) {
                        teleportrnd(partyMember);
                    }
                    startQuestTimer("tproom2", 480000, null, player);
                    g = 1;
                    break;
                case 2:
                    cancelQuestTimers("tproom2");
                    for (L2PcInstance partyMember : party.getPartyMembers()) {
                        teleportrnd(partyMember);
                    }
                    startQuestTimer("tproom3", 480000, null, player);
                    g = 1;
                    break;
                case 3:
                    cancelQuestTimers("tproom3");
                    teleCoord tele = new teleCoord();
                    tele.instanceId = player.getInstanceId();
                    tele.x = -108064;
                    tele.y = -154688;
                    tele.z = -6752;
                    for (L2PcInstance partyMember : party.getPartyMembers()) {
                        teleportplayer(partyMember, tele);
                    }
                    g = 1;
                    break;
            }
        }
        return "";
    }

    public String onAttack(L2Npc npc, L2PcInstance attacker) {
        InstanceWorld tmpworld = InstanceManager.getInstance().getWorld(npc.getInstanceId());
        if (tmpworld instanceof CDWorld) {
            CDWorld world = (CDWorld) tmpworld;
            switch(npc.getNpcId()) {
                case FAIL1:
                    world.chesta.deleteMe();
                    world.chestb.deleteMe();
                    world.chestc.deleteMe();
                    world.chestd.deleteMe();
                    break;
                case PRIZ:
                    world.chestb.deleteMe();
                    world.chestc.deleteMe();
                    world.chestd.deleteMe();
                    break;
            }
        }
        return null;
    }

    public String onKill(L2Npc npc, L2PcInstance player) {
        if (npc.getNpcId() == AENKINEL) {
            InstanceWorld tmpworld = InstanceManager.getInstance().getWorld(npc.getInstanceId());
            if (tmpworld instanceof CDWorld) {
                CDWorld world = (CDWorld) tmpworld;
                world.chesta = addSpawn(PRIZ, -121524, -155073, -6752, 0, false, 0, false, world.instanceId);
                world.chesta.setIsNoRndWalk(true);
                world.chestb = addSpawn(FAIL1, -121486, -155070, -6752, 0, false, 0, false, world.instanceId);
                world.chestb.setIsNoRndWalk(true);
                world.chestc = addSpawn(FAIL2, -121457, -155071, -6752, 0, false, 0, false, world.instanceId);
                world.chestc.setIsNoRndWalk(true);
                world.chestd = addSpawn(FAIL3, -121428, -155070, -6752, 0, false, 0, false, world.instanceId);
                world.chestd.setIsNoRndWalk(true);
                _log.info("spawn");
            }
        }
        return "";
    }

    @Override
    public final String onFirstTalk(L2Npc npc, L2PcInstance player) {
        return npc.getNpcId() + ".htm";
    }

    @Override
    public String onTalk(L2Npc npc, L2PcInstance player) {
        QuestState st = player.getQuestState(qn);
        if (st == null) st = newQuestState(player);
        switch(npc.getNpcId()) {
            case GKSTART:
                if (enterInstance(player, "ChamberofDelusionTower.xml") != 0) startQuestTimer("tproom", 480000, null, player);
                break;
            case GKFINISH:
                InstanceWorld world = InstanceManager.getInstance().getPlayerWorld(player);
                if (world == null) return "";
                world.allowed.remove(world.allowed.indexOf(player.getObjectId()));
                teleCoord tele = new teleCoord();
                tele.instanceId = 0;
                tele.x = -114592;
                tele.y = -152509;
                tele.z = -6723;
                cancelQuestTimers("tproom");
                cancelQuestTimers("tproom1");
                cancelQuestTimers("tproom2");
                cancelQuestTimers("tproom3");
                penalty(world);
                for (L2PcInstance partyMember : player.getParty().getPartyMembers()) {
                    exitInstance(partyMember, tele);
                }
                break;
        }
        return "";
    }

    public ChamberOfDelusionTower(int questId, String name, String descr) {
        super(questId, name, descr);
        addStartNpc(GKSTART);
        addTalkId(GKSTART);
        addStartNpc(GKFINISH);
        addFirstTalkId(GKFINISH);
        addTalkId(GKFINISH);
        addKillId(AENKINEL);
        addAttackId(PRIZ);
        addAttackId(FAIL1);
    }

    public static void main(String[] args) {
        new ChamberOfDelusionTower(-1, qn, "instances");
    }
}
