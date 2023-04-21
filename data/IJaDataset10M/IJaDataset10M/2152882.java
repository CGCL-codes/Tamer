package sun.security.pkcs11;

import java.security.*;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.*;
import javax.crypto.spec.*;
import sun.security.internal.spec.TlsMasterSecretParameterSpec;
import static sun.security.pkcs11.TemplateManager.*;
import sun.security.pkcs11.wrapper.*;
import static sun.security.pkcs11.wrapper.PKCS11Constants.*;

/**
 * KeyGenerator for the SSL/TLS master secret.
 *
 * @author  Andreas Sterbenz
 * @since   1.6
 */
public final class P11TlsMasterSecretGenerator extends KeyGeneratorSpi {

    private static final String MSG = "TlsMasterSecretGenerator must be " + "initialized using a TlsMasterSecretParameterSpec";

    private final Token token;

    private final String algorithm;

    private long mechanism;

    private TlsMasterSecretParameterSpec spec;

    private P11Key p11Key;

    int version;

    P11TlsMasterSecretGenerator(Token token, String algorithm, long mechanism) throws PKCS11Exception {
        super();
        this.token = token;
        this.algorithm = algorithm;
        this.mechanism = mechanism;
    }

    protected void engineInit(SecureRandom random) {
        throw new InvalidParameterException(MSG);
    }

    protected void engineInit(AlgorithmParameterSpec params, SecureRandom random) throws InvalidAlgorithmParameterException {
        if (params instanceof TlsMasterSecretParameterSpec == false) {
            throw new InvalidAlgorithmParameterException(MSG);
        }
        this.spec = (TlsMasterSecretParameterSpec) params;
        SecretKey key = spec.getPremasterSecret();
        try {
            p11Key = P11SecretKeyFactory.convertKey(token, key, null);
        } catch (InvalidKeyException e) {
            throw new InvalidAlgorithmParameterException("init() failed", e);
        }
        version = (spec.getMajorVersion() << 8) | spec.getMinorVersion();
        if ((version < 0x0300) || (version > 0x0302)) {
            throw new InvalidAlgorithmParameterException("Only SSL 3.0, TLS 1.0, and TLS 1.1 supported");
        }
    }

    protected void engineInit(int keysize, SecureRandom random) {
        throw new InvalidParameterException(MSG);
    }

    protected SecretKey engineGenerateKey() {
        if (spec == null) {
            throw new IllegalStateException("TlsMasterSecretGenerator must be initialized");
        }
        CK_VERSION ckVersion;
        if (p11Key.getAlgorithm().equals("TlsRsaPremasterSecret")) {
            mechanism = (version == 0x0300) ? CKM_SSL3_MASTER_KEY_DERIVE : CKM_TLS_MASTER_KEY_DERIVE;
            ckVersion = new CK_VERSION(0, 0);
        } else {
            mechanism = (version == 0x0300) ? CKM_SSL3_MASTER_KEY_DERIVE_DH : CKM_TLS_MASTER_KEY_DERIVE_DH;
            ckVersion = null;
        }
        byte[] clientRandom = spec.getClientRandom();
        byte[] serverRandom = spec.getServerRandom();
        CK_SSL3_RANDOM_DATA random = new CK_SSL3_RANDOM_DATA(clientRandom, serverRandom);
        CK_SSL3_MASTER_KEY_DERIVE_PARAMS params = new CK_SSL3_MASTER_KEY_DERIVE_PARAMS(random, ckVersion);
        Session session = null;
        try {
            session = token.getObjSession();
            CK_ATTRIBUTE[] attributes = token.getAttributes(O_GENERATE, CKO_SECRET_KEY, CKK_GENERIC_SECRET, new CK_ATTRIBUTE[0]);
            long keyID = token.p11.C_DeriveKey(session.id(), new CK_MECHANISM(mechanism, params), p11Key.keyID, attributes);
            int major, minor;
            ckVersion = params.pVersion;
            if (ckVersion == null) {
                major = -1;
                minor = -1;
            } else {
                major = ckVersion.major;
                minor = ckVersion.minor;
            }
            SecretKey key = P11Key.masterSecretKey(session, keyID, "TlsMasterSecret", 48 << 3, attributes, major, minor);
            return key;
        } catch (Exception e) {
            throw new ProviderException("Could not generate key", e);
        } finally {
            token.releaseSession(session);
        }
    }
}
