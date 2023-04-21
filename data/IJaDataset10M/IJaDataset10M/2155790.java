package org.gyx.elips.technical;

/**
	A factory which creates an authenticator for the class.
	@author Mehmood Shaikh
*/
public class AuthentificationFactory {

    public static Authentificator createInstance(String authenticationClass) throws Exception {
        Class cls = Class.forName(authenticationClass);
        Object obj = (Authentificator) cls.newInstance();
        return (Authentificator) obj;
    }
}
