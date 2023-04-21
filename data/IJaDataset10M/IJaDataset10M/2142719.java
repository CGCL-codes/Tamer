package com.krobothsoftware.network.authorization;

import java.net.HttpURLConnection;
import com.krobothsoftware.network.util.Base64Coder;

/**
 * The Class BasicAuthorization using the basic scheme.
 * 
 * @since 1.0
 * @author Kyle Kroboth
 */
public class BasicAuthorization extends Authorization {

    /**
	 * Instantiates a new basic authorization.
	 * 
	 * @param username
	 *            the username
	 * @param password
	 *            the password
	 */
    public BasicAuthorization(String username, String password) {
        super(username, password);
    }

    @Override
    public void setup(HttpURLConnection urlConnection) {
        String encoded = Base64Coder.encodeString(username + ":" + password);
        encoded = encoded.replace("\n", "");
        urlConnection.setRequestProperty("Authorization", "Basic " + encoded);
    }

    @Override
    public void reset() {
    }
}
