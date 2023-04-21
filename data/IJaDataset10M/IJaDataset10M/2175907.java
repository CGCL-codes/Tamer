package com.juanfer.travelcostlog;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.appengine.api.datastore.Key;

@PersistenceCapable(detachable = "true")
public class Traveler {

    @Persistent(defaultFetchGroup = "true")
    private String name;

    @Persistent(defaultFetchGroup = "true")
    private String email;

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY, defaultFetchGroup = "true")
    private Key key;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
