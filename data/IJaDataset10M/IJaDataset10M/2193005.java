package sun.security.pkcs11;

import java.util.*;
import java.io.*;
import java.lang.ref.*;
import java.security.*;
import javax.security.auth.login.LoginException;
import sun.security.jca.JCAUtil;
import sun.security.pkcs11.wrapper.*;
import static sun.security.pkcs11.wrapper.PKCS11Constants.*;

/**
 * PKCS#11 token.
 *
 * @author  Andreas Sterbenz
 * @since   1.5
 */
class Token implements Serializable {

    private static final long serialVersionUID = 2541527649100571747L;

    private static final long CHECK_INTERVAL = 50;

    final SunPKCS11 provider;

    final PKCS11 p11;

    final Config config;

    final CK_TOKEN_INFO tokenInfo;

    final SessionManager sessionManager;

    private final TemplateManager templateManager;

    final boolean explicitCancel;

    final KeyCache secretCache;

    final KeyCache privateCache;

    private volatile P11KeyFactory rsaFactory, dsaFactory, dhFactory, ecFactory;

    private final Map<Long, CK_MECHANISM_INFO> mechInfoMap;

    private volatile P11SecureRandom secureRandom;

    private volatile P11KeyStore keyStore;

    private final boolean removable;

    private volatile boolean valid;

    private long lastPresentCheck;

    private byte[] tokenId;

    private boolean writeProtected;

    private volatile boolean loggedIn;

    private long lastLoginCheck;

    private static final Object CHECK_LOCK = new Object();

    private static final CK_MECHANISM_INFO INVALID_MECH = new CK_MECHANISM_INFO(0, 0, 0);

    Token(SunPKCS11 provider) throws PKCS11Exception {
        this.provider = provider;
        this.removable = provider.removable;
        this.valid = true;
        p11 = provider.p11;
        config = provider.config;
        tokenInfo = p11.C_GetTokenInfo(provider.slotID);
        writeProtected = (tokenInfo.flags & CKF_WRITE_PROTECTED) != 0;
        SessionManager sessionManager;
        try {
            sessionManager = new SessionManager(this);
            Session s = sessionManager.getOpSession();
            sessionManager.releaseSession(s);
        } catch (PKCS11Exception e) {
            if (writeProtected) {
                throw e;
            }
            writeProtected = true;
            sessionManager = new SessionManager(this);
            Session s = sessionManager.getOpSession();
            sessionManager.releaseSession(s);
        }
        this.sessionManager = sessionManager;
        secretCache = new KeyCache();
        privateCache = new KeyCache();
        templateManager = config.getTemplateManager();
        explicitCancel = config.getExplicitCancel();
        mechInfoMap = Collections.synchronizedMap(new HashMap<Long, CK_MECHANISM_INFO>(10));
    }

    boolean isWriteProtected() {
        return writeProtected;
    }

    boolean isLoggedIn(Session session) throws PKCS11Exception {
        boolean loggedIn = this.loggedIn;
        long time = System.currentTimeMillis();
        if (time - lastLoginCheck > CHECK_INTERVAL) {
            loggedIn = isLoggedInNow(session);
            lastLoginCheck = time;
        }
        return loggedIn;
    }

    boolean isLoggedInNow(Session session) throws PKCS11Exception {
        boolean allocSession = (session == null);
        try {
            if (allocSession) {
                session = getOpSession();
            }
            CK_SESSION_INFO info = p11.C_GetSessionInfo(session.id());
            boolean loggedIn = (info.state == CKS_RO_USER_FUNCTIONS) || (info.state == CKS_RW_USER_FUNCTIONS);
            this.loggedIn = loggedIn;
            return loggedIn;
        } finally {
            if (allocSession) {
                releaseSession(session);
            }
        }
    }

    void ensureLoggedIn(Session session) throws PKCS11Exception, LoginException {
        if (isLoggedIn(session) == false) {
            provider.login(null, null);
        }
    }

    boolean isValid() {
        if (removable == false) {
            return true;
        }
        return valid;
    }

    void ensureValid() {
        if (isValid() == false) {
            throw new ProviderException("Token has been removed");
        }
    }

    boolean isPresent(Session session) {
        if (removable == false) {
            return true;
        }
        if (valid == false) {
            return false;
        }
        long time = System.currentTimeMillis();
        if ((time - lastPresentCheck) >= CHECK_INTERVAL) {
            synchronized (CHECK_LOCK) {
                if ((time - lastPresentCheck) >= CHECK_INTERVAL) {
                    boolean ok = false;
                    try {
                        CK_SLOT_INFO slotInfo = provider.p11.C_GetSlotInfo(provider.slotID);
                        if ((slotInfo.flags & CKF_TOKEN_PRESENT) != 0) {
                            CK_SESSION_INFO sessInfo = provider.p11.C_GetSessionInfo(session.idInternal());
                            ok = true;
                        }
                    } catch (PKCS11Exception e) {
                    }
                    valid = ok;
                    lastPresentCheck = System.currentTimeMillis();
                    if (ok == false) {
                        destroy();
                    }
                }
            }
        }
        return valid;
    }

    void destroy() {
        valid = false;
        provider.uninitToken(this);
    }

    Session getObjSession() throws PKCS11Exception {
        return sessionManager.getObjSession();
    }

    Session getOpSession() throws PKCS11Exception {
        return sessionManager.getOpSession();
    }

    Session releaseSession(Session session) {
        return sessionManager.releaseSession(session);
    }

    Session killSession(Session session) {
        return sessionManager.killSession(session);
    }

    CK_ATTRIBUTE[] getAttributes(String op, long type, long alg, CK_ATTRIBUTE[] attrs) throws PKCS11Exception {
        CK_ATTRIBUTE[] newAttrs = templateManager.getAttributes(op, type, alg, attrs);
        for (CK_ATTRIBUTE attr : newAttrs) {
            if (attr.type == CKA_TOKEN) {
                if (attr.getBoolean()) {
                    try {
                        ensureLoggedIn(null);
                    } catch (LoginException e) {
                        throw new ProviderException("Login failed", e);
                    }
                }
                break;
            }
        }
        return newAttrs;
    }

    P11KeyFactory getKeyFactory(String algorithm) {
        P11KeyFactory f;
        if (algorithm.equals("RSA")) {
            f = rsaFactory;
            if (f == null) {
                f = new P11RSAKeyFactory(this, algorithm);
                rsaFactory = f;
            }
        } else if (algorithm.equals("DSA")) {
            f = dsaFactory;
            if (f == null) {
                f = new P11DSAKeyFactory(this, algorithm);
                dsaFactory = f;
            }
        } else if (algorithm.equals("DH")) {
            f = dhFactory;
            if (f == null) {
                f = new P11DHKeyFactory(this, algorithm);
                dhFactory = f;
            }
        } else if (algorithm.equals("EC")) {
            f = ecFactory;
            if (f == null) {
                f = new P11ECKeyFactory(this, algorithm);
                ecFactory = f;
            }
        } else {
            throw new ProviderException("Unknown algorithm " + algorithm);
        }
        return f;
    }

    P11SecureRandom getRandom() {
        if (secureRandom == null) {
            secureRandom = new P11SecureRandom(this);
        }
        return secureRandom;
    }

    P11KeyStore getKeyStore() {
        if (keyStore == null) {
            keyStore = new P11KeyStore(this);
        }
        return keyStore;
    }

    CK_MECHANISM_INFO getMechanismInfo(long mechanism) throws PKCS11Exception {
        CK_MECHANISM_INFO result = mechInfoMap.get(mechanism);
        if (result == null) {
            try {
                result = p11.C_GetMechanismInfo(provider.slotID, mechanism);
                mechInfoMap.put(mechanism, result);
            } catch (PKCS11Exception e) {
                if (e.getErrorCode() != PKCS11Constants.CKR_MECHANISM_INVALID) {
                    throw e;
                } else {
                    mechInfoMap.put(mechanism, INVALID_MECH);
                }
            }
        } else if (result == INVALID_MECH) {
            result = null;
        }
        return result;
    }

    private synchronized byte[] getTokenId() {
        if (tokenId == null) {
            SecureRandom random = JCAUtil.getSecureRandom();
            tokenId = new byte[20];
            random.nextBytes(tokenId);
            serializedTokens.add(new WeakReference<Token>(this));
        }
        return tokenId;
    }

    private static final List<Reference<Token>> serializedTokens = new ArrayList<Reference<Token>>();

    private Object writeReplace() throws ObjectStreamException {
        if (isValid() == false) {
            throw new NotSerializableException("Token has been removed");
        }
        return new TokenRep(this);
    }

    private static class TokenRep implements Serializable {

        private static final long serialVersionUID = 3503721168218219807L;

        private final byte[] tokenId;

        TokenRep(Token token) {
            tokenId = token.getTokenId();
        }

        private Object readResolve() throws ObjectStreamException {
            for (Reference<Token> tokenRef : serializedTokens) {
                Token token = tokenRef.get();
                if ((token != null) && token.isValid()) {
                    if (Arrays.equals(token.getTokenId(), tokenId)) {
                        return token;
                    }
                }
            }
            throw new NotSerializableException("Could not find token");
        }
    }
}
