package com.sri.scenewiz.persist;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import java.io.Serializable;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class Token implements Serializable {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    private String token;

    private String secret;

    private String userId;

    public Token(User user, String token, String secret) {
        this.token = token;
        this.userId = user.getUserId();
        this.secret = secret;
    }

    @SuppressWarnings({ "UnusedDeclaration" })
    public void setKey(Key key) {
        this.key = key;
    }

    @SuppressWarnings({ "UnusedDeclaration" })
    public Key getKey() {
        return this.key;
    }

    public String getToken() {
        return token;
    }

    @SuppressWarnings({ "UnusedDeclaration" })
    public void setToken(String token) {
        this.token = token;
    }

    public String getSecret() {
        return secret;
    }

    public String getUserId() {
        return userId;
    }
}
