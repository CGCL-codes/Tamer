package com.intel.gpe.clients.gpe4gtk;

import javax.xml.rpc.Stub;
import org.globus.axis.gsi.GSIConstants;
import org.globus.gsi.GlobusCredential;
import org.globus.gsi.GlobusCredentialException;
import org.globus.wsrf.impl.security.authorization.HostAuthorization;
import org.globus.wsrf.impl.security.descriptor.SecurityConfig;
import org.globus.wsrf.security.Constants;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;
import com.intel.util.cmd.CommandLineOptionsParser;

public class GSISecureConversationSetup implements ISecuritySetup {

    private GSSCredential cred;

    public GSISecureConversationSetup(CommandLineOptionsParser args) throws GlobusCredentialException, GSSException {
        String[] proxies = args.getOptionValue("proxy");
        if (proxies != null && proxies.length > 0) {
            String proxyFile = proxies[0];
            cred = SecurityConfig.toGSSCredential(new GlobusCredential(proxyFile));
        }
    }

    public GSISecureConversationSetup(String proxyFileName) throws GlobusCredentialException, GSSException {
        cred = SecurityConfig.toGSSCredential(new GlobusCredential(proxyFileName));
    }

    public void setupDelegation(Stub stub) throws Exception {
        stub._setProperty(GSIConstants.GSI_CREDENTIALS, cred);
        stub._setProperty(GSIConstants.GSI_TRANSPORT, Constants.SIGNATURE);
        stub._setProperty(Constants.AUTHORIZATION, HostAuthorization.getInstance());
        stub._setProperty(Constants.GSI_SEC_CONV, Constants.SIGNATURE);
        stub._setProperty(GSIConstants.GSI_MODE, GSIConstants.GSI_MODE_FULL_DELEG);
    }

    public void setup(Stub stub) throws Exception {
        stub._setProperty(GSIConstants.GSI_CREDENTIALS, cred);
        stub._setProperty(GSIConstants.GSI_TRANSPORT, Constants.SIGNATURE);
        stub._setProperty(Constants.AUTHORIZATION, HostAuthorization.getInstance());
        stub._setProperty(Constants.GSI_SEC_CONV, Constants.SIGNATURE);
        stub._setProperty(GSIConstants.GSI_MODE, GSIConstants.GSI_MODE_FULL_DELEG);
    }
}
