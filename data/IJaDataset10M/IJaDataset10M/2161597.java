package storeMovie.JavaBean;

public class User {

    private String userName;

    private String password;

    private String displayName;

    public User(String userName, String password, String displayName) {
        this.userName = userName;
        this.password = password;
        this.displayName = displayName;
    }

    public User() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
