package gnu.javax.crypto.sasl.anonymous;

import gnu.java.security.Registry;
import gnu.javax.crypto.sasl.ClientMechanism;
import gnu.javax.crypto.sasl.IllegalMechanismStateException;
import java.io.UnsupportedEncodingException;
import javax.security.sasl.AuthenticationException;
import javax.security.sasl.SaslClient;
import javax.security.sasl.SaslException;

/**
 * The ANONYMOUS client-side mechanism.
 */
public class AnonymousClient extends ClientMechanism implements SaslClient {

    public AnonymousClient() {
        super(Registry.SASL_ANONYMOUS_MECHANISM);
    }

    protected void initMechanism() throws SaslException {
    }

    protected void resetMechanism() throws SaslException {
    }

    public boolean hasInitialResponse() {
        return true;
    }

    public byte[] evaluateChallenge(final byte[] challenge) throws SaslException {
        if (complete) {
            throw new IllegalMechanismStateException("evaluateChallenge()");
        }
        return response();
    }

    private byte[] response() throws SaslException {
        if (!AnonymousUtil.isValidTraceInformation(authorizationID)) throw new AuthenticationException("Authorisation ID is not a valid email address");
        complete = true;
        final byte[] result;
        try {
            result = authorizationID.getBytes("UTF-8");
        } catch (UnsupportedEncodingException x) {
            throw new AuthenticationException("response()", x);
        }
        return result;
    }
}
