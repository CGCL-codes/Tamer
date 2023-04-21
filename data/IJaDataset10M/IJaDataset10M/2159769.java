package org.ultracalendar.login_system;

import javax.ejb.Remote;
import org.ultracalendar.InvalidArgumentException;
import org.ultracalendar.user_management.User;

/**
 * Loggt Nutzer ein.
 * 
 * @author Robert Schulze
 * @author Hannes John
 * @since 21.05.2009 16:14:45
 */
@Remote
public interface IUserLogin {

    /**
	 * Loggt einen Nutzer ein.
	 * 
	 * @param username
	 *            Der Nutzername, nicht leer oder null
	 * @param password
	 *            Das Passwort, nicht leer oder null
	 * @return Der eingeloggte Nutzer
	 * @throws LoginException
	 *             Wenn Nutzername oder Passwort falsch sind
	 * @throws InvalidArgumentException
	 *             Wenn ungültige Daten angegeben wurden
	 */
    public User logUserIn(String username, String password) throws LoginException, InvalidArgumentException;
}
