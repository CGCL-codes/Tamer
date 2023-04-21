package de.sicari.demo.mockup;

import java.security.PermissionCollection;
import de.sicari.util.AbstractPermission;

/**
 * @author Jan Peters
 * @version $Id: MockupPermission.java 204 2007-07-11 19:26:55Z jpeters $
 */
public class MockupPermission extends AbstractPermission {

    /**
     * The list of actions supported by this class.
     */
    private static final String[] acronyms_ = { "setUserDB", "getProperty", "getPhoto", "getCertificate" };

    public MockupPermission(String name, String actions) {
        super(name, actions, acronyms_);
    }

    /**
     * Returns the actions string.
     *
     * @return The actions string.
     */
    public String getActions() {
        return super.getActions(acronyms_);
    }

    /**
     * Returns a string representation of this instance.
     *
     * @return The string representation.
     */
    public String toString() {
        return super.toString(acronyms_);
    }

    /**
     * DOCUMENT ME!
     *
     * @return A new <code>PermissionCollection</code> for
     *         <code>VariablesPermission</code> instances.
     */
    public PermissionCollection newPermissionCollection() {
        return super.newPermissionCollection();
    }
}
