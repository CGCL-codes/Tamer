package org.ietf.jgss;

import java.security.Provider;
import java.security.Security;
import junit.framework.TestCase;

/**
 * Tests GSSManager class
 */
public class GSSManagerTest extends TestCase {

    /**
     * Tests loading of a default provider with valid class references.
     */
    public void testGetInstance_valid() {
        String oldProvider = Security.getProperty(GSSManager.MANAGER);
        try {
            Security.setProperty(GSSManager.MANAGER, TestManager.class.getName());
            GSSManager m = GSSManager.getInstance();
            assertNotNull(m);
            assertEquals(TestManager.class.getName(), m.getClass().getName());
        } finally {
            Security.setProperty(GSSManager.MANAGER, (oldProvider == null) ? "" : oldProvider);
        }
    }

    /**
     * Tests loading of a default provider with invalid class references.
     */
    public void testGetInstance_invalid() {
        String oldProvider = Security.getProperty(GSSManager.MANAGER);
        try {
            try {
                Security.setProperty(GSSManager.MANAGER, "a.b.c.D");
                GSSManager.getInstance();
                fail("should throw SecurityException for invalid klass");
            } catch (SecurityException ok) {
            }
            try {
                Security.setProperty(GSSManager.MANAGER, "");
                GSSManager.getInstance();
                fail("should throw SecurityException for empty klass");
            } catch (SecurityException ok) {
            }
        } finally {
            Security.setProperty(GSSManager.MANAGER, (oldProvider == null) ? "" : oldProvider);
        }
    }

    public static class TestManager extends GSSManager {

        @Override
        public void addProviderAtEnd(Provider p, Oid mech) throws GSSException {
        }

        @Override
        public void addProviderAtFront(Provider p, Oid mech) throws GSSException {
        }

        @Override
        public GSSContext createContext(byte[] interProcessToken) throws GSSException {
            return null;
        }

        @Override
        public GSSContext createContext(GSSCredential myCred) throws GSSException {
            return null;
        }

        @Override
        public GSSContext createContext(GSSName peer, Oid mech, GSSCredential myCred, int lifetime) throws GSSException {
            return null;
        }

        @Override
        public GSSCredential createCredential(GSSName name, int lifetime, Oid mech, int usage) throws GSSException {
            return null;
        }

        @Override
        public GSSCredential createCredential(GSSName name, int lifetime, Oid[] mechs, int usage) throws GSSException {
            return null;
        }

        @Override
        public GSSCredential createCredential(int usage) throws GSSException {
            return null;
        }

        @Override
        public GSSName createName(byte[] name, Oid nameType, Oid mech) throws GSSException {
            return null;
        }

        @Override
        public GSSName createName(byte[] name, Oid nameType) throws GSSException {
            return null;
        }

        @Override
        public GSSName createName(String nameStr, Oid nameType, Oid mech) throws GSSException {
            return null;
        }

        @Override
        public GSSName createName(String nameStr, Oid nameType) throws GSSException {
            return null;
        }

        @Override
        public Oid[] getMechs() {
            return null;
        }

        @Override
        public Oid[] getMechsForName(Oid nameType) {
            return null;
        }

        @Override
        public Oid[] getNamesForMech(Oid mech) throws GSSException {
            return null;
        }
    }
}
