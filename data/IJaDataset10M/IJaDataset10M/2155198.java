package realcix20.sessions;

/**
 *
 * @author JerryChen
 */
public class LoginEntry {

    private String user_id;

    private String password;

    private String lang;

    public LoginEntry() {
    }

    public LoginEntry(String user_id, String password, String lang) {
        this.setUser_id(user_id);
        this.setPassword(password);
        this.setLang(lang);
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}
