package org.dvb.net.tuning;

/**
 * This class is for tuner permissions. A TunerPermission contains no name and
 * no actions list. If an application has the tuner permission, then it shall
 * not receive a <code>SecurityException</code> from those methods in that API
 * defined to throw one. Without such a permission, it shall receive such an
 * exception.
 */
public class TunerPermission extends java.security.BasicPermission {

    /**
	 * Creates a new TunerPermission. The name string is currently unused and
	 * should be empty.
	 * @param name the name of the TunerPermission.
	 */
    public TunerPermission(String name) {
        super(name);
    }

    /**
	 * Creates a new TunerPermission. The name string is currently unused and
	 * should be empty. The actions string is currently unused and should be
	 * null. This constructor exists for use by the Policy object to instantiate
	 * new Permission objects.
	 * @param name the name of the TunerPermission.
	 * @param actions the actions list
	 */
    public TunerPermission(String name, String actions) {
        super(name, actions);
    }

    /**
	 * Checks if the specified permission is "implied" by this object.
	 * <p>
	 * Since name and actions aren't used, the only check needed is whether p is
	 * also a TunerPermission.
	 * @param p the permission to check against.
	 * @return true if the passed permission is equal to or implied by this
	 *         permission, false otherwise.
	 */
    public boolean implies(java.security.Permission p) {
        return false;
    }
}
