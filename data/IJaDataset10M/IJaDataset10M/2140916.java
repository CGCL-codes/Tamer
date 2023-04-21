package org.apache.xerces.impl.dv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

/**
 * This class is duplicated for each subpackage so keep it in sync.
 * It is package private and therefore is not exposed as part of any API.
 * 
 * @xerces.internal
 */
final class SecuritySupport {

    static ClassLoader getContextClassLoader() {
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction() {

            public Object run() {
                ClassLoader cl = null;
                try {
                    cl = Thread.currentThread().getContextClassLoader();
                } catch (SecurityException ex) {
                }
                return cl;
            }
        });
    }

    static ClassLoader getSystemClassLoader() {
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction() {

            public Object run() {
                ClassLoader cl = null;
                try {
                    cl = ClassLoader.getSystemClassLoader();
                } catch (SecurityException ex) {
                }
                return cl;
            }
        });
    }

    static ClassLoader getParentClassLoader(final ClassLoader cl) {
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction() {

            public Object run() {
                ClassLoader parent = null;
                try {
                    parent = cl.getParent();
                } catch (SecurityException ex) {
                }
                return (parent == cl) ? null : parent;
            }
        });
    }

    static String getSystemProperty(final String propName) {
        return (String) AccessController.doPrivileged(new PrivilegedAction() {

            public Object run() {
                return System.getProperty(propName);
            }
        });
    }

    static FileInputStream getFileInputStream(final File file) throws FileNotFoundException {
        try {
            return (FileInputStream) AccessController.doPrivileged(new PrivilegedExceptionAction() {

                public Object run() throws FileNotFoundException {
                    return new FileInputStream(file);
                }
            });
        } catch (PrivilegedActionException e) {
            throw (FileNotFoundException) e.getException();
        }
    }

    static InputStream getResourceAsStream(final ClassLoader cl, final String name) {
        return (InputStream) AccessController.doPrivileged(new PrivilegedAction() {

            public Object run() {
                InputStream ris;
                if (cl == null) {
                    ris = ClassLoader.getSystemResourceAsStream(name);
                } else {
                    ris = cl.getResourceAsStream(name);
                }
                return ris;
            }
        });
    }

    static boolean getFileExists(final File f) {
        return ((Boolean) AccessController.doPrivileged(new PrivilegedAction() {

            public Object run() {
                return f.exists() ? Boolean.TRUE : Boolean.FALSE;
            }
        })).booleanValue();
    }

    static long getLastModified(final File f) {
        return ((Long) AccessController.doPrivileged(new PrivilegedAction() {

            public Object run() {
                return new Long(f.lastModified());
            }
        })).longValue();
    }

    private SecuritySupport() {
    }
}
