package database;

import gameServer.GameTracker;
import gameServer.RoundCounter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import shared.PlayerInfo;

public class DataBaseManager {

    private static DataBaseManager instance = null;

    private String dbName = "makaoDb";

    private String framework = "derbyclient";

    private String driver = "org.apache.derby.jdbc.ClientDriver";

    private String protocol = "jdbc:derby://localhost:1527/";

    private Connection conn = null;

    private PreparedStatement psInsert = null;

    private PreparedStatement psUpdate = null;

    private Statement s = null;

    private ResultSet rs = null;

    private DataBaseManager() {
        loadDriver();
    }

    public static DataBaseManager getInstance() {
        if (instance == null) instance = new DataBaseManager();
        return instance;
    }

    private void loadDriver() {
        try {
            Class.forName(driver).newInstance();
            System.out.println("Loaded the appropriate driver");
        } catch (ClassNotFoundException cnfe) {
            System.err.println("\nUnable to load the JDBC driver " + driver);
            System.err.println("Please check your CLASSPATH.");
            cnfe.printStackTrace(System.err);
        } catch (InstantiationException ie) {
            System.err.println("\nUnable to instantiate the JDBC driver " + driver);
            ie.printStackTrace(System.err);
        } catch (IllegalAccessException iae) {
            System.err.println("\nNot allowed to access the JDBC driver " + driver);
            iae.printStackTrace(System.err);
        }
    }

    public void openConnection() throws SQLException {
        conn = null;
        conn = DriverManager.getConnection(protocol + dbName + ";create=true");
        conn.setAutoCommit(false);
    }

    public void closeConnection() throws SQLException {
        conn.close();
    }

    public void uninstallDataBase() throws SQLException {
        if (conn != null) {
            if (!conn.isClosed()) try {
                s = conn.createStatement();
                s.execute("Drop table playerGameInfo");
                s.execute("Drop table games");
                s.execute("Drop table players");
                conn.commit();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void installDataBase() throws SQLException {
        if (conn != null) {
            if (!conn.isClosed()) try {
                s = conn.createStatement();
                s.execute("CREATE TABLE games(id int NOT NULL GENERATED ALWAYS AS IDENTITY CONSTRAINT games_pk PRIMARY KEY, startTime timestamp, endTime timestamp, winner varchar(8), playersCount int)");
                s.execute("CREATE TABLE players(id int NOT NULL GENERATED ALWAYS AS IDENTITY CONSTRAINT players_pk PRIMARY KEY, name varchar(8), password varchar(10), wins int, fails int, level int, UNIQUE(name))");
                s.execute("CREATE TABLE playerGameInfo(id int NOT NULL GENERATED ALWAYS AS IDENTITY CONSTRAINT pgi_pk PRIMARY KEY, games_id int, players_id int, cardNum int,FOREIGN KEY(games_id) REFERENCES games(id),FOREIGN KEY(players_id) REFERENCES players(id))");
                conn.commit();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void register(String user, String password) throws SQLException {
        if (conn != null) if (!conn.isClosed()) {
            psInsert = conn.prepareStatement("INSERT INTO players (name,password,wins,fails,level) VALUES (?,?,?,?,?)");
            psInsert.setString(1, user);
            psInsert.setString(2, password);
            psInsert.setInt(3, 0);
            psInsert.setInt(4, 0);
            psInsert.setInt(5, 0);
            psInsert.executeUpdate();
            conn.commit();
        }
    }

    public boolean login(String user, String password) throws SQLException {
        if (conn != null) if (!conn.isClosed()) {
            psInsert = conn.prepareStatement("SELECT password FROM players WHERE name = ?");
            psInsert.setString(1, user);
            rs = psInsert.executeQuery();
            if (rs.next()) {
                if (rs.getString(1).equals(password)) return true;
            }
        }
        return false;
    }

    public void unregister(String user, String password) {
    }

    public void storeGameState(GameTracker tracker) throws SQLException {
        if (conn != null) if (!conn.isClosed()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            String sdate = dateFormat.format(tracker.getStartTime());
            String stime = timeFormat.format(tracker.getStartTime());
            String edate = dateFormat.format(tracker.getEndTime());
            String etime = timeFormat.format(tracker.getEndTime());
            PlayerInfo[] players = tracker.getPlayersInfo();
            int gameid = 0;
            if (players != null) {
                psInsert = conn.prepareStatement("INSERT INTO games (startTime,endTime,playersCount) VALUES (?,?,?)");
                psInsert.setString(1, (sdate + " " + stime));
                psInsert.setString(2, (edate + " " + etime));
                psInsert.setInt(3, players.length);
                psInsert.executeUpdate();
                psInsert = conn.prepareStatement("SELECT id from games where (startTime = ?)");
                psInsert.setString(1, (sdate + " " + stime));
                rs = psInsert.executeQuery();
                if (rs.next()) {
                    gameid = rs.getInt(1);
                }
                for (PlayerInfo pi : players) {
                    updatePlayerRecord(pi);
                    int playerId = 0;
                    int num = pi.getCardNum();
                    String name = pi.getNickName();
                    if (num == 0) {
                        psInsert = conn.prepareStatement("UPDATE games SET winner = ? where (id = ?)");
                        psInsert.setString(1, name);
                        psInsert.setInt(2, gameid);
                        psInsert.executeUpdate();
                    }
                    psInsert = conn.prepareStatement("SELECT id from players where (name = ?)");
                    psInsert.setString(1, name);
                    rs = psInsert.executeQuery();
                    if (rs.next()) {
                        playerId = rs.getInt(1);
                    }
                    psInsert = conn.prepareStatement("INSERT INTO playerGameInfo  (games_id,players_id,cardNum) VALUES (?,?,?)");
                    psInsert.setInt(1, gameid);
                    psInsert.setInt(2, playerId);
                    psInsert.setInt(3, num);
                    psInsert.executeUpdate();
                }
            }
            conn.commit();
        }
    }

    private void updatePlayerRecord(PlayerInfo player) throws SQLException {
        if (player.getCardNum() == 0) {
            psInsert = conn.prepareStatement("UPDATE players SET wins = wins+1 where (name = ?)");
        } else {
            psInsert = conn.prepareStatement("UPDATE players SET fails = fails+1 where (name = ?)");
        }
        psInsert.setString(1, player.getNickName());
        psInsert.executeUpdate();
        psInsert = conn.prepareStatement("UPDATE players SET level = wins/fails*10 where (name = ?) AND (wins > 0) AND (fails > 0)");
        psInsert.setString(1, player.getNickName());
        psInsert.executeUpdate();
    }

    public void customQuery(String query) {
    }
}
