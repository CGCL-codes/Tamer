package bg.tu_sofia.refg.imsqti.user;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import bg.tu_sofia.refg.imsqti.db.DBConnector;
import bg.tu_sofia.refg.imsqti.web.beans.UserAlreadyExistsException;

public class User {

    private int userId;

    private String userName;

    private String password;

    private final Set<String> sessionSet = new HashSet<String>();

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    private User(int userId, String userName, String password) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
    }

    public void create() throws SQLException, UserAlreadyExistsException {
        DBConnector connector = DBConnector.getInstance();
        try {
            this.userId = connector.createUser(userName, password);
        } catch (SQLException e) {
            throw new UserAlreadyExistsException();
        }
    }

    public Set<String> getSessionsIDs() {
        return sessionSet;
    }

    public int getUserId() {
        return this.userId;
    }

    private boolean checkedOut = false;

    private static final Map<Integer, User> usersMap = new Hashtable<Integer, User>();

    public static User checkOut(String userName, String password) throws AuthenticationException {
        User user;
        try {
            Integer userId = DBConnector.getInstance().checkOutUser(userName, password);
            if (userId != null) {
                user = usersMap.get(userId);
                if (user == null) {
                    user = new User(userId, userName, password);
                    usersMap.put(userId, user);
                }
            } else {
                throw new AuthenticationException("User " + userName + " does not exists or password does not match!");
            }
        } catch (SQLException e) {
            throw new AuthenticationException(e);
        }
        if (user.checkedOut) {
            throw new AuthenticationException("Already logged in!");
        }
        user.checkedOut = true;
        return user;
    }

    public void checkIn() {
        checkedOut = false;
    }
}
