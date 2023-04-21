package de.fhg.igd.semoa.server;

import de.fhg.igd.util.AbstractPermission;
import de.fhg.igd.util.CanonicalPath;

/**
 * Represents permissions to read and modify entries in the
 * global <code>Environment</code>. The target name is the
 * path or pattern that refers to one or multiple objects
 * in the global <code>Environment</code>. The following
 * actions are supported:
 * <dl>
 * <dt> lookup
 * <dd> Request the reference to a (proxy) object.
 * <dt> retract
 * <dd> Remove a (proxy) object.
 * <dt> master
 * <dd> Get the reference to the wrapped object of a proxy.
 *   This is a highly security sensitive operation, guard
 *   this permission with care!
 * <dt> watch
 * <dd> Add a watch path to the global environment. Watch
 *   paths allow to track the time of last modification to
 *   a set of entries whose paths start with the given
 *   watch path.
 * <dt> callback
 * <dd> Register an <code>EnvironmentCallback</code> interface.
 * <dt> publish
 * <dd> Make a (proxy) object visibal globally.
 * <dt> NONE
 * <dd> Permission to publish objects without a wrapping
 *   proxy.
 * <dt> PLAIN
 * <dd> Permission to publish objects with a plain proxy
 *   that supports cutting off the reference to the wrapped
 *   object.
 * <dt> ASYNC
 * <dd> Permission to publish objects with an async proxy
 *   that supports asynchronous method invocation on top
 *   of cutt-off.
 * <dt> FULL
 * <dd> Permission to publish objects with a full proxy
 *   that supports asynchronous method invocation, cut-off,
 *   marshalling and unmarshalling of method arguments and
 *   results.
 * </dl>
 *
 * @author Volker Roth
 * @version "$Id: EnvironmentPermission.java 1913 2007-08-08 02:41:53Z jpeters $"
 */
public class EnvironmentPermission extends AbstractPermission {

    /**
     * The list os actions supported by this permission class.
     */
    private static String[] acronyms_ = { "lookup", "retract", "master", "watch", "publish", "callback", "NONE", "PLAIN", "ASYNC", "FULL" };

    /**
     * The <code>CanonicalPath</code> of this instance.
     */
    private CanonicalPath path_;

    /**
     * Creates an instance with the given path and actions.
     *
     * @param path The path; the separator must be a slash.
     * @param actions The list of actions.
     */
    public EnvironmentPermission(String path, String actions) {
        super(path, actions, acronyms_);
        path_ = new CanonicalPath(path, '/');
    }

    /**
     * Creates an instance with the given path and actions.
     *
     * @param path The path.
     * @param actions The list of actions.
     */
    public EnvironmentPermission(CanonicalPath path, String actions) {
        super(path.toString(), actions, acronyms_);
        path_ = path;
    }

    public String getActions() {
        return super.getActions(acronyms_);
    }

    public String toString() {
        return super.toString(acronyms_);
    }

    public boolean implies(java.security.Permission p) {
        if (p == null || getClass() != p.getClass()) {
            return false;
        }
        EnvironmentPermission that;
        int m;
        that = (EnvironmentPermission) p;
        m = that.getActionsMask();
        if ((m == 0) || ((getActionsMask() & m) != m)) {
            return false;
        }
        return path_.implies(that.path_);
    }

    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EnvironmentPermission that;
        that = (EnvironmentPermission) o;
        if (getActionsMask() != that.getActionsMask()) {
            return false;
        }
        if (path_ == that.path_) {
            return true;
        }
        return path_.equals(that.path_);
    }
}
