package system;

/**
 *
 * @author Micke
 */
public class User {

    private String userName, password;

    private int userID;

    public User(String userName, int userID, String password) {
        this.userName = userName;
        this.userID = userID;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public int getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }
}
