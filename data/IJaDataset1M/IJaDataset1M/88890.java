package org.atricore.idbus.idojos.strongauthscheme;

import org.atricore.idbus.kernel.main.authn.BaseCredential;

/**
 * X.509 Credential used for Strong Authentication.
 * <p/>
 * Acts as a wrapper of an java.security.cert.X509Certificate instance.
 *
 * @author <a href="mailto:gbrigand@josso.org">Gianluca Brigandi</a>
 * @version CVS $Id: X509CertificateCredential.java 1040 2009-03-05 00:56:52Z gbrigand $
 */
public class X509CertificateCredential extends BaseCredential {

    public X509CertificateCredential(Object credential) {
        super(credential);
    }
}
