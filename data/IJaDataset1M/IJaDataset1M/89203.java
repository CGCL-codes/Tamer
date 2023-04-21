package com.blogspot.devenphillips.helpdesk.auth.plugins.ldap;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Label;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;
import com.blogspot.devenphillips.helpdesk.auth.AuthException;
import com.blogspot.devenphillips.helpdesk.auth.AuthProvider;
import com.blogspot.devenphillips.helpdesk.auth.plugins.ad.ActiveDirectoryProvider;
import com.sun.jndi.ldap.LdapCtxFactory;

public class LDAPProvider implements AuthProvider {

    private Properties cfg = null;

    public LDAPProvider(Properties config) {
        cfg = config;
    }

    public boolean authenticate(String uid, String password) throws AuthException {
        boolean isAuthenticated = false;
        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.SECURITY_PRINCIPAL, "");
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_CREDENTIALS, password);
        if (password.length() != 0) {
            int port = 389;
            String useSSLString = (String) cfg.get("auth.ldap.useSSL");
            String protocol = null;
            if (useSSLString.contains("true")) {
                port = 636;
                protocol = "ldaps";
            } else {
                protocol = "ldap";
            }
            try {
                DirContext ctx = LdapCtxFactory.getLdapCtxInstance(protocol + "://" + cfg.get("auth.ldap.ldapdc") + ":" + port + "/" + cfg.get("auth.ldap.basedn"), env);
                isAuthenticated = true;
                ctx.close();
                ctx = null;
            } catch (Throwable e) {
                isAuthenticated = false;
                e.printStackTrace();
            }
        }
        return isAuthenticated;
    }

    public String getDisplayName(String username) throws AuthException {
        String displayName = null;
        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.SECURITY_PRINCIPAL, "");
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_CREDENTIALS, "");
        String[] filterArgs = { username };
        String filter = "(uid={0})";
        int port = 389;
        String useSSLString = (String) cfg.get("auth.ldap.useSSL");
        String protocol = null;
        if (useSSLString.contains("true")) {
            port = 636;
            protocol = "ldaps";
        } else {
            protocol = "ldap";
        }
        try {
            DirContext ctx = LdapCtxFactory.getLdapCtxInstance(protocol + "://" + cfg.get("auth.ldap.ldapdc") + ":" + port + "/" + cfg.get("auth.ldap.basedn"), env);
            SearchControls sctrls = new SearchControls();
            sctrls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            sctrls.setDerefLinkFlag(true);
            NamingEnumeration<SearchResult> results;
            results = ctx.search((String) cfg.get("auth.ldap.domain"), filter, filterArgs, sctrls);
            if (results.hasMore()) {
                SearchResult current = results.next();
                Attributes attr = current.getAttributes();
                displayName = (String) attr.get("displayName").get(0);
                attr = null;
                current = null;
            }
            results.close();
            results = null;
            ctx.close();
            ctx = null;
        } catch (Throwable e) {
            displayName = "";
            e.printStackTrace();
        }
        return displayName;
    }

    public String[] getUserGroups(String username) throws AuthException {
        ArrayList<String> groups = new ArrayList<String>();
        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.SECURITY_PRINCIPAL, cfg.get("auth.ad.adminuser") + "@" + cfg.get("auth.ad.domain"));
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_CREDENTIALS, (String) cfg.get("auth.ad.adminpass"));
        String[] filterArgs = { username };
        String filter = "(samaccountName={0})";
        int port = 389;
        String useSSLString = (String) cfg.get("auth.ad.useSSL");
        String protocol = null;
        if (useSSLString.contains("true")) {
            port = 636;
            protocol = "ldaps";
        } else {
            protocol = "ldap";
        }
        try {
            DirContext ctx = LdapCtxFactory.getLdapCtxInstance(protocol + "://" + cfg.get("auth.ad.domaincontroller") + ":" + port + "/" + cfg.get("auth.ad.basedn"), env);
            SearchControls sctrls = new SearchControls();
            sctrls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            sctrls.setDerefLinkFlag(true);
            NamingEnumeration<SearchResult> results;
            results = ctx.search((String) cfg.get("auth.ad.domain"), filter, filterArgs, sctrls);
            while (results.hasMore()) {
                SearchResult current = results.next();
                Attributes attr = current.getAttributes();
                Attribute memberships = attr.get("memberOf");
                int count = memberships.size();
                for (int x = 0; x < count; x++) {
                    String groupDn = (String) memberships.get(x);
                    groups.add(groupDn.split(",")[0].split("=")[1]);
                }
                attr = null;
                current = null;
            }
            results.close();
            results = null;
            ctx.close();
            ctx = null;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return (String[]) groups.toArray();
    }

    public boolean isUserInGroup(String username, String group) throws AuthException {
        return false;
    }

    public static String getModuleShortName() {
        return "LDAP";
    }

    public static String getModuleName() {
        return "LDAP Authentication Provider";
    }

    public static String getModuleDescription() {
        return "Allows for authenticating users and pulling user information from an LDAP directory.";
    }

    public static String getVersion() {
        return "0.0.1";
    }

    /**
	 * Display an object which contains all of the inputs needed to configure the 
	 * LDAP Auth Provider module
	 * @return An object which has components and events to configure the LDAP Auth Provider module
	 */
    public static Vbox showConfig() {
        java.io.InputStream zulInput = ActiveDirectoryProvider.class.getClassLoader().getResourceAsStream("com/blogspot/devenphillips/helpdesk/auth/plugins/ldap/config.zul");
        Reader zulReader = new InputStreamReader(zulInput);
        Vbox retVal = new Vbox();
        try {
            retVal = (Vbox) Executions.createComponentsDirectly(zulReader, "zul", null, new HashMap<String, String>());
        } catch (Exception e) {
            Window errWin = new Window();
            errWin.setTitle("Configuration Error");
            try {
                errWin.setMode("overlapped");
            } catch (InterruptedException e1) {
            }
            Label errMessage = new Label("Unable to load configuration interface for LDAP.");
            errWin.setWidth("400px");
            errWin.setHeight("200px");
            errWin.appendChild(errMessage);
            retVal.appendChild(errWin);
        }
        return retVal;
    }
}
