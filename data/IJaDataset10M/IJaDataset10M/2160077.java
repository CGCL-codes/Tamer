package agkvs_monitor2;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

/**
 *
 * Verwaltet den ActiveDirectoryServer
 * @author Konstantin Rduch
 */
public class ActiveDirectory {

    private Hashtable env = new Hashtable();

    private InitialDirContext ctx = null;

    private boolean loggedIn = false;

    private boolean isClient = false;

    private String uid = "";

    public ActiveDirectory(String _user, String _password) {
        Login(_user, _password);
        LookUPUser(_user);
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public boolean isIsClient() {
        return isClient;
    }

    public String getUid() {
        return uid;
    }

    public void Login(String _user, String _password) {
        String dn = "sn=" + _user + ",ou=agkvs";
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://localhost:1337");
        env.put(Context.SECURITY_PRINCIPAL, dn);
        env.put(Context.SECURITY_CREDENTIALS, _password);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
    }

    public void LookUPUser(String _user) {
        try {
            ctx = new InitialDirContext(env);
            String base = "ou=agkvs";
            String filter = "(&(objectClass=person)(sn=" + _user + "))";
            System.out.println(filter);
            SearchControls ctls = new SearchControls();
            ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            ctls.setReturningAttributes(new String[] { "uid", "cn" });
            NamingEnumeration resultEnum = ctx.search(base, filter, ctls);
            while (resultEnum.hasMore()) {
                SearchResult result = (SearchResult) resultEnum.next();
                System.out.println(result.getNameInNamespace());
                Attributes attrs = result.getAttributes();
                NamingEnumeration e = attrs.getAll();
                while (e.hasMore()) {
                    Attribute attr = (Attribute) e.next();
                    System.out.println(attr);
                    if (attr.getID().equals("uid")) {
                        uid = attr.get().toString();
                        if (uid.charAt(0) == 'c') {
                            isClient = true;
                        }
                    }
                }
            }
            loggedIn = true;
            ctx.close();
        } catch (javax.naming.NamingException e) {
            System.out.println(e);
            loggedIn = false;
        }
    }
}
