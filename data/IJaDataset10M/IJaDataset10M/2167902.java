package getraenkehandel;

import java.sql.*;

/**
 * @author Michael
 * 
 * Das Interface Besteller definiert die Methoden f�r den Zugriff auf die
 * Tabelle Besteller in der Datenbank
 */
public interface Besteller {

    String gesamtAnfrage = "SELECT id, name, vorname FROM besteller";

    Statement stmt = null;

    ResultSet ergebnis = null;

    Connection dbconn = null;

    /**
	 * id
	 * Benutzer-ID des Bestellers
	 * Spalte der Tabelle besteller
	 */
    int id = 0;

    /**
	 * vorname
	 * Vorname des Bestellers
	 * Spalte der Tabelle besteller
	 */
    String vorname = null;

    /**
	 * name
	 * Name des Bestellers
	 * Spalte der Tabelle besteller
	 */
    String name = null;

    String leseGesamt() throws ClassNotFoundException, SQLException;

    void getNextBesteller();

    /**
	 *  
	 */
    int getId();

    /**
	 * 
	 * @uml.property name="id"
	 */
    void setId(int id);

    /**
	 * 
	 * @uml.property name="name" multiplicity="(0 1)"
	 */
    String getName();

    /**
	 * 
	 * @uml.property name="name"
	 */
    void setName(String name);

    /**
	 * 
	 * @uml.property name="vorname" multiplicity="(0 1)"
	 */
    String getVorname();

    /**
	 * 
	 * @uml.property name="vorname"
	 */
    void setVorname(String vorname);
}
