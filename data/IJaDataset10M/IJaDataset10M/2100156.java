package cdc.standard.rc6;

import java.security.InvalidAlgorithmParameterException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.SecureRandom;
import javax.crypto.KeyGeneratorSpi;
import javax.crypto.SecretKey;

/**
 * RC6KeyGenerator creates RC6 Keys.
 * The default keysize is 128 bits, it can be 128, 192 or 256 bits.<p>
 *
 * <dl>
 * <dt><b>Progress:</b>
 * <dd>9th December 1998, created</dd>
 * <dd>11th January 1999, first implementation</dd>
 * </dl>
 *
 * @author  Christoph Sesterhenn, Christoph Ender
 * @version 1.00
 */
public class RC6KeyGenerator extends KeyGeneratorSpi {

    private int strength = 128;

    private SecureRandom random;

    /**
	 * Generates a random secret key.
	 *
	 * @return the RC6 key.
	 */
    protected SecretKey engineGenerateKey() {
        byte[] bytes;
        if ((strength != 128) && (strength != 192) && (strength != 256)) strength = 128;
        bytes = new byte[(strength + 7) / 8];
        if (random == null) random = new SecureRandom();
        random.nextBytes(bytes);
        return new RC6Key(bytes);
    }

    /**
	 * Initialises this key generator for a certain strength, using the
	 * given source of randomness.
	 *
	 * @param strength the strength of the key. This is an
	 *    algorithm-specific metric specified in number of bits.
	 * @param random the source of randomness for this key generator
	 */
    protected void engineInit(int strength, SecureRandom random) {
        this.strength = strength;
        this.random = random;
    }

    /**
	 * Initialises the key generator with the given random source.
	 *
	 * @param random        a source of random numbers for this generator.
	 */
    protected void engineInit(SecureRandom random) {
        this.random = random;
    }

    /**
	 * This method is not implemented as there is no AlgorithmParameterSpec
	 * defined for RC6.  (Use one of the other initialisation methods!)
	 *
	 * @param params the algorithm parameter specs for this generator.
	 * @param random	a source of random numbers for this generator.
	 * @exception InvalidAlgorithmParameterException	An invalid
	 * 	parameter specification is provided.
	 */
    protected void engineInit(AlgorithmParameterSpec params, SecureRandom random) throws InvalidAlgorithmParameterException {
        throw new InvalidAlgorithmParameterException("Not Implemented");
    }
}
