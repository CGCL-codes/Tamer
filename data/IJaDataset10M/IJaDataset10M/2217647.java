package de.ueppste.ljb.client;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import de.ueppste.ljb.share.LJBCInterface;
import de.ueppste.ljb.share.LJBSInterface;
import de.ueppste.ljb.share.LJBServerConfig;
import de.ueppste.ljb.share.Mp3;
import de.ueppste.ljb.share.Mp3AndFavorite;
import de.ueppste.ljb.share.Mp3AndUser;
import de.ueppste.ljb.share.Mp3Cover;
import de.ueppste.ljb.share.Mp3File;
import de.ueppste.ljb.share.User;

public class LJBServer {

    private LJBSInterface ljbsInterface = null;

    private String errorMsg = null;

    private static Object[] options = { "Retry", "Close" };

    private de.ueppste.ljb.share.User user = null;

    private LJBConfig config;

    private static int retryCount = 0;

    private static int maxRetrys = 10;

    private static Logger logger = Logger.getLogger(LJBServer.class.getName());

    /**
	 * Gibt Error Message zurück
	 */
    public String getErrorMsg() {
        return this.errorMsg;
    }

    /**
	 * Konstruktor
	 * @param mainCtrl Controll des GUI
	 * @param config Konfigurations Instanz
	 */
    public LJBServer(LJBConfig config) {
        this.config = config;
    }

    /**
	 * Führt einen server connect aus und überprüft gleichzeitig ob der Benutzer mit dem inder datenbank übereinstimmt.
	 * Bei Fehler wird die Fehler message in errorMsg geschrieben um diese dann extern über getErrorMsg im Fehlerfall auszugeben.
	 * @param user User mit dem connect werden soll.
	 * @return Richtige User instanz bei erfolg sonst null
	 */
    public User serverConnect(User user) {
        String serverIP = config.getServerIP();
        String serverPort = config.getServerPort();
        try {
            ljbsInterface = (LJBSInterface) Naming.lookup("rmi://" + serverIP + ":" + serverPort + "/LJBS");
        } catch (Exception e) {
            this.errorMsg = e.getMessage();
            return null;
        }
        try {
            user.setUserOnline();
            this.user = ljbsInterface.getRealUser(user);
            if (this.user != null) {
                this.user.setUserOnline();
                return this.user;
            } else {
                this.errorMsg = "Benutzername/Passwort falsch!";
                return null;
            }
        } catch (RemoteException e) {
            this.errorMsg = e.getMessage();
            return null;
        }
    }

    /**
	 * Gibt das Server Interface zurück 
	 * @return LJBSInterface Instanz
	 */
    public LJBSInterface getServerInterface() {
        if (this.ljbsInterface != null) return this.ljbsInterface; else return null;
    }

    /**
	 * Übergibt dem Server die User Instanz sowie das Client Interface (Ctrl)
	 * @param clientInterface Client Interface, hier die Controll des Haupt GUI
	 * @param user User instanz
	 * @return Verbindung erfolgreich (true - false)
	 */
    public boolean connect(LJBCInterface clientInterface, User user) {
        try {
            boolean con = ljbsInterface.connect(clientInterface, user);
            retryCount = 0;
            return con;
        } catch (RemoteException e) {
            if (connectionError()) {
                serverConnect(user);
                connect(clientInterface, user);
            } else connect(clientInterface, user);
            return false;
        }
    }

    /**
	 * Wird aufgerufen bei RemoteException.
	 * retryCount wir bis maxRetrys hochgezählt, anschließend startet abfrage ob reconnect oder das Programm geschlossen werden soll.
	 * Wurde eingebaut um kurze Verbindungsprobleme zu überbrücken.
	 * @return
	 */
    private static boolean connectionError() {
        if (retryCount == maxRetrys) {
            retryCount = 0;
            int auswahl = JOptionPane.showOptionDialog(null, "Die Verbindung zum Server wurde unterbrochen", "Verbindungsfehler", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
            if (auswahl == 0) {
                return true;
            }
            logger.log(Level.WARNING, "Serververbindung wurde Unterbrochen und der Client beendet.");
            System.exit(-1);
        } else {
            retryCount++;
            return false;
        }
        return true;
    }

    /**
	 * Ruft die aktuelle Server Konfiguration ab
	 * @return - Server Config
	 */
    public LJBServerConfig getServerConfig() {
        try {
            LJBServerConfig config = ljbsInterface.getServerConfig();
            retryCount = 0;
            return config;
        } catch (RemoteException e) {
            if (connectionError()) {
                serverConnect(user);
                getServerConfig();
            } else getServerConfig();
            return null;
        }
    }

    /**
	 * Ruft den aktuellen MP3 Vektor ab um die Mediathek zu f�llen
	 * @param user - Aktuelle User instanz
	 * @return - Mp3 Vektor
	 */
    public Vector<Mp3> getMp3Vektor(User user) {
        try {
            Vector<Mp3> vek = ljbsInterface.getMp3Vektor(user);
            retryCount = 0;
            return vek;
        } catch (RemoteException e) {
            if (connectionError()) {
                serverConnect(user);
                getMp3Vektor(user);
            } else getMp3Vektor(user);
            return null;
        }
    }

    /**
	 * Ruft den Playlistvektor ab
	 * @param user - Aktuelle Userinstanz
	 * @return - Playlistvektor mit Mp3AndUser Eintr�gen
	 */
    public Vector<Mp3AndUser> getPlVektor(User user) {
        try {
            Vector<Mp3AndUser> vek = ljbsInterface.getPlVektor(user);
            retryCount = 0;
            return vek;
        } catch (RemoteException e) {
            if (connectionError()) {
                serverConnect(user);
                getPlVektor(user);
            } else getPlVektor(user);
            return null;
        }
    }

    /**
	 * Ruft den Archivtvektor ab
	 * @param user - Aktuelle Userinstanz
	 * @return - Archivvektor mit Mp3AndUser Eintr�gen
	 */
    public Vector<Mp3AndUser> getArchivVektor(User user) {
        try {
            Vector<Mp3AndUser> vek = ljbsInterface.getArchivVektor(user);
            retryCount = 0;
            return vek;
        } catch (RemoteException e) {
            if (connectionError()) {
                serverConnect(user);
                getArchivVektor(user);
            } else getArchivVektor(user);
            return null;
        }
    }

    /**
	 * Ruft den uservektor ab (alle User außer nicht Sichtbare User)
	 * @param user - Aktuelle Userinstanz
	 * @return - Userverktor
	 */
    public Vector<User> getUserVektor(User user) {
        try {
            Vector<User> vek = ljbsInterface.getUserVektor(user);
            retryCount = 0;
            return vek;
        } catch (RemoteException e) {
            if (connectionError()) {
                serverConnect(user);
                getUserVektor(user);
            } else getUserVektor(user);
            return null;
        }
    }

    /**
	 * Ruft ALLE Benutzer des Servers ab
	 * @param user - aktuelle Userisntanz
	 * @return Vektor mit allen Benutzern
	 */
    public Vector<User> getUserManagementVektor(User user) {
        try {
            Vector<User> vek = ljbsInterface.getUserManagementVektor(user);
            retryCount = 0;
            return vek;
        } catch (RemoteException e) {
            if (connectionError()) {
                serverConnect(user);
                getUserManagementVektor(user);
            } else getUserManagementVektor(user);
            return null;
        }
    }

    public Vector<Mp3AndFavorite> getFavoritMp3(User myUser, User user) {
        try {
            Vector<Mp3AndFavorite> vek = ljbsInterface.getFavoritMp3(myUser, user);
            retryCount = 0;
            return vek;
        } catch (RemoteException e) {
            if (connectionError()) {
                serverConnect(user);
                getFavoritMp3(myUser, user);
            } else getFavoritMp3(myUser, user);
            return null;
        }
    }

    /**
	 * F�gt eine MP3 zur Playlist hinzu
	 * @param mp3 - Hinzuzuf�gende Mp3
	 * @param user - Aktuelle Userinstanz
	 * @return - True > Erfolgreich; False > Fehlgeschlagen
	 */
    public boolean addMp3ToPlayList(Mp3 mp3, User user) {
        try {
            boolean bool = ljbsInterface.addMp3ToPlayList(mp3, user);
            retryCount = 0;
            return bool;
        } catch (RemoteException e) {
            if (connectionError()) {
                serverConnect(user);
                addMp3ToPlayList(mp3, user);
            } else addMp3ToPlayList(mp3, user);
            return false;
        }
    }

    /**
	 * Startet einen Mp3 Scan
	 * @param user - Aktulle Userinstanz
	 * @return - True > Erfolgreich; False > Fehlgeschlagen
	 */
    public boolean startMp3Scan(User user) {
        try {
            boolean bool = ljbsInterface.startMp3Scan(user);
            retryCount = 0;
            return bool;
        } catch (RemoteException e) {
            if (connectionError()) {
                serverConnect(user);
                startMp3Scan(user);
            } else startMp3Scan(user);
            return false;
        }
    }

    /**
	 * Lied weiter
	 * @param user - Aktuelle User Instanz
	 * @return - True > Erfolgreich; False > Fehlgeschlagen
	 */
    public boolean playNextMp3(User user) {
        try {
            boolean bool = ljbsInterface.playNextMp3(user);
            retryCount = 0;
            return bool;
        } catch (RemoteException e) {
            if (connectionError()) {
                serverConnect(user);
                playNextMp3(user);
            } else playNextMp3(user);
            return false;
        }
    }

    /**
	 * Mp3 File Upload
	 * @param mp3File - Hochzuladendes Mp3-File
	 * @param user - Aktuelle Userinstanz
	 * @return - True > Erfolgreich; False > Fehlgeschlagen
	 */
    public boolean addMp3ToServer(Mp3File mp3File, User user) {
        try {
            boolean bool = ljbsInterface.addMp3ToServer(mp3File, user);
            retryCount = 0;
            return bool;
        } catch (RemoteException e) {
            if (connectionError()) {
                serverConnect(user);
                addMp3ToServer(mp3File, user);
            } else addMp3ToServer(mp3File, user);
            return false;
        }
    }

    /**
	 * Gibt den Scanfortschritt zur�ck
	 * @return - Scanfortschritt
	 */
    public int getScanPercentages() {
        try {
            int per = ljbsInterface.getScanPercentages();
            retryCount = 0;
            return per;
        } catch (RemoteException e) {
            if (connectionError()) {
                serverConnect(user);
                getScanPercentages();
            } else getScanPercentages();
            return -1;
        }
    }

    /**
	 * Entfernt die angegebene Mp3 von der Playlist
	 * @param mp3 - zu entfernende Mp3
	 * @param user - Aktuelle Userinstanz
	 * @return - True > Erfolgreich; False > Fehlgeschlagen
	 */
    public boolean dellMp3FromPlayList(Mp3 mp3, User user) {
        try {
            boolean per = ljbsInterface.dellMp3FromPlayList(mp3, user);
            retryCount = 0;
            return per;
        } catch (RemoteException e) {
            if (connectionError()) {
                serverConnect(user);
                dellMp3FromPlayList(mp3, user);
            } else dellMp3FromPlayList(mp3, user);
            return false;
        }
    }

    /**
	 * F�gt neuen Chateintrag hinzu
	 * @param user - Aktuelle Userinstanz
	 * @param text - Hinzuzuf�gender Text
	 * @return - True > Erfolgreich; False > Fehlgeschlagen
	 */
    public boolean addChatItem(User user, String text) {
        try {
            boolean per = ljbsInterface.addChatItem(user, text);
            retryCount = 0;
            return per;
        } catch (RemoteException e) {
            if (connectionError()) {
                serverConnect(user);
                addChatItem(user, text);
            } else addChatItem(user, text);
            return false;
        }
    }

    /**
	 * Gibt das Cover der �bergebenen Mp3 zur�ck
	 * @param mp3 - Mp3
	 * @param user - Aktuelle Userinstanz
	 * @return - Cover
	 */
    public Mp3Cover getCover(Mp3 mp3, User user) {
        try {
            Mp3Cover cover = ljbsInterface.getCover(mp3, user);
            retryCount = 0;
            return cover;
        } catch (RemoteException e) {
            if (connectionError()) {
                serverConnect(user);
                getCover(mp3, user);
            } else getCover(mp3, user);
            return null;
        }
    }

    /**
	 * Startet das Abspielen
	 * @param user - Aktuelle Userinstanz
	 * @return - True > Erfolgreich; False > Fehlgeschlagen
	 */
    public boolean startPlaying(User user) {
        try {
            boolean per = ljbsInterface.startPlaying(user);
            retryCount = 0;
            return per;
        } catch (RemoteException e) {
            if (connectionError()) {
                serverConnect(user);
                startPlaying(user);
            } else startPlaying(user);
            return false;
        }
    }

    /**
	 * Stopt das Abspielen
	 * @param user - Aktuelle Userinstanz
	 * @return - True > Erfolgreich; False > Fehlgeschlagen
	 */
    public boolean stopPlaying(User user) {
        try {
            boolean per = ljbsInterface.stopPlaying(user);
            retryCount = 0;
            return per;
        } catch (RemoteException e) {
            if (connectionError()) {
                serverConnect(user);
                stopPlaying(user);
            } else stopPlaying(user);
            return false;
        }
    }

    /**
	 * Abfrage ob Abgespielt wird
	 * @return - True > Erfolgreich; False > Fehlgeschlagen
	 */
    public boolean isPlaying() {
        try {
            boolean per = ljbsInterface.isPlaying();
            retryCount = 0;
            return per;
        } catch (RemoteException e) {
            if (connectionError()) {
                serverConnect(user);
                return isPlaying();
            } else return isPlaying();
        }
    }

    /**
	 * F�gt neuen User hinzu
	 * @param myUser - Aktuelle User Instanz
	 * @param newUser - Neuer User
	 * @return - True > Erfolgreich; False > Fehlgeschlagen
	 */
    public boolean addUser(User myUser, User newUser) {
        try {
            boolean per = ljbsInterface.addUser(myUser, newUser);
            retryCount = 0;
            return per;
        } catch (RemoteException e) {
            if (connectionError()) {
                serverConnect(user);
                addUser(myUser, newUser);
            } else addUser(myUser, newUser);
            return false;
        }
    }

    /**
	 * L�scht User
	 * @param myUser - Aktuelle User Instanz
	 * @param delUser - Zu entfernender User
	 * @return - True > Erfolgreich; False > Fehlgeschlagen
	 */
    public boolean removeUser(User myUser, User delUser) {
        try {
            boolean per = ljbsInterface.removeUser(myUser, delUser);
            retryCount = 0;
            return per;
        } catch (RemoteException e) {
            if (connectionError()) {
                serverConnect(user);
                removeUser(myUser, delUser);
            } else removeUser(myUser, delUser);
            return false;
        }
    }

    /**
	 * �ndert Userdaten
	 * @param myUser - Aktuelle User Instanz
	 * @param oldUser - Alter User
	 * @param editUser - Editierter User
	 * @return - True > Erfolgreich; False > Fehlgeschlagen
	 */
    public boolean editUser(User myUser, User oldUser, User editUser) {
        try {
            boolean per = ljbsInterface.editUser(myUser, oldUser, editUser);
            retryCount = 0;
            return per;
        } catch (RemoteException e) {
            if (connectionError()) {
                serverConnect(user);
                editUser(myUser, oldUser, editUser);
            } else editUser(myUser, oldUser, editUser);
            return false;
        }
    }

    /**
	 * Shuffelt die Playlist
	 * @param user - Aktuelle user instanz
	 * @return - True > Erfolgreich; False > Fehlgeschlagen
	 */
    public boolean shufflePlayList(User user) {
        try {
            boolean per = ljbsInterface.shufflePlayList(user);
            retryCount = 0;
            return per;
        } catch (RemoteException e) {
            if (connectionError()) {
                serverConnect(user);
                shufflePlayList(user);
            } else shufflePlayList(user);
            return false;
        }
    }

    /**
	 * Ändert das Passwort einer Userinstanz
	 * @param user - Aktuelle userinstanz
	 * @param pwHash - neuer Passworthash
	 * @return - True > Erfolgreich; False > Fehlgeschlagen
	 */
    public boolean editUser(User user, int pwHash) {
        try {
            boolean per = ljbsInterface.editUser(user, pwHash);
            retryCount = 0;
            return per;
        } catch (RemoteException e) {
            if (connectionError()) {
                serverConnect(user);
                editUser(user, pwHash);
            } else editUser(user, pwHash);
            return false;
        }
    }

    /**
	 * Gibt die neue richtige Userinstanz zurück
	 * @param user User vbon dem die Userinstanz vom Servergeholt werden soll
	 * @return - Neue Userinstanz
	 */
    public User getRealUser(User user) {
        try {
            this.user = ljbsInterface.getRealUser(user);
            retryCount = 0;
            return this.user;
        } catch (RemoteException e) {
            if (connectionError()) {
                serverConnect(user);
                getRealUser(user);
            } else getRealUser(user);
            return null;
        }
    }

    /**
	 * Setzt den Benutzerstatus
	 * @param user - Benutzerinstnaz mit gesetztem Status
	 * @return - True > Erfolgreich; False > Fehlgeschlagen
	 */
    public boolean setUserStatus(User user) {
        try {
            boolean errorBool = ljbsInterface.setUserStatus(user);
            retryCount = 0;
            return errorBool;
        } catch (RemoteException e) {
            if (connectionError()) {
                serverConnect(user);
                setUserStatus(user);
            } else setUserStatus(user);
            return false;
        }
    }

    /**
	 * Votet für den Skip der aktuellen Mp3
	 * @param user - Aktuelle Benutzerintsanz
	 * @return - True > Erfolgreich; False > Fehlgeschlagen
	 */
    public boolean playNextMp3Vote(User user) {
        try {
            boolean errorBool = ljbsInterface.playNextMp3Vote(user);
            retryCount = 0;
            return errorBool;
        } catch (RemoteException e) {
            if (connectionError()) {
                serverConnect(user);
                playNextMp3Vote(user);
            } else playNextMp3Vote(user);
            return false;
        }
    }
}
