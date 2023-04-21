package es.ehrflex.core.db;

import org.xmldb.api.modules.XMLResource;

/**
 * Schnittstele für Datenbankzugriffe die Benutzer betreffen
 */
public interface DBUser {

    /**
     * Checks if the user and password is in the database and if so it delivers the user.
     * 
     * @param login
     *            username
     * @param password
     *            password
     * @param tx
     *            open transaction
     * 
     * @return null or found user
     * @throws Exception
     */
    public XMLResource login(String login, String password) throws Exception;

    /**
     * Saves the given user in the database. Form is a xml file in form of a Java-Object created by a XMLEncoder.
     * 
     * @param user
     *            user xml string
     * @param login 
     * 		  user login --> filename   
     *         
     * 
     * @throws Exception
     */
    public void saveUser(String user, String login) throws Exception;

    /**
     * Delivers a user by his login
     * 
     * @param login
     *            username
     * 
     * @return user or null if there's no user with the given login
     * 
     * @throws Exception
     */
    public XMLResource getUser(String login) throws Exception;
}
