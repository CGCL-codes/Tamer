package edu.vt.middleware.crypt.pbe;

import edu.vt.middleware.crypt.pkcs.PBKDF2Parameters;
import edu.vt.middleware.crypt.symmetric.SymmetricAlgorithm;

/**
 * Implements the PBES2 encryption scheme defined in PKCS#5v2.
 *
 * @author  Middleware Services
 * @version  $Revision: 1818 $
 */
public class PBES2EncryptionScheme extends AbstractVariableKeySizeEncryptionScheme {

    /**
   * Creates a new instance with the given parameters.
   *
   * @param  alg  Symmetric cipher algorithm used for encryption/decryption. The
   * cipher is expected to be initialized with whatever initialization data is
   * required for encryption/decryption, e.g. initialization vector.
   * @param  params  Container for required salt, iterations, and key length.
   */
    public PBES2EncryptionScheme(final SymmetricAlgorithm alg, final PBKDF2Parameters params) {
        setCipher(alg);
        setGenerator(new PBKDF2KeyGenerator(params.getSalt(), params.getIterationCount()));
        setKeyLength(params.getLength() * 8);
    }
}
