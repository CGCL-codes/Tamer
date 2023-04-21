package de.fhg.igd.crypto;

import java.security.AlgorithmParameterGeneratorSpi;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.ProviderException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.spec.RC2ParameterSpec;

/**
 * This class generates parameters for RC2 according to
 * RFC 2311 and RFC 2268. Encoding and Decoding conforms
 * to ASN.1/DER as set forth in said RFCs. The
 * RC2ParameterSpec and subclasses thereof are supported
 * for initialising the parameters.
 *
 * @author Volker Roth
 * @version "$Id: RC2ParameterGenerator.java 1913 2007-08-08 02:41:53Z jpeters $"
 *
 * @see DESParameters
 * @see java.security.spec.AlgorithmParameterSpec
 */
public class RC2ParameterGenerator extends AlgorithmParameterGeneratorSpi {

    /**
     * The source of randomness used to generate IVs.
     */
    protected SecureRandom rnd_;

    /**
     * The key size.
     */
    protected int size_;

    /**
     * Initializes this parameter generator for a certain size
     * and source of randomness. This method does nothing.
     *
     * @param size the size (number of bits).
     * @param random the source of randomness.
     */
    protected void engineInit(int size, SecureRandom random) {
        if (random == null) throw new NullPointerException("SecureRandom is null!");
        rnd_ = random;
        if (size > 1024) size = 1024;
        if (size < 1) size = 1;
        size_ = size;
    }

    /**
     * Initializes this parameter generator with a set of
     * algorithm-specific parameter generation values.
     *
     * @param spec The set of algorithm-specific parameter
     *   generation values.
     * @param random the source of randomness.
     *
     * @exception InvalidAlgorithmParameterException if the
     *   given parameter generation values are inappropriate
     *   for this parameter generator.
     */
    protected void engineInit(AlgorithmParameterSpec spec, SecureRandom random) throws InvalidAlgorithmParameterException {
        if (!(spec instanceof RC2ParameterSpec)) throw new InvalidAlgorithmParameterException("Need a RC2ParameterSpec!");
        engineInit(((RC2ParameterSpec) spec).getEffectiveKeyBits(), random);
    }

    /**
     * Generates the parameters.
     *
     * @return the new AlgorithmParameters object.
     */
    protected AlgorithmParameters engineGenerateParameters() {
        AlgorithmParameters params;
        byte[] iv;
        iv = new byte[8];
        if (rnd_ == null) rnd_ = new SecureRandom();
        rnd_.nextBytes(iv);
        try {
            params = AlgorithmParameters.getInstance("RC2", A8Provider.name);
            params.init(new RC2ParameterSpec(size_, iv));
            return params;
        } catch (Exception e) {
            throw new ProviderException("The '" + A8Provider.name + "' is not installed!");
        }
    }
}
