package de.forsthaus.backend.dao;

import de.forsthaus.backend.model.GuestBook;

/**
 * EN: DAO methods Interface for working with Guestbook data.<br>
 * DE: DAO Methoden Interface fuer die Gaestebuch Daten.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public interface GuestBookDAO {

    /**
	 * EN: Get a new GuestBook object.<br>
	 * DE: Gibt ein neues Gaestebuch Objekt zurueck.<br>
	 * 
	 * @return GuestBook
	 */
    public GuestBook getNewGuestBook();

    /**
	 * EN: Get a GuestBook object by ID.<br>
	 * DE: Gibt ein neues Gaestebuch Objekt anhand seiner ID zurueck.<br>
	 * 
	 * @param id
	 *            / the persistence identifier / der PrimaerKey
	 * @return GuestBook
	 */
    public GuestBook getGuestBookByID(long id);

    /**
	 * EN: Get the count of all GuestBook.<br>
	 * DE: Gibt die Anzahl aller Gaestebucheintraege zurueck.<br>
	 * 
	 * @return int
	 */
    public int getCountAllGuestBooks();

    /**
	 * EN: Saves new or updates a GuestBook entry.<br>
	 * DE: Speichert oder aktualisiert einen Gaestebuch Eintrag.<br>
	 */
    public void saveOrUpdate(GuestBook entity);

    /**
	 * EN: Deletes a GuestBook entry.<br>
	 * DE: Loescht einen Gaestebuch Eintrag.<br>
	 */
    public void delete(GuestBook entity);

    /**
	 * EN: Saves a GuestBook entry.<br>
	 * DE: Speichert einen Gaestebuch Eintrag.<br>
	 */
    public void save(GuestBook entity);
}
