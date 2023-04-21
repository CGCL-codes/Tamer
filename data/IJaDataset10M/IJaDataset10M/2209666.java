package org.acegisecurity.providers.ldap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;
import javax.naming.Context;
import javax.naming.NamingException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * LDAP Utility methods.
 *
 * @author Luke Taylor
 * @version $Id: LdapUtils.java,v 1.5 2006/01/27 18:53:37 luke_t Exp $
 */
public class LdapUtils {

    private static final Log logger = LogFactory.getLog(LdapUtils.class);

    public static void closeContext(Context ctx) {
        try {
            if (ctx != null) {
                ctx.close();
            }
        } catch (NamingException e) {
            logger.error("Failed to close context.", e);
        }
    }

    /**
     * Parses the supplied LDAP URL.
     * @param url the URL (e.g. <tt>ldap://monkeymachine:11389/dc=acegisecurity,dc=org</tt>).
     * @return the URI object created from the URL
     * @throws IllegalArgumentException if the URL is null, empty or the URI syntax is invalid.
     */
    public static URI parseLdapUrl(String url) {
        Assert.hasLength(url);
        try {
            return new URI(url);
        } catch (URISyntaxException e) {
            IllegalArgumentException iae = new IllegalArgumentException("Unable to parse url: " + url);
            iae.initCause(e);
            throw iae;
        }
    }

    public static byte[] getUtf8Bytes(String s) {
        try {
            return s.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Failed to convert string to UTF-8 bytes. Shouldn't be possible");
        }
    }

    public static String escapeNameForFilter(String name) {
        return name;
    }

    /**
     * Obtains the part of a DN relative to a supplied base context.
     * <p>
     * If the DN is "cn=bob,ou=people,dc=acegisecurity,dc=org" and the base context
     * name is "ou=people,dc=acegisecurity,dc=org" it would return "cn=bob".
     * </p>
     *
     * @param fullDn the DN
     * @param baseCtx the context to work out the name relative to.
     * @return the
     * @throws NamingException any exceptions thrown by the context are propagated.
     */
    public static String getRelativeName(String fullDn, Context baseCtx) throws NamingException {
        String baseDn = baseCtx.getNameInNamespace();
        if (baseDn.length() == 0) {
            return fullDn;
        }
        if (baseDn.equals(fullDn)) {
            return "";
        }
        int index = fullDn.lastIndexOf(baseDn);
        Assert.isTrue(index > 0, "Context base DN is not contained in the full DN");
        return fullDn.substring(0, index - 1);
    }
}
