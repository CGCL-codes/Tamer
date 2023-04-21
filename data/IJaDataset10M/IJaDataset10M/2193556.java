package core;

/**
 * Przechowuje login, haslo, i inne dane uzytkownika
 */
public class ID {

    String login, pass, host, cosinne;

    int port;

    /**
	 * @param host adres serwera
	 * @param l login
	 * @param p haslo
	 */
    public ID(String host, String l, String p) {
        login = l;
        pass = p;
        this.host = host;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
