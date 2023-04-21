package com.mindbright.ssh2;

import java.util.Hashtable;
import com.mindbright.jca.security.MessageDigest;

/**
 * Base class for implementing ssh key exchange algorithms.
 */
public abstract class SSH2KeyExchanger {

    private static Hashtable algorithms;

    static {
        algorithms = new Hashtable();
        algorithms.put("diffie-hellman-group1-sha1", "com.mindbright.ssh2.SSH2KEXDHGroup1SHA1");
        algorithms.put("diffie-hellman-group-exchange-sha1", "com.mindbright.ssh2.SSH2KEXDHGroupXSHA1");
    }

    protected SSH2KeyExchanger() {
    }

    public static SSH2KeyExchanger getInstance(String algorithm) throws SSH2KEXFailedException {
        String alg = (String) algorithms.get(algorithm);
        SSH2KeyExchanger kex = null;
        if (alg != null) {
            try {
                Class c = Class.forName(alg);
                kex = (SSH2KeyExchanger) c.newInstance();
            } catch (Throwable t) {
                kex = null;
            }
        }
        if (kex == null) {
            throw new SSH2KEXFailedException("Unknown kex algorithm: " + algorithm);
        }
        return kex;
    }

    public abstract void init(SSH2Transport transport) throws SSH2Exception;

    public abstract void processKEXMethodPDU(SSH2TransportPDU pdu) throws SSH2Exception;

    public abstract MessageDigest getExchangeHashAlgorithm();

    public abstract byte[] getSharedSecret_K();

    public abstract byte[] getExchangeHash_H();

    public abstract String getHostKeyAlgorithms();
}
