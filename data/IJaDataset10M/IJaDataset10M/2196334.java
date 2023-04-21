package org.atricore.idbus.kernel.main.authn.scheme;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.atricore.idbus.kernel.main.authn.Credential;
import org.atricore.idbus.kernel.main.authn.CredentialProvider;

/**
 * @org.apache.xbean.XBean element="basic-auth-credential-provider" 
 */
public class UsernamePasswordCredentialProvider implements CredentialProvider {

    /**
     * The name of the credential representing a password.
     * Used to get a new credential instance based on its name and value.
     * Value : password
     *
     * @see Credential newCredential(String name, Object value)
     */
    public static final String PASSWORD_CREDENTIAL_NAME = "password";

    /**
     * The name of the credential representing a username.
     * Used to get a new credential instance based on its name and value.
     * Value : username
     *
     * @see Credential newCredential(String name, Object value)
     */
    public static final String USERNAME_CREDENTIAL_NAME = "username";

    private static final Log logger = LogFactory.getLog(UsernamePasswordCredentialProvider.class);

    /**
     * Creates a new credential based on its name and value.
     *
     * @param name  the credential name
     * @param value the credential value
     * @return the Credential instance representing the supplied name-value pair.
     */
    public Credential newCredential(String name, Object value) {
        if (name.equalsIgnoreCase(USERNAME_CREDENTIAL_NAME)) {
            return new UsernameCredential(value);
        }
        if (name.equalsIgnoreCase(PASSWORD_CREDENTIAL_NAME)) {
            return new PasswordCredential(value);
        }
        if (logger.isDebugEnabled()) logger.debug("Unknown credential name : " + name);
        return null;
    }

    /**
     * Creates a new 'encoded credential'
     * @param name
     * @param value
     * @return
     */
    public Credential newEncodedCredential(String name, Object value) {
        return newCredential(name, value);
    }
}
