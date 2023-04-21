package sun.security.jgss.wrapper;

import org.ietf.jgss.*;
import javax.security.auth.kerberos.ServicePermission;

/**
 * This class is an utility class for Kerberos related stuff.
 * @author Valerie Peng
 * @since 1.6
 */
class Krb5Util {

    static String getTGSName(GSSNameElement name) throws GSSException {
        String krbPrinc = name.getKrbName();
        int atIndex = krbPrinc.indexOf("@");
        String realm = krbPrinc.substring(atIndex + 1);
        StringBuffer buf = new StringBuffer("krbtgt/");
        buf.append(realm).append('@').append(realm);
        return buf.toString();
    }

    static void checkServicePermission(String target, String action) {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            SunNativeProvider.debug("Checking ServicePermission(" + target + ", " + action + ")");
            ServicePermission perm = new ServicePermission(target, action);
            sm.checkPermission(perm);
        }
    }
}
