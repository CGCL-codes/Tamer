package org.apache.naming;

import java.util.Hashtable;

/**
 * Handles the access control on the JNDI contexts.
 *
 * @author Remy Maucherat
 * @version $Revision: 467222 $ $Date: 2006-10-24 05:17:11 +0200 (Tue, 24 Oct 2006) $
 */
public class ContextAccessController {

    /**
     * Catalina context names on which writing is not allowed.
     */
    private static Hashtable readOnlyContexts = new Hashtable();

    /**
     * Security tokens repository.
     */
    private static Hashtable securityTokens = new Hashtable();

    /**
     * Set a security token for a context. Can be set only once.
     * 
     * @param name Name of the context
     * @param token Security token
     */
    public static void setSecurityToken(Object name, Object token) {
        if ((!securityTokens.containsKey(name)) && (token != null)) {
            securityTokens.put(name, token);
        }
    }

    /**
     * Remove a security token for a context.
     * 
     * @param name Name of the context
     * @param token Security token
     */
    public static void unsetSecurityToken(Object name, Object token) {
        if (checkSecurityToken(name, token)) {
            securityTokens.remove(name);
        }
    }

    /**
     * Check a submitted security token. The submitted token must be equal to
     * the token present in the repository. If no token is present for the 
     * context, then returns true.
     * 
     * @param name Name of the context
     * @param token Submitted security token
     */
    public static boolean checkSecurityToken(Object name, Object token) {
        Object refToken = securityTokens.get(name);
        if (refToken == null) return (true);
        if ((refToken != null) && (refToken.equals(token))) return (true);
        return (false);
    }

    /**
     * Allow writing to a context.
     * 
     * @param name Name of the context
     * @param token Security token
     */
    public static void setWritable(Object name, Object token) {
        if (checkSecurityToken(name, token)) readOnlyContexts.remove(name);
    }

    /**
     * Set whether or not a context is writable.
     * 
     * @param name Name of the context
     */
    public static void setReadOnly(Object name) {
        readOnlyContexts.put(name, name);
    }

    /**
     * Returns if a context is writable.
     * 
     * @param name Name of the context
     */
    public static boolean isWritable(Object name) {
        return !(readOnlyContexts.containsKey(name));
    }
}
