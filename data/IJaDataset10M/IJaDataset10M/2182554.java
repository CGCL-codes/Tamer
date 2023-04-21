package net.sourceforge.iaxclient;

import net.sourceforge.iaxclient.jni.Constants;

public class Registration implements Constants {

    private int id;

    private String user;

    private String pass;

    private String host;

    Registration() {
        this(-1);
    }

    Registration(int id) {
        this(id, null, null, null);
    }

    Registration(int id, String user, String pass, String host) {
        this.id = id;
        this.user = user;
        this.pass = pass;
        this.host = host;
    }

    protected int getId() {
        return id;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return pass;
    }

    public String getHost() {
        return host;
    }

    public String toString() {
        return "" + id + " " + user + "@" + host;
    }
}
