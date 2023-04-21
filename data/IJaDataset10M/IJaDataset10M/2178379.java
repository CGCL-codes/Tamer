package net.assimilator.springcontainer.jericonnector;

import java.net.URL;
import java.rmi.RMISecurityManager;
import java.security.*;
import java.util.logging.Logger;

/**
 * Utility class to load a security policy for a class and set the policy as the JVM's
 * security policy.
 *
 * @author Kevin Hartig
 * @version $Id: SecurityPolicyLoader.java 4603 2007-02-02 01:57:44Z kevin.hartig $
 */
public class SecurityPolicyLoader {

    /**
     * The Logger for this class
     */
    private static final Logger logger = Logger.getLogger("net.assimilator.jericonnector");

    /**
     * Load and set the java.security.policy property for a Class
     *
     * @param clazz      The Class that will be used to get the policy as a resource
     * @param policyName The name of the policy file to load
     */
    public static void load(Class clazz, String policyName) {
        Policy.setPolicy(new Policy() {

            public PermissionCollection getPermissions(CodeSource codesource) {
                Permissions perms = new Permissions();
                perms.add(new AllPermission());
                return (perms);
            }

            public void refresh() {
            }
        });
        if (System.getSecurityManager() != null) {
            return;
        } else {
            System.setSecurityManager(new RMISecurityManager());
        }
        try {
            URL policy = clazz.getResource(policyName);
            if (policy == null) {
                logger.warning("Warning: can't find [" + clazz.getPackage() + "." + policyName + "] resource");
            } else {
                System.setProperty("java.security.policy", policy.toString());
            }
            System.getSecurityManager().checkPermission(new RuntimePermission("getClassLoader"));
        } catch (SecurityException se) {
            se.printStackTrace();
            logger.severe("Failed to Load Security Manager.");
        }
    }
}
