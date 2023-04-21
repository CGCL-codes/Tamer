package gnu.java.security.x509;

import gnu.java.security.OID;
import gnu.java.security.x509.ext.Extension;
import java.security.cert.X509Extension;
import java.util.Collection;

public interface GnuPKIExtension extends X509Extension {

    /**
   * Returns the extension object for the given object identifier.
   *
   * @param oid The OID of the extension to get.
   * @return The extension, or null if there is no such extension.
   */
    Extension getExtension(OID oid);

    Collection getExtensions();
}
