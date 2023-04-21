package de.ueppste.ljb.share;

import java.rmi.*;
import java.util.Vector;

/**
 * Inteface des Servers
 * Stellt dem Clienten ueber Java-RMI Methoden zur Kommunikation bereit
 * @author bernhard
 */
public interface LJBSInterface extends Remote {

    /**
	 * Meldet einen Clienten am Server an.
	 * Online Status des Users wird entsprechend der Angabe gesetzt
	 * Der Server wird den Clienten anschließend über Änderungen benachrichtigen
	 * @param clientInterface	Interface auf den der Server Zugriff erhält
	 * @param user				Userinstanz der eine Anmeldung erlaubt ist
	 * @return	Falls der User sich anmelden darf und der Client noch nicht registriert ist, wird true zurück gegeben, ansonsten false.
	 * @throws RemoteException
	 */
    boolean connect(LJBCInterface clientInterface, User user) throws RemoteException;

    /**
	 * Setzt den Status eines Users auf den angegebenen Status
	 * @param user	User zur Authentifizierung und mit angegebenem Status
	 * @return	boolean erfolgreich
	 * @throws RemoteException
	 */
    boolean setUserStatus(User user) throws RemoteException;

    /**
	 * Setzt den Status eines anderen Users auf den angegebenen Status
	 * @param myUser	User zur Authentifizierung
	 * @param user		User dessen Status geändert werden soll
	 * @return	boolean erfolgreich
	 * @throws RemoteException
	 */
    boolean setUserStatus(User myUser, User user) throws RemoteException;

    /**
	 * Gibt die Configuration des Servers zurueck
	 * @return Server Configuration
	 * @throws RemoteException
	 */
    LJBServerConfig getServerConfig() throws RemoteException;

    /**
	 * Gibt alle Mp3's in einem Vektor zurueck
	 * @param user	Userinstanz zur Authentifizierung
	 * @return Vektor mit allen Mp3s des Servers
	 * @throws RemoteException
	 */
    Vector<Mp3> getMp3Vektor(User user) throws RemoteException;

    /**
	 * Bearbeitet eine Mp3 Datei.
	 * Trägt Artist, Album, Title und Genre in die Datei und Datenbank ein.
	 * ID der Mp3 zeigen auf die alte Mp3 die geändert wird
	 * @param user	Der die Änderungen durchführt (EditMp3 erforderlich)
	 * @param mp3
	 * @return etwas geändert?
	 * @throws RemoteException
	 */
    boolean editMp3(User user, Mp3 mp3) throws RemoteException;

    /**
	 * Gibt das Cover zu einer Mp3-Datei zurück
	 * @param mp3	Mp3 zu der das Cover benötigt wird
	 * @param user	Userinstanz zur Authentifizierung
	 * @return	Das Cover der Mp3
	 * @throws RemoteException
	 */
    Mp3Cover getCover(Mp3 mp3, User user) throws RemoteException;

    /**
	 * Gibt alle Einträge in der Playlist als Vektor zurueck
	 * @param user	Userinstanz zur Authentifizierung
	 * @return Vektor mit Mp3s aus der Playlist
	 * @throws RemoteException
	 */
    Vector<Mp3AndUser> getPlVektor(User user) throws RemoteException;

    /**
	 * Fügt eine Mp3 der Playlist hinzu
	 * @param mp3	Mp3, welche hinzugefuegt werden soll
	 * @param user	Userinstanz zur Authentifizierung
	 * @return Hinzufuegen erfolgreich (true oder false)
	 * @throws RemoteException
	 */
    boolean addMp3ToPlayList(Mp3 mp3, User user) throws RemoteException;

    /**
	 * Loescht eine Mp3 aus der Playlist
	 * @param mp3	Mp3 welche aus der Playlist geloescht werden soll
	 * @param user	Userinstanz zur Authentifizierung
	 * @return erfolgreich geloescht (true oder false)
	 * @throws RemoteException
	 */
    boolean dellMp3FromPlayList(Mp3 mp3, User user) throws RemoteException;

    /**
	 * Sortiert die Playlist zufällig um
	 * @param user	User der diese Aktion ausführt.
	 * @return true oder false
	 * @throws RemoteException
	 */
    boolean shufflePlayList(User user) throws RemoteException;

    /**
	 * Startet einen Scan des Mp3 Vezeichnisses
	 * Adminrechte erforderlich
	 * @param user	Userinstanz mit Rechten zur Authentifizierung
	 * @return Scan erfolgreich gestartet (true oder false)
	 * @throws RemoteException
	 */
    boolean startMp3Scan(User user) throws RemoteException;

    /**
	 * Gibt den Prozentualen Fortschritt des Scannen zurueck
	 * @return Mp3s-Scannen zu x Prozent fertig
	 * @throws RemoteException
	 */
    int getScanPercentages() throws RemoteException;

    /**
	 * Bricht die aktuelle Wiedergabe ab und spielt die naechste Mp3
	 * Rechte erforderlich
	 * @param user	Userinstanz Authentifizierung
	 * @return Ausfuehrung erfolgreich (true oder false)
	 * @throws RemoteException
	 */
    boolean playNextMp3(User user) throws RemoteException;

    /**
	 * Stimmt für das Skippen des aktuellen Songs
	 * @param user	Userinstanz Authentifizierung
	 * @return Ausfuehrung erfolgreich (true oder false)
	 * @throws RemoteException
	 */
    boolean playNextMp3Vote(User user) throws RemoteException;

    /**
	 * Startet das Abspielen der Musik
	 * @param user	Userinstanz zur Authentifizierung
	 * @return Ausfuehrung erfolgreich (true oder false)
	 * @throws RemoteException
	 */
    boolean startPlaying(User user) throws RemoteException;

    /**
	 * Stopt das Abspielen der Musik
	 * @param user	Userinstanz zur Authentifizierung
	 * @return Ausfuehrung erfolgreich (true oder false)
	 * @throws RemoteException
	 */
    boolean stopPlaying(User user) throws RemoteException;

    /**
	 * Zeigt ob die Musik auf dem Server läuft
	 * @return	Musik wird abgespielt (true oder false)
	 * @throws RemoteException
	 */
    boolean isPlaying() throws RemoteException;

    /**
	 * Gibt Eintraege aus dem Archiv als Vektor zurueck
	 * @param user	Userinstanz zur Authentifizierung
	 * @return Vektor mit Mp3s aus dem Archiv
	 * @throws RemoteException
	 */
    Vector<Mp3AndUser> getArchivVektor(User user) throws RemoteException;

    /**
	 * Ermoegtlich das Hinzufuegen von Mp3-Dateien zum Server
	 * Mp3-Datei mit schon vorhandenem Datei-Namen bewirgt ein Update
	 * @param mp3File	Mp3Fileinstanz welche die Mp3-Datei beinhaltet
	 * @param user		Userinstanz zur Authentifizierung
	 * @return Mp3 erfolgreich hinzugefuegt (true oder false)
	 * @throws RemoteException
	 */
    boolean addMp3ToServer(Mp3File mp3File, User user) throws RemoteException;

    /**
	 * Gibt alle User in einem Vektor zurueck
	 * @param user	Userinstanz zur Authentifizierung, muss Management Rechte besitzen
	 * @return Vektor mit allen User des Servers
	 * @throws RemoteException
	 */
    Vector<User> getUserManagementVektor(User user) throws RemoteException;

    /**
	 * Gibt alle User in einem Vektor zurueck die sichtbar sind
	 * @param user	Userinstanz zur Authentifizierung
	 * @return Vektor mit allen User des Servers
	 * @throws RemoteException
	 */
    Vector<User> getUserVektor(User user) throws RemoteException;

    /**
	 * Ermittelt den richtigen User aus der Datenbank
	 * Kann zur Authentifizierung genutzt werden
	 * @param user	Userinstanz welche uerberpruegt werden soll
	 * @return null falls User nicht vorhanden (Name, Passwort falsch, Login nicht erlaubt) oder komplette Userinstanz
	 * @throws RemoteException
	 */
    User getRealUser(User user) throws RemoteException;

    /**
	 * Legt einen neuen User auf dem Server an
	 * @param myUser	Userinstanz mit Rechten zur Authentifizierung
	 * @param newUser	Der anzulegende User
	 * @return User erfolgreich hinzugefuegt (true oder false)
	 * @throws RemoteException
	 */
    boolean addUser(User myUser, User newUser) throws RemoteException;

    /**
	 * Entfernt einen User auf dem Server
	 * @param myUser	Userinstanz mit Techten zur Authentifizierung
	 * @param delUser	Der zu loeschende User
	 * @return User erfolgreich geloescht (true oder false)
	 * @throws RemoteException
	 */
    boolean removeUser(User myUser, User delUser) throws RemoteException;

    /**
	 * Ermoeglicht das Bearbeiten von Usern.
	 * Entsprechende Rechte notwendig
	 * @param myUser	Userinstanz zur Authentifizierung
	 * @param oldUser	User der editiert werden soll
	 * @param editUser	Wie der User nacher sein soll
	 * @return User erfolgreich bearbeitet (true oder false)
	 * @throws RemoteException
	 */
    boolean editUser(User myUser, User oldUser, User editUser) throws RemoteException;

    /**
	 * Bearbeitet das Passwort eines Users
	 * @param user		User der sich selbst bearbeiten möchte
	 * @param pwHash	Hash des neuen Passworts
	 * @return User erfolgreich bearbeitet (true oder false)
	 * @throws RemoteException
	 */
    boolean editUser(User user, int pwHash) throws RemoteException;

    /**
	 * Gibt die Favoriten zu einem User oder aller User zurück
	 * @param myUser	Userinstanz zur Authentifizierung
	 * @param user	User, von dem die Favoriten abgefragt werden sollen. Null für alle User
	 * @return Vektor mit Mp3instanzen, Null im Fehlerfall
	 * @throws RemoteException
	 */
    Vector<Mp3AndFavorite> getFavoritMp3(User myUser, User user) throws RemoteException;

    /**
	 * Schreibt einen Text in den Chat
	 * @param user	Userinstanz zur Authentifizierung
	 * @param text	Text, der eingetragen werden soll
	 * @return erfolgreich eingetragen (true oder false)
	 * @throws RemoteException
	 */
    boolean addChatItem(User user, String text) throws RemoteException;
}
