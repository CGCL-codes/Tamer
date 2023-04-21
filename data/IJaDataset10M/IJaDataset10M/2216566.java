package org.encuestame.persistence.utils;

import static org.encuestame.persistence.utils.EncodingUtils.hexEncode;

/**
 * A StringKeyGenerator that uses a SecureRandom to generate hex-encoded String keys.
 * Defaults to 8 byte keys produced by the SHA1PRNG algorithm developed by the Sun Provider.
 * @author Picado, Juan juanATencuestame.org
 * @since Dec 24, 2010 4:11:18 PM
 * @version $Id:$
 */
public final class SecureRandomStringKeyGenerator implements StringKeyGenerator {

    private final SecureRandomKeyGenerator keyGenerator;

    /**
     * Creates a secure random string key generator with the defaults.
     */
    public SecureRandomStringKeyGenerator() {
        keyGenerator = new SecureRandomKeyGenerator();
    }

    /**
     * Creates a fully customized string key generator.
     */
    public SecureRandomStringKeyGenerator(String algorithm, String provider, int keyLength) {
        keyGenerator = new SecureRandomKeyGenerator(algorithm, provider, keyLength);
    }

    public String generateKey() {
        return hexEncode(keyGenerator.generateKey());
    }
}
