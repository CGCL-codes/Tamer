package server.services.user;

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import server.services.GlobalService;
import server.services.InvalidServiceConfigurationException;
import server.services.ServiceManager;

/**
 * This GlobalService is one of the core services of the server. It maintains
 * a table of all registered users, both those who are currently logged in, and
 * those who are not. It is able to retrieve and update their information, register
 * new users, and remove old ones.
 * 
 * @author Adrian Petrescu
 *
 */
public class UserManager implements GlobalService {

    private ConcurrentHashMap<String, User> userTable;

    public static final String SERVICE_NAME = "UserManager";

    public String getIdentifier() {
        return SERVICE_NAME;
    }

    /**
	 * Called when information about Users becomes relevant (i.e, after the database
	 * is up). It constructs a table of every user registered with the server.
	 * 
	 * @param properties A Properties structure containing configuration data for the
	 * UserManager.
	 * <br>
	 * <b>Required configuration options:</b>
	 * <br>
	 * user_table (the table containing the user records).
	 */
    public void initialize(Properties properties) throws InvalidServiceConfigurationException {
        userTable = new ConcurrentHashMap<String, User>();
        String[] userNames = ServiceManager.getDatabaseManager().getRegisteredUsers();
        for (String userName : userNames) {
            User user = new User(userName);
            userTable.put(userName, user);
        }
    }

    /**
	 * Called when the server is shutting down. The connection to the database is closed
	 * and any further requests for data will be met with refusal.
	 */
    public void shutdown() {
    }

    /**
	 * Get information about a User registered with the system.
	 * 
	 * @param userName The user name of the desired User.
	 * @return A registered User corresponding to the given name.
	 * @throws NoSuchUserException Thrown if no User with the given name has registered
	 * with the server.
	 */
    public User getUser(String userName) throws NoSuchUserException {
        User user = userTable.get(userName);
        if (user == null) {
            throw new NoSuchUserException(userName);
        } else {
            return user;
        }
    }
}
