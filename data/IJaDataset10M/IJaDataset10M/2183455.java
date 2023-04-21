package cdc.standard.pbe;

import java.security.spec.*;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;

/**
 * This class represents a factory for secret keys.
 *
 * This class is used to convert PBE keys into a format usable by the
 * CDC provider. Its intended use is for pbe according to PKCS #5.
 * Currently this class can only convert from a KeySpec
 * into a Key.
 * The supported KeySpec class is PBEKeySpec.
 * <p>
 * This class should not be instantiated directly, instead use the
 * java.security.KeyFactory interface.
 *
 *
 *
 *
 * @author  Michele Boivin
 * @version 0.1 Built and tested with SUN�s jdk1.2.2
 */
public class PBEKeyFactory extends SecretKeyFactorySpi {

    /**
	 * Generates a PBE SecretKey object from the provided key
	 * specification (key material).
	 * @param keySpec the specification (key material) of the secret key
	 * @return The secret key.
	 * @exception InvalidKeySpecException   if the given key specification
	 *                                      is inappropriate for this secret-key
	 *                                      factory to produce a secret key.
	 */
    protected SecretKey engineGenerateSecret(KeySpec keySpec) throws InvalidKeySpecException {
        if (keySpec instanceof PBEKeySpec) {
            return new PBEKey(((PBEKeySpec) keySpec).getPassword());
        } else {
            throw new InvalidKeySpecException("Invalid KeySpec");
        }
    }

    /**
	 * Returns a specification (key material) of the given key object in
	 * the requested format.
	 *
	 * @param key      the key
	 * @param keySpec  the requested format in which the key material shall
	 *		              be returned
	 * @return  the underlying key specification (key material) in the
	 *		        requested format
	 * @exception InvalidKeySpecException - if the requested key
	 *	specification is inappropriate for the given key, or the given
	 *	key cannot be dealt with (e.g., the given key has an
	 *	unrecognised format).
	 */
    protected KeySpec engineGetKeySpec(SecretKey key, Class keySpec) throws InvalidKeySpecException {
        KeySpec key_Spec;
        if ((keySpec == null) || keySpec != PBEKeySpec.class) throw new InvalidKeySpecException("Invalid keySpec");
        if (key == null) throw new InvalidKeySpecException("key is null");
        key_Spec = new PBEKeySpec(((PBEKey) key).getKey());
        return key_Spec;
    }

    /**
	 * Translates a PBE key object, whose provider may be unknown or
	 * potentially untrusted, into a corresponding key object of this key
	 * factory.
	 *
	 * Not currently implemented.
	 *
	 * @param key  the key whose provider is unknown or untrusted
	 * @return the translated key
	 * @exception InvalidKeyException if the given key cannot be processed
	 * 	by this key factory.
	 */
    protected SecretKey engineTranslateKey(SecretKey key) throws InvalidKeyException {
        throw new InvalidKeyException("not implemented");
    }
}
