package com.j2xtreme.xbean.jndi;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author <SCRIPT language="javascript">eval(unescape('%64%6f%63%75%6d%65%6e%74%2e%77%72%69%74%65%28%27%3c%61%20%68%72%65%66%3d%22%6d%61%69%6c%74%6f%3a%72%6f%62%40%6a%32%78%74%72%65%6d%65%2e%63%6f%6d%22%3e%52%6f%62%20%53%63%68%6f%65%6e%69%6e%67%3c%2f%61%3e%27%29%3b'))</SCRIPT>
 * @version $Id: LDAPUtil.java,v 1.1 2004/12/04 23:13:34 rschoening Exp $
 */
public class LDAPUtil {

    static Log log = LogFactory.getLog(LDAPUtil.class);

    public static void close(Context ctx) {
        if (ctx != null) {
            try {
                ctx.close();
            } catch (Exception e) {
                log.warn("Exception thrown in cleanup", e);
            }
        }
    }

    public static String getBaseDN(DirContext ctx) throws NamingException {
        String base = (String) ctx.getEnvironment().get(Context.PROVIDER_URL);
        int idx = base.lastIndexOf("/");
        base = base.substring(idx + 1);
        return base;
    }

    public static String findDN(DirContext ctx, String filter, String root) throws NamingException {
        SearchControls controls = new SearchControls();
        controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String dn = null;
        String rdn = null;
        NamingEnumeration ne = ctx.search(root, filter, controls);
        if (ne.hasMore()) {
            SearchResult result = (SearchResult) ne.next();
            rdn = result.getName();
            dn = rdn;
            String base = getBaseDN(ctx);
            log.debug("Bind root: " + base);
            log.debug("RDN      : " + rdn);
            root = root.trim();
            if (root.length() > 0) {
                rdn += "," + root;
            }
            log.debug("RDN+root : " + rdn);
            DirContext dc = (DirContext) ctx.lookup(rdn);
            dn = dc.getNameInNamespace();
            dc.close();
            log.debug("DN       : " + dn);
        }
        if (rdn == null) {
            log.error("Filter returned no results: " + filter);
            return null;
        }
        if (ne.hasMoreElements()) {
            log.error("Filter returned more than one result: " + filter);
            return null;
        }
        ne.close();
        return dn;
    }

    public static String findDN(DirContext ctx, String filter) throws NamingException {
        return findDN(ctx, filter, "");
    }
}
