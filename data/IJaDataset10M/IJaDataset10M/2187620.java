package net.sf.l2j.gameserver.model.entity.L2JTeonEvents;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;
import javolution.text.TextBuilder;
import net.sf.l2j.Config;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.Announcements;
import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.datatables.NpcTable;
import net.sf.l2j.gameserver.datatables.SpawnTable;
import net.sf.l2j.gameserver.model.L2Effect;
import net.sf.l2j.gameserver.model.L2Party;
import net.sf.l2j.gameserver.model.L2Spawn;
import net.sf.l2j.gameserver.model.L2Summon;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PetInstance;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUser;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.StatusUpdate;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DM {

    private static final Log _log = LogFactory.getLog(DM.class.getName());

    public static String _eventName = new String(), _eventDesc = new String(), _joiningLocationName = new String();

    public static Vector<String> _savePlayers = new Vector<String>();

    public static Vector<L2PcInstance> _players = new Vector<L2PcInstance>();

    public static boolean _joining = false, _teleport = false, _started = false, _sitForced = false;

    public static L2Spawn _npcSpawn;

    public static L2PcInstance _topPlayer;

    public static int _npcId = 0, _npcX = 0, _npcY = 0, _npcZ = 0, _rewardId = 0, _rewardAmount = 0, _topKills = 0, _minlvl = 0, _maxlvl = 0, _playerColors = 0, _playerX = 0, _playerY = 0, _playerZ = 0;

    public static void setNpcPos(L2PcInstance activeChar) {
        _npcX = activeChar.getX();
        _npcY = activeChar.getY();
        _npcZ = activeChar.getZ();
    }

    public static boolean checkMaxLevel(int maxlvl) {
        if (_minlvl >= maxlvl) return false;
        return true;
    }

    public static boolean checkMinLevel(int minlvl) {
        if (_maxlvl <= minlvl) return false;
        return true;
    }

    public static void setPlayersPos(L2PcInstance activeChar) {
        _playerX = activeChar.getX();
        _playerY = activeChar.getY();
        _playerZ = activeChar.getZ();
    }

    public static boolean checkPlayerOk() {
        if (_started || _teleport || _joining) return false;
        return true;
    }

    public static void startJoin(L2PcInstance activeChar) {
        if (!startJoinOk()) {
            if (_log.isDebugEnabled()) _log.debug("DM Engine[startJoin(" + activeChar.getName() + ")]: startJoinOk() = false");
            return;
        }
        _joining = true;
        spawnEventNpc(activeChar);
        Announcements.getInstance().announceToAll(_eventName + "(DM): Joinable in " + _joiningLocationName + "!");
    }

    private static boolean startJoinOk() {
        if (_started || _teleport || _joining || _eventName.equals("") || _joiningLocationName.equals("") || _eventDesc.equals("") || _npcId == 0 || _npcX == 0 || _npcY == 0 || _npcZ == 0 || _rewardId == 0 || _rewardAmount == 0 || _playerX == 0 || _playerY == 0 || _playerZ == 0) return false;
        return true;
    }

    private static void spawnEventNpc(L2PcInstance activeChar) {
        L2NpcTemplate tmpl = NpcTable.getInstance().getTemplate(_npcId);
        try {
            _npcSpawn = new L2Spawn(tmpl);
            _npcSpawn.setLocx(_npcX);
            _npcSpawn.setLocy(_npcY);
            _npcSpawn.setLocz(_npcZ);
            _npcSpawn.setAmount(1);
            _npcSpawn.setHeading(activeChar.getHeading());
            _npcSpawn.setRespawnDelay(1);
            SpawnTable.getInstance().addNewSpawn(_npcSpawn, false);
            _npcSpawn.init();
            _npcSpawn.getLastSpawn().getStatus().setCurrentHp(999999999);
            _npcSpawn.getLastSpawn().setTitle(_eventName);
            _npcSpawn.getLastSpawn()._isEventMobDM = true;
            _npcSpawn.getLastSpawn().isAggressive();
            _npcSpawn.getLastSpawn().decayMe();
            _npcSpawn.getLastSpawn().spawnMe(_npcSpawn.getLastSpawn().getX(), _npcSpawn.getLastSpawn().getY(), _npcSpawn.getLastSpawn().getZ());
            _npcSpawn.getLastSpawn().broadcastPacket(new MagicSkillUser(_npcSpawn.getLastSpawn(), _npcSpawn.getLastSpawn(), 1034, 1, 1, 1));
        } catch (Exception e) {
            if (_log.isDebugEnabled()) _log.debug("DM Engine[spawnEventNpc(" + activeChar.getName() + ")]: exception: " + e.getMessage());
        }
    }

    public static void teleportStart() {
        if (!_joining || _started || _teleport) return;
        _joining = false;
        Announcements.getInstance().announceToAll(_eventName + "(DM): Teleport to team spot in 20 seconds!");
        setUserData();
        ThreadPoolManager.getInstance().scheduleGeneral(new Runnable() {

            public void run() {
                DM.sit();
                for (L2PcInstance player : DM._players) if (player != null) {
                    if (Config.DM_ON_START_UNSUMMON_PET) if (player.getPet() != null) {
                        L2Summon summon = player.getPet();
                        for (L2Effect e : summon.getAllEffects()) e.exit();
                        if (summon instanceof L2PetInstance) summon.unSummon(player);
                    }
                    if (Config.DM_ON_START_REMOVE_ALL_EFFECTS) for (L2Effect e : player.getAllEffects()) if (e != null) e.exit();
                    if (player.getParty() != null) {
                        L2Party party = player.getParty();
                        party.removePartyMember(player);
                    }
                    player.teleToLocation(_playerX, _playerY, _playerZ);
                }
            }
        }, 20000);
        _teleport = true;
    }

    public static void startEvent(L2PcInstance activeChar) {
        if (!startEventOk()) {
            if (_log.isDebugEnabled()) _log.debug("DM Engine[startEvent(" + activeChar.getName() + ")]: startEventOk() = false");
            return;
        }
        _teleport = false;
        sit();
        Announcements.getInstance().announceToAll(_eventName + "(DM): Started. Go to kill your enemies!");
        _started = true;
    }

    private static boolean startEventOk() {
        if (_joining || !_teleport || _started) return false;
        return true;
    }

    public static void setUserData() {
        for (L2PcInstance player : _players) {
            player._originalNameColorDM = player.getAppearance().getNameColor();
            player._originalKarmaDM = player.getKarma();
            player._inEventDM = true;
            player._countDMkills = 0;
            player.getAppearance().setNameColor(_playerColors);
            player.setKarma(0);
            player.broadcastUserInfo();
        }
    }

    public static void removeUserData() {
        for (L2PcInstance player : _players) {
            player.getAppearance().setNameColor(player._originalNameColorDM);
            player.setKarma(player._originalKarmaDM);
            player._inEventDM = false;
            player._countDMkills = 0;
            player.broadcastUserInfo();
        }
    }

    public static void finishEvent(L2PcInstance activeChar) {
        if (!finishEventOk()) {
            if (_log.isDebugEnabled()) _log.debug("DM Engine[finishEvent(" + activeChar.getName() + ")]: finishEventOk() = false");
            return;
        }
        _started = false;
        unspawnEventNpc();
        processTopPlayer();
        if (_topKills == 0) Announcements.getInstance().announceToAll(_eventName + "(DM): No players win the match(nobody killed)."); else {
            Announcements.getInstance().announceToAll(_eventName + "(DM): " + _topPlayer.getName() + " wins the match! " + _topKills + " kills.");
            rewardPlayer(activeChar);
        }
        teleportFinish();
    }

    private static boolean finishEventOk() {
        if (!_started) return false;
        return true;
    }

    public static void processTopPlayer() {
        for (L2PcInstance player : _players) if (player._countDMkills > _topKills) {
            _topPlayer = player;
            _topKills = player._countDMkills;
        }
    }

    public static void rewardPlayer(L2PcInstance activeChar) {
        if (_topPlayer != null) {
            _topPlayer.addItem("DM Event: " + _eventName, _rewardId, _rewardAmount, _topPlayer, true);
            StatusUpdate su = new StatusUpdate(_topPlayer.getObjectId());
            su.addAttribute(StatusUpdate.CUR_LOAD, _topPlayer.getCurrentLoad());
            _topPlayer.sendPacket(su);
            NpcHtmlMessage nhm = new NpcHtmlMessage(5);
            TextBuilder replyMSG = new TextBuilder("");
            replyMSG.append("<html><body>You won the event. Look in your inventory for the reward.</body></html>");
            nhm.setHtml(replyMSG.toString());
            _topPlayer.sendPacket(nhm);
            _topPlayer.sendPacket(ActionFailed.STATIC_PACKET);
        }
    }

    public static void abortEvent() {
        if (!_joining && !_teleport && !_started) return;
        _joining = false;
        _teleport = false;
        _started = false;
        unspawnEventNpc();
        Announcements.getInstance().announceToAll(_eventName + "(DM): Match aborted!");
        teleportFinish();
    }

    public static void sit() {
        if (_sitForced) _sitForced = false; else _sitForced = true;
        for (L2PcInstance player : _players) if (player != null) if (_sitForced) {
            player.stopMove(null, false);
            player.abortAttack();
            player.abortCast();
            if (!player.isSitting()) player.sitDown();
        } else if (player.isSitting()) player.standUp();
    }

    public static void dumpData() {
        System.out.println("");
        System.out.println("");
        if (!_joining && !_teleport && !_started) {
            System.out.println("<<---------------------------------->>");
            System.out.println(">> DM Engine infos dump (INACTIVE) <<");
            System.out.println("<<--^----^^-----^----^^------^^----->>");
        } else if (_joining && !_teleport && !_started) {
            System.out.println("<<--------------------------------->>");
            System.out.println(">> DM Engine infos dump (JOINING) <<");
            System.out.println("<<--^----^^-----^----^^------^----->>");
        } else if (!_joining && _teleport && !_started) {
            System.out.println("<<---------------------------------->>");
            System.out.println(">> DM Engine infos dump (TELEPORT) <<");
            System.out.println("<<--^----^^-----^----^^------^^----->>");
        } else if (!_joining && !_teleport && _started) {
            System.out.println("<<--------------------------------->>");
            System.out.println(">> DM Engine infos dump (STARTED) <<");
            System.out.println("<<--^----^^-----^----^^------^----->>");
        }
        System.out.println("Name: " + _eventName);
        System.out.println("Desc: " + _eventDesc);
        System.out.println("Join location: " + _joiningLocationName);
        System.out.println("Min lvl: " + _minlvl);
        System.out.println("Max lvl: " + _maxlvl);
        System.out.println("");
        System.out.println("##################################");
        System.out.println("# _players(Vector<L2PcInstance>) #");
        System.out.println("##################################");
        System.out.println("Total Players : " + _players.size());
        for (L2PcInstance player : _players) if (player != null) System.out.println("Name: " + player.getName() + " kills :" + player._countDMkills);
        System.out.println("");
        System.out.println("################################");
        System.out.println("# _savePlayers(Vector<String>) #");
        System.out.println("################################");
        for (String player : _savePlayers) System.out.println("Name: " + player);
        System.out.println("");
        System.out.println("");
    }

    public static void loadData() {
        _eventName = new String();
        _eventDesc = new String();
        _joiningLocationName = new String();
        _savePlayers = new Vector<String>();
        _players = new Vector<L2PcInstance>();
        _topPlayer = null;
        _npcSpawn = null;
        _joining = false;
        _teleport = false;
        _started = false;
        _sitForced = false;
        _npcId = 0;
        _npcX = 0;
        _npcY = 0;
        _npcZ = 0;
        _rewardId = 0;
        _rewardAmount = 0;
        _topKills = 0;
        _minlvl = 0;
        _maxlvl = 0;
        _playerColors = 0;
        _playerX = 0;
        _playerY = 0;
        _playerZ = 0;
        java.sql.Connection con = null;
        try {
            PreparedStatement statement;
            ResultSet rs;
            con = L2DatabaseFactory.getInstance().getConnection();
            statement = con.prepareStatement("Select * from dm");
            rs = statement.executeQuery();
            while (rs.next()) {
                _eventName = rs.getString("eventName");
                _eventDesc = rs.getString("eventDesc");
                _joiningLocationName = rs.getString("joiningLocation");
                _minlvl = rs.getInt("minlvl");
                _maxlvl = rs.getInt("maxlvl");
                _npcId = rs.getInt("npcId");
                _npcX = rs.getInt("npcX");
                _npcY = rs.getInt("npcY");
                _npcZ = rs.getInt("npcZ");
                _rewardId = rs.getInt("rewardId");
                _rewardAmount = rs.getInt("rewardAmount");
                _playerColors = rs.getInt("color");
                _playerX = rs.getInt("playerX");
                _playerY = rs.getInt("playerY");
                _playerZ = rs.getInt("playerZ");
            }
            statement.close();
        } catch (Exception e) {
            _log.error("Exception: DM.loadData(): " + e.getMessage());
        } finally {
            try {
                con.close();
            } catch (Exception e) {
            }
        }
    }

    public static void saveData() {
        java.sql.Connection con = null;
        try {
            con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement;
            statement = con.prepareStatement("Delete from dm");
            statement.execute();
            statement.close();
            statement = con.prepareStatement("INSERT INTO dm (eventName, eventDesc, joiningLocation, minlvl, maxlvl, npcId, npcX, npcY, npcZ, rewardId, rewardAmount, color, playerX, playerY, playerZ ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            statement.setString(1, _eventName);
            statement.setString(2, _eventDesc);
            statement.setString(3, _joiningLocationName);
            statement.setInt(4, _minlvl);
            statement.setInt(5, _maxlvl);
            statement.setInt(6, _npcId);
            statement.setInt(7, _npcX);
            statement.setInt(8, _npcY);
            statement.setInt(9, _npcZ);
            statement.setInt(10, _rewardId);
            statement.setInt(11, _rewardAmount);
            statement.setInt(12, _playerColors);
            statement.setInt(13, _playerX);
            statement.setInt(14, _playerY);
            statement.setInt(15, _playerZ);
            statement.execute();
            statement.close();
        } catch (Exception e) {
            _log.error("Exception: DM.saveData(): " + e.getMessage());
        } finally {
            try {
                con.close();
            } catch (Exception e) {
            }
        }
    }

    public static void showEventHtml(L2PcInstance eventPlayer, String objectId) {
        try {
            NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
            TextBuilder replyMSG = new TextBuilder("<html><body>");
            replyMSG.append("DM Match<br><br><br>");
            replyMSG.append("Current event...<br1>");
            replyMSG.append("    ... name:&nbsp;<font color=\"00FF00\">" + _eventName + "</font><br1>");
            replyMSG.append("    ... description:&nbsp;<font color=\"00FF00\">" + _eventDesc + "</font><br><br>");
            if (!_started && !_joining) replyMSG.append("<center>Wait till the admin/gm start the participation.</center>"); else if (!_started && _joining && eventPlayer.getLevel() >= _minlvl && eventPlayer.getLevel() <= _maxlvl) {
                if (_players.contains(eventPlayer)) {
                    replyMSG.append("You participated already!<br><br>");
                    replyMSG.append("<table border=\"0\"><tr>");
                    replyMSG.append("<td width=\"200\">Wait till event start or</td>");
                    replyMSG.append("<td width=\"60\"><center><button value=\"remove\" action=\"bypass -h npc_" + objectId + "_dmevent_player_leave\" width=50 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></center></td>");
                    replyMSG.append("<td width=\"100\">your participation!</td>");
                    replyMSG.append("</tr></table>");
                } else {
                    replyMSG.append("You want to participate in the event?<br><br>");
                    replyMSG.append("<td width=\"200\">Admin set min lvl : <font color=\"00FF00\">" + _minlvl + "</font></td><br>");
                    replyMSG.append("<td width=\"200\">Admin set max lvl : <font color=\"00FF00\">" + _maxlvl + "</font></td><br><br>");
                    replyMSG.append("<button value=\"Join\" action=\"bypass -h npc_" + objectId + "_dmevent_player_join\" width=50 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">");
                }
            } else if (_started && !_joining) replyMSG.append("<center>DM match is in progress.</center>"); else if (eventPlayer.getLevel() < _minlvl || eventPlayer.getLevel() > _maxlvl) {
                replyMSG.append("Your lvl : <font color=\"00FF00\">" + eventPlayer.getLevel() + "</font><br>");
                replyMSG.append("Admin set min lvl : <font color=\"00FF00\">" + _minlvl + "</font><br>");
                replyMSG.append("Admin set max lvl : <font color=\"00FF00\">" + _maxlvl + "</font><br><br>");
                replyMSG.append("<font color=\"FFFF00\">You can't participate to this event.</font><br>");
            }
            replyMSG.append("</body></html>");
            adminReply.setHtml(replyMSG.toString());
            eventPlayer.sendPacket(adminReply);
            eventPlayer.sendPacket(ActionFailed.STATIC_PACKET);
        } catch (Exception e) {
            _log.error("DM Engine[showEventHtlm(" + eventPlayer.getName() + ", " + objectId + ")]: exception" + e.getMessage());
        }
    }

    public static void addPlayer(L2PcInstance player) {
        if (!addPlayerOk(player)) return;
        _players.add(player);
        player._originalNameColorDM = player.getAppearance().getNameColor();
        player._originalKarmaDM = player.getKarma();
        player._inEventDM = true;
        player._countDMkills = 0;
        _savePlayers.add(player.getName());
    }

    public static boolean addPlayerOk(L2PcInstance eventPlayer) {
        if (eventPlayer._inEventTvT) {
            eventPlayer.sendMessage("You already participated to another event!");
            return false;
        }
        if (eventPlayer._inEventCTF) {
            eventPlayer.sendMessage("You already participated to another event!");
            return false;
        }
        if (eventPlayer._inEventDM) {
            eventPlayer.sendMessage("You already participated in the event!");
            return false;
        }
        if (eventPlayer._inEventFOS) {
            eventPlayer.sendMessage("You already participated to another event!");
            return false;
        }
        return true;
    }

    public static synchronized void addDisconnectedPlayer(L2PcInstance player) {
        if (!_players.contains(player) && _savePlayers.contains(player.getName())) {
            if (Config.DM_ON_START_REMOVE_ALL_EFFECTS) for (L2Effect e : player.getAllEffects()) if (e != null) e.exit();
            _players.add(player);
            player._originalNameColorDM = player.getAppearance().getNameColor();
            player._originalKarmaDM = player.getKarma();
            player._inEventDM = true;
            player._countDMkills = 0;
            if (_teleport || _started) {
                player.getAppearance().setNameColor(_playerColors);
                player.setKarma(0);
                player.broadcastUserInfo();
                player.teleToLocation(_playerX, _playerY, _playerZ);
            }
        }
    }

    public static void removePlayer(L2PcInstance player) {
        if (player != null) _players.remove(player);
    }

    public static void cleanDM() {
        for (L2PcInstance player : _players) removePlayer(player);
        _savePlayers = new Vector<String>();
        _topPlayer = null;
        _npcSpawn = null;
        _joining = false;
        _teleport = false;
        _started = false;
        _sitForced = false;
        _topKills = 0;
        _players = new Vector<L2PcInstance>();
    }

    public static void unspawnEventNpc() {
        if (_npcSpawn == null) return;
        _npcSpawn.getLastSpawn().deleteMe();
        _npcSpawn.stopRespawn();
        SpawnTable.getInstance().deleteSpawn(_npcSpawn, true);
    }

    public static void teleportFinish() {
        Announcements.getInstance().announceToAll(_eventName + "(DM): Teleport back to participation NPC in 20 seconds!");
        removeUserData();
        ThreadPoolManager.getInstance().scheduleGeneral(new Runnable() {

            public void run() {
                for (L2PcInstance player : _players) if (player != null) player.teleToLocation(_npcX, _npcY, _npcZ);
                cleanDM();
            }
        }, 20000);
    }
}
