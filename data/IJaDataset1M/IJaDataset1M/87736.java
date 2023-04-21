package com.c0nflict.server;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.Vector;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.HttpSession;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;
import com.c0nflict.server.Player;
import com.c0nflict.server.ReverseAjaxTracker;
import com.c0nflict.server.Table;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;

public class TableManager {

    public int watchernum = 0;

    public boolean useDOM = true;

    public int[] ELO_LEVELS = { 0 };

    private long GAME_START_AFTER = 30000;

    public ConcurrentHashMap<Table, Collection<Player>> __TablePlayers = new ConcurrentHashMap<Table, Collection<Player>>();

    public Map<Table, Collection<Player>> tablePlayers = Collections.synchronizedMap(__TablePlayers);

    private HashMap<Player, Table> __PlayerTables = new HashMap<Player, Table>();

    public Map<Player, Table> playerTables = Collections.synchronizedMap(__PlayerTables);

    private HashMap<ScriptSession, Table> __SessionTable = new HashMap<ScriptSession, Table>();

    public Map<ScriptSession, Table> sessionTable = Collections.synchronizedMap(__SessionTable);

    private HashMap<Player, ScriptSession> __UserSessions = new HashMap<Player, ScriptSession>();

    public Map<Player, ScriptSession> userSessions = Collections.synchronizedMap(__UserSessions);

    private HashMap<ScriptSession, Player> __SessionUsers = new HashMap<ScriptSession, Player>();

    public Map<ScriptSession, Player> sessionUsers = Collections.synchronizedMap(__SessionUsers);

    private HashMap<HttpSession, Player> __HttpSessionUsers = new HashMap<HttpSession, Player>();

    public Map<HttpSession, Player> httpSessionUsers = Collections.synchronizedMap(__HttpSessionUsers);

    public Collection<Player> allChat = new HashSet<Player>();

    public Collection<String> serverMute = new HashSet<String>();

    public HashMap<Table, Date> startGames = new HashMap<Table, Date>();

    public Database db = null;

    private Connection dbConnection = null;

    public Vector<Integer> automatonElos = new Vector<Integer>();

    Table singleTable = new Table(13, 8, 18);

    ReverseAjaxTracker tracker = null;

    Timer tableTimerWatchdog = null;

    private boolean disableLowAdmin = false;

    public void disableLowAdmin() {
        disableLowAdmin = true;
    }

    public void enableLowAdmin() {
        disableLowAdmin = false;
    }

    public boolean lowAdminEnabled() {
        return !disableLowAdmin;
    }

    public final Date WATCHDOG = new Date();

    public int loopcounter = 0;

    public final ScriptBuffer afkScript = new ScriptBuffer("document.location.href = 'http://c0nflict.com/war/afk.html';");

    public void startTimer() {
        try {
            tableTimerWatchdog.cancel();
        } catch (Exception e) {
        }
        tableTimerWatchdog = new Timer();
        tableTimerWatchdog.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                if (tables == null) return;
                try {
                    WATCHDOG.setTime(new Date().getTime());
                    loopcounter++;
                    try {
                        if (loopcounter > 50) {
                            loopcounter = 0;
                            Collection<Player> afk = new HashSet<Player>();
                            for (Player p : tables.userSessions.keySet()) if (!p.checkActivity()) {
                                afk.add(p);
                            }
                            for (Player p : afk) {
                                p.getSession().addScript(afkScript);
                                tables.playerLeavesSilently(p);
                                tables.removeEntries(p, p.http);
                                p.getSession().invalidate();
                            }
                        }
                    } catch (Exception e) {
                        System.out.print("AFKERR:");
                        e.printStackTrace();
                    }
                    try {
                        Collection<Table> tableReset = new HashSet<Table>();
                        Collection<Table> tablesInPlay = tablePlayers.keySet();
                        Date current = new Date();
                        synchronized (tablesInPlay) {
                            for (Table t : tablesInPlay) {
                                if (current.getTime() - t.lastTimerTick.getTime() > 13000 && t.isRunning()) tableReset.add(t);
                            }
                        }
                        for (final Table t : tableReset) {
                            try {
                                tracker.srv.serverTurnOver(tracker.tables, t, t.getCurrentPlayerId());
                            } catch (Exception e) {
                                System.out.print("TABLETIMER SRVTURNOVER ERROR:");
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        System.out.print("TABLETIMER ERROR:");
                        e.printStackTrace();
                    }
                    try {
                        Collection<Table> removeStart = new HashSet<Table>();
                        for (Table finalTable : startGames.keySet()) if (new Date().getTime() - startGames.get(finalTable).getTime() >= GAME_START_AFTER) {
                            removeStart.add(finalTable);
                            int sitters = 0;
                            for (int x = 0; x < finalTable.getMaxPlayers(); x++) {
                                if (finalTable.players[x].isPlayer()) sitters++;
                            }
                            finalTable.tableIsStarting = false;
                            if (sitters > 2 && !finalTable.isRunning()) reset(finalTable, tracker.srv);
                        }
                        for (Table t : removeStart) startGames.remove(t);
                    } catch (Exception e) {
                        System.out.print("TABLESTART ERROR:");
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    System.out.print("TABLETIMER2 ERROR:");
                    e.printStackTrace();
                }
            }
        }, (250), 550);
    }

    public TableManager() {
    }

    public void loadObject(ReverseAjaxTracker caller) {
        if (db == null) db = new Database();
        loadTables();
        tracker = caller;
        startTimer();
    }

    public TableManager(ReverseAjaxTracker caller) {
        if (db == null) db = new Database();
        loadTables();
        tracker = caller;
        startTimer();
    }

    public void addEntries(Player player, ScriptSession session, HttpSession http) {
        if (player != null) {
            player.setSession(session);
            userSessions.put(player, session);
            sessionUsers.put(session, player);
            if (http != null) httpSessionUsers.put(http, player);
        }
    }

    public void putTableRating(int table_id, int rating) {
        for (Table t : this.spawnableTables) {
            if (t.getId() == table_id) {
                t.modifyRating(rating);
            }
        }
    }

    public void resetTableRatings() {
        for (Table t : this.spawnableTables) {
            t.clearRating();
        }
        for (Player p : playerTables.keySet()) {
            if (p.isNobody()) p.resetTableRatings();
        }
    }

    public void removeEntries(Player player, HttpSession http) {
        if (player == null) return;
        player.scriptToFriends(player.getFriendPoke(2), 2);
        if (player != null) {
            sessionUsers.remove(player.getSession());
            userSessions.remove(player);
        }
        if (http != null) httpSessionUsers.remove(http);
        player.friendList.clear();
        player.friendReqs.clear();
    }

    public void replaceSession(Player player, ScriptSession session, HttpSession http) {
        removeEntries(player, http);
        addEntries(player, session, http);
    }

    public CRandom R = new CRandom();

    public Player getPlayerByName(String name) {
        Collection<Player> ps = userSessions.keySet();
        for (Player p : ps) if (p.getName().equals(name)) return p;
        return null;
    }

    public Vector<String> battleNames = new Vector<String>();

    public void setPreferences(Player u, String prefs) {
        setELO(u, u.getELO(), prefs);
    }

    public void setELO(Player u, int elo, String preferences) {
        if (u.isAutomaton() && !u.isAdmin()) automatonElos.add(new Integer(elo));
        try {
            StringBuffer buf = new StringBuffer();
            buf.append("UPDATE dq_user set preferences='" + preferences + "',elo='").append(elo).append("' where user_id=").append(u.getDbId());
            dbConnection = db.getConnection();
            Statement stmt = (Statement) dbConnection.createStatement();
            int r = stmt.executeUpdate(buf.toString());
            if (r < 1) {
                buf = new StringBuffer();
                buf.append("INSERT INTO dq_user (user_id, elo, preferences, news_id) select ");
                buf.append(u.getDbId()).append(",").append(elo).append(",'").append(preferences);
                buf.append("', max(id) from dq_news;");
                stmt = (Statement) dbConnection.createStatement();
                stmt.executeUpdate(buf.toString());
            }
            stmt.close();
        } catch (SQLException e) {
        }
    }

    public Table getBestFitTable(Player u) {
        Table current = playerTables.get(u);
        if (current != null) {
            if (u.isPlayer()) {
                return current;
            } else playerLeavesSilently(u);
        }
        if (u == null || u.isBlocked()) {
            Collection<Table> tables = tablePlayers.keySet();
            Table newTable = null;
            for (Table t : tables) {
                if (t.isRunning()) return t; else {
                    if (tablePlayers.get(t).size() == 0) {
                        newTable = t;
                    }
                }
            }
            if (1 == 1) {
                if (newTable == null) newTable = spawnTable();
            } else newTable = tablePlayers.keySet().iterator().next();
            return newTable;
        }
        current = null;
        Table choose = null;
        Collection<Table> _tables = tablePlayers.keySet();
        Collection<Table> tables = Collections.synchronizedCollection(_tables);
        int totalcount = 0;
        int usedcount = 0;
        int elomin = getUserEloMin(u);
        synchronized (tablePlayers) {
            synchronized (tables) {
                for (Table b : tables) {
                    Collection<Player> bs = tablePlayers.get(b);
                    int bt = b.getMaxPlayers();
                    int sitting = 0;
                    for (Player p : bs) {
                        if (!p.isBlocked()) usedcount++;
                        if (p.isPlayer() || p.isNobody()) sitting++;
                    }
                    totalcount += bt;
                    if ((sitting < bt + 4) && !b.isRunning() && !b.isQuiet() && b.getEloMinimum() == elomin) choose = b; else current = b;
                }
            }
        }
        if (choose != null) return choose;
        int size = R.randomBaseZero(100);
        String sizeStr = "small";
        if (size < 55) sizeStr = "small"; else if (size < 85) sizeStr = "medium"; else sizeStr = "large";
        resetTableRatings();
        Table newTable = spawnTableBySize(sizeStr, false);
        newTable.setEloMinimum(elomin);
        return newTable;
    }

    public int getUserEloMin(Player p) {
        int ret = 0;
        for (int z = 0; z < ELO_LEVELS.length; z++) {
            if (p.getELO() >= ELO_LEVELS[z]) ret = ELO_LEVELS[z];
        }
        return ret;
    }

    public void playerLeaves(Player uplay) {
        Table table = playerTables.get(uplay);
        playerLeavesSilently(uplay);
        if (!uplay.isGameAdmin() && !uplay.isBlocked()) {
            ScriptBuffer s = new ScriptBuffer();
            StringBuffer buf = new StringBuffer();
            s.appendScript(buf.append("sendChat('").append(uplay.getName()).append(" has left.<br>');").toString());
            sendJavascript(table, s);
        }
        updateRoster(table, 1);
    }

    public void playerLeavesSilently(Player uplay) {
        Table table = playerTables.get(uplay);
        try {
            sessionTable.remove(uplay.getSession());
        } catch (Exception e) {
            e.printStackTrace();
        }
        playerTables.remove(uplay);
        if (table == null) return;
        try {
            Collection<Player> playersAtTable = tablePlayers.get(table);
            playersAtTable.remove(uplay);
        } catch (Exception e) {
            System.out.print("WTFE:");
            e.printStackTrace();
        }
        return;
    }

    public Table getTableByName(String tableName) {
        Collection<Table> table = tablePlayers.keySet();
        for (Table b : table) {
            if (b.getName().equals(tableName)) return b;
        }
        return singleTable;
    }

    public Table getTableByJSID(long tableName) {
        Collection<Table> table = tablePlayers.keySet();
        for (Table b : table) {
            if (b.getJSID() == (tableName)) return b;
        }
        return singleTable;
    }

    public String getTableNames() {
        StringBuffer buf = new StringBuffer();
        Collection<Table> tables = tablePlayers.keySet();
        boolean firstTime = true;
        TreeMap<String, Table> tmap = new TreeMap<String, Table>();
        for (Table b : tables) {
            tmap.put(b.getStatus() + "." + b.getEloMinimum() + "." + b.getJSID(), b);
        }
        Collection<Table> sorted = tmap.values();
        for (Table b : sorted) {
            if (!b.isQuiet()) {
                if (!firstTime) buf.append(",");
                firstTime = false;
                buf.append("'").append(b.getName()).append("',");
                Collection<Player> players = tablePlayers.get(b);
                int sitting = 0;
                int watching = 0;
                for (Player p : players) if (!p.isAutomaton() && !p.isBlocked()) if (p.getOrderId() >= 0) sitting++; else watching++;
                buf.append("'").append(sitting).append("/").append(watching).append("',");
                buf.append(b.getJSID()).append(",");
                buf.append(b.getStatus());
                buf.append(",'").append(b.getParams()).append("'");
                buf.append(",").append(b.getEloMinimum());
            }
        }
        return buf.toString();
    }

    public void killTable(Table table) {
        Collection<Player> players = null;
        try {
            players = tablePlayers.get(table);
        } catch (Exception e) {
            return;
        }
        tablePlayers.remove(table);
        for (Player p : players) {
            playerTables.remove(p);
            sessionTable.remove(p.getSession());
        }
        ScriptBuffer s = new ScriptBuffer();
        s.appendScript("Tracker.loadEvent()");
        for (Player p : players) {
            p.getSession().addScript(s);
        }
    }

    public void playerSwitchesTables(Player uplay, long newJSID) {
        if (uplay == null) return;
        Table oldTable = sessionTable.get(uplay.getSession());
        Table newTable = getTableByJSID(newJSID);
        if (newTable == null) newTable = getBestFitTable(uplay);
        if (oldTable == null) {
            if (uplay.getColor() <= 0) {
                playerJoins(uplay, newTable, 1);
                clearInactiveTables();
                return;
            }
        }
        for (int x = 0; x < oldTable.getMaxPlayers(); x++) {
            if (oldTable.players[x] == uplay || uplay.getOrderId() > 0) {
                ScriptBuffer s = new ScriptBuffer();
                s.appendScript("sendStatus('You may not leave this table while sitting.<br>');");
                uplay.getSession().addScript(s);
                return;
            }
        }
        playerLeaves(uplay);
        playerJoins(uplay, getTableByJSID(newJSID), 1);
        clearInactiveTables();
    }

    public ResultSet loginUserPass(String username, String password) throws SQLException {
        dbConnection = db.getConnection();
        Statement stmt = (Statement) dbConnection.createStatement();
        ResultSet rs = (ResultSet) stmt.executeQuery("SELECT username, user_avatar, user_avatar_type, jforum_users.user_id, coalesce(elo,0) as elo, coalesce(elo,-1) as has_played, " + " preferences, news_id, last_privmsg from jforum_users left join dq_user on dq_user.user_id=jforum_users.user_id where lower(user_email)=lower('" + username + "') and user_password=MD5('" + password + "');");
        return rs;
    }

    public ResultSet loginEmail(String email) throws SQLException {
        dbConnection = db.getConnection();
        Statement stmt = (Statement) dbConnection.createStatement();
        ResultSet rs = (ResultSet) stmt.executeQuery("SELECT username, user_avatar, user_avatar_type, jforum_users.user_id, coalesce(elo,0) as elo, preferences, news_id, last_privmsg " + "from jforum_users left join dq_user on dq_user.user_id=jforum_users.user_id where lower(user_email)=lower('" + email + "');");
        return rs;
    }

    public Table playerJoins(Player uplay, Table table, int existing) {
        uplay.setTable(table);
        Collection<Player> players = tablePlayers.get(table);
        Player drop = null;
        if (players == null) {
            killTable(table);
            return playerJoins(uplay, getBestFitTable(uplay), 1);
        }
        for (Player p : players) {
            if (p.getSession() == uplay.getSession()) drop = p;
        }
        if (drop != null) {
            playerTables.remove(drop);
            players.remove(drop);
        }
        sessionTable.put(uplay.getSession(), table);
        playerTables.put(uplay, table);
        tablePlayers.get(table).add(uplay);
        ScriptBuffer script = new ScriptBuffer();
        script.appendScript("prep_map(" + table.getWidth() + "," + table.getHeight() + ");");
        script.appendScript("draw_map('" + table.getName() + "'," + table.getJSID() + "," + uplay.getColor() + "," + (table.isRunning() ? 1 : 0) + ");");
        script.appendScript("rate_map(" + uplay.getTableRating(table.getJSID()) + ");");
        script.appendScript("push_tables(Array(" + getTableNames() + "));");
        script.appendScript("random_color(" + table.getColors() + "," + table.getDiceCounts() + ");");
        uplay.getSession().addScript(script);
        updateRoster(table, 1);
        if (existing != 0 && !uplay.isGameAdmin() && !uplay.isBlocked()) {
            ScriptBuffer s = new ScriptBuffer();
            s.appendScript("sendChat('" + uplay.getName() + " has entered.<br>');");
            sendJavascript(table, s);
        }
        return table;
    }

    public Table newPlayerJoins(Player uplay) {
        return newPlayerJoins(uplay, null);
    }

    public Table newPlayerJoins(Player uplay, Table table) {
        ScriptBuffer sp = new ScriptBuffer();
        sp.appendScript("set_play_now(false);");
        sp.appendScript("prefs_parse('" + uplay.getPreferences() + "');");
        if (uplay.getDbAdminLevel() > 10 && !"1".equals(uplay.getSession().getAttribute("ADMIN"))) {
            uplay.getSession().setAttribute("ADMIN", "1");
            String menu_item = "<a href=\"admin/index.jsp\" target=\"_new\">Admin</a>";
            sp.appendScript("add_menu_item('menu_game', '" + menu_item + "');");
            sp.appendScript("add_menu_tm('<hr/>');");
            menu_item = "<a href=\"#chair\" onclick=\"ChatMessage(\\\'!:chair:XXR\\\');\" target=\"_self\">Sit/unsit player</a>";
            sp.appendScript("add_menu_tm('" + menu_item + "');");
            menu_item = "<a href=\"#reload\" onclick=\"ChatMessage(\\\'!:reload:XXR\\\');\" target=\"_self\">Force reload</a>";
            sp.appendScript("add_menu_tm('" + menu_item + "');");
            menu_item = "<a href=\"#refresh\" onclick=\"ChatMessage(\\\'!:refresh:XXR\\\');\" target=\"_self\">Force refresh</a>";
            sp.appendScript("add_menu_tm('" + menu_item + "');");
            menu_item = "<a href=\"#ping\" onclick=\"ChatMessage(\\\'!:ping:XXR\\\');\" target=\"_self\">Ping player</a>";
            sp.appendScript("add_menu_tm('" + menu_item + "');");
            menu_item = "<a href=\"#smute\" onclick=\"ChatMessage(\\\'!:smute:XXR\\\');\" target=\"_self\">System mute</a>";
            sp.appendScript("add_menu_tm('" + menu_item + "');");
            menu_item = "<a href=\"#killplayer\" onclick=\"ChatMessage(\\\'!:pdn:XXR\\\');\" target=\"_self\">Kill player</a>";
            sp.appendScript("add_menu_tm('" + menu_item + "');");
        }
        uplay.getSession().addScript(sp);
        Table bestfit = null;
        if (table == null) bestfit = getBestFitTable(uplay); else bestfit = table;
        uplay.loadFriendLists();
        uplay.loadTableRatings();
        uplay.setNews();
        this.pushMessages(uplay);
        uplay.setInfo();
        return playerJoins(uplay, bestfit, 1);
    }

    public void checkRegistration(ScriptSession session, String username, String password, String email) throws SQLException {
        if (username.contains("\'") || username.contains("\"") || username.contains("=") || username.contains(";")) return;
        if (password.contains("\'") || password.contains("\"") || password.contains("=") || password.contains(";")) return;
        if (email.contains("\'") || email.contains("\"") || email.contains("=") || email.contains(";")) return;
        username = escapeHTML(username);
        int usermatch = 0;
        int emailmatch = 0;
        int passmatch = 0;
        int incre = 0;
        ScriptBuffer s = new ScriptBuffer();
        dbConnection = db.getConnection();
        Statement stmt = (Statement) dbConnection.createStatement();
        ResultSet rs = (ResultSet) stmt.executeQuery("SELECT user_id,username,user_email,user_password FROM jforum_users where lower(user_email)=lower('" + email + "') or lower(username)=lower('" + username + "');");
        while (rs.next()) {
            if (++incre > 1) return;
            if (rs.getString("username").equals(username)) usermatch = 1;
            if (rs.getString("user_email").equals(email)) emailmatch = 1;
            if (rs.getString("user_password").equals(R.MD5(password))) passmatch = 1;
            if (usermatch == 1 && emailmatch == 1 && passmatch == 0) {
                s.appendScript("regfailed('You are already registered, incorrect password.');");
                session.addScript(s);
                stmt.close();
                return;
            } else if (usermatch == 1 && emailmatch == 0) {
                s.appendScript("regfailed('Username in use already.');");
                session.addScript(s);
                stmt.close();
                return;
            } else if (usermatch == 0 && emailmatch == 1) {
                s.appendScript("regfailed('Email previously registered');");
                session.addScript(s);
                stmt.close();
                return;
            } else if (usermatch + emailmatch + passmatch == 3) {
                s.appendScript("hidereg();");
                s.appendScript("Tracker.loginEvent('" + email + "','" + password + "');");
                session.addScript(s);
                stmt.close();
                return;
            }
        }
        if (usermatch + emailmatch == 0) {
            stmt.execute("INSERT INTO jforum_users (username,user_email,user_password,user_regdate) SELECT '" + username + "','" + email + "',md5('" + password + "'),current_timestamp;");
            s.appendScript("hidereg();");
            s.appendScript("gadc();");
            s.appendScript("Tracker.loginEvent('" + email + "','" + password + "');");
            session.addScript(s);
        }
        stmt.close();
    }

    public String singleValueSql(String sql) {
        String value = null;
        try {
            dbConnection = db.getConnection();
            Statement stmt = (Statement) dbConnection.createStatement();
            ResultSet rs = (ResultSet) stmt.executeQuery(sql);
            if (rs.next()) {
                value = rs.getString(1);
                rs.close();
            } else value = "";
            stmt.close();
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
        return value;
    }

    public int updateSQL(String sql) {
        int rs = -1;
        try {
            dbConnection = db.getConnection();
            Statement stmt = (Statement) dbConnection.createStatement();
            rs = stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    public Vector<Table> spawnableTables = new Vector<Table>();

    public void loadTables() {
        try {
            dbConnection = db.getConnection();
            Statement stmt = (Statement) dbConnection.createStatement();
            ResultSet rs = (ResultSet) stmt.executeQuery("select * from dq_tables_view order by table_id ASC;");
            String tableLayout = null;
            while (rs.next()) {
                try {
                    tableLayout = rs.getString("layout");
                } catch (Exception e) {
                }
                Table newTable = new Table(rs.getString("tablename"), rs.getInt("width"), rs.getInt("height"), rs.getInt("starting_dice"), rs.getDouble("black_percent"), rs.getInt("starting_lands"), rs.getInt("max_dice_per"), rs.getInt("table_id"), rs.getDouble("movable_land"), rs.getDouble("movable_dice"), rs.getInt("partialvictory"));
                if (tableLayout != null && !tableLayout.trim().equals("") && !"null".equals(tableLayout)) {
                    newTable.setLayout(tableLayout);
                    newTable.setAuthor(rs.getString("author"));
                    newTable.setAuthorName(rs.getString("username"));
                }
                spawnableTables.add(newTable);
            }
            rs = (ResultSet) stmt.executeQuery("select * from dq_battles;");
            while (rs.next()) battleNames.add(rs.getString("name"));
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int minTableCount = 4;

    public void setMinTableCount(int min) {
        minTableCount = min;
    }

    public void clearInactiveTables() {
        if (tablePlayers.keySet().size() * 5 <= sessionUsers.keySet().size()) return;
        Collection<Table> killTables = new HashSet<Table>();
        Collection<Table> allTables = tablePlayers.keySet();
        if (allTables.size() <= minTableCount) return;
        int weHaveOpen = 1;
        for (Table t : allTables) {
            Collection<Player> players = tablePlayers.get(t);
            if (t.isQuiet() && t.getStatus() == 1 && t.getCurrentTurn() > 1) killTables.add(t); else if (players.size() == 0) killTables.add(t);
            if (players.size() > 0 && t.isNotRunning() && !t.isQuiet()) weHaveOpen = 0;
        }
        int[] elominopen = new int[ELO_LEVELS.length];
        for (int z = 0; z < elominopen.length; z++) {
            elominopen[z] = 0;
        }
        Iterator<Table> tableit = killTables.iterator();
        for (int x = 0; x < killTables.size(); x++) {
            Table t = tableit.next();
            if (weHaveOpen > 0 && !t.isQuiet()) {
                weHaveOpen--;
            } else {
                if (t.getEloMinimum() == 0 || elominopen[idxOfElo(t.getEloMinimum())] > 0) killTable(t); else elominopen[idxOfElo(t.getEloMinimum())]++;
            }
        }
        if (killTables.size() > 0) {
            ScriptBuffer s = new ScriptBuffer();
            s.appendScript("push_tables(Array(" + getTableNames() + "));");
            sendJavascript(null, s);
        }
    }

    private int idxOfElo(int elo) {
        for (int z = 0; z < ELO_LEVELS.length; z++) {
            if (ELO_LEVELS[z] == elo) return z;
        }
        return 0;
    }

    private long tableSpawnedCount = 0;

    public Table spawnTable() {
        int selector = R.randomBaseZero(spawnableTables.size());
        return spawnTable(selector);
    }

    public Table spawnTableById(int id) {
        return spawnTableById(id, true);
    }

    public Table spawnTableById(int id, boolean quiet) {
        for (int z = 0; z < spawnableTables.size(); z++) {
            if (spawnableTables.get(z).getId() == id) return _spawnTable(z, quiet);
        }
        return null;
    }

    public Table spawnTableBySize(String size) {
        return spawnTableBySize(size, true);
    }

    public Table spawnTableBySize(String size, boolean quiet) {
        int area = 0;
        int area_top = 70;
        if (size.equals("large")) {
            area = 100;
            area_top = 999;
        } else if (size.equals("medium")) {
            area = 60;
            area_top = 100;
        }
        int z = 0;
        int zcount = 0;
        while (zcount < 5000) {
            zcount++;
            z = R.randomBaseZero(spawnableTables.size());
            if (spawnableTables.get(z).getRatingEstimate() > -3 && spawnableTables.get(z).getTableSize() > area && spawnableTables.get(z).getTableSize() < area_top) return _spawnTable(z, quiet);
        }
        return null;
    }

    public Table spawnTable(int selector) {
        return _spawnTable(selector, false);
    }

    public Table _spawnTable(int selector, boolean quiet) {
        tableSpawnedCount++;
        Table newTable = null;
        try {
            newTable = new Table(spawnableTables.get(selector));
            newTable.setQuiet(quiet);
            newTable.setAuthor(spawnableTables.get(selector).getAuthor());
            newTable.setAuthorName(spawnableTables.get(selector).getAuthorName());
            if (newTable.getAuthorName() == "") {
                int r2 = R.randomBaseZero(battleNames.size() - 1);
                newTable.setName(battleNames.get(r2));
            }
            if (tableSpawnedCount > 900000000) tableSpawnedCount = 0;
            newTable.setJSID(tableSpawnedCount);
            newTable.startLogger();
            newTable.reset(this);
            newTable.stop();
            for (int x = 0; x < newTable.getMaxPlayers(); x++) playerTables.put(newTable.tableSeats[x], newTable);
            Set<Player> __EmptyPlayers = new HashSet<Player>();
            Collection<Player> EmptyPlayers = Collections.synchronizedSet(__EmptyPlayers);
            tablePlayers.put(newTable, EmptyPlayers);
            if (!quiet) {
                ScriptBuffer script = new ScriptBuffer();
                script.appendScript("push_tables(Array(" + getTableNames() + "));");
                sendJavascript(null, script);
            }
            return newTable;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Table getTable(Player user) {
        return playerTables.get(user);
    }

    public Table getTable(ScriptSession ses) {
        Table t = sessionTable.get(ses);
        if (t == null) t = getBestFitTable(null);
        return t;
    }

    private Collection<Player> deadPlayers = new HashSet<Player>();

    public void sendJavascript(Table whichBoard, String script) {
        ScriptBuffer s = new ScriptBuffer(script);
        sendJavascript(whichBoard, s);
    }

    @SuppressWarnings("unchecked")
    public void sendJavascript(Table whichBoard, ScriptBuffer script) {
        try {
            int colsize = 0;
            if (whichBoard != null) {
                Collection<Player> PlayersAt = Collections.synchronizedSet((Set<Player>) tablePlayers.get(whichBoard));
                if (PlayersAt == null) {
                    System.out.println("WTFE: non existent table being sent messages");
                    killTable(whichBoard);
                    return;
                }
                for (Player thisu : PlayersAt) {
                    if (thisu.getSession().isInvalidated() && !(thisu.getColor() > 0)) {
                        deadPlayers.add(thisu);
                    } else {
                        try {
                            thisu.getSession().addScript(script);
                        } catch (Exception e) {
                            deadPlayers.add(thisu);
                        }
                        colsize++;
                    }
                }
            } else {
                Collection<ScriptSession> sessions = new HashSet<ScriptSession>();
                sessions.addAll(tracker.sctx.getAllScriptSessions());
                for (ScriptSession insys : sessions) {
                    Player t = sessionUsers.get(insys);
                    if (insys.isInvalidated()) {
                        if (t != null && t.getColor() <= 0) deadPlayers.add(t);
                    } else try {
                        insys.addScript(script);
                    } catch (Exception e) {
                        deadPlayers.add(t);
                    }
                }
            }
            for (Player p : deadPlayers) {
                try {
                    this.removeEntries(p, p.http);
                } catch (Exception e) {
                    System.out.print("SESREMERROR: ");
                    e.printStackTrace();
                }
                try {
                    allChat.remove(p);
                    playerLeavesSilently(p);
                    p.getSession().invalidate();
                } catch (Exception e) {
                    System.out.print("WTFE:");
                    e.printStackTrace();
                }
            }
        } finally {
            deadPlayers.clear();
        }
    }

    public void clearRoster(Table table) {
        table.clearSeats();
        Collection<Player> users = (Collection<Player>) tablePlayers.get(table);
        for (Player player : users) player.setOrderId(-1);
        updateRoster(table, 0);
    }

    public void updateRoster(Player u) {
        updateRoster(getTable(u), 0);
    }

    public void updateRoster(Table table, int change) {
        String[] ros = { "", "", "", "", "", "", "" };
        int sitting = 0;
        int watching = 0;
        StringBuffer rosterHTML = new StringBuffer("Array(");
        boolean firstTime = true;
        Collection<Player> users = (Collection<Player>) tablePlayers.get(table);
        for (Player player : users) if (!player.isBlocked()) {
            if (!player.isAutomaton()) if (player.getOrderId() >= 0) sitting++; else watching++;
            if (player.getOrderId() >= 0) ros[player.getOrderId()] = "'" + player.getAvatarHtml() + player.getNameRoster() + "'," + player.getELO(); else {
                if (!firstTime) rosterHTML.append(",");
                firstTime = false;
                rosterHTML.append("'").append(player.getNameRoster()).append("'");
            }
        }
        rosterHTML.append(")");
        StringBuffer VIPList = new StringBuffer("Array(");
        firstTime = true;
        for (int x = 0; x < table.getMaxPlayers(); x++) {
            if (!firstTime) VIPList.append(",");
            firstTime = false;
            if (ros[x].length() > 0) VIPList.append(ros[x]); else VIPList.append("\'\',\'\'");
        }
        VIPList.append(")");
        StringBuffer script = new StringBuffer("push_roster(");
        script.append(VIPList);
        script.append(", ");
        script.append(rosterHTML);
        script.append(");");
        ScriptBuffer scriptb = new ScriptBuffer();
        scriptb.appendScript(script.toString());
        sendJavascript(table, scriptb);
        if (change != 0) {
            ScriptBuffer scriptc = new ScriptBuffer();
            scriptc.appendScript("change_tc(" + table.getJSID() + ",'" + sitting + "/" + watching + "');");
            sendJavascript(null, scriptc);
        }
    }

    public void broadcastAllChat(ScriptBuffer s) {
        Collection<Player> allRemove = new HashSet<Player>();
        for (Player p : allChat) {
            if (p.getSession().isInvalidated()) {
                allRemove.add(p);
            } else if (!s.toString().contains("/" + p.getName() + "/")) {
                p.getSession().addScript(s);
            }
        }
        for (Player p : allRemove) {
            allChat.remove(p);
        }
    }

    public void endGame(Table table) {
        ScriptBuffer s = new ScriptBuffer();
        if (table.isQuiet()) {
            s.appendScript("ChatMessage('/creategame');");
            sendJavascript(table, s);
            Collection<Player> watchersOnQuiet = tablePlayers.get(table);
            for (Player p : watchersOnQuiet) {
                if (p.getDbId() <= 0) {
                    p.setBlocked();
                }
            }
            return;
        }
        s.appendScript("try { dwr.util.byId('table" + table.getJSID() + "').style.color='#ccc'; } catch (err) {} ");
        sendJavascript(null, s);
    }

    public void startGame(Table table, ServerAction srv) {
        if (!table.isQuiet()) {
            ScriptBuffer s = new ScriptBuffer();
            s.appendScript("try { dwr.util.byId('table" + table.getJSID() + "').style.color='#555'; } catch (err) {} ");
            sendJavascript(null, s);
        }
        table.resetClock();
        ScriptBuffer script = new ScriptBuffer();
        script.appendScript("gameStart();");
        script.appendScript("turnChange(" + table.getJSID() + "," + table.getCurrentTurn() + "," + (table.getCurrentPlayerId() + 1) + ");");
        sendJavascript(table, script);
    }

    public void reset(Table table, ServerAction srv) {
        table.reset(this);
        table.start();
        ScriptBuffer script = new ScriptBuffer();
        script.appendScript("prep_map(" + table.getWidth() + "," + table.getHeight() + ");");
        script.appendScript("draw_map('" + table.getName() + "'," + table.getJSID() + ",-1," + (table.isRunning() ? 1 : 0) + ");");
        script.appendScript("random_color(" + table.getColors() + "," + table.getDiceCounts() + ");");
        sendJavascript(table, script);
        startGame(table, srv);
    }

    int botindex = 0;

    public Player sitAutomaton(Table t) {
        if (t.getEloMinimum() != 0) return null;
        botindex++;
        String avatarStr = "//c0nflict.com/av/voltron.jpg";
        String namestr = "voltron";
        if (botindex % 16 == 0) namestr = "data"; else if (botindex % 15 == 0) namestr = "johnny5"; else if (botindex % 14 == 0) namestr = "erasmus"; else if (botindex % 13 == 0) {
            namestr = "bender";
            avatarStr = "//c0nflict.com/av/bender.gif";
        } else if (botindex % 12 == 0) {
            namestr = "ironhide";
            avatarStr = "//c0nflict.com/av/autobot.gif";
        } else if (botindex % 11 == 0) {
            namestr = "soundwave";
            avatarStr = "//c0nflict.com/av/decepticon.jpg";
        } else if (botindex % 10 == 0) {
            namestr = "megatron";
            avatarStr = "//c0nflict.com/av/decepticon.jpg";
        } else if (botindex % 9 == 0) {
            namestr = "bender";
            avatarStr = "//c0nflict.com/av/bender.gif";
        } else if (botindex % 8 == 0) {
            namestr = "optimus";
            avatarStr = "//c0nflict.com/av/autobot.gif";
        } else if (botindex % 7 == 0) {
            namestr = "c3p0";
            avatarStr = "//c0nflict.com/av/c3p0.jpg";
        } else if (botindex % 6 == 0) {
            namestr = "r2d2";
            avatarStr = "//c0nflict.com/av/r2d2.jpg";
        } else if (botindex % 5 == 0) {
            namestr = "bender";
            avatarStr = "//c0nflict.com/av/bender.gif";
        } else if (botindex % 4 == 0) namestr = "locutus"; else if (botindex % 3 == 0) namestr = "johnny5"; else if (botindex % 2 == 0) {
            namestr = "HAL";
            avatarStr = "//c0nflict.com/av/carebear.jpg";
        }
        Player robot = new Player(namestr, this);
        robot.setAvatar(avatarStr);
        robot.setAutomaton();
        robot.setPersonality(R.randomBaseOne(3));
        robot.setTable(t);
        this.sessionUsers.put(robot.getSession(), robot);
        this.userSessions.put(robot, robot.getSession());
        this.playerJoins(robot, t, 1);
        tracker.srv.serverChairEvent(this, robot);
        tracker.game_start(1, robot.getSession());
        return robot;
    }

    public void destroyOneAutomaton(Table t) {
        Player robot = null;
        Collection<Player> tableList = tablePlayers.get(t);
        for (Player p : tableList) {
            if (p.isAutomaton() && !p.isAdmin()) {
                robot = p;
                break;
            }
        }
        if (robot != null) {
            if (robot.isPlayer()) tracker.srv.serverChairEvent(this, robot);
            if (!robot.isPlayer()) {
                playerLeaves(robot);
                removeEntries(robot, robot.getHttpSession());
            }
        }
    }

    public void destroyAutomatons(Table t) {
        Stack<Player> robots = new Stack<Player>();
        Collection<Player> tableList = tablePlayers.get(t);
        for (Player p : tableList) {
            if (p.isAutomaton() && !p.isAdmin()) robots.add(p);
        }
        while (robots.size() > 0) {
            Player p = robots.pop();
            if (p.isPlayer()) tracker.srv.serverChairEvent(this, p);
            if (!p.isPlayer()) {
                playerLeaves(p);
                removeEntries(p, p.getHttpSession());
            }
        }
    }

    public void shutdownAll() {
        Collection<Table> tables = tablePlayers.keySet();
        for (Table b : tables) b.shutdown();
        ScriptBuffer script = new ScriptBuffer();
        script.appendScript("sendStatus('<b>Server restart in progress. Finish your game quickly.  Once the restart is complete, a refresh should bring the game back.</b><br>');");
        sendJavascript(null, script);
    }

    public void addFriendReq(Player from, String to) {
        String userto = singleValueSql("SELECT user_id from users where username='" + to + "';");
        if (userto == null || userto.equals("")) return;
        String usertofriendreqs = singleValueSql("SELECT friendreqs from dq_user where user_id=" + userto + ";");
        if (usertofriendreqs == null) usertofriendreqs = "";
        if (usertofriendreqs.contains(":" + from.getDbId() + ":")) return;
        try {
            updateSQL("update dq_user set friendreqs = CONCAT(COALESCE(friendreqs,':'),'" + from.getDbId() + "',':') where user_id=" + userto + ";");
        } catch (Exception e) {
        }
    }

    public void addFriend(Player from, String to) {
        String userto = singleValueSql("SELECT user_id from users where username='" + to + "';");
        if (userto == null) return;
        String usertofriend = singleValueSql("SELECT friends from dq_user where user_id=" + from.getDbId() + ";");
        if (usertofriend == null) usertofriend = "";
        if (usertofriend.contains(":" + userto + ":")) return;
        updateSQL("update dq_user set friends = CONCAT(COALESCE(friends,':'),'','" + userto + "',':') where user_id=" + from.getDbId() + ";");
        updateSQL("update dq_user set friendreqs = REPLACE(friendreqs,':" + userto + ":',':') where user_id=" + from.getDbId() + ";");
    }

    public static TableManager tables = new TableManager();

    public int getLastInsertId() {
        String query = "select last_insert_id() as id;";
        return new Integer(tables.singleValueSql(query)).intValue();
    }

    public void pushMessages(Player p) {
        StringBuffer SQL = new StringBuffer("SELECT * from dq_messages where privmsgs_to_userid = " + p.getDbId());
        if (p.lastMessageRead != -1) SQL.append(" and privmsgs_id>" + p.lastMessageRead);
        SQL.append(" order by privmsgs_id asc;");
        try {
            dbConnection = db.getConnection();
            Statement stmt = (Statement) dbConnection.createStatement();
            int lastid = -1;
            ResultSet rs = (ResultSet) stmt.executeQuery(SQL.toString());
            while (rs.next()) {
                String from = rs.getString("username");
                String at = rs.getString("privmsgs_date");
                String message = rs.getString("privmsgs_text");
                message = message.replace("'", "`");
                message = message.replace("\n", "");
                message = message.replace("\r", "");
                lastid = rs.getInt("privmsgs_id");
                ScriptBuffer s = new ScriptBuffer();
                s.appendScript("sendChat('<font class=\"privmsg\"><i>" + from + "/" + at + "</i>: " + message + "</font><br/>');");
                p.getSession().addScript(s);
            }
            if (lastid > 0 && lastid != p.lastMessageRead) updateSQL("update dq_user set last_privmsg = " + lastid + " where user_id = " + p.getDbId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static final String escapeHTML(String s) {
        StringBuffer sb = new StringBuffer();
        int returns = 0;
        int n = s.length();
        char lastc = ' ';
        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            switch(c) {
                case '\\':
                    sb.append("&#92;");
                    break;
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                case '&':
                    sb.append("&amp;");
                    break;
                case '\'':
                    sb.append("&rsquo;");
                    break;
                case '"':
                    sb.append("&quot;");
                    break;
                case '\r':
                    break;
                case '\n':
                    sb.append("<br>");
                    returns++;
                    if (returns > 2) return sb.toString();
                    break;
                case ' ':
                    if (lastc == ' ') break; else {
                        sb.append(" ");
                        break;
                    }
                default:
                    sb.append(c);
                    break;
            }
            lastc = c;
        }
        return sb.toString();
    }

    public void saveMessage(Player from, String to, String message) {
        message = message.replace("'", "");
        message = message.replace(";", ",");
        message = message.replace("\n", "");
        message = message.replace("\r", "");
        String toDbId = this.singleValueSql("SELECT user_id from users where username='" + to + "';");
        updateSQL("insert into jforum_privmsgs (privmsgs_type,privmsgs_subject,privmsgs_from_userid,privmsgs_to_userid,privmsgs_date) select 2,'In Game Message'," + from.getDbId() + "," + toDbId + ",now();");
        int messageId = getLastInsertId();
        updateSQL("insert into jforum_privmsgs_text (privmsgs_id, privmsgs_text) select " + messageId + ",'" + message + "';");
    }
}
