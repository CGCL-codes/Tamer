package universe.server.database;

/**
 *
 * @version $Id: PlayerHuman.java,v 1.4 2001/10/10 06:08:23 sstarkey Exp $
 */
public class PlayerHuman extends PlayerBase {

    private boolean created;

    private String login;

    private String password;

    public PlayerHuman() {
        created = false;
    }

    public boolean isCreated() {
        return created;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void create(String login, String password) {
        this.login = login;
        this.password = password;
        created = true;
    }

    public void quit() {
        this.login = null;
        this.password = null;
        created = false;
    }
}
