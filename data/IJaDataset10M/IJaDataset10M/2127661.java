package crawler.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import crawler.client.PageIdentifier;
import crawler.db.BandDB;
import crawler.db.BookDB;
import crawler.db.CommentDB;
import crawler.db.DBObjectDB;
import crawler.db.FriendDB;
import crawler.db.GenreDB;
import crawler.db.HeroesDB;
import crawler.db.MovieDB;
import crawler.db.MusicDB;
import crawler.db.MySpaceDB;
import crawler.db.ProfileDB;
import crawler.db.TelevisionDB;
import crawler.dto.MySpaceDTO;
import crawler.util.SocketConnection;

public class CrawlerServer extends Thread {

    public static final int DEFAULT_PORT = 33333;

    private ServerSocket server_socket;

    private List<SocketConnection> connections;

    private int num_connections;

    private boolean activ = true;

    private Connection dbconn = null;

    private int packet = 0;

    private int current = 0;

    public CrawlerServer(int port, int start, int packet) {
        this.packet = packet;
        connections = new ArrayList<SocketConnection>();
        num_connections = 0;
        current = start;
        try {
            server_socket = new ServerSocket(port);
            activ = true;
            ServerGUI.success(server_socket + "\nServer lauscht auf Port " + port);
            ServerGUI.success("Start-ID " + start + ", Packetgroesse " + packet);
            try {
                dbconn = DBObjectDB.get_connection();
                ServerGUI.success("Datenbankverbindung wurde erfolgreich hergestellt");
            } catch (SQLException e) {
                ServerGUI.error("Datenbankverbindung konnte nicht hergestellt werden");
            }
        } catch (IOException e) {
            ServerGUI.error("Eine Ausnahme trat beim Anlegen des Server Socket auf " + e.toString());
        }
        this.start();
    }

    public void run() {
        while (activ) {
            try {
                Socket client_socket = server_socket.accept();
                SocketConnection c = new SocketConnection(client_socket, this);
                addConnection(c);
            } catch (Exception e) {
            }
        }
        disconnect();
    }

    public void disconnect() {
        ServerGUI.info("Server close ... ");
        try {
            if (server_socket != null) server_socket.close();
            if (dbconn != null) dbconn.close();
            ServerGUI.success("OK");
        } catch (Exception e) {
            ServerGUI.error("Error" + e.toString());
        }
    }

    public synchronized void addConnection(SocketConnection c) {
        connections.add(c);
        num_connections++;
    }

    public synchronized void receive(Object obj) {
        if (obj != null) {
            if (obj instanceof MySpaceDTO) {
                final MySpaceDTO dto = (MySpaceDTO) obj;
                if (dto.getBand() == null) {
                    dto.setProfileType("profile");
                    ServerGUI.inkrementProfil();
                } else {
                    dto.setProfileType("band");
                    ServerGUI.inkrementBand();
                }
                try {
                    new Thread(new Runnable() {

                        public void run() {
                            insertDB(dto);
                        }
                    }).start();
                } catch (Exception e) {
                }
                ServerGUI.log(dto.getId() + " - " + dto.getProfileName() + " (" + dto.getProfile().getAge() + ") " + dto.getProfile().getSex());
            }
            if (obj instanceof Integer) {
                int type = Integer.parseInt(obj.toString());
                switch(type) {
                    case PageIdentifier.PRIVATEPROFILE:
                        ServerGUI.inkrementPrivate();
                        break;
                    case PageIdentifier.INVALIDID:
                        ServerGUI.inkrementInvalid();
                        break;
                }
            }
        }
        obj = null;
    }

    public synchronized int send(SocketConnection conn) throws IOException {
        if ((conn != null) && (conn.isAlive())) {
            conn.send(getIDList());
        }
        ServerGUI.success("ID Paket versendet");
        return packet;
    }

    private Collection<String> getIDList() {
        Collection<String> list = new ArrayList<String>();
        for (int i = current; i < (current + packet); i++) {
            list.add(i + "");
        }
        ServerGUI.success(current + " - " + (current + packet));
        current += packet;
        return list;
    }

    public void serverStop() {
        ServerGUI.success("Server wird nach dem Vorgang beendet");
        this.activ = false;
        for (SocketConnection conn : connections) {
            if (conn != null && conn.isAlive()) conn.setAvailable(false);
        }
    }

    public boolean insertDB(MySpaceDTO dto) {
        try {
            dbconn.setAutoCommit(false);
            MySpaceDB.insert(dbconn, dto);
            try {
                CommentDB.insert(dbconn, dto);
            } catch (Exception e) {
                ServerGUI.warn("Fehler beim einf�gen der Comments");
            }
            try {
                FriendDB.insert(dbconn, dto);
            } catch (Exception e) {
                ServerGUI.warn("Fehler beim einf�gen der Freunde" + e.toString());
            }
            if (dto.getProfileType().equals("profile")) {
                try {
                    ProfileDB.insert(dbconn, dto);
                } catch (Exception e) {
                    ServerGUI.warn("Fehler beim einf�gen des Profiles");
                }
                try {
                    HeroesDB.insert(dbconn, dto);
                } catch (Exception e) {
                    ServerGUI.warn("Fehler beim einf�gen der Heroes");
                }
                try {
                    MovieDB.insert(dbconn, dto);
                } catch (Exception e) {
                    ServerGUI.warn("Fehler beim einf�gen der Movies");
                }
                try {
                    MusicDB.insert(dbconn, dto);
                } catch (Exception e) {
                    ServerGUI.warn("Fehler beim einf�gen der Music");
                }
                try {
                    BookDB.insert(dbconn, dto);
                } catch (Exception e) {
                    ServerGUI.warn("Fehler beim einf�gen der Books");
                }
                try {
                    TelevisionDB.insert(dbconn, dto);
                } catch (Exception e) {
                    ServerGUI.warn("Fehler beim einf�gen der Television");
                }
            } else {
                BandDB.insert(dbconn, dto);
                GenreDB.insert(dbconn, dto);
            }
            dbconn.commit();
        } catch (SQLException e) {
            try {
                dbconn.rollback();
                ServerGUI.warn("Datenbank Rollback erfolgreich");
            } catch (SQLException e1) {
                ServerGUI.warn("Datenbank Rollback nicht erfolgreich");
            }
            return false;
        }
        return true;
    }
}
