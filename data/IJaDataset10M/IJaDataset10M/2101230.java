package sun.security.jgss.spnego;

import org.ietf.jgss.*;
import java.security.Provider;
import sun.security.jgss.GSSUtil;
import sun.security.jgss.ProviderList;
import sun.security.jgss.GSSCredentialImpl;
import sun.security.jgss.spi.GSSNameSpi;
import sun.security.jgss.spi.GSSCredentialSpi;

/**
 * This class is the cred element implementation for SPNEGO mech.
 * NOTE: The current implementation can only support one mechanism.
 * This should be changed once multi-mechanism support is needed.
 *
 * @author Valerie Peng
 * @since 1.6
 */
public class SpNegoCredElement implements GSSCredentialSpi {

    private GSSCredentialSpi cred = null;

    SpNegoCredElement(GSSCredentialSpi cred) throws GSSException {
        this.cred = cred;
    }

    Oid getInternalMech() {
        return cred.getMechanism();
    }

    public GSSCredentialSpi getInternalCred() {
        return cred;
    }

    public Provider getProvider() {
        return SpNegoMechFactory.PROVIDER;
    }

    public void dispose() throws GSSException {
        cred.dispose();
    }

    public GSSNameSpi getName() throws GSSException {
        return cred.getName();
    }

    public int getInitLifetime() throws GSSException {
        return cred.getInitLifetime();
    }

    public int getAcceptLifetime() throws GSSException {
        return cred.getAcceptLifetime();
    }

    public boolean isInitiatorCredential() throws GSSException {
        return cred.isInitiatorCredential();
    }

    public boolean isAcceptorCredential() throws GSSException {
        return cred.isAcceptorCredential();
    }

    public Oid getMechanism() {
        return GSSUtil.GSS_SPNEGO_MECH_OID;
    }
}
