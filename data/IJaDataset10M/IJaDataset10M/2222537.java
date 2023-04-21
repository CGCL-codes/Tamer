package com.restfb.types;

import java.util.Date;
import com.restfb.Facebook;

/**
 * Extension of RestFB
 * Represents an Open Authentication 2.0 object
 * 
 * Use this to store object's access token
 * 
 */
public class OAuth extends FacebookType {

    @Facebook("access_token")
    private String accessToken;

    @Facebook
    private int expires;

    public OAuth() {
        this.accessToken = "";
        this.expires = 0;
    }

    /**
   * The access token of the Object
   * 
   * @return A string representing the access token of the object
   */
    public String getAccessToken() {
        return accessToken;
    }

    /**
   * 
   * 
   * @return An integer representing when the access token will epire. 0 == No expiry
   */
    public int getExpires() {
        return expires;
    }
}
