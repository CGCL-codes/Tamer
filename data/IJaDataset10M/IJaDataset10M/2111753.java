package com.simconomy.cas.client.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class User implements Serializable {

    private String email;

    private String password;

    private Map<String, Object[]> values = new HashMap<String, Object[]>();

    public User() {
        super();
    }

    public User(String email) {
        super();
        this.email = email;
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

    public void putValue(String key, Object object) {
        values.put(key, new Object[] { object });
    }

    public void putValue(String key, Object[] objects) {
        values.put(key, objects);
    }

    public Set<String> getKeys() {
        return values.keySet();
    }

    public Object[] getValue(String key) {
        return values.get(key);
    }
}
