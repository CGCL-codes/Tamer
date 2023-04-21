package sun.security.ssl;

import javax.net.ssl.*;
import java.security.*;
import java.security.cert.*;
import java.security.cert.Certificate;
import java.util.*;
import java.net.Socket;
import javax.security.auth.x500.X500Principal;

/**
 * An implemention of X509KeyManager backed by a KeyStore.
 *
 * The backing KeyStore is inspected when this object is constructed.
 * All key entries containing a PrivateKey and a non-empty chain of
 * X509Certificate are then copied into an internal store. This means
 * that subsequent modifications of the KeyStore have no effect on the
 * X509KeyManagerImpl object.
 *
 * Note that this class assumes that all keys are protected by the same
 * password.
 *
 * The JSSE handshake code currently calls into this class via
 * chooseClientAlias() and chooseServerAlias() to find the certificates to
 * use. As implemented here, both always return the first alias returned by
 * getClientAliases() and getServerAliases(). In turn, these methods are
 * implemented by calling getAliases(), which performs the actual lookup.
 *
 * Note that this class currently implements no checking of the local
 * certificates. In particular, it is *not* guaranteed that:
 *  . the certificates are within their validity period and not revoked
 *  . the signatures verify
 *  . they form a PKIX compliant chain.
 *  . the certificate extensions allow the certificate to be used for
 *    the desired purpose.
 *
 * Chains that fail any of these criteria will probably be rejected by
 * the remote peer.
 *
 */
final class SunX509KeyManagerImpl extends X509ExtendedKeyManager {

    private static final Debug debug = Debug.getInstance("ssl");

    private static final String[] STRING0 = new String[0];

    private Map<String, X509Credentials> credentialsMap;

    private Map<String, String[]> serverAliasCache;

    private static class X509Credentials {

        PrivateKey privateKey;

        X509Certificate[] certificates;

        private Set<X500Principal> issuerX500Principals;

        X509Credentials(PrivateKey privateKey, X509Certificate[] certificates) {
            this.privateKey = privateKey;
            this.certificates = certificates;
        }

        synchronized Set<X500Principal> getIssuerX500Principals() {
            if (issuerX500Principals == null) {
                issuerX500Principals = new HashSet<X500Principal>();
                for (int i = 0; i < certificates.length; i++) {
                    issuerX500Principals.add(certificates[i].getIssuerX500Principal());
                }
            }
            return issuerX500Principals;
        }
    }

    SunX509KeyManagerImpl(KeyStore ks, char[] password) throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException {
        credentialsMap = new HashMap<String, X509Credentials>();
        serverAliasCache = new HashMap<String, String[]>();
        if (ks == null) {
            return;
        }
        for (Enumeration<String> aliases = ks.aliases(); aliases.hasMoreElements(); ) {
            String alias = aliases.nextElement();
            if (!ks.isKeyEntry(alias)) {
                continue;
            }
            Key key = ks.getKey(alias, password);
            if (key instanceof PrivateKey == false) {
                continue;
            }
            Certificate[] certs = ks.getCertificateChain(alias);
            if ((certs == null) || (certs.length == 0) || !(certs[0] instanceof X509Certificate)) {
                continue;
            }
            if (!(certs instanceof X509Certificate[])) {
                Certificate[] tmp = new X509Certificate[certs.length];
                System.arraycopy(certs, 0, tmp, 0, certs.length);
                certs = tmp;
            }
            X509Credentials cred = new X509Credentials((PrivateKey) key, (X509Certificate[]) certs);
            credentialsMap.put(alias, cred);
            if (debug != null && Debug.isOn("keymanager")) {
                System.out.println("***");
                System.out.println("found key for : " + alias);
                for (int i = 0; i < certs.length; i++) {
                    System.out.println("chain [" + i + "] = " + certs[i]);
                }
                System.out.println("***");
            }
        }
    }

    public X509Certificate[] getCertificateChain(String alias) {
        if (alias == null) {
            return null;
        }
        X509Credentials cred = credentialsMap.get(alias);
        if (cred == null) {
            return null;
        } else {
            return cred.certificates.clone();
        }
    }

    public PrivateKey getPrivateKey(String alias) {
        if (alias == null) {
            return null;
        }
        X509Credentials cred = credentialsMap.get(alias);
        if (cred == null) {
            return null;
        } else {
            return cred.privateKey;
        }
    }

    public String chooseClientAlias(String[] keyTypes, Principal[] issuers, Socket socket) {
        if (keyTypes == null) {
            return null;
        }
        for (int i = 0; i < keyTypes.length; i++) {
            String[] aliases = getClientAliases(keyTypes[i], issuers);
            if ((aliases != null) && (aliases.length > 0)) {
                return aliases[0];
            }
        }
        return null;
    }

    public String chooseEngineClientAlias(String[] keyType, Principal[] issuers, SSLEngine engine) {
        return chooseClientAlias(keyType, issuers, null);
    }

    public String chooseServerAlias(String keyType, Principal[] issuers, Socket socket) {
        if (keyType == null) {
            return null;
        }
        String[] aliases;
        if (issuers == null || issuers.length == 0) {
            aliases = serverAliasCache.get(keyType);
            if (aliases == null) {
                aliases = getServerAliases(keyType, issuers);
                if (aliases == null) {
                    aliases = STRING0;
                }
                serverAliasCache.put(keyType, aliases);
            }
        } else {
            aliases = getServerAliases(keyType, issuers);
        }
        if ((aliases != null) && (aliases.length > 0)) {
            return aliases[0];
        }
        return null;
    }

    public String chooseEngineServerAlias(String keyType, Principal[] issuers, SSLEngine engine) {
        return chooseServerAlias(keyType, issuers, null);
    }

    public String[] getClientAliases(String keyType, Principal[] issuers) {
        return getAliases(keyType, issuers);
    }

    public String[] getServerAliases(String keyType, Principal[] issuers) {
        return getAliases(keyType, issuers);
    }

    private String[] getAliases(String keyType, Principal[] issuers) {
        if (keyType == null) {
            return null;
        }
        if (issuers == null) {
            issuers = new X500Principal[0];
        }
        if (issuers instanceof X500Principal[] == false) {
            issuers = convertPrincipals(issuers);
        }
        String sigType;
        if (keyType.contains("_")) {
            int k = keyType.indexOf("_");
            sigType = keyType.substring(k + 1);
            keyType = keyType.substring(0, k);
        } else {
            sigType = null;
        }
        X500Principal[] x500Issuers = (X500Principal[]) issuers;
        List<String> aliases = new ArrayList<String>();
        for (Map.Entry<String, X509Credentials> entry : credentialsMap.entrySet()) {
            String alias = entry.getKey();
            X509Credentials credentials = entry.getValue();
            X509Certificate[] certs = credentials.certificates;
            if (!keyType.equals(certs[0].getPublicKey().getAlgorithm())) {
                continue;
            }
            if (sigType != null) {
                if (certs.length > 1) {
                    if (!sigType.equals(certs[1].getPublicKey().getAlgorithm())) {
                        continue;
                    }
                } else {
                    String sigAlgName = certs[0].getSigAlgName().toUpperCase(Locale.ENGLISH);
                    String pattern = "WITH" + sigType.toUpperCase(Locale.ENGLISH);
                    if (sigAlgName.contains(pattern) == false) {
                        continue;
                    }
                }
            }
            if (issuers.length == 0) {
                aliases.add(alias);
                if (debug != null && Debug.isOn("keymanager")) {
                    System.out.println("matching alias: " + alias);
                }
            } else {
                Set<X500Principal> certIssuers = credentials.getIssuerX500Principals();
                for (int i = 0; i < x500Issuers.length; i++) {
                    if (certIssuers.contains(issuers[i])) {
                        aliases.add(alias);
                        if (debug != null && Debug.isOn("keymanager")) {
                            System.out.println("matching alias: " + alias);
                        }
                        break;
                    }
                }
            }
        }
        String[] aliasStrings = aliases.toArray(STRING0);
        return ((aliasStrings.length == 0) ? null : aliasStrings);
    }

    private static X500Principal[] convertPrincipals(Principal[] principals) {
        List<X500Principal> list = new ArrayList<X500Principal>(principals.length);
        for (int i = 0; i < principals.length; i++) {
            Principal p = principals[i];
            if (p instanceof X500Principal) {
                list.add((X500Principal) p);
            } else {
                try {
                    list.add(new X500Principal(p.getName()));
                } catch (IllegalArgumentException e) {
                }
            }
        }
        return list.toArray(new X500Principal[list.size()]);
    }
}
