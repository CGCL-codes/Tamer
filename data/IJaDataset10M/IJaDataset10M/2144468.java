package org.vtt.stylebase.system;

import org.vtt.stylebase.database.IDatabase;
import org.vtt.stylebase.database.MyDatabase;

public class SessionAdmin {

    public static final int ROLE_NONE = 0;

    public static final int ROLE_PROGRAMMER = 1;

    public static final int ROLE_ARCHITECT = 2;

    private static SessionAdmin instance = new SessionAdmin();

    private IDatabase database;

    private int userRole;

    private SessionAdmin() {
        this.database = new MyDatabase();
    }

    public IDatabase getDatabase() {
        return this.database;
    }

    public static SessionAdmin getInstance() {
        return instance;
    }

    public boolean setUser(String userName, String password, int role) {
        boolean success = false;
        if (this.database.authenticate(userName, password)) {
            this.userRole = role;
            success = true;
        }
        return success;
    }

    public void closeSession() {
        this.database.disconnectUser();
    }

    public boolean checkCurrentUser() {
        return this.database.connect();
    }

    public String getUser() {
        return this.database.getUserName();
    }

    public int getUserRole() {
        return this.userRole;
    }

    public String[] getUserRoles() {
        String[] roles = { "All", "Designer", "Architect" };
        return roles;
    }
}
