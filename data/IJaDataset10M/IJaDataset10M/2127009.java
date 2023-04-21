package net.sf.f2s.service;

/**
 * This listener keeps an track on the Authentication process.
 * 
 * @author Rogiel
 * @since 1.0
 */
public interface AuthenticatorListener {

    /**
	 * The username and password informed was not valid.
	 * 
	 * @param credential
	 *            the authenticating credential
	 */
    void invalidCredentials(Credential credential);

    /**
	 * The username and password informed was valid. User is authenticated.
	 * 
	 * @param credential
	 *            the authenticating credential
	 */
    void loginSuccessful(Credential credential);

    void logout(Credential credential);
}
