package com.googlecode.webduff.authentication.provider;

public class BasicCredential implements Credential {

    private String username;

    private String password;

    public BasicCredential(String u, String p) {
        username = u;
        password = p;
    }

    public String getUsername() {
        return username;
    }

    public boolean checkPassword(String p) {
        return password.equals(p);
    }
}
