package org.enilu.domain;

public class Host {

    private String ip;

    private String userName;

    private String password;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
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

    public Host(String ip, String userName, String password) {
        super();
        this.ip = ip;
        this.userName = userName;
        this.password = password;
    }

    public Host() {
        super();
    }
}
