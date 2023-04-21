package org.apache.catalina.authenticator;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import org.apache.catalina.Authenticator;
import org.apache.catalina.Container;
import org.apache.catalina.Context;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.Pipeline;
import org.apache.catalina.Realm;
import org.apache.catalina.Session;
import org.apache.catalina.Valve;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.deploy.LoginConfig;
import org.apache.catalina.deploy.SecurityConstraint;
import org.apache.catalina.util.DateTool;
import org.apache.catalina.util.LifecycleSupport;
import org.apache.catalina.util.StringManager;
import org.apache.catalina.valves.ValveBase;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

/**
 * Basic implementation of the <b>Valve</b> interface that enforces the
 * <code>&lt;security-constraint&gt;</code> elements in the web application
 * deployment descriptor.  This functionality is implemented as a Valve
 * so that it can be ommitted in environments that do not require these
 * features.  Individual implementations of each supported authentication
 * method can subclass this base class as required.
 * <p>
 * <b>USAGE CONSTRAINT</b>:  When this class is utilized, the Context to
 * which it is attached (or a parent Container in a hierarchy) must have an
 * associated Realm that can be used for authenticating users and enumerating
 * the roles to which they have been assigned.
 * <p>
 * <b>USAGE CONSTRAINT</b>:  This Valve is only useful when processing HTTP
 * requests.  Requests of any other type will simply be passed through.
 *
 * @author Craig R. McClanahan
 * @version $Revision: 500626 $ $Date: 2007-01-27 22:25:41 +0100 (Sat, 27 Jan 2007) $
 */
public abstract class AuthenticatorBase extends ValveBase implements Authenticator, Lifecycle {

    private static Log log = LogFactory.getLog(AuthenticatorBase.class);

    /**
     * The default message digest algorithm to use if we cannot use
     * the requested one.
     */
    protected static final String DEFAULT_ALGORITHM = "MD5";

    /**
     * The number of random bytes to include when generating a
     * session identifier.
     */
    protected static final int SESSION_ID_BYTES = 16;

    /**
     * The message digest algorithm to be used when generating session
     * identifiers.  This must be an algorithm supported by the
     * <code>java.security.MessageDigest</code> class on your platform.
     */
    protected String algorithm = DEFAULT_ALGORITHM;

    /**
     * Should we cache authenticated Principals if the request is part of
     * an HTTP session?
     */
    protected boolean cache = true;

    /**
     * The Context to which this Valve is attached.
     */
    protected Context context = null;

    /**
     * Return the MessageDigest implementation to be used when
     * creating session identifiers.
     */
    protected MessageDigest digest = null;

    /**
     * A String initialization parameter used to increase the entropy of
     * the initialization of our random number generator.
     */
    protected String entropy = null;

    /**
     * Descriptive information about this implementation.
     */
    protected static final String info = "org.apache.catalina.authenticator.AuthenticatorBase/1.0";

    /**
     * Flag to determine if we disable proxy caching, or leave the issue
     * up to the webapp developer.
     */
    protected boolean disableProxyCaching = true;

    /**
     * Flag to determine if we disable proxy caching with headers incompatible
     * with IE 
     */
    protected boolean securePagesWithPragma = true;

    /**
     * The lifecycle event support for this component.
     */
    protected LifecycleSupport lifecycle = new LifecycleSupport(this);

    /**
     * A random number generator to use when generating session identifiers.
     */
    protected Random random = null;

    /**
     * The Java class name of the random number generator class to be used
     * when generating session identifiers.
     */
    protected String randomClass = "java.security.SecureRandom";

    /**
     * The string manager for this package.
     */
    protected static final StringManager sm = StringManager.getManager(Constants.Package);

    /**
     * The SingleSignOn implementation in our request processing chain,
     * if there is one.
     */
    protected SingleSignOn sso = null;

    /**
     * Has this component been started?
     */
    protected boolean started = false;

    /**
     * "Expires" header always set to Date(1), so generate once only
     */
    private static final String DATE_ONE = (new SimpleDateFormat(DateTool.HTTP_RESPONSE_DATE_HEADER, Locale.US)).format(new Date(1));

    /**
     * Return the message digest algorithm for this Manager.
     */
    public String getAlgorithm() {
        return (this.algorithm);
    }

    /**
     * Set the message digest algorithm for this Manager.
     *
     * @param algorithm The new message digest algorithm
     */
    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    /**
     * Return the cache authenticated Principals flag.
     */
    public boolean getCache() {
        return (this.cache);
    }

    /**
     * Set the cache authenticated Principals flag.
     *
     * @param cache The new cache flag
     */
    public void setCache(boolean cache) {
        this.cache = cache;
    }

    /**
     * Return the Container to which this Valve is attached.
     */
    public Container getContainer() {
        return (this.context);
    }

    /**
     * Set the Container to which this Valve is attached.
     *
     * @param container The container to which we are attached
     */
    public void setContainer(Container container) {
        if (!(container instanceof Context)) throw new IllegalArgumentException(sm.getString("authenticator.notContext"));
        super.setContainer(container);
        this.context = (Context) container;
    }

    /**
     * Return the entropy increaser value, or compute a semi-useful value
     * if this String has not yet been set.
     */
    public String getEntropy() {
        if (this.entropy == null) setEntropy(this.toString());
        return (this.entropy);
    }

    /**
     * Set the entropy increaser value.
     *
     * @param entropy The new entropy increaser value
     */
    public void setEntropy(String entropy) {
        this.entropy = entropy;
    }

    /**
     * Return descriptive information about this Valve implementation.
     */
    public String getInfo() {
        return (info);
    }

    /**
     * Return the random number generator class name.
     */
    public String getRandomClass() {
        return (this.randomClass);
    }

    /**
     * Set the random number generator class name.
     *
     * @param randomClass The new random number generator class name
     */
    public void setRandomClass(String randomClass) {
        this.randomClass = randomClass;
    }

    /**
     * Return the flag that states if we add headers to disable caching by
     * proxies.
     */
    public boolean getDisableProxyCaching() {
        return disableProxyCaching;
    }

    /**
     * Set the value of the flag that states if we add headers to disable
     * caching by proxies.
     * @param nocache <code>true</code> if we add headers to disable proxy 
     *              caching, <code>false</code> if we leave the headers alone.
     */
    public void setDisableProxyCaching(boolean nocache) {
        disableProxyCaching = nocache;
    }

    /**
     * Return the flag that states, if proxy caching is disabled, what headers
     * we add to disable the caching.
     */
    public boolean getSecurePagesWithPragma() {
        return securePagesWithPragma;
    }

    /**
     * Set the value of the flag that states what headers we add to disable
     * proxy caching.
     * @param securePagesWithPragma <code>true</code> if we add headers which 
     * are incompatible with downloading office documents in IE under SSL but
     * which fix a caching problem in Mozilla.
     */
    public void setSecurePagesWithPragma(boolean securePagesWithPragma) {
        this.securePagesWithPragma = securePagesWithPragma;
    }

    /**
     * Enforce the security restrictions in the web application deployment
     * descriptor of our associated Context.
     *
     * @param request Request to be processed
     * @param response Response to be processed
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if thrown by a processing element
     */
    public void invoke(Request request, Response response) throws IOException, ServletException {
        if (log.isDebugEnabled()) log.debug("Security checking request " + request.getMethod() + " " + request.getRequestURI());
        LoginConfig config = this.context.getLoginConfig();
        if (cache) {
            Principal principal = request.getUserPrincipal();
            if (principal == null) {
                Session session = request.getSessionInternal(false);
                if (session != null) {
                    principal = session.getPrincipal();
                    if (principal != null) {
                        if (log.isDebugEnabled()) log.debug("We have cached auth type " + session.getAuthType() + " for principal " + session.getPrincipal());
                        request.setAuthType(session.getAuthType());
                        request.setUserPrincipal(principal);
                    }
                }
            }
        }
        String contextPath = this.context.getPath();
        String requestURI = request.getDecodedRequestURI();
        if (requestURI.startsWith(contextPath) && requestURI.endsWith(Constants.FORM_ACTION)) {
            if (!authenticate(request, response, config)) {
                if (log.isDebugEnabled()) log.debug(" Failed authenticate() test ??" + requestURI);
                return;
            }
        }
        Realm realm = this.context.getRealm();
        SecurityConstraint[] constraints = realm.findSecurityConstraints(request, this.context);
        if ((constraints == null)) {
            if (log.isDebugEnabled()) log.debug(" Not subject to any constraint");
            getNext().invoke(request, response);
            return;
        }
        if (disableProxyCaching && !"POST".equalsIgnoreCase(request.getMethod())) {
            if (securePagesWithPragma) {
                response.setHeader("Pragma", "No-cache");
                response.setHeader("Cache-Control", "no-cache");
            } else {
                response.setHeader("Cache-Control", "private");
            }
            response.setHeader("Expires", DATE_ONE);
        }
        int i;
        if (log.isDebugEnabled()) {
            log.debug(" Calling hasUserDataPermission()");
        }
        if (!realm.hasUserDataPermission(request, response, constraints)) {
            if (log.isDebugEnabled()) {
                log.debug(" Failed hasUserDataPermission() test");
            }
            return;
        }
        boolean authRequired = true;
        for (i = 0; i < constraints.length && authRequired; i++) {
            if (!constraints[i].getAuthConstraint()) {
                authRequired = false;
            } else if (!constraints[i].getAllRoles()) {
                String[] roles = constraints[i].findAuthRoles();
                if (roles == null || roles.length == 0) {
                    authRequired = false;
                }
            }
        }
        if (authRequired) {
            if (log.isDebugEnabled()) {
                log.debug(" Calling authenticate()");
            }
            if (!authenticate(request, response, config)) {
                if (log.isDebugEnabled()) {
                    log.debug(" Failed authenticate() test");
                }
                return;
            }
        }
        if (log.isDebugEnabled()) {
            log.debug(" Calling accessControl()");
        }
        if (!realm.hasResourcePermission(request, response, constraints, this.context)) {
            if (log.isDebugEnabled()) {
                log.debug(" Failed accessControl() test");
            }
            return;
        }
        if (log.isDebugEnabled()) {
            log.debug(" Successfully passed all security constraints");
        }
        getNext().invoke(request, response);
    }

    /**
     * Associate the specified single sign on identifier with the
     * specified Session.
     *
     * @param ssoId Single sign on identifier
     * @param session Session to be associated
     */
    protected void associate(String ssoId, Session session) {
        if (sso == null) return;
        sso.associate(ssoId, session);
    }

    /**
     * Authenticate the user making this request, based on the specified
     * login configuration.  Return <code>true</code> if any specified
     * constraint has been satisfied, or <code>false</code> if we have
     * created a response challenge already.
     *
     * @param request Request we are processing
     * @param response Response we are creating
     * @param config    Login configuration describing how authentication
     *              should be performed
     *
     * @exception IOException if an input/output error occurs
     */
    protected abstract boolean authenticate(Request request, Response response, LoginConfig config) throws IOException;

    /**
     * Generate and return a new session identifier for the cookie that
     * identifies an SSO principal.
     */
    protected synchronized String generateSessionId() {
        byte bytes[] = new byte[SESSION_ID_BYTES];
        getRandom().nextBytes(bytes);
        bytes = getDigest().digest(bytes);
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            byte b1 = (byte) ((bytes[i] & 0xf0) >> 4);
            byte b2 = (byte) (bytes[i] & 0x0f);
            if (b1 < 10) result.append((char) ('0' + b1)); else result.append((char) ('A' + (b1 - 10)));
            if (b2 < 10) result.append((char) ('0' + b2)); else result.append((char) ('A' + (b2 - 10)));
        }
        return (result.toString());
    }

    /**
     * Return the MessageDigest object to be used for calculating
     * session identifiers.  If none has been created yet, initialize
     * one the first time this method is called.
     */
    protected synchronized MessageDigest getDigest() {
        if (this.digest == null) {
            try {
                this.digest = MessageDigest.getInstance(algorithm);
            } catch (NoSuchAlgorithmException e) {
                try {
                    this.digest = MessageDigest.getInstance(DEFAULT_ALGORITHM);
                } catch (NoSuchAlgorithmException f) {
                    this.digest = null;
                }
            }
        }
        return (this.digest);
    }

    /**
     * Return the random number generator instance we should use for
     * generating session identifiers.  If there is no such generator
     * currently defined, construct and seed a new one.
     */
    protected synchronized Random getRandom() {
        if (this.random == null) {
            try {
                Class clazz = Class.forName(randomClass);
                this.random = (Random) clazz.newInstance();
                long seed = System.currentTimeMillis();
                char entropy[] = getEntropy().toCharArray();
                for (int i = 0; i < entropy.length; i++) {
                    long update = ((byte) entropy[i]) << ((i % 8) * 8);
                    seed ^= update;
                }
                this.random.setSeed(seed);
            } catch (Exception e) {
                this.random = new java.util.Random();
            }
        }
        return (this.random);
    }

    /**
     * Attempts reauthentication to the <code>Realm</code> using
     * the credentials included in argument <code>entry</code>.
     *
     * @param ssoId identifier of SingleSignOn session with which the
     *              caller is associated
     * @param request   the request that needs to be authenticated
     */
    protected boolean reauthenticateFromSSO(String ssoId, Request request) {
        if (sso == null || ssoId == null) return false;
        boolean reauthenticated = false;
        Container parent = getContainer();
        if (parent != null) {
            Realm realm = parent.getRealm();
            if (realm != null) {
                reauthenticated = sso.reauthenticate(ssoId, realm, request);
            }
        }
        if (reauthenticated) {
            associate(ssoId, request.getSessionInternal(true));
            if (log.isDebugEnabled()) {
                log.debug(" Reauthenticated cached principal '" + request.getUserPrincipal().getName() + "' with auth type '" + request.getAuthType() + "'");
            }
        }
        return reauthenticated;
    }

    /**
     * Register an authenticated Principal and authentication type in our
     * request, in the current session (if there is one), and with our
     * SingleSignOn valve, if there is one.  Set the appropriate cookie
     * to be returned.
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are generating
     * @param principal The authenticated Principal to be registered
     * @param authType The authentication type to be registered
     * @param username Username used to authenticate (if any)
     * @param password Password used to authenticate (if any)
     */
    protected void register(Request request, Response response, Principal principal, String authType, String username, String password) {
        if (log.isDebugEnabled()) log.debug("Authenticated '" + principal.getName() + "' with type '" + authType + "'");
        request.setAuthType(authType);
        request.setUserPrincipal(principal);
        Session session = request.getSessionInternal(false);
        if (cache) {
            if (session != null) {
                session.setAuthType(authType);
                session.setPrincipal(principal);
                if (username != null) session.setNote(Constants.SESS_USERNAME_NOTE, username); else session.removeNote(Constants.SESS_USERNAME_NOTE);
                if (password != null) session.setNote(Constants.SESS_PASSWORD_NOTE, password); else session.removeNote(Constants.SESS_PASSWORD_NOTE);
            }
        }
        if (sso == null) return;
        String ssoId = (String) request.getNote(Constants.REQ_SSOID_NOTE);
        if (ssoId == null) {
            ssoId = generateSessionId();
            Cookie cookie = new Cookie(Constants.SINGLE_SIGN_ON_COOKIE, ssoId);
            cookie.setMaxAge(-1);
            cookie.setPath("/");
            cookie.setSecure(request.isSecure());
            String ssoDomain = sso.getCookieDomain();
            if (ssoDomain != null) {
                cookie.setDomain(ssoDomain);
            }
            response.addCookie(cookie);
            sso.register(ssoId, principal, authType, username, password);
            request.setNote(Constants.REQ_SSOID_NOTE, ssoId);
        } else {
            sso.update(ssoId, principal, authType, username, password);
        }
        if (session == null) session = request.getSessionInternal(true);
        sso.associate(ssoId, session);
    }

    /**
     * Add a lifecycle event listener to this component.
     *
     * @param listener The listener to add
     */
    public void addLifecycleListener(LifecycleListener listener) {
        lifecycle.addLifecycleListener(listener);
    }

    /**
     * Get the lifecycle listeners associated with this lifecycle. If this 
     * Lifecycle has no listeners registered, a zero-length array is returned.
     */
    public LifecycleListener[] findLifecycleListeners() {
        return lifecycle.findLifecycleListeners();
    }

    /**
     * Remove a lifecycle event listener from this component.
     *
     * @param listener The listener to remove
     */
    public void removeLifecycleListener(LifecycleListener listener) {
        lifecycle.removeLifecycleListener(listener);
    }

    /**
     * Prepare for the beginning of active use of the public methods of this
     * component.  This method should be called after <code>configure()</code>,
     * and before any of the public methods of the component are utilized.
     *
     * @exception LifecycleException if this component detects a fatal error
     *  that prevents this component from being used
     */
    public void start() throws LifecycleException {
        if (started) throw new LifecycleException(sm.getString("authenticator.alreadyStarted"));
        lifecycle.fireLifecycleEvent(START_EVENT, null);
        started = true;
        Container parent = context.getParent();
        while ((sso == null) && (parent != null)) {
            if (!(parent instanceof Pipeline)) {
                parent = parent.getParent();
                continue;
            }
            Valve valves[] = ((Pipeline) parent).getValves();
            for (int i = 0; i < valves.length; i++) {
                if (valves[i] instanceof SingleSignOn) {
                    sso = (SingleSignOn) valves[i];
                    break;
                }
            }
            if (sso == null) parent = parent.getParent();
        }
        if (log.isDebugEnabled()) {
            if (sso != null) log.debug("Found SingleSignOn Valve at " + sso); else log.debug("No SingleSignOn Valve is present");
        }
    }

    /**
     * Gracefully terminate the active use of the public methods of this
     * component.  This method should be the last one called on a given
     * instance of this component.
     *
     * @exception LifecycleException if this component detects a fatal error
     *  that needs to be reported
     */
    public void stop() throws LifecycleException {
        if (!started) throw new LifecycleException(sm.getString("authenticator.notStarted"));
        lifecycle.fireLifecycleEvent(STOP_EVENT, null);
        started = false;
        sso = null;
    }
}
