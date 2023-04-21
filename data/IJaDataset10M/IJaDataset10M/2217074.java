package com.banordhessen.guiobjects;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import com.banordhessen.datenbank.dbconnectionVer2;
import com.banordhessen.gpxobjects.GpxFileReturn;

/**
 * @author floaut
 *
 */
public class TrackFolder {

    private int folderID;

    private String folderName;

    private boolean isActive;

    private Collection<GpxFileReturn> gpxObject;

    /**
	 * Konstruktor
	 * 
	 * @param folderID
	 * @param folderName
	 * 
	 */
    public TrackFolder(int folderID, String folderName) {
        this.folderID = folderID;
        this.folderName = folderName;
        this.isActive = true;
        this.gpxObject = new ArrayList<GpxFileReturn>();
    }

    /**
	 * Konstuktor 
	 * 
	 * @param folderID
	 * @param folderName
	 * @param currentTracks
	 */
    public TrackFolder(int folderID, String folderName, ArrayList<GpxFileReturn> currentGpxObject) {
        this.folderID = folderID;
        this.folderName = folderName;
        this.isActive = true;
        this.gpxObject = currentGpxObject;
    }

    public TrackFolder(String folderName) {
        this.folderName = folderName;
        this.isActive = true;
    }

    /**
	 * Liefert die ID des aktuellen Track Folders
	 * 
	 * @return Track-Folder-ID
	 */
    public int getFolderID() {
        return folderID;
    }

    /**
	 * Setzt die ID des aktuellen Trackardners
	 * 
	 * @param folderID
	 */
    public void setFolderID(int folderID) {
        this.folderID = folderID;
    }

    /**
	 * Liefert das Names der Ordners
	 * 
	 * @return Foldername
	 */
    public String getFolderName() {
        return folderName;
    }

    /**
	 * Gibt an, ob der aktuelle Order aktiv oder inaktiv (gel�scht) ist
	 * @return	true (aktiv), false(gel�scht)
	 */
    public boolean isActive() {
        return isActive;
    }

    /**
	 * Setzt die Variable isActiv
	 * @param isActive true (Ordner aktiv), false (Ordner gel�scht)
	 */
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    /**
	 * Liefert die Tracks und Routen aus der Datenbank, welche
	 * dem Trackfolder zugeordnet sind.
	 * Die Daten werden bei jedem Aufrufen dieser Funktion aus
	 * der Datenbank geladen. 
	 * 
	 * @return	gpxObject mit Daten aus der Datenbank
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
    public Collection<GpxFileReturn> getGpxObject() throws SQLException, ClassNotFoundException {
        dbconnectionVer2 currentDbConnection = new dbconnectionVer2();
        this.gpxObject = currentDbConnection.getRouteTrack(this.folderID);
        return this.gpxObject;
    }

    /**
	 * Speichert den aktuellen Ordner in der Datenbank ab
	 * @param userId	ID des User, dem der Trackordner gehoert
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
    public void storeToDatabse(int userId) throws SQLException, ClassNotFoundException {
        dbconnectionVer2 currentDbConnection = new dbconnectionVer2();
        currentDbConnection.InsertIntoTrackordner(this.folderName, userId);
    }

    /**
	 * Entfernt den aktuellen Ordner mit allen dazugeh�rigen Tracks aus der Datenbank
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
    public void deleteFromDatabase() throws SQLException, ClassNotFoundException {
        dbconnectionVer2 currentDbConnection = new dbconnectionVer2();
        currentDbConnection.DeleteTrackordner(this.folderID);
    }

    /**
	 * Methode umd den namen des Trackordner im Objekt und in der Datenbank zu �ndern
	 * @param newName	neuer Name des Track-Ordners
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 * 
	 */
    public void changeName(String newName) throws SQLException, ClassNotFoundException {
        this.folderName = newName;
        dbconnectionVer2 currentDbConnection = new dbconnectionVer2();
        currentDbConnection.ChangeToName(this.folderID, this.folderName);
    }

    /**
	 * Gibt den aktuellen Track Ordner auf der Kommandozeile aus
	 */
    public void printTrackFolder() {
        System.out.println("||||| Trackfolder: " + this.folderID + " Foldername: " + this.folderName);
    }
}
