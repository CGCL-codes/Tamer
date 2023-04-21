package Negócio;

/**
 *
 * @author GABRIEL
 */
public class Amigo {

    private String nick;

    private String email;

    public Amigo(String nick, String email) {
        this.email = email;
        this.nick = nick;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
}
