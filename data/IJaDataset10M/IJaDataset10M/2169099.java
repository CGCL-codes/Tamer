package net.sourceforge.htmlunit.corejs.javascript;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;

/**
 * @author Attila Szegedi
 */
public class SecurityUtilities {

    /**
     * Retrieves a system property within a privileged block. Use it only when
     * the property is used from within Rhino code and is not passed out of it.
     * @param name the name of the system property
     * @return the value of the system property
     */
    public static String getSystemProperty(final String name) {
        return (String) AccessController.doPrivileged(new PrivilegedAction<Object>() {

            public Object run() {
                return System.getProperty(name);
            }
        });
    }

    public static ProtectionDomain getProtectionDomain(final Class<?> clazz) {
        return (ProtectionDomain) AccessController.doPrivileged(new PrivilegedAction<Object>() {

            public Object run() {
                return clazz.getProtectionDomain();
            }
        });
    }
}
