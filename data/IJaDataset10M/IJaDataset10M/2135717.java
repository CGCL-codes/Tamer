package borknet_services.core;

import borknet_services.core.*;

public class Auth {

    private String authnick;

    private String password;

    private String mail;

    private int level;

    private int suspended;

    private Long last;

    private String info;

    private String userflags;

    private String vhost;

    public Auth(String authnick) {
        this.authnick = authnick;
    }

    public String getAuthnick() {
        return authnick;
    }

    public void setAuthnick(String s) {
        authnick = s;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String s) {
        password = s;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String s) {
        mail = s;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(int s) {
        level = s;
    }

    public Integer getSuspended() {
        return suspended;
    }

    public void setSuspended(int s) {
        suspended = s;
    }

    public Long getLast() {
        return last;
    }

    public void setLast(Long s) {
        last = s;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String s) {
        info = s;
    }

    public String getUserflags() {
        return userflags;
    }

    public void setUserflags(String s) {
        userflags = s;
    }

    public String getVHost() {
        return vhost;
    }

    public void setVHost(String s) {
        vhost = s;
    }
}
