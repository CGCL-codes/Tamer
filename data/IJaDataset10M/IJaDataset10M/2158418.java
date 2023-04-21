package nz.ac.massey.softwarec.group3.session;

import java.util.List;
import javax.servlet.http.HttpSession;

/**
 * SessionTracker - Class which controls Sessions and monitors Games. 
 * @version 1.0 Release
 * @since 1.0
 * @authors Natalie Eustace | Wanting Huang | Paul Smith | Craig Spence
 */
public interface SessionTrackerInterface {

    /**
    * Getter for gameTracker.
    * @return GameTracker gameTracker - The GameTracker assosciated with the SessionTracker.
    */
    GameTracker getGameTracker();

    /**
    * Method to get a list of all the currently online users.
    * @return ArrayList<String> namesOfCurrentlyOnlineUsers - The ArrayList of the currently online user's names.
    */
    List<String> getListOfUsersWhoAreCurrentlyOnline();

    /**
    * Getter for sessions.
    * @return ArrayList<HttpSession> sessions - The ArrayList of sessions.
    */
    List<HttpSession> getSessions();

    /**
    * Method to check through all sessions to get the real name of a user
    * from an email address.
    * @param String email - The email to look for.
    * @return String - The users real name.
    */
    String getUserNameFromEmail(final String email);

    /**
     * Remove a given user from the game that they are in.
     * @param String userEmail - the email of the user to remove.
     */
    void removeUserFromGame(final String userEmail);
}
