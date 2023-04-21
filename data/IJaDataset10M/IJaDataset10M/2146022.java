package com.esri.gpt.framework.security.credentials;

/**
 * Occurs when a new password violates policy.
 */
public class PasswordPolicyException extends CredentialPolicyException {

    /**
 * Construct based upon an error message.
 * @param msg the error message
 */
    public PasswordPolicyException(String msg) {
        super(msg);
    }
}
