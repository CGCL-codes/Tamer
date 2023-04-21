package de.uos.virtuos.virtpresenter.verwalter.tools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.LinkedList;
import de.uos.virtuos.virtpresenter.verwalter.data.Datenspeicher;

public class DatabaseConnection {

    Connection conn;

    boolean connected = false;

    String connectionString = null;

    PreparedStatement newJobStatement = null;

    public DatabaseConnection() {
        this(Datenspeicher.getConfig().getDatabaseConnectionString());
    }

    public DatabaseConnection(String connectionString) {
        super();
        this.connectionString = connectionString;
        if (!checkDatabase()) initDatabase();
    }

    public boolean connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception e) {
            System.err.println("Datenbank-Treiber konnte nicht geladen werden");
            e.printStackTrace();
            return connected = false;
        }
        try {
            conn = DriverManager.getConnection(connectionString);
        } catch (SQLException e) {
            System.err.println("Konnte nicht mit Datenbank verbinden");
            e.printStackTrace();
            return connected = false;
        }
        return connected = true;
    }

    public void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            System.err.println("Verbindung zur Datenbank konnte nicht beendet werden");
            e.printStackTrace();
        }
        connected = false;
    }

    public Statement getStatement() {
        if (!connected) connect();
        try {
            return conn.createStatement();
        } catch (SQLException e) {
            return null;
        }
    }

    public boolean statusSpeichern(String jobId, int statusId, int serverId, String message) {
        return statusSpeichern(jobId, statusId, serverId, 0, message);
    }

    public boolean statusSpeichern(String jobId, int statusId, int serverId, int codecId, String message) {
        if (!isConnected()) if (!connect()) return false;
        Statement stmnt = getStatement();
        if (stmnt == null) return false;
        String command = "INSERT INTO `status` (job_id, status_id, server_id, codec_id, zeit";
        String values = ") VALUES ('" + jobId + "', '" + statusId + "', '" + serverId + "', '" + codecId + "', '" + (new Timestamp(System.currentTimeMillis())).toString() + "'";
        if (message != null) {
            command += ", message";
            values += ", '" + message + "'";
        }
        String query = command + values + ")";
        try {
            stmnt.executeUpdate(query);
        } catch (SQLException e) {
            System.err.println("Fehler beim Status speichern: Job id = " + jobId);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean updateStatus(int actionId, int statusId, String message) {
        if (!isConnected()) if (!connect()) return false;
        Statement stmnt = getStatement();
        if (stmnt == null) return false;
        String query = "UPDATE `status` SET `zeit` = NOW() ";
        if (statusId >= 0) query += ", `status_id` = '" + statusId + "' ";
        if (message != null) query += ", `message` = '" + message + "' ";
        query += " WHERE `action_id` = '" + actionId + "'";
        try {
            stmnt.executeUpdate(query);
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public int readActionId(String jobId, int serverId) {
        return readActionId(jobId, serverId, 0);
    }

    public int readActionId(String jobId, int serverId, int codecId) {
        if (!isConnected()) if (!connect()) return 0;
        Statement stmnt = getStatement();
        if (stmnt == null) return 0;
        String query = "SELECT action_id FROM status WHERE job_id = '" + jobId + "' AND server_id = '" + serverId + "' AND codec_id = '" + codecId + "'";
        try {
            ResultSet result = stmnt.executeQuery(query);
            if (!result.next()) return 0;
            return result.getInt("action_id");
        } catch (SQLException e) {
            return 0;
        }
    }

    public boolean isConnected() {
        try {
            if (conn == null) return connected;
            return (!conn.isClosed());
        } catch (SQLException e) {
            connected = false;
            return false;
        }
    }

    public boolean jobIsInDB(String jobId) {
        if (!isConnected()) if (!connect()) return false;
        Statement stmnt = getStatement();
        if (stmnt == null) return false;
        String query = "SELECT count( job_id ) AS number FROM jobs WHERE job_id = '" + jobId + "'";
        try {
            ResultSet result = stmnt.executeQuery(query);
            if (!result.next()) return false;
            if (result.getInt("number") != 0) return true;
            return false;
        } catch (SQLException e) {
            return false;
        }
    }

    public String readStatuscodeText(int code) {
        if (!isConnected()) if (!connect()) return null;
        Statement stmnt = getStatement();
        if (stmnt == null) return null;
        String query = "SELECT bedeutung FROM statuscodes WHERE status_id = '" + code + "'";
        try {
            ResultSet result = stmnt.executeQuery(query);
            if (!result.next()) return null;
            return result.getString("bedeutung");
        } catch (SQLException e) {
            return null;
        }
    }

    public boolean readStatuscodeSuccess(int code) {
        if (!isConnected()) if (!connect()) return false;
        Statement stmnt = getStatement();
        if (stmnt == null) return false;
        String query = "SELECT erfolg FROM statuscodes WHERE status_id = '" + code + "'";
        try {
            ResultSet result = stmnt.executeQuery(query);
            if (!result.next()) return false;
            return (result.getInt("erfolg") == 1);
        } catch (SQLException e) {
            return false;
        }
    }

    public String[] getAllSeminars() {
        if (!isConnected()) if (!connect()) return null;
        LinkedList<String> liste = new LinkedList<String>();
        String[] ergebnis = new String[1];
        Statement stmnt = getStatement();
        if (stmnt == null) return null;
        String query = "SELECT DISTINCT seminar_id FROM jobs";
        try {
            ResultSet result = stmnt.executeQuery(query);
            while (result.next()) liste.add(result.getString("seminar_id"));
        } catch (SQLException e) {
            return null;
        }
        return liste.toArray(ergebnis);
    }

    public int countRecordings(String seminarid) {
        if (!isConnected()) if (!connect()) return 0;
        Statement stmnt = getStatement();
        if (stmnt == null) return 0;
        String query = "SELECT count( job_id ) AS number FROM jobs WHERE seminar_id = '" + seminarid + "'";
        try {
            ResultSet result = stmnt.executeQuery(query);
            if (!result.next()) return 0;
            return result.getInt("number");
        } catch (SQLException e) {
            return 0;
        }
    }

    public LinkedList<Integer> getAllPodcastServer() {
        return getAllServerOfType(6);
    }

    public LinkedList<Integer> getAllWebServer() {
        return getAllServerOfType(4);
    }

    public LinkedList<Integer> getAllRed5Server() {
        return getAllServerOfType(5);
    }

    public LinkedList<Integer> getAllServerOfType(int type) {
        DatabaseConnection database = Datenspeicher.getDatabase();
        if (!database.isConnected()) database.connect();
        Statement stmnt = database.getStatement();
        LinkedList<Integer> liste = new LinkedList<Integer>();
        if (stmnt == null) return null;
        String query = "SELECT server_id FROM servers WHERE funktion_id = '" + type + "'";
        try {
            ResultSet result = stmnt.executeQuery(query);
            while (result.next()) {
                liste.add(new Integer(result.getInt("server_id")));
            }
        } catch (SQLException e) {
            System.err.println("Fehler beim Auslesen aller Server. Query: " + query);
            return liste;
        }
        return liste;
    }

    public static void main(String[] args) {
        System.out.println("Starting");
        DatabaseConnection db = new DatabaseConnection(args[0]);
        System.out.println("Seminare: " + db.getAllSeminars());
        db.close();
    }

    public boolean checkDatabase() {
        if (!isConnected()) if (!connect()) return false;
        Statement stmnt = getStatement();
        if (stmnt == null) return false;
        String query = "SELECT * FROM statuscodes WHERE 1";
        try {
            ResultSet result = stmnt.executeQuery(query);
            if (!result.next()) return false;
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public boolean initDatabase() {
        if (!isConnected()) if (!connect()) {
            initConnect();
            Statement stmnt = getStatement();
            if (stmnt == null) return false;
            String query = "CREATE DATABASE " + Datenspeicher.getConfig().getDatabaseName();
            try {
                stmnt.executeUpdate(query);
                stmnt.close();
                close();
            } catch (SQLException e) {
                System.err.println("Fehler beim einrichten der Verwalter-Datenbank");
                e.printStackTrace();
                return false;
            }
        }
        if (!isConnected()) if (!connect()) return false;
        Statement stmnt = getStatement();
        if (stmnt == null) return false;
        String query = "";
        try {
            BufferedReader sql = new BufferedReader(new FileReader(Datenspeicher.getConfig().getWebappPath() + "virtpresenter_verwalter.sql"));
            String in = "";
            while ((in = sql.readLine()) != null) {
                query += in + "\n";
                if (query.trim().endsWith(";")) {
                    stmnt.executeUpdate(query);
                    query = "";
                }
            }
            stmnt.executeUpdate(query);
        } catch (FileNotFoundException e) {
            System.err.println("Fehler beim einrichten der Verwalter-Datenbank: Init SQL konnte nicht gefunden werden");
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            System.err.println("Fehler beim einrichten der Verwalter-Datenbank: Init SQL konnte nicht gelesen werden");
            e.printStackTrace();
            return false;
        } catch (SQLException e) {
            System.err.println("Fehler beim einrichten der Verwalter-Datenbank: " + query);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean initConnect() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception e) {
            System.err.println("Datenbank-Treiber konnte nicht geladen werden");
            e.printStackTrace();
            return false;
        }
        try {
            String connectionString = Datenspeicher.getConfig().getDatabaseServer() + "/?user=" + Datenspeicher.getConfig().getDatabaseUser() + "&password=" + Datenspeicher.getConfig().getDatabasePassword();
            conn = DriverManager.getConnection(connectionString);
        } catch (SQLException e) {
            System.err.println("Konnte nicht mit Datenbank verbinden");
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
