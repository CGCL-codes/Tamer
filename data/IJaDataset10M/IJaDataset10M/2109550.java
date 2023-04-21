package core;

/**
 * @author  Vincenzo Frascino
 */
public class ChatUser implements UserInterface {

    String Username = new String();

    String Password = new String();

    /**
     * @uml.property  name="admin"
     */
    boolean admin = false;

    public ChatUser(String User, String Pwd) {
        this.Username = User;
        this.Password = Pwd;
    }

    public boolean setAdmin(String User, boolean admin) {
        if (this.Username == User) {
            this.admin = admin;
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return
     * @uml.property  name="admin"
     */
    public boolean isAdmin() {
        return this.admin;
    }

    public String getUser() {
        return this.Username;
    }

    public String getPwd() {
        return this.Password;
    }

    public void setUser(String Username, String Password) {
        this.Username = Username;
        this.Password = Password;
    }
}
