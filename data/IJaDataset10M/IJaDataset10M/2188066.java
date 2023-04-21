package net.sf.jradius.tests;

import java.security.PrivilegedAction;

/**
 * @author David Bird
 */
class TestAction implements PrivilegedAction {

    public Object run() {
        System.out.println("Running TestAction...");
        return null;
    }
}
