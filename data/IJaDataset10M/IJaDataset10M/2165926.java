package org.szegedi.spring.crypto;

import java.security.SecureRandom;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import org.szegedi.spring.crypto.support.ProviderBasedFactory;

/**
 * Generates a new secret key. Note that as the key is not preserved across 
 * application context restarts, whatever you might have encrypted using it will 
 * become invalid no later than when the JVM is shut down.
 * @author Attila Szegedi
 * @version $Id: GeneratedSecretKeyFactory.java 79 2007-09-17 11:24:06Z szegedia $
 */
public class GeneratedSecretKeyFactory extends ProviderBasedFactory {

    private String algorithm = "AES";

    private SecureRandom secureRandom;

    private int keySize = 128;

    /**
     * Sets the key algorithm to use. Defaults to "AES".
     * @param algorithm the key algorithm
     */
    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    /**
     * Sets the size of the keys in bits. Defaults to 128.
     * @param keySize the size of the keys.
     */
    public void setKeySize(int keySize) {
        this.keySize = keySize;
    }

    /**
     * Sets an instance of secure random to use. If not set, a one-off instance
     * created using a private instance of {@link SecureRandomFactory} will be
     * created, with the configured security provider.
     * @param secureRandom the secure random instance to use.
     */
    public void setSecureRandom(SecureRandom secureRandom) {
        this.secureRandom = secureRandom;
    }

    protected Object createInstance() throws Exception {
        if (secureRandom == null) {
            SecureRandomFactory secureRandomFactory = new SecureRandomFactory();
            secureRandomFactory.setProvider(provider);
            secureRandomFactory.afterPropertiesSet();
            secureRandom = (SecureRandom) secureRandomFactory.createInstance();
        }
        KeyGenerator kg;
        if (provider == null) {
            kg = KeyGenerator.getInstance(algorithm);
        } else {
            kg = KeyGenerator.getInstance(algorithm, provider);
        }
        kg.init(keySize, secureRandom);
        return kg.generateKey();
    }

    /**
     * @return {@link SecretKey}.class
     */
    public Class getObjectType() {
        return SecretKey.class;
    }
}
