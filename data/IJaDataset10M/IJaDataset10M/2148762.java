package miloo;

/**
 * 
 * @author VERLOO Fabien
 */
public class Contact {

    private String login;

    private String email;

    private String status;

    public Contact(String login, String email, String status) {
        this.login = login;
        this.email = email;
        this.status = status;
    }

    public String getLogin() {
        return login;
    }

    public String getEmail() {
        return email;
    }

    public String getStatus() {
        return status;
    }
}
