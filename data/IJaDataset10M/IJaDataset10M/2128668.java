package l1j.server.server.model;

import java.lang.reflect.Constructor;
import java.util.logging.Logger;
import l1j.server.server.IdFactory;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_NPCPack;
import l1j.server.server.templates.L1Npc;

public class L1WarSpawn {

    private static final Logger _log = Logger.getLogger(L1WarSpawn.class.getName());

    private static L1WarSpawn _instance;

    private Constructor _constructor;

    public L1WarSpawn() {
    }

    public static L1WarSpawn getInstance() {
        if (_instance == null) {
            _instance = new L1WarSpawn();
        }
        return _instance;
    }

    public void SpawnTower(int castleId) {
        int npcId = 81111;
        if (castleId == L1CastleLocation.ADEN_CASTLE_ID) {
            npcId = 81189;
        }
        L1Npc l1npc = NpcTable.getInstance().getTemplate(npcId);
        int[] loc = new int[3];
        loc = L1CastleLocation.getTowerLoc(castleId);
        SpawnWarObject(l1npc, loc[0], loc[1], (short) (loc[2]));
        if (castleId == L1CastleLocation.ADEN_CASTLE_ID) {
            spawnSubTower();
        }
    }

    private void spawnSubTower() {
        L1Npc l1npc;
        int[] loc = new int[3];
        for (int i = 1; i <= 4; i++) {
            l1npc = NpcTable.getInstance().getTemplate(81189 + i);
            loc = L1CastleLocation.getSubTowerLoc(i);
            SpawnWarObject(l1npc, loc[0], loc[1], (short) (loc[2]));
        }
    }

    public void SpawnCrown(int castleId) {
        L1Npc l1npc = NpcTable.getInstance().getTemplate(81125);
        int[] loc = new int[3];
        loc = L1CastleLocation.getTowerLoc(castleId);
        SpawnWarObject(l1npc, loc[0], loc[1], (short) (loc[2]));
    }

    public void SpawnFlag(int castleId) {
        L1Npc l1npc = NpcTable.getInstance().getTemplate(81122);
        int[] loc = new int[5];
        loc = L1CastleLocation.getWarArea(castleId);
        int x = 0;
        int y = 0;
        int locx1 = loc[0];
        int locx2 = loc[1];
        int locy1 = loc[2];
        int locy2 = loc[3];
        short mapid = (short) loc[4];
        for (x = locx1, y = locy1; x <= locx2; x += 8) {
            SpawnWarObject(l1npc, x, y, mapid);
        }
        for (x = locx2, y = locy1; y <= locy2; y += 8) {
            SpawnWarObject(l1npc, x, y, mapid);
        }
        for (x = locx2, y = locy2; x >= locx1; x -= 8) {
            SpawnWarObject(l1npc, x, y, mapid);
        }
        for (x = locx1, y = locy2; y >= locy1; y -= 8) {
            SpawnWarObject(l1npc, x, y, mapid);
        }
    }

    private void SpawnWarObject(L1Npc l1npc, int locx, int locy, short mapid) {
        try {
            if (l1npc != null) {
                Object obj = null;
                String s = l1npc.getImpl();
                _constructor = Class.forName((new StringBuilder()).append("l1j.server.server.model.Instance.").append(s).append("Instance").toString()).getConstructors()[0];
                Object aobj[] = { l1npc };
                L1NpcInstance npc = (L1NpcInstance) _constructor.newInstance(aobj);
                npc.setId(IdFactory.getInstance().nextId());
                npc.setX(locx);
                npc.setY(locy);
                npc.setHomeX(locx);
                npc.setHomeY(locy);
                npc.setHeading(0);
                npc.setMap(mapid);
                L1World.getInstance().storeObject(npc);
                L1World.getInstance().addVisibleObject(npc);
                for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
                    npc.addKnownObject(pc);
                    pc.addKnownObject(npc);
                    pc.sendPackets(new S_NPCPack(npc));
                    pc.broadcastPacket(new S_NPCPack(npc));
                }
            }
        } catch (Exception exception) {
        }
    }
}
