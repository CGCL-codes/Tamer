package ca.uwaterloo.crysp.otr.crypt;

/**
 * Generates Key for HMAC.
 * 
 * @author Can Tang (c24tang@gmail.com)
 */
public abstract class HMACKeyGenerator implements KeyGenerator {

    public final String getAlgorithm() {
        return "HMAC";
    }
}
