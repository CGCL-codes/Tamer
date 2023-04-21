package org.apache.harmony.jndi.tests.javax.naming.ldap;

import javax.naming.NamingException;
import javax.naming.ldap.Control;
import javax.naming.ldap.UnsolicitedNotification;

public class MockUnsolicitedNotification implements UnsolicitedNotification {

    /**
     * <p></p>
     */
    private static final long serialVersionUID = 1L;

    public String[] getReferrals() {
        return null;
    }

    public NamingException getException() {
        return null;
    }

    public String getID() {
        return null;
    }

    public byte[] getEncodedValue() {
        return null;
    }

    public Control[] getControls() throws NamingException {
        return null;
    }
}
