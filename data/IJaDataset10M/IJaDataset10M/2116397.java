package org.apache.qpid.management.common.sasl;

import java.util.Map;
import org.apache.harmony.javax.security.auth.callback.CallbackHandler;
import org.apache.harmony.javax.security.sasl.SaslClient;
import org.apache.harmony.javax.security.sasl.SaslClientFactory;
import org.apache.harmony.javax.security.sasl.SaslException;

public class ClientSaslFactory implements SaslClientFactory {

    public SaslClient createSaslClient(String[] mechs, String authorizationId, String protocol, String serverName, Map props, CallbackHandler cbh) throws SaslException {
        for (int i = 0; i < mechs.length; i++) {
            if (mechs[i].equals("PLAIN")) {
                return new PlainSaslClient(authorizationId, cbh);
            }
        }
        return null;
    }

    /**
     * Simple-minded implementation that ignores props
     */
    public String[] getMechanismNames(Map props) {
        return new String[] { "PLAIN" };
    }
}
