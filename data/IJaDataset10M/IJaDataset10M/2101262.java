package uturismu.bean;

public class UserSignup implements UTurismuBean {

    private static final long serialVersionUID = -115963750298643358L;

    private String email;

    private String password;

    public UserSignup() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
