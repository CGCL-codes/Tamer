package net.sf.archimede.model.user;

public class UserImpl implements User {

    private String username;

    private String password;

    public UserImpl() {
    }

    public UserImpl(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean equals(Object object) {
        if (object instanceof User) {
            User user = (User) object;
            if (this.getUsername().equals(user.getUsername())) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return this.username.hashCode();
    }
}
