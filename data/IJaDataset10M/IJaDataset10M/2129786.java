package com.banordhessen.guiobjects;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import com.banordhessen.datenbank.dbconnectionVer2;
import com.banordhessen.gpxobjects.Track;

/**
 * In dieser Klasse werden die User definiert.
 * 
 * @author floaut
 * 
 */
public class User {

    private int userid;

    private String username;

    private boolean isActive;

    private Collection<TrackFolder> userTrackFolder;

    /**
	 * Konstruktor
	 * 
	 * @param userid	ID des Users
	 * @param username	Name des Users
	 */
    public User(int userid, String username) {
        this.userid = userid;
        this.username = username;
        this.isActive = true;
        this.userTrackFolder = new ArrayList<TrackFolder>();
    }

    public User(String username) {
        this.username = username;
        this.isActive = true;
        this.userTrackFolder = new ArrayList<TrackFolder>();
    }

    /**
	 * Konstruktor 
	 * 
	 * @param userid
	 * @param username
	 * @param trackfolder
	 * @param directUserTracks
	 */
    public User(int userid, String username, ArrayList<TrackFolder> trackfolder) {
        this.userid = userid;
        this.username = username;
        this.isActive = true;
        this.userTrackFolder = trackfolder;
    }

    /**
	 * Liefert die User-ID
	 * 
	 * @return User-ID
	 */
    public int getUserid() {
        return userid;
    }

    /**
	 * Setzt die User-ID
	 * 
	 * @param userid
	 *            ID des Users
	 */
    public void setUserid(int userid) {
        this.userid = userid;
    }

    /**
	 * 
	 * @return
	 */
    public String getUsername() {
        return username;
    }

    /**
	 * Setzt den Namen des Users
	 * 
	 * @param username
	 */
    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    /**
	 * Liefert die Collection mit Track-Ordnern
	 * 
	 * @return
	 */
    public Collection<TrackFolder> getTrackfolder() {
        return userTrackFolder;
    }

    /**
	 * Setzt die Collection mit Track-Ordern
	 * 
	 * @param trackfolder
	 */
    public void setTrackfolder(Collection<TrackFolder> trackfolder) {
        this.userTrackFolder = trackfolder;
    }

    /**
	 * F�gt der Collection -Trackfolder- einen weiteren Ordner hinzu. 
	 * Es wird abgepr�ft, ob der Track evtl. schon vorhanden ist. Falls 
	 * die der Fall ist werden nur der Inhalt des Trackordners zu dem bereits 
	 * vorhandenen Trackordner hinzugef�gt.
	 * 
	 * @param currentTrack	Track der neu hinzugef�gt werden soll
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
    public void addTrackfolderToCollection(TrackFolder currentTrackfolder) throws SQLException, ClassNotFoundException {
        if (trackFolderInCollection(currentTrackfolder.getFolderID())) {
            this.userTrackFolder.add(currentTrackfolder);
        } else {
            this.userTrackFolder.add(currentTrackfolder);
            dbconnectionVer2 currentDbConnection = new dbconnectionVer2();
            currentDbConnection.InsertIntoTrackordner(currentTrackfolder.getFolderName(), this.userid);
            currentTrackfolder.storeToDatabse(this.userid);
        }
        this.userTrackFolder.add(currentTrackfolder);
    }

    public void addTrackFolder2(TrackFolder currentTrackFolder) {
        this.userTrackFolder.add(currentTrackFolder);
    }

    /**
	 * Pr�ft ab, ob der Trackfolder mit der �bergeben ID bereits in der Collection vorhanden ist
	 * 
	 * @param currentTrackfolder
	 * @return	true (falls Trackfolder bereits enthalten) , false (falls noch nicht enthalten)
	 */
    private boolean trackFolderInCollection(int currentTrackfolderId) {
        Boolean returnValue = false;
        for (Iterator<TrackFolder> iterator = this.userTrackFolder.iterator(); iterator.hasNext(); ) {
            TrackFolder type = (TrackFolder) iterator.next();
            if (type.getFolderID() == currentTrackfolderId) {
                returnValue = true;
            }
        }
        return returnValue;
    }

    /**
	 * Speichert den User in die Datenbank
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
    public void storeToDatabase() throws SQLException, ClassNotFoundException {
        dbconnectionVer2 currentDbConnection = new dbconnectionVer2();
        currentDbConnection.InsertIntoUser(this.username);
        this.userid = currentDbConnection.getDataUserID(this.username);
    }

    /**
	 * Entfernt den aktuellen User aus der Datenbank
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
    public void deleteFromDatabase() throws SQLException, ClassNotFoundException {
        dbconnectionVer2 currentDbConnection = new dbconnectionVer2();
        currentDbConnection.DeleteUser(this.userid);
    }

    /**
	 * �ndert den Namen des User im Userobjekt und in der Datenbank
	 * 
	 * @param newName	neuer Name des Users
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
    public void changeUserName(String newName) throws SQLException, ClassNotFoundException {
        this.username = newName;
        dbconnectionVer2 currentDbConnection = new dbconnectionVer2();
        currentDbConnection.ChangeUserName(this.userid, this.username);
    }

    /**
	 * Entfernt einen bestimmten Track Ordner aus der Collection des Users und aus der Datenbank
	 * 
	 * @param folderId	ID des Trackordner, welcher entfernt werden soll
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
    public void deleteTrackFolder(int folderId) throws SQLException, ClassNotFoundException {
        for (Iterator<TrackFolder> iterator = userTrackFolder.iterator(); iterator.hasNext(); ) {
            TrackFolder type = (TrackFolder) iterator.next();
            if (type.getFolderID() == folderId) {
                userTrackFolder.remove(type);
                type.deleteFromDatabase();
            }
        }
    }

    /**
	 * Diese Methode l�scht den TrackFolder mit angegeben Namen aus
	 * der Collection des Users und aus der Datenbank
	 * 
	 * @param trackFolderName	Name des Folders der entfernt werden soll
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
    public void deleteTrackFolder(String trackFolderName) throws SQLException, ClassNotFoundException {
        for (Iterator<TrackFolder> iterator = this.userTrackFolder.iterator(); iterator.hasNext(); ) {
            TrackFolder type = (TrackFolder) iterator.next();
            if (type.getFolderName().endsWith(trackFolderName)) {
                type.deleteFromDatabase();
                this.userTrackFolder.remove(type);
            }
        }
    }

    /**
	 * Gibt die Daten des User Objektes in der Kommandozeile aus
	 */
    public void printUser() {
        System.out.println("||| User mit der ID: " + this.userid + " und Namen " + this.username);
        for (Iterator<TrackFolder> iterator = userTrackFolder.iterator(); iterator.hasNext(); ) {
            TrackFolder type = (TrackFolder) iterator.next();
            type.printTrackFolder();
        }
    }
}
