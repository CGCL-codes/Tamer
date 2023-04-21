package ch.ethz.dcg.spamato.base.common.util.hash;

/**
 * This class provides an easy to use wrapper for calculating SHA1 hash values
 * out of strings or bytes.
 * 
 * @author simon
 */
public class HashSHA1 extends HashEngine {

    protected String getHashAlgorithmName() {
        return "SHA1";
    }
}
