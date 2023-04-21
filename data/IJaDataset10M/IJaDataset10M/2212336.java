package com.ordocalendarws.model.objects;

import javax.jdo.annotations.PersistenceCapable;
import twitter4j.auth.RequestToken;

@PersistenceCapable
public class OAuthRequestToken extends OAuthToken {

    public OAuthRequestToken(RequestToken requestToken) {
        super("sanctoral", requestToken.getToken(), requestToken.getTokenSecret());
    }
}
