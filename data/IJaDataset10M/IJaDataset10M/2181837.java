package eu.mpower.framework.sensor.Camera;

/**
 *
 * @author SFM
 */
public class UserPassword {

    private String User;

    private String Password;

    private String realm;

    public UserPassword(String User, String Password) {
        this.User = User;
        this.Password = Password;
    }

    public UserPassword(String User, String Password, String realm) {
        this.User = User;
        this.Password = Password;
        this.realm = realm;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String User) {
        this.User = User;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }
}
