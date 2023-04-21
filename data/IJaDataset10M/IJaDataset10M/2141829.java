package de.fhg.igd.semoa.service;

import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import de.fhg.igd.logging.Logger;
import de.fhg.igd.logging.LoggerFactory;
import de.fhg.igd.semoa.server.Environment;
import de.fhg.igd.semoa.server.Service;
import de.fhg.igd.util.WhatIs;

/**
 * This abstract class is extended by each SeMoA service;
 * The methods defined in it return information on the service
 * such as its author, version and description.<p>
 *
 * An important feature of this interface is a method which should
 * return a commented API for the service. This should enable agents
 * to roam in a network collecting interfaces of new services
 * which can be used by a programmer to create agents which
 * subsequently access those services.<p>
 *
 * On creation, a service instance will store away the current
 * <code>AccessControlContext</code>. Whenever a service executes a
 * <code>PrivilegedAction</code> it should pass this context in
 * order to limit the rights to those of the caller. The context
 * may be retrieved using the {@link #getACC() getACC} method.<p>
 *
 * @author Volker Roth
 * @version $Id: AbstractService.java 1913 2007-08-08 02:41:53Z jpeters $
 */
public abstract class AbstractService extends Object implements Service {

    /** 
     * The <code>Logger</code> instance for this class 
     */
    private static Logger log_ = LoggerFactory.getLogger("semoa/core");

    /**
     * The prefix of the revision identifier string.
     */
    public static final String REV_PREFIX = "$Revision: ";

    /**
     * The postfix of the revision identifier string.
     */
    public static final String REV_POSTFIX = "$";

    /**
     * Holds the Object which serves as the Service's lock.
     */
    private Object lock_ = new Object();

    /**
     * The private <code>AccessControlContext</code> that was
     * retrieved on creation of this service. This context should
     * be used whenever a privileged action is executed.
     */
    private AccessControlContext acc_;

    /**
     * The {@link Environment Environment} of this service.
     * It is captured upon creation. Since getting the <code>
     * Environment</code> requires that the current thread's
     * calling stack is clean, in appropriate creation of
     * instances may cause an <code>AccesControlException
     * </code>.
     */
    private Environment environment_;

    /**
     * Creates a Service instance.
     */
    protected AbstractService() {
        acc_ = AccessController.getContext();
        environment_ = Environment.getEnvironment();
        if (!checkDependencies()) {
            log_.severe("Dependency check failed for SeMoA service '" + this.getClass().getName() + "'");
            throw new RuntimeException("Dependency check failed for SeMoA service '" + this.getClass().getName() + "'");
        } else {
            log_.trace("Dependency check successfull for SeMoA service '" + this.getClass().getName() + "'");
        }
    }

    /**
     * Returns the <code>Environment</code> that was inherited
     * when this instance was created.
     *
     * @return The <code>Environment</code>.
     */
    protected Environment getEnvironment() {
        return environment_;
    }

    /**
     * Returns the String representation of an URL where
     * documentation on this service can be found such as
     * the interface description and rationale.<p>
     *
     * This method provides a default implementation that
     * appends the class name plus &quot;.html&quot; to the
     * value of the {@link WhatIs WhatIs} key <code>
     * SEMOA_DOCS_URL</code>. Periods in the class name are
     * converted to slashes first.
     *
     * @return The URL to documentation of this class as a
     *   string.
     */
    public String docs() {
        String name;
        name = getClass().getName().replace('.', '/');
        return WhatIs.stringValue("SEMOA_DOCS_URL") + name + ".html";
    }

    /**
     * Returns the informative string which should describe the
     * service's essence in a sentence.
     *
     * @return the String with the service's short description.
     */
    public abstract String info();

    /**
     * Returns the name of the author of the service. The format
     * to be used should be <i>John Doe &lt;jdoe@zilch.net&gt;</i>.
     *
     * @return the String with the author's name.
     */
    public abstract String author();

    /**
     * @return The major version number of this service, or -1
     *   if the major version cannot be determined.
     * @see #revision()
     */
    public int majorVersion() {
        String v;
        int n;
        v = revision();
        try {
            if (v != null && v.startsWith(REV_PREFIX)) {
                n = v.indexOf('.', REV_PREFIX.length());
                if (n > 0) {
                    return Integer.parseInt(v.substring(REV_PREFIX.length(), n));
                }
            }
        } catch (Exception e) {
        }
        return -1;
    }

    /**
     * @return The minor version number of this service, or -1
     *   if the minor version cannot be determined.
     * @see #revision()
     */
    public int minorVersion() {
        String v;
        int n;
        int m;
        v = revision();
        try {
            if (v != null && v.startsWith(REV_PREFIX)) {
                n = v.indexOf('.', REV_PREFIX.length());
                if (n > 0) {
                    m = v.indexOf(REV_POSTFIX);
                    if (m > 0) {
                        return Integer.parseInt(v.substring(n, m));
                    }
                }
            }
        } catch (Exception e) {
        }
        return -1;
    }

    /**
     * Returns the revision number of this class as a string.
     * This class provides default implementations of methods
     * <code>majorVersion()</code>, <code>minorVersion</code>
     * based on the assumption that this method returns a
     * revision string as generated by rcs(1) for the template
     * &quot;$Revision: 1913 $/$Date: 2007-08-07 22:41:53 -0400 (Tue, 07 Aug 2007) $&quot;.
     */
    public abstract String revision();

    /**
     * Checks the service dependencies returned by
     * <code>dependencies()</code> as described in
     * {@link de.fhg.igd.semoa.server.Service Service}. 
     *
     * @return <code>true</code>, if 
     */
    public boolean checkDependencies() {
        String[] dep;
        String key;
        int n;
        int i;
        dep = dependencies();
        if (dep == null) {
            return true;
        }
        for (i = 0; i < dep.length; i++) {
            if (dep[i] == null) {
                continue;
            }
            key = dep[i].trim();
            if (key.length() >= 7 && key.substring(0, 7).equalsIgnoreCase("whatis:")) {
                key = key.substring(7);
                log_.trace("whatis module '" + key + "'");
                key = WhatIs.stringValue(key);
            }
            log_.trace("check service path '" + key + "'");
            if (key == null) {
                log_.warning("service path is null");
                return false;
            }
            if (environment_.lookup(key) == null) {
                log_.warning("service '" + key + "' not found");
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the dependencies of this service as described in
     * {@link de.fhg.igd.semoa.server.Service Service}. This default 
     * implementation returns an array of length 0.
     *
     * @return An array of length 0.
     */
    public String[] dependencies() {
        return new String[0];
    }

    /**
     * @return The string representation of this instance. This
     *   default implementation returns information on the
     *   author, version, revision, and general info.
     */
    public String toString() {
        return "Name    : " + getClass().getName() + "\nInfo    : " + info() + "\nAuthor  : " + author() + "\nRevision: " + revision();
    }

    /**
     * Retrieves the private AccessControlContext that was valid
     * upon creating this service.
     *
     * @return The AccessControlContext.
     */
    protected final AccessControlContext getACC() {
        return acc_;
    }

    /**
     * This convenience method calls the AccessController
     * method<p>
     * <code>
     *   public Object doPrivileged(
     *     PrivilegedAction action,
     *     AccessControlContext context)
     * </code>
     * <p>
     * and passes the AccessControlContext of this service.
     * The AccessControlContext of this service may be retrieved
     * by calling {@link #getACC getACC()}. This method is
     * declared final such that subclassing does not open a hole
     * that allows to circumvent this check.
     *
     * @param action The PrivilegedAction to be executed.
     * @return whatever the call to the AccessController returns.
     */
    protected final Object doPrivileged(PrivilegedAction action) {
        return AccessController.doPrivileged(action, getACC());
    }

    /**
     * This convenience method calls the AccessController
     * method<p>
     * <code>
     *   public Object doPrivileged(
     *     PrivilegedExceptionAction action,
     *     AccessControlContext context)
     *       throws PrivilegedActionException
     * </code>
     * <p>
     * and passes the AccessControlContext of this service.
     * The AccessControlContext of this service may be retrieved
     * by calling {@link #getACC getACC()}. This method is
     * declared final such that subclassing does not open a hole
     * that allows to circumvent this check.
     *
     * @param action The PrivilegedAction to be executed.
     * @return whatever the call to the AccessController returns.
     */
    protected final Object doPrivileged(PrivilegedExceptionAction action) throws PrivilegedActionException {
        return AccessController.doPrivileged(action, getACC());
    }
}
