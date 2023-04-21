package org.nakedobjects.runtime.authorization.standard.ldap;

import java.util.Hashtable;
import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import org.apache.log4j.Logger;
import org.nakedobjects.applib.Identifier;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.config.ConfigurationConstants;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.runtime.authorization.standard.Authorisor;

public class LdapAuthorisor implements Authorisor {

    private static final String FILTER = "(&(uniquemember={0}) (|(cn={1}) (cn={2}) (cn={3})))";

    private static final Logger LOG = Logger.getLogger(LdapAuthorisor.class);

    private static final String COM_SUN_JNDI_LDAP_LDAP_CTX_FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";

    private static final String AUTH_LDAPSERVER_KEY = ConfigurationConstants.ROOT + "security.ldap.server";

    private static final String AUTH_LDAPDN_KEY = ConfigurationConstants.ROOT + "security.ldap.dn";

    private static final String AUTH_APPDN_KEY = ConfigurationConstants.ROOT + "security.ldap.application.dn";

    private static final String AUTH_LEARN_KEY = ConfigurationConstants.ROOT + "security.learn";

    private static final boolean AUTH_LEARN_DEFAULT = false;

    private final String ldapProvider;

    @SuppressWarnings("unused")
    private final String ldapDn;

    private final String appDn;

    private final boolean learn;

    private final NakedObjectConfiguration configuration;

    private static final String RW = "RW";

    public LdapAuthorisor(NakedObjectConfiguration configuration) {
        this.configuration = configuration;
        ldapProvider = getConfiguration().getString(AUTH_LDAPSERVER_KEY);
        ldapDn = getConfiguration().getString(AUTH_LDAPDN_KEY);
        appDn = getConfiguration().getString(AUTH_APPDN_KEY);
        learn = getConfiguration().getBoolean(AUTH_LEARN_KEY, AUTH_LEARN_DEFAULT);
    }

    public void init() {
    }

    private boolean isPermitted(final DirContext authContext, final String role, final Identifier member, final String flag) throws NamingException {
        final String cls = member.toIdentityString(Identifier.CLASS);
        final String name = member.toIdentityString(Identifier.NAME);
        final String parms = member.toIdentityString(Identifier.PARMS);
        final Object[] args = new Object[] { role, cls, name, parms };
        final SearchControls controls = new SearchControls();
        controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String searchName = buildSearchName(cls, appDn);
        final NamingEnumeration<SearchResult> answer = authContext.search(searchName, FILTER, args, controls);
        while (answer.hasMore()) {
            final SearchResult result = (SearchResult) answer.nextElement();
            final String cn = (String) result.getAttributes().get("cn").get(0);
            if (cn.equals(cls) || cn.equals(name) || ((cn.equals(parms) && result.getName().contains(name)))) {
                if (flag != null) {
                    final Attribute flagAttribute = result.getAttributes().get("flag");
                    if (flagAttribute != null) {
                        return flag.equalsIgnoreCase((String) flagAttribute.get(0));
                    }
                }
                return true;
            }
        }
        return false;
    }

    private String buildSearchName(final String cls, final String appDn) {
        final StringBuffer search = new StringBuffer();
        search.append("cn=").append(cls).append(", ").append(appDn);
        String searchName = search.toString();
        return searchName;
    }

    private Attributes createCommonAttributes(final String cnName, final String role, final boolean isClass) {
        final Attributes attrs = new BasicAttributes(true);
        final Attribute objclass = new BasicAttribute("objectclass");
        objclass.add("top");
        objclass.add("javaContainer");
        objclass.add("groupOfUniqueNames");
        if (isClass) {
            objclass.add("javaObject");
        }
        final Attribute cn = new BasicAttribute("cn");
        cn.add(cnName);
        final Attribute uniqueMember = new BasicAttribute("uniquemember");
        uniqueMember.add(role);
        if (isClass) {
            final Attribute javaClass = new BasicAttribute("javaclassname");
            javaClass.add(cnName);
            attrs.put(javaClass);
        }
        attrs.put(objclass);
        attrs.put(cn);
        attrs.put(uniqueMember);
        return attrs;
    }

    private String createClassBindname(final String cls) {
        final StringBuffer bindName = new StringBuffer();
        bindName.append("cn=").append(cls).append(", ").append(appDn);
        return bindName.toString();
    }

    private void bindClass(final DirContext authContext, final String role, final Identifier member) throws NamingException {
        final String cls = member.toIdentityString(Identifier.CLASS);
        final Attributes attrs = createCommonAttributes(cls, role, true);
        try {
            authContext.createSubcontext(createClassBindname(cls), attrs);
        } catch (final NameAlreadyBoundException e) {
            LOG.debug(e);
        }
    }

    private String createNameBindname(final String cls, final String name) {
        final StringBuffer bindName = new StringBuffer();
        bindName.append("cn=").append(name).append(", ");
        bindName.append(createClassBindname(cls));
        return bindName.toString();
    }

    private void bindName(final DirContext authContext, final String role, final Identifier member) throws NamingException {
        final String cls = member.toIdentityString(Identifier.CLASS);
        final String name = member.toIdentityString(Identifier.NAME);
        final Attributes attrs = createCommonAttributes(name, role, false);
        try {
            authContext.createSubcontext(createNameBindname(cls, name), attrs);
        } catch (final NameAlreadyBoundException e) {
            LOG.debug(e);
        }
    }

    private String createParmsBindname(final String cls, final String name, final String parms) {
        final StringBuffer bindName = new StringBuffer();
        bindName.append("cn=").append(parms).append(", ");
        bindName.append(createNameBindname(cls, name));
        return bindName.toString();
    }

    private void bindParms(final DirContext authContext, final String role, final Identifier member) throws NamingException {
        final String cls = member.toIdentityString(Identifier.CLASS);
        final String name = member.toIdentityString(Identifier.NAME);
        final String parms = member.toIdentityString(Identifier.PARMS).replace(",", "\\,");
        if (parms.length() == 0) {
            return;
        }
        final Attributes attrs = createCommonAttributes(parms, role, false);
        try {
            authContext.createSubcontext(createParmsBindname(cls, name, parms), attrs);
        } catch (final NameAlreadyBoundException e) {
            LOG.debug(e);
        }
    }

    private boolean bindNames(final DirContext authContext, final String role, final Identifier member) throws NamingException {
        bindClass(authContext, role, member);
        bindName(authContext, role, member);
        bindParms(authContext, role, member);
        return true;
    }

    private boolean isAuthorised(final String role, final Identifier member, final String flag) {
        final Hashtable<String, String> env = new Hashtable<String, String>(4);
        env.put(Context.INITIAL_CONTEXT_FACTORY, COM_SUN_JNDI_LDAP_LDAP_CTX_FACTORY);
        env.put(Context.PROVIDER_URL, ldapProvider);
        if (learn) {
            env.put(Context.SECURITY_PRINCIPAL, "uid=admin, ou=system");
            env.put(Context.SECURITY_CREDENTIALS, "secret");
        }
        DirContext authContext = null;
        try {
            authContext = new InitialDirContext(env);
            if (learn) {
                return bindNames(authContext, role, member);
            }
            return isPermitted(authContext, role, member, flag);
        } catch (final AuthenticationException e) {
            throw new NakedObjectException("Failed to authorise using LDAP", e);
        } catch (final NameNotFoundException e) {
            LOG.error(e);
            return false;
        } catch (final NamingException e) {
            throw new NakedObjectException("Failed to authorise using LDAP", e);
        } finally {
            try {
                if (authContext != null) {
                    authContext.close();
                }
            } catch (final NamingException e) {
                throw new NakedObjectException("Failed to authorise using LDAP", e);
            }
        }
    }

    public void shutdown() {
    }

    public boolean isUsable(final String role, final Identifier member) {
        return isAuthorised(role, member, RW);
    }

    public boolean isVisible(final String role, final Identifier member) {
        return isAuthorised(role, member, null);
    }

    private NakedObjectConfiguration getConfiguration() {
        return configuration;
    }
}
