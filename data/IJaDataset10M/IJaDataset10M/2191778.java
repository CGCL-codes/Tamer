package net.sf.l2j.gameserver.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javolution.util.FastMap;
import net.sf.l2j.Config;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.instancemanager.DayNightSpawnManager;
import net.sf.l2j.gameserver.model.L2Spawn;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;

/**
 * This class ...
 *
 * @author Nightmare
 * @version $Revision: 1.5.2.6.2.7 $ $Date: 2005/03/27 15:29:18 $
 */
public class SpawnTable {

    private static Logger _log = Logger.getLogger(SpawnTable.class.getName());

    private Map<Integer, L2Spawn> _spawntable = new FastMap<Integer, L2Spawn>().shared();

    private int _npcSpawnCount;

    private int _customSpawnCount;

    private int _highestId;

    public static SpawnTable getInstance() {
        return SingletonHolder._instance;
    }

    private SpawnTable() {
        if (!Config.ALT_DEV_NO_SPAWNS) fillSpawnTable();
    }

    public Map<Integer, L2Spawn> getSpawnTable() {
        return _spawntable;
    }

    private void fillSpawnTable() {
        Connection con = null;
        try {
            con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement;
            if (Config.DELETE_GMSPAWN_ON_CUSTOM) statement = con.prepareStatement("SELECT id, count, npc_templateid, locx, locy, locz, heading, respawn_delay, loc_id, periodOfDay FROM spawnlist where id NOT in ( select id from nospawnlist where isCustom = false ) ORDER BY id"); else statement = con.prepareStatement("SELECT id, count, npc_templateid, locx, locy, locz, heading, respawn_delay, loc_id, periodOfDay FROM spawnlist ORDER BY id");
            ResultSet rset = statement.executeQuery();
            L2Spawn spawnDat;
            L2NpcTemplate template1;
            while (rset.next()) {
                template1 = NpcTable.getInstance().getTemplate(rset.getInt("npc_templateid"));
                if (template1 != null) {
                    if (template1.type.equalsIgnoreCase("L2SiegeGuard")) {
                    } else if (template1.type.equalsIgnoreCase("L2RaidBoss")) {
                    } else if (!Config.ALLOW_CLASS_MASTERS && template1.type.equals("L2ClassMaster")) {
                    } else {
                        spawnDat = new L2Spawn(template1);
                        spawnDat.setId(rset.getInt("id"));
                        spawnDat.setAmount(rset.getInt("count"));
                        spawnDat.setLocx(rset.getInt("locx"));
                        spawnDat.setLocy(rset.getInt("locy"));
                        spawnDat.setLocz(rset.getInt("locz"));
                        spawnDat.setHeading(rset.getInt("heading"));
                        spawnDat.setRespawnDelay(rset.getInt("respawn_delay"));
                        int loc_id = rset.getInt("loc_id");
                        spawnDat.setLocation(loc_id);
                        switch(rset.getInt("periodOfDay")) {
                            case 0:
                                _npcSpawnCount += spawnDat.init();
                                break;
                            case 1:
                                DayNightSpawnManager.getInstance().addDayCreature(spawnDat);
                                _npcSpawnCount++;
                                break;
                            case 2:
                                DayNightSpawnManager.getInstance().addNightCreature(spawnDat);
                                _npcSpawnCount++;
                                break;
                        }
                        _spawntable.put(spawnDat.getId(), spawnDat);
                        if (spawnDat.getId() > _highestId) _highestId = spawnDat.getId();
                    }
                } else _log.warning("SpawnTable: Data missing in NPC table for ID: " + rset.getInt("npc_templateid") + ".");
            }
            rset.close();
            statement.close();
        } catch (Exception e) {
            _log.log(Level.WARNING, "SpawnTable: Spawn could not be initialized: " + e.getMessage(), e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
            }
        }
        _log.info("SpawnTable: Loaded " + _spawntable.size() + " Npc Spawn Locations.");
        if (Config.CUSTOM_SPAWNLIST_TABLE) {
            try {
                con = L2DatabaseFactory.getInstance().getConnection();
                PreparedStatement statement;
                if (Config.DELETE_GMSPAWN_ON_CUSTOM) statement = con.prepareStatement("SELECT id, count, npc_templateid, locx, locy, locz, heading, respawn_delay, loc_id, periodOfDay FROM custom_spawnlist where id NOT in ( select id from nospawnlist where isCustom = true ) ORDER BY id"); else statement = con.prepareStatement("SELECT id, count, npc_templateid, locx, locy, locz, heading, respawn_delay, loc_id, periodOfDay FROM custom_spawnlist ORDER BY id");
                ResultSet rset = statement.executeQuery();
                L2Spawn spawnDat;
                L2NpcTemplate template1;
                while (rset.next()) {
                    template1 = NpcTable.getInstance().getTemplate(rset.getInt("npc_templateid"));
                    if (template1 != null) {
                        if (template1.type.equalsIgnoreCase("L2SiegeGuard")) {
                        } else if (template1.type.equalsIgnoreCase("L2RaidBoss")) {
                        } else if (!Config.ALLOW_CLASS_MASTERS && template1.type.equals("L2ClassMaster")) {
                        } else {
                            spawnDat = new L2Spawn(template1);
                            spawnDat.setId(rset.getInt("id"));
                            spawnDat.setAmount(rset.getInt("count"));
                            spawnDat.setLocx(rset.getInt("locx"));
                            spawnDat.setLocy(rset.getInt("locy"));
                            spawnDat.setLocz(rset.getInt("locz"));
                            spawnDat.setHeading(rset.getInt("heading"));
                            spawnDat.setRespawnDelay(rset.getInt("respawn_delay"));
                            spawnDat.setCustom(true);
                            int loc_id = rset.getInt("loc_id");
                            spawnDat.setLocation(loc_id);
                            switch(rset.getInt("periodOfDay")) {
                                case 0:
                                    _customSpawnCount += spawnDat.init();
                                    break;
                                case 1:
                                    DayNightSpawnManager.getInstance().addDayCreature(spawnDat);
                                    _customSpawnCount++;
                                    break;
                                case 2:
                                    DayNightSpawnManager.getInstance().addNightCreature(spawnDat);
                                    _customSpawnCount++;
                                    break;
                            }
                            _spawntable.put(spawnDat.getId(), spawnDat);
                            if (spawnDat.getId() > _highestId) _highestId = spawnDat.getId();
                        }
                    } else _log.warning("CustomSpawnTable: Data missing in NPC table for ID: " + rset.getInt("npc_templateid") + ".");
                }
                rset.close();
                statement.close();
            } catch (Exception e) {
                _log.log(Level.WARNING, "CustomSpawnTable: Spawn could not be initialized: " + e.getMessage(), e);
            } finally {
                try {
                    con.close();
                } catch (Exception e) {
                }
            }
            _log.info("CustomSpawnTable: Loaded " + _customSpawnCount + " Npc Spawn Locations.");
        }
        if (Config.DEBUG) _log.fine("SpawnTable: Spawning completed, total number of NPCs in the world: " + (_npcSpawnCount + _customSpawnCount));
    }

    public L2Spawn getTemplate(int id) {
        return _spawntable.get(id);
    }

    public Map<Integer, L2Spawn> getAllTemplates() {
        return _spawntable;
    }

    public void addNewSpawn(L2Spawn spawn, boolean storeInDb) {
        _highestId++;
        spawn.setId(_highestId);
        _spawntable.put(_highestId, spawn);
        if (storeInDb) {
            Connection con = null;
            String spawnTable;
            if (spawn.isCustom() && Config.CUSTOM_SPAWNLIST_TABLE) spawnTable = "custom_spawnlist"; else spawnTable = "spawnlist";
            try {
                con = L2DatabaseFactory.getInstance().getConnection();
                PreparedStatement statement = con.prepareStatement("INSERT INTO " + spawnTable + "(id,count,npc_templateid,locx,locy,locz,heading,respawn_delay,loc_id) values(?,?,?,?,?,?,?,?,?)");
                statement.setInt(1, spawn.getId());
                statement.setInt(2, spawn.getAmount());
                statement.setInt(3, spawn.getNpcid());
                statement.setInt(4, spawn.getLocx());
                statement.setInt(5, spawn.getLocy());
                statement.setInt(6, spawn.getLocz());
                statement.setInt(7, spawn.getHeading());
                statement.setInt(8, spawn.getRespawnDelay() / 1000);
                statement.setInt(9, spawn.getLocation());
                statement.execute();
                statement.close();
            } catch (Exception e) {
                _log.log(Level.WARNING, "SpawnTable: Could not store spawn in the DB:" + e.getMessage(), e);
            } finally {
                try {
                    con.close();
                } catch (Exception e) {
                }
            }
        }
    }

    public void deleteSpawn(L2Spawn spawn, boolean updateDb) {
        if (_spawntable.remove(spawn.getId()) == null) return;
        if (updateDb) {
            Connection con = null;
            if (Config.DELETE_GMSPAWN_ON_CUSTOM) try {
                con = L2DatabaseFactory.getInstance().getConnection();
                PreparedStatement statement = con.prepareStatement("Replace into nospawnlist VALUES (?,?)");
                statement.setInt(1, spawn.getId());
                statement.setBoolean(2, spawn.isCustom());
                statement.execute();
                statement.close();
            } catch (Exception e) {
                _log.log(Level.WARNING, "SpawnTable: Spawn " + spawn.getId() + " could not be insert into DB: " + e.getMessage(), e);
            } finally {
                try {
                    con.close();
                } catch (Exception e) {
                }
            } else try {
                con = L2DatabaseFactory.getInstance().getConnection();
                PreparedStatement statement = con.prepareStatement("DELETE FROM " + (spawn.isCustom() ? "custom_spawnlist" : "spawnlist") + " WHERE id=?");
                statement.setInt(1, spawn.getId());
                statement.execute();
                statement.close();
            } catch (Exception e) {
                _log.log(Level.WARNING, "SpawnTable: Spawn " + spawn.getId() + " could not be removed from DB: " + e.getMessage(), e);
            } finally {
                try {
                    con.close();
                } catch (Exception e) {
                }
            }
        }
    }

    public void reloadAll() {
        fillSpawnTable();
    }

    /**
	 * Get all the spawn of a NPC<BR>
	 * <BR>
	 *
	 * @param npcId
	 *            : ID of the NPC to find.
	 * @return
	 */
    public void findNPCInstances(L2PcInstance activeChar, int npcId, int teleportIndex) {
        int index = 0;
        for (L2Spawn spawn : _spawntable.values()) if (npcId == spawn.getNpcid()) {
            index++;
            if (teleportIndex > -1) {
                if (teleportIndex == index) activeChar.teleToLocation(spawn.getLocx(), spawn.getLocy(), spawn.getLocz(), true);
            } else activeChar.sendMessage(index + " - " + spawn.getTemplate().name + " (" + spawn.getId() + "): " + spawn.getLocx() + " " + spawn.getLocy() + " " + spawn.getLocz());
        }
        if (index == 0) activeChar.sendMessage("No current spawns found.");
    }

    @SuppressWarnings("synthetic-access")
    private static class SingletonHolder {

        protected static final SpawnTable _instance = new SpawnTable();
    }
}
