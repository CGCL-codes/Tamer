package com.yoursway.crashkit.internal.model;

public class ClientCredentials {

    private final String id;

    private final String cookie;

    public ClientCredentials(String id, String cookie) {
        if (id == null) throw new NullPointerException("id is null");
        if (cookie == null) throw new NullPointerException("cookie is null");
        this.id = id;
        this.cookie = cookie;
    }

    public String id() {
        return id;
    }

    public String cookie() {
        return cookie;
    }
}
