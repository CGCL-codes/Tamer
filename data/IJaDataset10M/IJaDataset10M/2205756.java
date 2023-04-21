package cryptix.sasl;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import javax.security.auth.callback.CallbackHandler;
import javax.security.sasl.Sasl;
import javax.security.sasl.SaslServer;
import javax.security.sasl.SaslException;

/**
 * The Cryptix implementation of the base SASL server-side mechanism.
 *
 * @version $Revision: 1.5 $
 * @see cryptix.sasl.sm2.SM2Server
 * @see cryptix.sasl.srp.SRPServer
 */
public abstract class ServerMechanism implements SaslServer, SaslParams {

    /** Name of this mechanism. */
    protected String mechanism;

    /** Name of protocol using this mechanism. */
    protected String protocol;

    /** Name of server to authenticate to. */
    protected String serverName;

    /** Properties of qualities desired for this mechanism. */
    protected Map properties;

    /** Callback handler to use with this mechanism instance. */
    protected CallbackHandler handler;

    /** Whether authentication phase is completed (true) or not (false). */
    protected boolean complete = false;

    /** The authorisation identity. */
    protected String authorizationID;

    /** The state of the authentication automaton. */
    protected int state = 0;

    /** The provider for authentication information. */
    protected AuthInfoServices authenticator;

    private int rawSendSize = BUFFER_LIMIT;

    protected ServerMechanism(String mechanism, String protocol, String serverName, Map props, CallbackHandler cbh) {
        this.mechanism = mechanism;
        this.protocol = protocol;
        this.serverName = serverName;
        this.properties = (props == null ? new HashMap() : props);
        this.handler = cbh;
        authenticator = AuthInfo.getProvider(mechanism);
    }

    public abstract byte[] evaluateResponse(byte[] response) throws SaslException;

    public boolean isComplete() {
        return complete;
    }

    public byte[] unwrap(byte[] incoming, int offset, int len) throws SaslException {
        if (!isComplete()) throw new IllegalMechanismStateException();
        return this.engineUnwrap(incoming, offset, len);
    }

    public byte[] wrap(byte[] outgoing, int offset, int len) throws SaslException {
        if (!isComplete()) throw new IllegalMechanismStateException();
        return this.engineWrap(outgoing, offset, len);
    }

    public String getMechanismName() {
        return this.mechanism;
    }

    public String getAuthorizationID() {
        return this.authorizationID;
    }

    public Object getNegotiatedProperty(String propName) throws SaslException {
        if (!isComplete()) throw new IllegalMechanismStateException();
        if (Sasl.QOP.equals(propName)) return getNegotiatedQOP(); else if (Sasl.STRENGTH.equals(propName)) return getNegotiatedStrength(); else if (Sasl.SERVER_AUTH.equals(propName)) return getNegotiatedServerAuth(); else if (Sasl.MAX_BUFFER.equals(propName)) return getNegotiatedMaxBuffer(); else if (Sasl.CLIENT_PKGS.equals(propName)) return "cryptix.sasl"; else if (Sasl.SERVER_PKGS.equals(propName)) return "cryptix.sasl"; else if (Sasl.RAW_SEND_SIZE.equals(propName)) return getNegotiatedRawSendSize(); else if (Sasl.POLICY_NOPLAINTEXT.equals(propName)) return getNegotiatedPolicyNoPlainText(); else if (Sasl.POLICY_NOACTIVE.equals(propName)) return getNegotiatedPolicyNoActive(); else if (Sasl.POLICY_NODICTIONARY.equals(propName)) return getNegotiatedPolicyNoDictionary(); else if (Sasl.POLICY_NOANONYMOUS.equals(propName)) return getNegotiatedPolicyNoAnonymous(); else if (Sasl.POLICY_FORWARD_SECRECY.equals(propName)) return getNegotiatedPolicyForwardSecrecy(); else if (Sasl.POLICY_PASS_CREDENTIALS.equals(propName)) return getNegotiatedPolicyPassCredentials(); else return null;
    }

    public void dispose() throws SaslException {
    }

    protected String getNegotiatedQOP() {
        return null;
    }

    protected String getNegotiatedStrength() {
        return null;
    }

    protected String getNegotiatedServerAuth() {
        return null;
    }

    protected String getNegotiatedMaxBuffer() {
        return null;
    }

    protected String getNegotiatedPolicyNoPlainText() {
        return null;
    }

    protected String getNegotiatedPolicyNoActive() {
        return null;
    }

    protected String getNegotiatedPolicyNoDictionary() {
        return null;
    }

    protected String getNegotiatedPolicyNoAnonymous() {
        return null;
    }

    protected String getNegotiatedPolicyForwardSecrecy() {
        return null;
    }

    protected String getNegotiatedPolicyPassCredentials() {
        return null;
    }

    protected String getNegotiatedRawSendSize() {
        return String.valueOf(rawSendSize);
    }

    protected byte[] engineUnwrap(byte[] incoming, int offset, int len) throws SaslException {
        byte[] buffy = new byte[len];
        System.arraycopy(incoming, offset, buffy, 0, len);
        return buffy;
    }

    protected byte[] engineWrap(byte[] outgoing, int offset, int len) throws SaslException {
        byte[] buffy = new byte[len];
        System.arraycopy(outgoing, offset, buffy, 0, len);
        return buffy;
    }
}
