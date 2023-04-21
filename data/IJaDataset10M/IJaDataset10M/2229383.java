package ai.individual;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import javolution.util.FastList;
import javolution.util.FastMap;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.ai.CtrlIntention;
import net.sf.l2j.gameserver.instancemanager.GrandBossManager;
import net.sf.l2j.gameserver.datatables.DoorTable;
import net.sf.l2j.gameserver.datatables.NpcTable;
import net.sf.l2j.gameserver.datatables.SkillTable;
import net.sf.l2j.gameserver.datatables.SpawnTable;
import net.sf.l2j.gameserver.model.L2CharPosition;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2Spawn;
import net.sf.l2j.gameserver.model.L2Effect.EffectType;
import net.sf.l2j.gameserver.model.actor.instance.L2DoorInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2GrandBossInstance;
import net.sf.l2j.gameserver.model.zone.type.L2BossZone;
import net.sf.l2j.gameserver.network.serverpackets.CameraMode;
import net.sf.l2j.gameserver.network.serverpackets.CreatureSay;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUser;
import net.sf.l2j.gameserver.network.serverpackets.SpecialCamera;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;
import net.sf.l2j.gameserver.templates.StatsSet;
import net.sf.l2j.util.Rnd;
import ai.group_template.L2AttackableAIScript;

/**
 * This class ...
 * control for sequence of fight against "High Priestess van Halter".
 @version $Revision: $ $Date: $
 @author L2J_JP SANDMAN
 * compatibility with l2j by Kerberos
**/
public class VanHalter extends L2AttackableAIScript {

    @SuppressWarnings("unchecked")
    protected Map<Integer, List<L2PcInstance>> _bleedingPlayers = new FastMap<Integer, List<L2PcInstance>>();

    protected Map<Integer, L2Spawn> _monsterSpawn = new FastMap<Integer, L2Spawn>();

    protected List<L2Spawn> _royalGuardSpawn = new FastList<L2Spawn>();

    protected List<L2Spawn> _royalGuardCaptainSpawn = new FastList<L2Spawn>();

    protected List<L2Spawn> _royalGuardHelperSpawn = new FastList<L2Spawn>();

    protected List<L2Spawn> _triolRevelationSpawn = new FastList<L2Spawn>();

    protected List<L2Spawn> _triolRevelationAlive = new FastList<L2Spawn>();

    protected List<L2Spawn> _guardOfAltarSpawn = new FastList<L2Spawn>();

    protected List<L2Spawn> _cameraMarkerSpawn = new FastList<L2Spawn>();

    protected L2Spawn _ritualOfferingSpawn = null;

    protected L2Spawn _ritualSacrificeSpawn = null;

    protected L2Spawn _vanHalterSpawn = null;

    protected List<L2NpcInstance> _monsters = new FastList<L2NpcInstance>();

    protected List<L2NpcInstance> _royalGuard = new FastList<L2NpcInstance>();

    protected List<L2NpcInstance> _royalGuardCaptain = new FastList<L2NpcInstance>();

    protected List<L2NpcInstance> _royalGuardHepler = new FastList<L2NpcInstance>();

    protected List<L2NpcInstance> _triolRevelation = new FastList<L2NpcInstance>();

    protected List<L2NpcInstance> _guardOfAltar = new FastList<L2NpcInstance>();

    protected List<L2NpcInstance> _cameraMarker = new FastList<L2NpcInstance>();

    protected List<L2DoorInstance> _doorOfAltar = new FastList<L2DoorInstance>();

    protected List<L2DoorInstance> _doorOfSacrifice = new FastList<L2DoorInstance>();

    protected L2NpcInstance _ritualOffering = null;

    protected L2NpcInstance _ritualSacrifice = null;

    protected L2GrandBossInstance _vanHalter = null;

    protected ScheduledFuture<?> _movieTask = null;

    protected ScheduledFuture<?> _closeDoorOfAltarTask = null;

    protected ScheduledFuture<?> _openDoorOfAltarTask = null;

    protected ScheduledFuture<?> _lockUpDoorOfAltarTask = null;

    protected ScheduledFuture<?> _callRoyalGuardHelperTask = null;

    protected ScheduledFuture<?> _timeUpTask = null;

    protected ScheduledFuture<?> _intervalTask = null;

    protected ScheduledFuture<?> _halterEscapeTask = null;

    protected ScheduledFuture<?> _setBleedTask = null;

    boolean _isLocked = false;

    boolean _isHalterSpawned = false;

    boolean _isSacrificeSpawned = false;

    boolean _isCaptainSpawned = false;

    boolean _isHelperCalled = false;

    private static final byte NOTSPAWN = 0;

    private static final byte INTERVAL = 1;

    private static final byte ALIVE = 2;

    public VanHalter(int id, String name, String descr) {
        super(id, name, descr);
        int[] mobs = { 29062, 22188, 32058, 32059, 32060, 32061, 32062, 32063, 32064, 32065, 32066 };
        this.registerMobs(mobs);
        int _state = GrandBossManager.getInstance().getBossStatus(29062);
        _isLocked = false;
        _isCaptainSpawned = false;
        _isHelperCalled = false;
        _isHalterSpawned = false;
        _doorOfAltar.add(DoorTable.getInstance().getDoor(19160014));
        _doorOfAltar.add(DoorTable.getInstance().getDoor(19160015));
        openDoorOfAltar(true);
        _doorOfSacrifice.add(DoorTable.getInstance().getDoor(19160016));
        _doorOfSacrifice.add(DoorTable.getInstance().getDoor(19160017));
        closeDoorOfSacrifice();
        loadRoyalGuard();
        loadTriolRevelation();
        loadRoyalGuardCaptain();
        loadRoyalGuardHelper();
        loadGuardOfAltar();
        loadVanHalter();
        loadRitualOffering();
        loadRitualSacrifice();
        spawnRoyalGuard();
        spawnTriolRevelation();
        spawnVanHalter();
        spawnRitualOffering();
        _cameraMarkerSpawn.clear();
        try {
            L2NpcTemplate template1 = NpcTable.getInstance().getTemplate(13014);
            L2Spawn tempSpawn;
            tempSpawn = new L2Spawn(template1);
            tempSpawn.setLocx(-16397);
            tempSpawn.setLocy(-55200);
            tempSpawn.setLocz(-10449);
            tempSpawn.setHeading(16384);
            tempSpawn.setAmount(1);
            tempSpawn.setRespawnDelay(60000);
            SpawnTable.getInstance().addNewSpawn(tempSpawn, false);
            _cameraMarkerSpawn.add(tempSpawn);
            template1 = NpcTable.getInstance().getTemplate(13018);
            tempSpawn = new L2Spawn(template1);
            tempSpawn.setLocx(-16397);
            tempSpawn.setLocy(-55200);
            tempSpawn.setLocz(-10051);
            tempSpawn.setHeading(16384);
            tempSpawn.setAmount(1);
            tempSpawn.setRespawnDelay(60000);
            SpawnTable.getInstance().addNewSpawn(tempSpawn, false);
            _cameraMarkerSpawn.add(tempSpawn);
            template1 = NpcTable.getInstance().getTemplate(13018);
            tempSpawn = new L2Spawn(template1);
            tempSpawn.setLocx(-16397);
            tempSpawn.setLocy(-55200);
            tempSpawn.setLocz(-9741);
            tempSpawn.setHeading(16384);
            tempSpawn.setAmount(1);
            tempSpawn.setRespawnDelay(60000);
            SpawnTable.getInstance().addNewSpawn(tempSpawn, false);
            _cameraMarkerSpawn.add(tempSpawn);
            template1 = NpcTable.getInstance().getTemplate(13018);
            tempSpawn = new L2Spawn(template1);
            tempSpawn.setLocx(-16397);
            tempSpawn.setLocy(-55200);
            tempSpawn.setLocz(-9394);
            tempSpawn.setHeading(16384);
            tempSpawn.setAmount(1);
            tempSpawn.setRespawnDelay(60000);
            SpawnTable.getInstance().addNewSpawn(tempSpawn, false);
            _cameraMarkerSpawn.add(tempSpawn);
            template1 = NpcTable.getInstance().getTemplate(13018);
            tempSpawn = new L2Spawn(template1);
            tempSpawn.setLocx(-16397);
            tempSpawn.setLocy(-55197);
            tempSpawn.setLocz(-8739);
            tempSpawn.setHeading(16384);
            tempSpawn.setAmount(1);
            tempSpawn.setRespawnDelay(60000);
            SpawnTable.getInstance().addNewSpawn(tempSpawn, false);
            _cameraMarkerSpawn.add(tempSpawn);
        } catch (Exception e) {
            _log.warning("VanHalterManager: " + e.getMessage());
        }
        if (_timeUpTask != null) _timeUpTask.cancel(false);
        _timeUpTask = ThreadPoolManager.getInstance().scheduleGeneral(new TimeUp(), 1000);
        if (_setBleedTask != null) _setBleedTask.cancel(false);
        _setBleedTask = ThreadPoolManager.getInstance().scheduleGeneral(new Bleeding(), 2000);
        _log.info("VanHalterManager: State of High Priestess van Halter is " + _state + ".");
        if (_state == INTERVAL) enterInterval(); else GrandBossManager.getInstance().setBossStatus(29062, NOTSPAWN);
    }

    protected void loadRoyalGuard() {
        _royalGuardSpawn.clear();
        Connection con = null;
        try {
            con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT id, count, npc_templateid, locx, locy, locz, heading, respawn_delay FROM vanhalter_spawnlist Where npc_templateid between ? and ? ORDER BY id");
            statement.setInt(1, 22175);
            statement.setInt(2, 22176);
            ResultSet rset = statement.executeQuery();
            L2Spawn spawnDat;
            L2NpcTemplate template1;
            while (rset.next()) {
                template1 = NpcTable.getInstance().getTemplate(rset.getInt("npc_templateid"));
                if (template1 != null) {
                    spawnDat = new L2Spawn(template1);
                    spawnDat.setAmount(rset.getInt("count"));
                    spawnDat.setLocx(rset.getInt("locx"));
                    spawnDat.setLocy(rset.getInt("locy"));
                    spawnDat.setLocz(rset.getInt("locz"));
                    spawnDat.setHeading(rset.getInt("heading"));
                    spawnDat.setRespawnDelay(rset.getInt("respawn_delay"));
                    SpawnTable.getInstance().addNewSpawn(spawnDat, false);
                    _royalGuardSpawn.add(spawnDat);
                } else {
                    _log.warning("VanHalterManager.loadRoyalGuard: Data missing in NPC table for ID: " + rset.getInt("npc_templateid") + ".");
                }
            }
            rset.close();
            statement.close();
            _log.info("VanHalterManager: Loaded " + _royalGuardSpawn.size() + " Royal Guard spawn locations.");
        } catch (Exception e) {
            _log.warning("VanHalterManager: Spawn could not be initialized: " + e);
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    protected void spawnRoyalGuard() {
        if (!_royalGuard.isEmpty()) deleteRoyalGuard();
        for (L2Spawn rgs : _royalGuardSpawn) {
            rgs.startRespawn();
            _royalGuard.add(rgs.doSpawn());
        }
    }

    protected void deleteRoyalGuard() {
        for (L2NpcInstance rg : _royalGuard) {
            rg.getSpawn().stopRespawn();
            rg.deleteMe();
        }
        _royalGuard.clear();
    }

    protected void loadTriolRevelation() {
        _triolRevelationSpawn.clear();
        Connection con = null;
        try {
            con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT id, count, npc_templateid, locx, locy, locz, heading, respawn_delay FROM vanhalter_spawnlist Where npc_templateid between ? and ? ORDER BY id");
            statement.setInt(1, 32058);
            statement.setInt(2, 32068);
            ResultSet rset = statement.executeQuery();
            L2Spawn spawnDat;
            L2NpcTemplate template1;
            while (rset.next()) {
                template1 = NpcTable.getInstance().getTemplate(rset.getInt("npc_templateid"));
                if (template1 != null) {
                    spawnDat = new L2Spawn(template1);
                    spawnDat.setAmount(rset.getInt("count"));
                    spawnDat.setLocx(rset.getInt("locx"));
                    spawnDat.setLocy(rset.getInt("locy"));
                    spawnDat.setLocz(rset.getInt("locz"));
                    spawnDat.setHeading(rset.getInt("heading"));
                    spawnDat.setRespawnDelay(rset.getInt("respawn_delay"));
                    SpawnTable.getInstance().addNewSpawn(spawnDat, false);
                    _triolRevelationSpawn.add(spawnDat);
                } else {
                    _log.warning("VanHalterManager: Data missing in NPC table for ID: " + rset.getInt("npc_templateid") + ".");
                }
            }
            rset.close();
            statement.close();
            _log.info("VanHalterManager: Loaded " + _triolRevelationSpawn.size() + " Triol's Revelation spawn locations.");
        } catch (Exception e) {
            _log.warning("VanHalterManager: Spawn could not be initialized: " + e);
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    protected void spawnTriolRevelation() {
        if (!_triolRevelation.isEmpty()) deleteTriolRevelation();
        for (L2Spawn trs : _triolRevelationSpawn) {
            trs.startRespawn();
            _triolRevelation.add(trs.doSpawn());
            if (trs.getNpcid() != 32067 && trs.getNpcid() != 32068) _triolRevelationAlive.add(trs);
        }
    }

    protected void deleteTriolRevelation() {
        for (L2NpcInstance tr : _triolRevelation) {
            tr.getSpawn().stopRespawn();
            tr.deleteMe();
        }
        _triolRevelation.clear();
        _bleedingPlayers.clear();
    }

    protected void loadRoyalGuardCaptain() {
        _royalGuardCaptainSpawn.clear();
        Connection con = null;
        try {
            con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT id, count, npc_templateid, locx, locy, locz, heading, respawn_delay FROM vanhalter_spawnlist Where npc_templateid = ? ORDER BY id");
            statement.setInt(1, 22188);
            ResultSet rset = statement.executeQuery();
            L2Spawn spawnDat;
            L2NpcTemplate template1;
            while (rset.next()) {
                template1 = NpcTable.getInstance().getTemplate(rset.getInt("npc_templateid"));
                if (template1 != null) {
                    spawnDat = new L2Spawn(template1);
                    spawnDat.setAmount(rset.getInt("count"));
                    spawnDat.setLocx(rset.getInt("locx"));
                    spawnDat.setLocy(rset.getInt("locy"));
                    spawnDat.setLocz(rset.getInt("locz"));
                    spawnDat.setHeading(rset.getInt("heading"));
                    spawnDat.setRespawnDelay(rset.getInt("respawn_delay"));
                    SpawnTable.getInstance().addNewSpawn(spawnDat, false);
                    _royalGuardCaptainSpawn.add(spawnDat);
                } else {
                    _log.warning("VanHalterManager: Data missing in NPC table for ID: " + rset.getInt("npc_templateid") + ".");
                }
            }
            rset.close();
            statement.close();
            _log.info("VanHalterManager: Loaded " + _royalGuardCaptainSpawn.size() + " Royal Guard Captain spawn locations.");
        } catch (Exception e) {
            _log.warning("VanHalterManager: Spawn could not be initialized: " + e);
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    protected void spawnRoyalGuardCaptain() {
        if (!_royalGuardCaptain.isEmpty()) deleteRoyalGuardCaptain();
        for (L2Spawn trs : _royalGuardCaptainSpawn) {
            trs.startRespawn();
            _royalGuardCaptain.add(trs.doSpawn());
        }
        _isCaptainSpawned = true;
    }

    protected void deleteRoyalGuardCaptain() {
        for (L2NpcInstance tr : _royalGuardCaptain) {
            tr.getSpawn().stopRespawn();
            tr.deleteMe();
        }
        _royalGuardCaptain.clear();
    }

    protected void loadRoyalGuardHelper() {
        _royalGuardHelperSpawn.clear();
        Connection con = null;
        try {
            con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT id, count, npc_templateid, locx, locy, locz, heading, respawn_delay FROM vanhalter_spawnlist Where npc_templateid = ? ORDER BY id");
            statement.setInt(1, 22191);
            ResultSet rset = statement.executeQuery();
            L2Spawn spawnDat;
            L2NpcTemplate template1;
            while (rset.next()) {
                template1 = NpcTable.getInstance().getTemplate(rset.getInt("npc_templateid"));
                if (template1 != null) {
                    spawnDat = new L2Spawn(template1);
                    spawnDat.setAmount(rset.getInt("count"));
                    spawnDat.setLocx(rset.getInt("locx"));
                    spawnDat.setLocy(rset.getInt("locy"));
                    spawnDat.setLocz(rset.getInt("locz"));
                    spawnDat.setHeading(rset.getInt("heading"));
                    spawnDat.setRespawnDelay(rset.getInt("respawn_delay"));
                    SpawnTable.getInstance().addNewSpawn(spawnDat, false);
                    _royalGuardHelperSpawn.add(spawnDat);
                } else {
                    _log.warning("VanHalterManager: Data missing in NPC table for ID: " + rset.getInt("npc_templateid") + ".");
                }
            }
            rset.close();
            statement.close();
            _log.info("VanHalterManager: Loaded " + _royalGuardHelperSpawn.size() + " Royal Guard Helper spawn locations.");
        } catch (Exception e) {
            _log.warning("VanHalterManager: Spawn could not be initialized: " + e);
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    protected void spawnRoyalGuardHepler() {
        for (L2Spawn trs : _royalGuardHelperSpawn) {
            trs.startRespawn();
            _royalGuardHepler.add(trs.doSpawn());
        }
    }

    protected void deleteRoyalGuardHepler() {
        for (L2NpcInstance tr : _royalGuardHepler) {
            tr.getSpawn().stopRespawn();
            tr.deleteMe();
        }
        _royalGuardHepler.clear();
    }

    protected void loadGuardOfAltar() {
        _guardOfAltarSpawn.clear();
        Connection con = null;
        try {
            con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT id, count, npc_templateid, locx, locy, locz, heading, respawn_delay FROM vanhalter_spawnlist Where npc_templateid = ? ORDER BY id");
            statement.setInt(1, 32051);
            ResultSet rset = statement.executeQuery();
            L2Spawn spawnDat;
            L2NpcTemplate template1;
            while (rset.next()) {
                template1 = NpcTable.getInstance().getTemplate(rset.getInt("npc_templateid"));
                if (template1 != null) {
                    spawnDat = new L2Spawn(template1);
                    spawnDat.setAmount(rset.getInt("count"));
                    spawnDat.setLocx(rset.getInt("locx"));
                    spawnDat.setLocy(rset.getInt("locy"));
                    spawnDat.setLocz(rset.getInt("locz"));
                    spawnDat.setHeading(rset.getInt("heading"));
                    spawnDat.setRespawnDelay(rset.getInt("respawn_delay"));
                    SpawnTable.getInstance().addNewSpawn(spawnDat, false);
                    _guardOfAltarSpawn.add(spawnDat);
                } else {
                    _log.warning("VanHalterManager: Data missing in NPC table for ID: " + rset.getInt("npc_templateid") + ".");
                }
            }
            rset.close();
            statement.close();
            _log.info("VanHalterManager: Loaded " + _guardOfAltarSpawn.size() + " Guard Of Altar spawn locations.");
        } catch (Exception e) {
            _log.warning("VanHalterManager: Spawn could not be initialized: " + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
            }
        }
    }

    protected void spawnGuardOfAltar() {
        if (!_guardOfAltar.isEmpty()) deleteGuardOfAltar();
        for (L2Spawn trs : _guardOfAltarSpawn) {
            trs.startRespawn();
            _guardOfAltar.add(trs.doSpawn());
        }
    }

    protected void deleteGuardOfAltar() {
        for (L2NpcInstance tr : _guardOfAltar) {
            tr.getSpawn().stopRespawn();
            tr.deleteMe();
        }
        _guardOfAltar.clear();
    }

    protected void loadVanHalter() {
        _vanHalterSpawn = null;
        Connection con = null;
        try {
            con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT id, count, npc_templateid, locx, locy, locz, heading, respawn_delay FROM vanhalter_spawnlist Where npc_templateid = ? ORDER BY id");
            statement.setInt(1, 29062);
            ResultSet rset = statement.executeQuery();
            L2Spawn spawnDat;
            L2NpcTemplate template1;
            while (rset.next()) {
                template1 = NpcTable.getInstance().getTemplate(rset.getInt("npc_templateid"));
                if (template1 != null) {
                    spawnDat = new L2Spawn(template1);
                    spawnDat.setAmount(rset.getInt("count"));
                    spawnDat.setLocx(rset.getInt("locx"));
                    spawnDat.setLocy(rset.getInt("locy"));
                    spawnDat.setLocz(rset.getInt("locz"));
                    spawnDat.setHeading(rset.getInt("heading"));
                    spawnDat.setRespawnDelay(rset.getInt("respawn_delay"));
                    SpawnTable.getInstance().addNewSpawn(spawnDat, false);
                    _vanHalterSpawn = spawnDat;
                } else {
                    _log.warning("VanHalterManager: Data missing in NPC table for ID: " + rset.getInt("npc_templateid") + ".");
                }
            }
            rset.close();
            statement.close();
            _log.info("VanHalterManager: Loaded High Priestess van Halter spawn locations.");
        } catch (Exception e) {
            _log.warning("VanHalterManager: Spawn could not be initialized: " + e);
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    protected void spawnVanHalter() {
        _vanHalter = (L2GrandBossInstance) _vanHalterSpawn.doSpawn();
        _vanHalter.setIsImmobilized(true);
        _vanHalter.setIsInvul(true);
        _isHalterSpawned = true;
    }

    protected void deleteVanHalter() {
        _vanHalter.setIsImmobilized(false);
        _vanHalter.setIsInvul(false);
        _vanHalter.getSpawn().stopRespawn();
        _vanHalter.deleteMe();
    }

    protected void loadRitualOffering() {
        _ritualOfferingSpawn = null;
        Connection con = null;
        try {
            con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT id, count, npc_templateid, locx, locy, locz, heading, respawn_delay FROM vanhalter_spawnlist Where npc_templateid = ? ORDER BY id");
            statement.setInt(1, 32038);
            ResultSet rset = statement.executeQuery();
            L2Spawn spawnDat;
            L2NpcTemplate template1;
            while (rset.next()) {
                template1 = NpcTable.getInstance().getTemplate(rset.getInt("npc_templateid"));
                if (template1 != null) {
                    spawnDat = new L2Spawn(template1);
                    spawnDat.setAmount(rset.getInt("count"));
                    spawnDat.setLocx(rset.getInt("locx"));
                    spawnDat.setLocy(rset.getInt("locy"));
                    spawnDat.setLocz(rset.getInt("locz"));
                    spawnDat.setHeading(rset.getInt("heading"));
                    spawnDat.setRespawnDelay(rset.getInt("respawn_delay"));
                    SpawnTable.getInstance().addNewSpawn(spawnDat, false);
                    _ritualOfferingSpawn = spawnDat;
                } else {
                    _log.warning("VanHalterManager: Data missing in NPC table for ID: " + rset.getInt("npc_templateid") + ".");
                }
            }
            rset.close();
            statement.close();
            _log.info("VanHalterManager: Loaded Ritual Offering spawn locations.");
        } catch (Exception e) {
            _log.warning("VanHalterManager.loadRitualOffering: Spawn could not be initialized: " + e);
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    protected void spawnRitualOffering() {
        _ritualOffering = _ritualOfferingSpawn.doSpawn();
        _ritualOffering.setIsImmobilized(true);
        _ritualOffering.setIsInvul(true);
        _ritualOffering.setIsParalyzed(true);
    }

    protected void deleteRitualOffering() {
        _ritualOffering.setIsImmobilized(false);
        _ritualOffering.setIsInvul(false);
        _ritualOffering.setIsParalyzed(false);
        _ritualOffering.getSpawn().stopRespawn();
        _ritualOffering.deleteMe();
    }

    protected void loadRitualSacrifice() {
        _ritualSacrificeSpawn = null;
        Connection con = null;
        try {
            con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT id, count, npc_templateid, locx, locy, locz, heading, respawn_delay FROM vanhalter_spawnlist Where npc_templateid = ? ORDER BY id");
            statement.setInt(1, 22195);
            ResultSet rset = statement.executeQuery();
            L2Spawn spawnDat;
            L2NpcTemplate template1;
            while (rset.next()) {
                template1 = NpcTable.getInstance().getTemplate(rset.getInt("npc_templateid"));
                if (template1 != null) {
                    spawnDat = new L2Spawn(template1);
                    spawnDat.setAmount(rset.getInt("count"));
                    spawnDat.setLocx(rset.getInt("locx"));
                    spawnDat.setLocy(rset.getInt("locy"));
                    spawnDat.setLocz(rset.getInt("locz"));
                    spawnDat.setHeading(rset.getInt("heading"));
                    spawnDat.setRespawnDelay(rset.getInt("respawn_delay"));
                    SpawnTable.getInstance().addNewSpawn(spawnDat, false);
                    _ritualSacrificeSpawn = spawnDat;
                } else {
                    _log.warning("VanHalterManager: Data missing in NPC table for ID: " + rset.getInt("npc_templateid") + ".");
                }
            }
            rset.close();
            statement.close();
            _log.info("VanHalterManager: Loaded Ritual Sacrifice spawn locations.");
        } catch (Exception e) {
            _log.warning("VanHalterManager: Spawn could not be initialized: " + e);
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    protected void spawnRitualSacrifice() {
        _ritualSacrifice = _ritualSacrificeSpawn.doSpawn();
        _ritualSacrifice.setIsImmobilized(true);
        _ritualSacrifice.setIsInvul(true);
        _isSacrificeSpawned = true;
    }

    protected void deleteRitualSacrifice() {
        if (!_isSacrificeSpawned) return;
        _ritualSacrifice.getSpawn().stopRespawn();
        _ritualSacrifice.deleteMe();
        _isSacrificeSpawned = false;
    }

    protected void spawnCameraMarker() {
        if (!_cameraMarker.isEmpty()) deleteCameraMarker();
        for (int i = 0; i <= _cameraMarkerSpawn.size(); i++) {
            _cameraMarker.add(_cameraMarkerSpawn.get(i).doSpawn());
            _cameraMarker.get(i).getSpawn().stopRespawn();
            _cameraMarker.get(i).setIsImmobilized(true);
        }
    }

    protected void deleteCameraMarker() {
        if (_cameraMarker.isEmpty()) return;
        for (int i = 0; i <= _cameraMarker.size(); i++) {
            _cameraMarker.get(i).deleteMe();
        }
        _cameraMarker.clear();
    }

    /**
	 * @param intruder
	 */
    public void intruderDetection(L2PcInstance intruder) {
        if (_lockUpDoorOfAltarTask == null && !_isLocked && _isCaptainSpawned) {
            _lockUpDoorOfAltarTask = ThreadPoolManager.getInstance().scheduleGeneral(new LockUpDoorOfAltar(), 180000);
        }
    }

    private class LockUpDoorOfAltar implements Runnable {

        public void run() {
            closeDoorOfAltar(false);
            _isLocked = true;
            _lockUpDoorOfAltarTask = null;
        }
    }

    protected void openDoorOfAltar(boolean loop) {
        for (L2DoorInstance door : _doorOfAltar) {
            try {
                door.openMe();
            } catch (Exception e) {
                _log.severe(e.getMessage());
            }
        }
        if (loop) {
            _isLocked = false;
            if (_closeDoorOfAltarTask != null) _closeDoorOfAltarTask.cancel(false);
            _closeDoorOfAltarTask = null;
            _closeDoorOfAltarTask = ThreadPoolManager.getInstance().scheduleGeneral(new CloseDoorOfAltar(), 180000);
        } else {
            if (_closeDoorOfAltarTask != null) _closeDoorOfAltarTask.cancel(false);
            _closeDoorOfAltarTask = null;
        }
    }

    private class OpenDoorOfAltar implements Runnable {

        public void run() {
            openDoorOfAltar(true);
        }
    }

    protected void closeDoorOfAltar(boolean loop) {
        for (L2DoorInstance door : _doorOfAltar) {
            door.closeMe();
        }
        if (loop) {
            if (_openDoorOfAltarTask != null) _openDoorOfAltarTask.cancel(false);
            _openDoorOfAltarTask = null;
            _openDoorOfAltarTask = ThreadPoolManager.getInstance().scheduleGeneral(new OpenDoorOfAltar(), 180000);
        } else {
            if (_openDoorOfAltarTask != null) _openDoorOfAltarTask.cancel(false);
            _openDoorOfAltarTask = null;
        }
    }

    private class CloseDoorOfAltar implements Runnable {

        public void run() {
            closeDoorOfAltar(true);
        }
    }

    protected void openDoorOfSacrifice() {
        for (L2DoorInstance door : _doorOfSacrifice) {
            try {
                door.openMe();
            } catch (Exception e) {
                _log.severe(e.getMessage());
            }
        }
    }

    protected void closeDoorOfSacrifice() {
        for (L2DoorInstance door : _doorOfSacrifice) {
            try {
                door.closeMe();
            } catch (Exception e) {
                _log.severe(e.getMessage());
            }
        }
    }

    public void checkTriolRevelationDestroy() {
        if (_isCaptainSpawned) return;
        boolean isTriolRevelationDestroyed = true;
        for (L2Spawn tra : _triolRevelationAlive) {
            if (!tra.getLastSpawn().isDead()) isTriolRevelationDestroyed = false;
        }
        if (isTriolRevelationDestroyed) {
            spawnRoyalGuardCaptain();
        }
    }

    public void checkRoyalGuardCaptainDestroy() {
        if (!_isHalterSpawned) return;
        deleteRoyalGuard();
        deleteRoyalGuardCaptain();
        spawnGuardOfAltar();
        openDoorOfSacrifice();
        CreatureSay cs = new CreatureSay(0, 1, "Altar's Gatekeeper", "The door of the 3rd floor in the altar was opened.");
        for (L2PcInstance pc : getPlayersInside()) {
            pc.sendPacket(cs);
        }
        _vanHalter.setIsImmobilized(true);
        _vanHalter.setIsInvul(true);
        spawnCameraMarker();
        if (_timeUpTask != null) _timeUpTask.cancel(false);
        _timeUpTask = null;
        _movieTask = ThreadPoolManager.getInstance().scheduleGeneral(new Movie(1), 20000);
    }

    protected void combatBeginning() {
        if (_timeUpTask != null) _timeUpTask.cancel(false);
        _timeUpTask = ThreadPoolManager.getInstance().scheduleGeneral(new TimeUp(), 7200000);
        Map<Integer, L2PcInstance> _targets = new FastMap<Integer, L2PcInstance>();
        int i = 0;
        for (L2PcInstance pc : _vanHalter.getKnownList().getKnownPlayers().values()) {
            i++;
            _targets.put(i, pc);
        }
        _vanHalter.reduceCurrentHp(1, _targets.get(Rnd.get(1, i)));
    }

    public void callRoyalGuardHelper() {
        if (!_isHelperCalled) {
            _isHelperCalled = true;
            _halterEscapeTask = ThreadPoolManager.getInstance().scheduleGeneral(new HalterEscape(), 500);
            _callRoyalGuardHelperTask = ThreadPoolManager.getInstance().scheduleGeneral(new CallRoyalGuardHelper(), 1000);
        }
    }

    private class CallRoyalGuardHelper implements Runnable {

        public void run() {
            spawnRoyalGuardHepler();
            if (_royalGuardHepler.size() <= 6 && !_vanHalter.isDead()) {
                if (_callRoyalGuardHelperTask != null) _callRoyalGuardHelperTask.cancel(false);
                _callRoyalGuardHelperTask = ThreadPoolManager.getInstance().scheduleGeneral(new CallRoyalGuardHelper(), 10000);
            } else {
                if (_callRoyalGuardHelperTask != null) _callRoyalGuardHelperTask.cancel(false);
                _callRoyalGuardHelperTask = null;
            }
        }
    }

    private class HalterEscape implements Runnable {

        public void run() {
            if (_royalGuardHepler.size() <= 6 && !_vanHalter.isDead()) {
                if (_vanHalter.isAfraid()) {
                    _vanHalter.stopFear(null);
                } else {
                    _vanHalter.startFear();
                    if (_vanHalter.getZ() >= -10476) {
                        L2CharPosition pos = new L2CharPosition(-16397, -53308, -10448, 0);
                        if (_vanHalter.getX() == pos.x && _vanHalter.getY() == pos.y) {
                            _vanHalter.stopFear(null);
                        } else {
                            _vanHalter.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, pos);
                        }
                    } else if (_vanHalter.getX() >= -16397) {
                        L2CharPosition pos = new L2CharPosition(-15548, -54830, -10475, 0);
                        _vanHalter.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, pos);
                    } else {
                        L2CharPosition pos = new L2CharPosition(-17248, -54830, -10475, 0);
                        _vanHalter.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, pos);
                    }
                }
                if (_halterEscapeTask != null) _halterEscapeTask.cancel(false);
                _halterEscapeTask = ThreadPoolManager.getInstance().scheduleGeneral(new HalterEscape(), 5000);
            } else {
                _vanHalter.stopFear(null);
                if (_halterEscapeTask != null) _halterEscapeTask.cancel(false);
                _halterEscapeTask = null;
            }
        }
    }

    protected void addBleeding() {
        L2Skill bleed = SkillTable.getInstance().getInfo(4615, 12);
        for (L2NpcInstance tr : _triolRevelation) {
            if (tr.getKnownList().getKnownPlayersInRadius(tr.getAggroRange()).size() == 0 || tr.isDead()) continue;
            List<L2PcInstance> bpc = new FastList<L2PcInstance>();
            for (L2PcInstance pc : tr.getKnownList().getKnownPlayersInRadius(tr.getAggroRange())) {
                if (pc.getFirstEffect(bleed) == null) {
                    bleed.getEffects(tr, pc);
                    tr.broadcastPacket(new MagicSkillUser(tr, pc, bleed.getId(), 12, 1, 1));
                }
                bpc.add(pc);
            }
            _bleedingPlayers.remove(tr.getNpcId());
            _bleedingPlayers.put(tr.getNpcId(), bpc);
        }
    }

    @SuppressWarnings("unchecked")
    public void removeBleeding(int npcId) {
        if (_bleedingPlayers.get(npcId) == null) return;
        for (L2PcInstance pc : (FastList<L2PcInstance>) _bleedingPlayers.get(npcId)) {
            if (pc.getFirstEffect(EffectType.DMG_OVER_TIME) != null) pc.stopEffects(EffectType.DMG_OVER_TIME);
        }
        _bleedingPlayers.remove(npcId);
    }

    private class Bleeding implements Runnable {

        public void run() {
            addBleeding();
            if (_setBleedTask != null) _setBleedTask.cancel(false);
            _setBleedTask = ThreadPoolManager.getInstance().scheduleGeneral(new Bleeding(), 2000);
        }
    }

    public void enterInterval() {
        if (_callRoyalGuardHelperTask != null) _callRoyalGuardHelperTask.cancel(false);
        _callRoyalGuardHelperTask = null;
        if (_closeDoorOfAltarTask != null) _closeDoorOfAltarTask.cancel(false);
        _closeDoorOfAltarTask = null;
        if (_halterEscapeTask != null) _halterEscapeTask.cancel(false);
        _halterEscapeTask = null;
        if (_intervalTask != null) _intervalTask.cancel(false);
        _intervalTask = null;
        if (_lockUpDoorOfAltarTask != null) _lockUpDoorOfAltarTask.cancel(false);
        _lockUpDoorOfAltarTask = null;
        if (_movieTask != null) _movieTask.cancel(false);
        _movieTask = null;
        if (_openDoorOfAltarTask != null) _openDoorOfAltarTask.cancel(false);
        _openDoorOfAltarTask = null;
        if (_timeUpTask != null) _timeUpTask.cancel(false);
        _timeUpTask = null;
        if (_vanHalter.isDead()) {
            _vanHalter.getSpawn().stopRespawn();
        } else {
            deleteVanHalter();
        }
        deleteRoyalGuardHepler();
        deleteRoyalGuardCaptain();
        deleteRoyalGuard();
        deleteRitualOffering();
        deleteRitualSacrifice();
        deleteGuardOfAltar();
        if (_intervalTask != null) _intervalTask.cancel(false);
        if (GrandBossManager.getInstance().getBossStatus(29062) != INTERVAL) {
            int respawnTime = Rnd.get(172800000, 172800000 + 8640000);
            GrandBossManager.getInstance().setBossStatus(29062, INTERVAL);
            StatsSet info = GrandBossManager.getInstance().getStatsSet(29062);
            info.set("respawn_time", (System.currentTimeMillis() + respawnTime));
            GrandBossManager.getInstance().setStatsSet(29062, info);
        }
        StatsSet info = GrandBossManager.getInstance().getStatsSet(29062);
        long temp = info.getLong("respawn_time") - System.currentTimeMillis();
        _intervalTask = ThreadPoolManager.getInstance().scheduleGeneral(new Interval(), temp);
    }

    private class Interval implements Runnable {

        public void run() {
            setupAltar();
        }
    }

    public void setupAltar() {
        if (_callRoyalGuardHelperTask != null) _callRoyalGuardHelperTask.cancel(false);
        _callRoyalGuardHelperTask = null;
        if (_closeDoorOfAltarTask != null) _closeDoorOfAltarTask.cancel(false);
        _closeDoorOfAltarTask = null;
        if (_halterEscapeTask != null) _halterEscapeTask.cancel(false);
        _halterEscapeTask = null;
        if (_intervalTask != null) _intervalTask.cancel(false);
        _intervalTask = null;
        if (_lockUpDoorOfAltarTask != null) _lockUpDoorOfAltarTask.cancel(false);
        _lockUpDoorOfAltarTask = null;
        if (_movieTask != null) _movieTask.cancel(false);
        _movieTask = null;
        if (_openDoorOfAltarTask != null) _openDoorOfAltarTask.cancel(false);
        _openDoorOfAltarTask = null;
        if (_timeUpTask != null) _timeUpTask.cancel(false);
        _timeUpTask = null;
        deleteVanHalter();
        deleteTriolRevelation();
        deleteRoyalGuardHepler();
        deleteRoyalGuardCaptain();
        deleteRoyalGuard();
        deleteRitualSacrifice();
        deleteRitualOffering();
        deleteGuardOfAltar();
        deleteCameraMarker();
        _isLocked = false;
        _isCaptainSpawned = false;
        _isHelperCalled = false;
        _isHalterSpawned = false;
        closeDoorOfSacrifice();
        openDoorOfAltar(true);
        spawnTriolRevelation();
        spawnRoyalGuard();
        spawnRitualOffering();
        spawnVanHalter();
        GrandBossManager.getInstance().setBossStatus(29062, NOTSPAWN);
        if (_timeUpTask != null) _timeUpTask.cancel(false);
        _timeUpTask = ThreadPoolManager.getInstance().scheduleGeneral(new TimeUp(), 21600000);
    }

    private class TimeUp implements Runnable {

        public void run() {
            enterInterval();
        }
    }

    private class Movie implements Runnable {

        private int _distance = 6502500;

        private int _taskId;

        private List<L2PcInstance> _players = getPlayersInside();

        public Movie(int taskId) {
            _taskId = taskId;
        }

        public void run() {
            _vanHalter.setHeading(16384);
            _vanHalter.setTarget(_ritualOffering);
            switch(_taskId) {
                case 1:
                    GrandBossManager.getInstance().setBossStatus(29062, ALIVE);
                    for (L2PcInstance pc : _players) {
                        if (pc.getPlanDistanceSq(_vanHalter) <= _distance) {
                            enterMovieMode(pc);
                            specialCamera(pc, _vanHalter, 50, 90, 0, 0, 15000);
                        } else {
                            leaveMovieMode(pc);
                        }
                    }
                    if (_movieTask != null) _movieTask.cancel(false);
                    _movieTask = null;
                    _movieTask = ThreadPoolManager.getInstance().scheduleGeneral(new Movie(2), 16);
                    break;
                case 2:
                    for (L2PcInstance pc : _players) {
                        if (pc.getPlanDistanceSq(_cameraMarker.get(5)) <= _distance) {
                            enterMovieMode(pc);
                            specialCamera(pc, _cameraMarker.get(5), 1842, 100, -3, 0, 15000);
                        } else {
                            leaveMovieMode(pc);
                        }
                    }
                    if (_movieTask != null) _movieTask.cancel(false);
                    _movieTask = null;
                    _movieTask = ThreadPoolManager.getInstance().scheduleGeneral(new Movie(3), 1);
                    break;
                case 3:
                    for (L2PcInstance pc : _players) {
                        if (pc.getPlanDistanceSq(_cameraMarker.get(5)) <= _distance) {
                            enterMovieMode(pc);
                            specialCamera(pc, _cameraMarker.get(5), 1861, 97, -10, 1500, 15000);
                        } else {
                            leaveMovieMode(pc);
                        }
                    }
                    if (_movieTask != null) _movieTask.cancel(false);
                    _movieTask = null;
                    _movieTask = ThreadPoolManager.getInstance().scheduleGeneral(new Movie(4), 1500);
                    break;
                case 4:
                    for (L2PcInstance pc : _players) {
                        if (pc.getPlanDistanceSq(_cameraMarker.get(4)) <= _distance) {
                            enterMovieMode(pc);
                            specialCamera(pc, _cameraMarker.get(4), 1876, 97, 12, 0, 15000);
                        } else {
                            leaveMovieMode(pc);
                        }
                    }
                    if (_movieTask != null) _movieTask.cancel(false);
                    _movieTask = null;
                    _movieTask = ThreadPoolManager.getInstance().scheduleGeneral(new Movie(5), 1);
                    break;
                case 5:
                    for (L2PcInstance pc : _players) {
                        if (pc.getPlanDistanceSq(_cameraMarker.get(4)) <= _distance) {
                            enterMovieMode(pc);
                            specialCamera(pc, _cameraMarker.get(4), 1839, 94, 0, 1500, 15000);
                        } else {
                            leaveMovieMode(pc);
                        }
                    }
                    if (_movieTask != null) _movieTask.cancel(false);
                    _movieTask = null;
                    _movieTask = ThreadPoolManager.getInstance().scheduleGeneral(new Movie(6), 1500);
                    break;
                case 6:
                    for (L2PcInstance pc : _players) {
                        if (pc.getPlanDistanceSq(_cameraMarker.get(3)) <= _distance) {
                            enterMovieMode(pc);
                            specialCamera(pc, _cameraMarker.get(3), 1872, 94, 15, 0, 15000);
                        } else {
                            leaveMovieMode(pc);
                        }
                    }
                    if (_movieTask != null) _movieTask.cancel(false);
                    _movieTask = null;
                    _movieTask = ThreadPoolManager.getInstance().scheduleGeneral(new Movie(7), 1);
                    break;
                case 7:
                    for (L2PcInstance pc : _players) {
                        if (pc.getPlanDistanceSq(_cameraMarker.get(3)) <= _distance) {
                            enterMovieMode(pc);
                            specialCamera(pc, _cameraMarker.get(3), 1839, 92, 0, 1500, 15000);
                        } else {
                            leaveMovieMode(pc);
                        }
                    }
                    if (_movieTask != null) _movieTask.cancel(false);
                    _movieTask = null;
                    _movieTask = ThreadPoolManager.getInstance().scheduleGeneral(new Movie(8), 1500);
                    break;
                case 8:
                    for (L2PcInstance pc : _players) {
                        if (pc.getPlanDistanceSq(_cameraMarker.get(2)) <= _distance) {
                            enterMovieMode(pc);
                            specialCamera(pc, _cameraMarker.get(2), 1872, 92, 15, 0, 15000);
                        } else {
                            leaveMovieMode(pc);
                        }
                    }
                    if (_movieTask != null) _movieTask.cancel(false);
                    _movieTask = null;
                    _movieTask = ThreadPoolManager.getInstance().scheduleGeneral(new Movie(9), 1);
                    break;
                case 9:
                    for (L2PcInstance pc : _players) {
                        if (pc.getPlanDistanceSq(_cameraMarker.get(2)) <= _distance) {
                            enterMovieMode(pc);
                            specialCamera(pc, _cameraMarker.get(2), 1839, 90, 5, 1500, 15000);
                        } else {
                            leaveMovieMode(pc);
                        }
                    }
                    if (_movieTask != null) _movieTask.cancel(false);
                    _movieTask = null;
                    _movieTask = ThreadPoolManager.getInstance().scheduleGeneral(new Movie(10), 1500);
                    break;
                case 10:
                    for (L2PcInstance pc : _players) {
                        if (pc.getPlanDistanceSq(_cameraMarker.get(1)) <= _distance) {
                            enterMovieMode(pc);
                            specialCamera(pc, _cameraMarker.get(1), 1872, 90, 5, 0, 15000);
                        } else {
                            leaveMovieMode(pc);
                        }
                    }
                    if (_movieTask != null) _movieTask.cancel(false);
                    _movieTask = null;
                    _movieTask = ThreadPoolManager.getInstance().scheduleGeneral(new Movie(11), 1);
                    break;
                case 11:
                    for (L2PcInstance pc : _players) {
                        if (pc.getPlanDistanceSq(_cameraMarker.get(1)) <= _distance) {
                            enterMovieMode(pc);
                            specialCamera(pc, _cameraMarker.get(1), 2002, 90, 2, 1500, 15000);
                        } else {
                            leaveMovieMode(pc);
                        }
                    }
                    if (_movieTask != null) _movieTask.cancel(false);
                    _movieTask = null;
                    _movieTask = ThreadPoolManager.getInstance().scheduleGeneral(new Movie(12), 2000);
                    break;
                case 12:
                    for (L2PcInstance pc : _players) {
                        if (pc.getPlanDistanceSq(_vanHalter) <= _distance) {
                            enterMovieMode(pc);
                            specialCamera(pc, _vanHalter, 50, 90, 10, 0, 15000);
                        } else {
                            leaveMovieMode(pc);
                        }
                    }
                    if (_movieTask != null) _movieTask.cancel(false);
                    _movieTask = null;
                    _movieTask = ThreadPoolManager.getInstance().scheduleGeneral(new Movie(13), 1000);
                    break;
                case 13:
                    L2Skill skill = SkillTable.getInstance().getInfo(1168, 7);
                    _ritualOffering.setIsInvul(false);
                    _vanHalter.setTarget(_ritualOffering);
                    _vanHalter.setIsImmobilized(false);
                    _vanHalter.doCast(skill);
                    _vanHalter.setIsImmobilized(true);
                    if (_movieTask != null) _movieTask.cancel(false);
                    _movieTask = null;
                    _movieTask = ThreadPoolManager.getInstance().scheduleGeneral(new Movie(14), 4700);
                    break;
                case 14:
                    _ritualOffering.setIsInvul(false);
                    _ritualOffering.reduceCurrentHp(_ritualOffering.getMaxHp() + 1, _vanHalter);
                    if (_movieTask != null) _movieTask.cancel(false);
                    _movieTask = null;
                    _movieTask = ThreadPoolManager.getInstance().scheduleGeneral(new Movie(15), 4300);
                    break;
                case 15:
                    spawnRitualSacrifice();
                    deleteRitualOffering();
                    for (L2PcInstance pc : _players) {
                        if (pc.getPlanDistanceSq(_vanHalter) <= _distance) {
                            enterMovieMode(pc);
                            specialCamera(pc, _vanHalter, 100, 90, 15, 1500, 15000);
                        } else {
                            leaveMovieMode(pc);
                        }
                    }
                    if (_movieTask != null) _movieTask.cancel(false);
                    _movieTask = null;
                    _movieTask = ThreadPoolManager.getInstance().scheduleGeneral(new Movie(16), 2000);
                    break;
                case 16:
                    for (L2PcInstance pc : _players) {
                        if (pc.getPlanDistanceSq(_vanHalter) <= _distance) {
                            enterMovieMode(pc);
                            specialCamera(pc, _vanHalter, 5200, 90, -10, 9500, 6000);
                        } else {
                            leaveMovieMode(pc);
                        }
                    }
                    if (_movieTask != null) _movieTask.cancel(false);
                    _movieTask = null;
                    _movieTask = ThreadPoolManager.getInstance().scheduleGeneral(new Movie(17), 6000);
                    break;
                case 17:
                    for (L2PcInstance pc : _players) {
                        leaveMovieMode(pc);
                    }
                    deleteRitualSacrifice();
                    deleteCameraMarker();
                    _vanHalter.setIsImmobilized(false);
                    _vanHalter.setIsInvul(false);
                    if (_movieTask != null) _movieTask.cancel(false);
                    _movieTask = null;
                    _movieTask = ThreadPoolManager.getInstance().scheduleGeneral(new Movie(18), 1000);
                    break;
                case 18:
                    combatBeginning();
                    if (_movieTask != null) _movieTask.cancel(false);
                    _movieTask = null;
            }
        }
    }

    public void enterMovieMode(L2PcInstance player) {
        if (player == null) return;
        player.setTarget(null);
        player.stopMove(null);
        player.setIsInvul(true);
        player.setIsImmobilized(true);
        player.sendPacket(new CameraMode(1));
    }

    public void leaveMovieMode(L2PcInstance player) {
        if (player == null) return;
        if (!player.isGM()) player.setIsInvul(false);
        player.setIsImmobilized(false);
        player.sendPacket(new CameraMode(0));
    }

    /**
	 * yaw:North=90, south=270, east=0, west=180<BR>
	 * pitch > 0:looks up,pitch < 0:looks down<BR>
	 * time:faster that small value is.<BR>
	 */
    public void specialCamera(L2PcInstance player, L2NpcInstance target, int dist, int yaw, int pitch, int time, int duration) {
        player.sendPacket(new SpecialCamera(target.getObjectId(), dist, yaw, pitch, time, duration));
    }

    protected List<L2PcInstance> getPlayersInside() {
        List<L2PcInstance> lst = new FastList<L2PcInstance>();
        L2BossZone _Zone = GrandBossManager.getInstance().getZone(-16373, -53562, -10300);
        if (_Zone != null) {
            for (L2Character cha : _Zone.getCharactersInside().values()) {
                if (cha instanceof L2PcInstance) lst.add((L2PcInstance) cha);
            }
            return lst;
        }
        _log.warning("VanHalterManager: Zone for Van Halter is missing.");
        return null;
    }

    public String onAttack(L2NpcInstance npc, L2PcInstance attacker, int damage, boolean isPet) {
        int npcId = npc.getNpcId();
        if (npcId == 29062) {
            if (npc.getCurrentHp() / npc.getMaxHp() * 100 <= 20) callRoyalGuardHelper();
        }
        return super.onAttack(npc, attacker, damage, isPet);
    }

    public String onKill(L2NpcInstance npc, L2PcInstance killer, boolean isPet) {
        int npcId = npc.getNpcId();
        if (npcId == 29062) {
            enterInterval();
        } else if (npcId == 22188) {
            checkRoyalGuardCaptainDestroy();
        } else {
            removeBleeding(npcId);
            checkTriolRevelationDestroy();
        }
        return super.onKill(npc, killer, isPet);
    }

    public static void main(String[] args) {
        new VanHalter(-1, "vanhalter", "ai");
    }
}
