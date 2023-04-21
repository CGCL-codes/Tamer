package edu.mit.osidimpl.authentication.kerberos.client.tty;

import edu.mit.osidimpl.manager.*;
import edu.mit.osidimpl.authentication.shared.*;

/**
 *  <p>
 *  Implements the AuthenticationManager for swing-based kerberos clients.
 *  This implementation is responsible for checking and acquiring 
 *  Kerberos credentials through prompting the user at the command line. 
 *  </p><p>
 *  Implementation Properties:
 *  <table border=0>
 *  <tr><td valign=top><code>banner</code></td>
 *      <td valign=top>A banner to display beefore prompting.
 *      <td valign=top>null</td></tr>
 *  <tr><td valign=top><code>cache_name</code></td>
 *      <td valign=top>the name of the credentials cache to use</td>
 *      <td valign=top></td></tr>
 *  <tr><td valign=top><code>retries</code></td>
 *      <td valign=top>the number of login attempts allowed before 
 *                     giving up</td>
 *      <td valign=top>3</td></tr>
 *  <tr><td valign=top><code>use_ticket_cache</code></td>
 *      <td valign=top>whether a credentials cache should be used. If 
 *                     valid credentials exist in the cache, the user 
 *                     won't be prompted for password.</td>
 *      <td valign=top>true</td></tr>
 *  <tr><td valign=top><code>timeout</code></td>
 *      <td valign=top>the amount of time in seconds the user has to enter 
 *                     name and password</td>
 *      <td valign=top>60</td></tr>
 *  </table>
 *  </p><p>
 *  CVS $Id: AuthenticationManager.java,v 1.1 2005/08/25 15:45:53 tom Exp $
 *  </p>
 *  
 *  @author  Tom Coppeto
 *  @version $OSID: 2.0$ $Revision: 1.1 $
 *  @see     org.osid.authentication.AuthenticationManager
 */
public class AuthenticationManager extends OsidManagerWithCascadingPropertiesAndLogging implements org.osid.authentication.AuthenticationManager {

    private sun.security.krb5.PrincipalName principal;

    private edu.mit.osidutil.swing.Login login;

    OsidLogger logger;

    private boolean use_ticket_cache = true;

    private String cache_name = null;

    private int retries = 3;

    private String banner = null;

    private int timeout = 60;

    /**
     *  Constructs a new <code>AuthenticationManager</code>.
     */
    public AuthenticationManager() {
        super();
    }

    protected void initialize() throws org.osid.OsidException {
        String property;
        logger = getLogger();
        logger.logDebug("initializing AuthenticationManager");
        property = getConfiguration("cache_name");
        if (property != null) {
            this.cache_name = property;
            logger.logTrace("cache_name=" + this.cache_name);
        }
        property = getConfiguration("use_ticket_cache");
        if (property != null) {
            this.use_ticket_cache = edu.mit.osidutil.contrivance.BBoolean.parseBoolean(property);
            logger.logTrace("use_ticket_cache=" + this.use_ticket_cache);
        }
        property = getConfiguration("retries");
        if (property != null) {
            try {
                this.retries = Integer.parseInt(property);
            } catch (NumberFormatException nfe) {
                logger.logError("retries property not a number: " + property);
            }
            logger.logTrace("retries=" + this.retries);
        }
        property = getConfiguration("timeout");
        if (property != null) {
            try {
                this.timeout = Integer.parseInt(property);
            } catch (NumberFormatException nfe) {
                logger.logError("timeout property not a number: " + property);
            }
            logger.logTrace("timeout=" + this.timeout);
        }
        property = getConfiguration("banner");
        if (property != null) {
            this.banner = property;
            logger.logTrace("banner=" + this.banner);
        }
        try {
            sun.security.krb5.Config.refresh();
        } catch (sun.security.krb5.KrbException ke) {
            logger.logError("unable to initialize Kerberos: " + ke.toString());
            throw new org.osid.OsidException(org.osid.OsidException.OPERATION_FAILED);
        }
        return;
    }

    /**
     *  Get the authentication Type supported.
     *
     *  @return org.osid.shared.TypeIterator
     *
     *  @throws org.osid.authentication.AuthenticationException An exception
     *          with one of the following messages defined in
     *          org.osid.authentication.AuthenticationException may be thrown:
     *          {@link
     *          org.osid.authentication.AuthenticationException#OPERATION_FAILED
     *          OPERATION_FAILED}
     */
    public org.osid.shared.TypeIterator getAuthenticationTypes() throws org.osid.authentication.AuthenticationException {
        logger.logDebug("getAuthenticationTypes()");
        try {
            return (edu.mit.osidimpl.authentication.shared.AuthenticationType.getTypes());
        } catch (org.osid.shared.SharedException se) {
            logger.logError("unable to create TypeIterator: " + se.toString());
            throw new org.osid.authentication.AuthenticationException(org.osid.authentication.AuthenticationException.OPERATION_FAILED);
        }
    }

    /**
     *  Invoke the authentication process of the specified Type to identify the
     *  user. The username and password are prompted at the command line
     *  and checked against the password file. <code>password_file</code>
     *  is a property.
     *
     *  @param authenticationType ignored
     *
     *  @throws org.osid.authentication.AuthenticationException An exception
     *         with one of the following messages defined in
     *         org.osid.authentication.AuthenticationException may be thrown:
     *         {@link
     *         org.osid.authentication.AuthenticationException#OPERATION_FAILED
     *         OPERATION_FAILED}.
     */
    public void authenticateUser(org.osid.shared.Type authenticationType) throws org.osid.authentication.AuthenticationException {
        logger.logDebug("authenticateUser(" + org.osidx.registry.OsidTypeRegistry.typeToString(authenticationType) + ")");
        if (this.banner != null) {
            System.out.println(this.banner);
        }
        int retry = 0;
        while (true) {
            if (retry++ > this.retries) {
                System.err.println("too many failed logins");
                logger.logNotice("too many login attempts");
                throw new org.osid.authentication.AuthenticationException(org.osid.authentication.AuthenticationException.OPERATION_FAILED);
            }
            LoginThread lt = new LoginThread();
            lt.start();
            try {
                lt.join(this.timeout * 1000);
            } catch (InterruptedException ie) {
                System.out.println("interrupted");
                continue;
            }
            if (lt.isComplete() == false) {
                System.err.println("\nlogin timed out after " + this.timeout + " seconds");
                logger.logNotice("login window timed out");
                throw new org.osid.authentication.AuthenticationException(org.osid.authentication.AuthenticationException.OPERATION_FAILED);
            }
            String user = lt.getUser();
            String pass = lt.getPassword();
            if ((user == null) || (pass == null)) {
                continue;
            }
            if ((user.length() == 0) || (pass.length() == 0)) {
                continue;
            }
            StringBuffer p = new StringBuffer(pass);
            sun.security.krb5.EncryptionKey key;
            sun.security.krb5.Credentials c;
            try {
                this.principal = new sun.security.krb5.PrincipalName(user, sun.security.krb5.PrincipalName.KRB_NT_PRINCIPAL);
                key = new sun.security.krb5.EncryptionKey(new StringBuffer().append(pass), this.principal.getSalt());
            } catch (sun.security.krb5.KrbException ke) {
                logger.logError("unable to make encryption key:  " + ke.toString());
                throw new org.osid.authentication.AuthenticationException(org.osid.authentication.AuthenticationException.OPERATION_FAILED);
            }
            try {
                c = sun.security.krb5.Credentials.acquireTGT(this.principal, key);
            } catch (sun.security.krb5.KrbException ke) {
                System.err.println("passwd incorrect");
                logger.logError("unable to acquire tgt:  " + ke.toString());
                continue;
            } catch (java.io.IOException ie) {
                logger.logError("IO error getting tgt:  " + ie.toString());
                continue;
            }
            if (c == null) {
                logger.logNotice("unable to get credentials");
                continue;
            }
            javax.security.auth.kerberos.KerberosTicket ticket = new javax.security.auth.kerberos.KerberosTicket(c.getEncoded(), new javax.security.auth.kerberos.KerberosPrincipal(c.getClient().getName()), new javax.security.auth.kerberos.KerberosPrincipal(c.getServer().getName()), c.getSessionKey().getBytes(), c.getSessionKey().getEType(), c.getFlags(), c.getAuthTime(), c.getStartTime(), c.getEndTime(), c.getRenewTill(), c.getClientAddresses());
            try {
                edu.mit.osidutil.contrivance.KrbCredCache cache = new edu.mit.osidutil.contrivance.KrbCredCache("/tmp/cc");
                cache.initialize(new javax.security.auth.kerberos.KerberosPrincipal(c.getClient().getName()));
                cache.store(ticket, key);
            } catch (edu.mit.osidutil.contrivance.KrbCredCacheException ke) {
                logger.logWarning("unable to store credentials: " + ke.toString());
            }
            return;
        }
    }

    /**
     *  Tests to see if the user has authenticated and the authentication
     *  has not expired.
     *
     *  @param authenticationType ignored
     *  @return <code>true</code> if the user is still authenticated
     *  @throws org.osid.authentication.AuthenticationException
     */
    public boolean isUserAuthenticated(org.osid.shared.Type authenticationType) throws org.osid.authentication.AuthenticationException {
        sun.security.krb5.Credentials c;
        logger.logDebug("isUserAuthenticated(" + org.osidx.registry.OsidTypeRegistry.typeToString(authenticationType) + ")");
        try {
            c = sun.security.krb5.Credentials.acquireTGTFromCache(this.principal, "/tmp/cc");
        } catch (sun.security.krb5.KrbException ke) {
            logger.logDebug("unable to acquire tgt from cache: " + ke.toString());
            return (false);
        } catch (java.io.IOException ie) {
            logger.logDebug("unable to acquire tgt from cache: " + ie.toString());
            return (false);
        }
        if (c == null) {
            logger.logDebug("unable to acquire tgt from cache");
            return (false);
        }
        long start;
        if (c.getStartTime() != null) {
            start = c.getStartTime().getTime();
        } else {
            start = c.getAuthTime().getTime();
        }
        long end = c.getEndTime().getTime();
        java.util.Date now = new java.util.Date();
        if ((now.getTime() >= (start - 10000)) && (now.getTime() <= end)) {
            logger.logTrace("credentials valid");
            return (true);
        } else {
            logger.logTrace("credentials expired");
            return (false);
        }
    }

    /**
     *  Get an Id of some Agent that represents the user for the
     *  specified AuthenticationType.  This is currently nonsense.
     *
     *  @param authenticationType ignored
     *  @return the id if the agent
     *  @see org.osid.agent.Agent
     *  @throws org.osid.authentication.AuthenticationException
     */
    public org.osid.shared.Id getUserId(org.osid.shared.Type authenticationType) throws org.osid.authentication.AuthenticationException {
        throw new org.osid.authentication.AuthenticationException(org.osid.authentication.AuthenticationException.UNIMPLEMENTED);
    }

    /**
     *  Destroys authentication.
     *
     *  @throws org.osid.authentication.AuthenticationException
     */
    public void destroyAuthentication() throws org.osid.authentication.AuthenticationException {
        logger.logDebug("destroyAuthentication()");
        try {
            edu.mit.osidutil.contrivance.KrbCredCache cache = new edu.mit.osidutil.contrivance.KrbCredCache("/tmp/cc");
            cache.destroy();
        } catch (edu.mit.osidutil.contrivance.KrbCredCacheException ke) {
            logger.logError("unable to destroy credentials cache: " + ke.toString());
        }
        return;
    }

    /**
     *  Destroys authentication as well. It's like the first one,
     *  only better.
     *
     *  @param authenticationType ignored
     *  @throws org.osid.authentication.AuthenticationException
     */
    public void destroyAuthenticationForType(org.osid.shared.Type authenticationType) throws org.osid.authentication.AuthenticationException {
        logger.logDebug("destroyAuthenticationForType(" + org.osidx.registry.OsidTypeRegistry.typeToString(authenticationType) + ")");
        try {
            edu.mit.osidutil.contrivance.KrbCredCache cache = new edu.mit.osidutil.contrivance.KrbCredCache("/tmp/cc");
            cache.destroy();
        } catch (edu.mit.osidutil.contrivance.KrbCredCacheException ke) {
            logger.logError("unable to destroy credentials cache: " + ke.toString());
        }
        return;
    }

    class LoginThread extends Thread {

        private String user;

        private String pass;

        private boolean complete = false;

        public void run() {
            this.complete = false;
            this.user = edu.mit.osidutil.tty.Console.getInput("login: ");
            this.pass = edu.mit.osidutil.tty.Console.getMaskedInput("password: ");
            this.complete = true;
        }

        public boolean isComplete() {
            return (this.complete);
        }

        public String getUser() {
            return (this.user);
        }

        public String getPassword() {
            return (this.pass);
        }
    }
}
