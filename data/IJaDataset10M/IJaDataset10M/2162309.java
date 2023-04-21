package net.sf.jerkbot.session;

import java.security.Principal;

/**
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 *         <p>The jerkbot session service. The default implementation uses the LoginModuleService hidden from this interface
 *         The LoginModuleService is dynamically resolved at runtime if the authentication plugin is installed
 *         If there's no LoginModule service available there's not authentication performed and no session created.</p>
 * @version 0.0.1
 */
public interface UserSessionService {

    /**
     * Creates a new session for a given nick/hostname pair and a database entity associated to a user
     *
     * @param key The user key
     * @param admin The administrative user instance from the database
     */
    void createSession(UserKey key, Principal admin);

    /**
     * Destroy a session
     *
     * @param key The session key
     */
    void destroySession(UserKey key);

    /**
     * Updates the session timeout(through the jmx command in the default implementation as JMX is only enable for administrative users)
     *
     * @param key The user key
     */
    void updateSessionTimeOut(UserKey key);

    /**
     * Returns the current active session if any
     *
     * @param key The user key
     * @return The active session associated to the given key
     */
    UserSession getActiveSession(UserKey key);

    /**
     * Returns the permit instance valid until the expiration date
     *
     * @param key The user key
     * @param expirationDate The permit expiration date
     * @return the permit instance valid until the expiration date
     */
    UserPermit createPermit(UserKey key, long expirationDate);

    /**
     * Returns an existing permit for an active administrative user
     *
     * @param key The user key
     * @return an existing permit for an active administrative user
     */
    UserPermit getPermit(UserKey key);

    /**
     * Tells whether or not an administrator is a member of a given group
     *
     * @param admin     An administrative user
     * @param groupName An administrative group
     * @return whether or not an administrator is a member of a given group
     */
    boolean isInRole(Principal admin, String groupName);
}
