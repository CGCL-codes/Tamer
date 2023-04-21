package de.uos.virtuos.virtpresenter.verwalter.server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import de.uos.virtuos.virtpresenter.verwalter.data.Datenspeicher;
import de.uos.virtuos.virtpresenter.verwalter.tools.DatabaseConnection;

public class Server {

    /**
	 * function-codes for servers
	 */
    public static final int VIDEORECORDER_SERVER = 1;

    public static final int VIDEOCONVERTER_SERVER = 2;

    public static final int GENERATOR_SERVER = 3;

    public static final int WEBSERVER = 4;

    public static final int RED5_SERVER = 5;

    public static final int PODCAST_SERVER = 6;

    public static final int ENHANCED_PODCAST_SERVER = 7;

    int id = 0;

    String url;

    String benutzer;

    String passwort;

    String publicUrl;

    String defaultPath;

    String ftpBenutzer;

    String ftpPasswort;

    int port;

    int funktionId;

    public Server() {
        super();
    }

    public Server(String url, String benutzer, String passwort) {
        super();
        this.url = url;
        this.benutzer = benutzer;
        this.passwort = passwort;
        this.publicUrl = url;
        this.defaultPath = "";
    }

    public Server(String url, String benutzer, String passwort, String publicUrl, String defaultPath) {
        this(url, benutzer, passwort);
        this.publicUrl = publicUrl;
        this.defaultPath = defaultPath;
    }

    public String getBenutzer() {
        return benutzer;
    }

    public void setBenutzer(String benutzer) {
        this.benutzer = benutzer;
    }

    public String getDefaultPath() {
        if (defaultPath == null) return "";
        return defaultPath;
    }

    public void setDefaultPath(String defaultPath) {
        this.defaultPath = defaultPath;
    }

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }

    public String getPublicUrl() {
        return publicUrl;
    }

    public void setPublicUrl(String publicUrl) {
        this.publicUrl = publicUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public static Server readServer(int serverID) {
        if (serverID == 0) return null;
        DatabaseConnection database = Datenspeicher.getDatabase();
        if (!database.isConnected()) database.connect();
        Server newServer = null;
        Statement stmnt = database.getStatement();
        if (stmnt == null) return null;
        String query = "SELECT * FROM servers WHERE server_id = '" + serverID + "'";
        try {
            ResultSet result = stmnt.executeQuery(query);
            if (!result.next()) {
                return null;
            }
            newServer = new Server();
            newServer.id = serverID;
            newServer.url = result.getString("url");
            newServer.benutzer = result.getString("benutzer");
            newServer.passwort = result.getString("passwort");
            newServer.port = result.getInt("port");
            newServer.defaultPath = result.getString("ftp_pfad");
            newServer.ftpBenutzer = result.getString("ftp_benutzer");
            newServer.ftpPasswort = result.getString("ftp_passwort");
            newServer.publicUrl = result.getString("public_url");
            newServer.funktionId = result.getInt("funktion_id");
        } catch (SQLException e) {
            System.err.println("ReadServer: Fehler beim Auslesen des Servers " + serverID + " Query: " + query);
            e.printStackTrace();
            return null;
        }
        return newServer;
    }

    public static Server readServer(String url, int funktion_id) {
        return readServer(readServerId(url, funktion_id));
    }

    public static int readServerId(String url, int funktion_id) {
        if (url == null || funktion_id == 0) return 0;
        DatabaseConnection database = Datenspeicher.getDatabase();
        if (!database.isConnected()) database.connect();
        Statement stmnt = database.getStatement();
        if (stmnt == null) return 0;
        String query = "SELECT server_id FROM servers WHERE url = '" + url + "' AND funktion_id = '" + funktion_id + "'";
        System.err.println("Looking for Server: " + query);
        try {
            ResultSet result = stmnt.executeQuery(query);
            if (!result.next()) return 0;
            System.err.println("Server found: " + result.getInt("server_id"));
            return result.getInt("server_id");
        } catch (SQLException e) {
            return 0;
        }
    }

    public static Server createNewServer(String url, String benutzer, String passwort, String publicUrl, String defaultPath, String ftpBenutzer, String ftpPasswort, int port, int funktion_id) {
        Server newServer = new Server(url, benutzer, passwort, publicUrl, defaultPath);
        newServer.port = port;
        newServer.funktionId = funktion_id;
        newServer.ftpBenutzer = ftpBenutzer;
        newServer.ftpPasswort = ftpPasswort;
        DatabaseConnection database = Datenspeicher.getDatabase();
        if (!database.isConnected()) database.connect();
        Statement stmnt = database.getStatement();
        if (stmnt == null) return null;
        String command = "INSERT INTO servers (url, funktion_id";
        String values = ") VALUES ('" + url + "', '" + funktion_id + "'";
        if (benutzer != null) {
            command += ", benutzer";
            values += ", '" + benutzer + "'";
        }
        if (passwort != null) {
            command += ", passwort";
            values += ", '" + passwort + "'";
        }
        if (port >= 0) {
            command += ", port";
            values += ", '" + port + "'";
        }
        if (ftpBenutzer != null) {
            command += ", ftp_benutzer";
            values += ", '" + ftpBenutzer + "'";
        }
        if (ftpPasswort != null) {
            command += ", ftp_passwort";
            values += ", '" + ftpPasswort + "'";
        }
        if (publicUrl != null) {
            command += ", public_url";
            values += ", '" + publicUrl + "'";
        }
        if (defaultPath != null) {
            command += ", ftp_pfad";
            values += ", '" + defaultPath + "'";
        }
        String query = command + values + ")";
        try {
            stmnt.executeUpdate(query);
        } catch (SQLException e) {
            System.err.println("Neuer Server konnte nicht angelegt werden.");
            return null;
        }
        newServer.id = readServerId(url, funktion_id);
        return newServer;
    }

    public String getFtpBenutzer() {
        return ftpBenutzer;
    }

    public void setFtpBenutzer(String ftpBenutzer) {
        this.ftpBenutzer = ftpBenutzer;
    }

    public String getFtpPasswort() {
        return ftpPasswort;
    }

    public void setFtpPasswort(String ftpPasswort) {
        this.ftpPasswort = ftpPasswort;
    }

    public int getFunktionId() {
        return funktionId;
    }

    public void setFunktionId(int funktionId) {
        this.funktionId = funktionId;
    }

    public boolean equalUrl(String url2) {
        try {
            return InetAddress.getByName(url).equals(InetAddress.getByName(url2));
        } catch (UnknownHostException e) {
            return false;
        }
    }
}
