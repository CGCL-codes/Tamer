package server.messages;

public class MsgRegistrationRequest extends ServerMessage {

    String userName;

    String password;

    String email;

    public MsgRegistrationRequest(String name, String pass, String email, long id) {
        super(Type.REGISTRATION_REQUEST, id);
        this.userName = name;
        this.password = pass;
        this.email = email;
    }

    public MsgRegistrationRequest(long id) {
        this("", "", "", id);
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
