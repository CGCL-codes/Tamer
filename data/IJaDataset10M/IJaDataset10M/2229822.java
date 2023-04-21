package be.xios.mobile.project.webservice;

public class Login {

    protected java.lang.String login;

    protected java.lang.String passwd;

    public Login() {
    }

    public Login(java.lang.String login, java.lang.String passwd) {
        this.login = login;
        this.passwd = passwd;
    }

    public java.lang.String getLogin() {
        return login;
    }

    public void setLogin(java.lang.String login) {
        this.login = login;
    }

    public java.lang.String getPasswd() {
        return passwd;
    }

    public void setPasswd(java.lang.String passwd) {
        this.passwd = passwd;
    }
}
