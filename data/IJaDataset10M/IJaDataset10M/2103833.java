package com.technoetic.xplanner.security;

import java.io.Serializable;
import java.util.Map;
import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;

public interface LoginModule extends Serializable {

    String MESSAGE_NULL_PASSWORD_KEY = "authentication.module.message.passwordNotSet";

    String MESSAGE_SERVER_ERROR_KEY = "authentication.module.message.serverError";

    String MESSAGE_AUTHENTICATION_FAILED_KEY = "authentication.module.message.authenticationFailed";

    String MESSAGE_USER_NOT_FOUND_KEY = "authentication.module.message.userNotFound";

    String MESSAGE_COMMUNICATION_ERROR_KEY = "authentication.module.message.communicationError";

    String MESSAGE_SERVER_NOT_FOUND_KEY = "authentication.module.message.serverNotFound";

    String MESSAGE_CONFIGURATION_ERROR_KEY = "authentication.module.message.serverConfigurationError";

    String MESSAGE_NO_MODULE_NAME_SPECIFIED_ERROR_KEY = "authentication.module.message.serverConfigurationError.noModuleName";

    String ATTEMPTING_TO_AUTHENTICATE = "Attempting to authenticate with login module: ";

    String AUTHENTICATION_SUCCESFULL = "Authentication successful with login module: ";

    /**
    * Authenticates a user through some specific mechansism.
    * @param userId
    * @param password
    * @return A Subject containing at least a Person principal and one or more Role principals.
    * @throws AuthenticationException
    */
    Subject authenticate(String userId, String password) throws AuthenticationException;

    /**
     * Predicate that indicates whether this module can modify passwords.
     * @return True if password can be changed, false otherwise.
     */
    boolean isCapableOfChangingPasswords();

    /**
     * Changes a user's password.
     * @param userId
     * @param password
     * @throws AuthenticationException if password cannot be changed.
     */
    void changePassword(String userId, String password) throws AuthenticationException;

    /**
     * Log out a user. At a minimum this method should invalidate the user's session.
     * @throws AuthenticationException
     */
    void logout(HttpServletRequest request) throws AuthenticationException;

    String getName();

    void setName(String name);

    void setOptions(Map options);
}
