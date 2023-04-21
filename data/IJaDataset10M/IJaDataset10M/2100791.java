package services;

public class Login {

    private String userName;

    private String password;

    private boolean admin;

    public Login(String userName, String password, boolean admin) {
        this.userName = userName;
        this.password = password;
        this.admin = admin;
    }

    public Login(String userName, String password) {
        this.userName = userName;
        this.password = password;
        this.admin = false;
    }

    public boolean isLogin(String userNameDb, String passwordDb) {
        if (this.userName.equals(userNameDb) && this.password.equals(passwordDb)) return true;
        return false;
    }

    public boolean isAdmin() {
        return this.admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
