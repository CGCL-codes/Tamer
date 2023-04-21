package javax.net.ssl;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

/**
 * The <i>Service Provider Interface</i> (<b>SPI</b>) for key manager
 * factories.
 *
 * @author Casey Marshall (rsdio@metastatic.org)
 */
public abstract class KeyManagerFactorySpi {

    public KeyManagerFactorySpi() {
        super();
    }

    /**
   * Engine method for retrieving this factory's key managers.
   *
   * @return The key managers.
   */
    protected abstract KeyManager[] engineGetKeyManagers();

    /**
   * Engine method for initializing this factory with some
   * algorithm-specific parameters.
   *
   * @param params The factory parameters.
   * @throws InvalidAlgorithmParameterException If the supplied parameters
   *   are inappropriate for this instance.
   */
    protected abstract void engineInit(ManagerFactoryParameters params) throws InvalidAlgorithmParameterException;

    /**
   * Engine method for initializing this factory with a key store and a
   * password for private keys. Either parameter may be <code>null</code>,
   * in which case some default parameters (possibly derived from system
   * properties) should be used.
   *
   * @param store The key store.
   * @param passwd The private key password.
   * @throws KeyStoreException If the key store cannot be accessed.
   * @throws NoSuchAlgorithmException If some of the data from the key
   *   store cannot be retrieved.
   * @throws UnrecoverableKeyException If a private key cannot be retrieved,
   *   likely from a wrong password.
   */
    protected abstract void engineInit(KeyStore store, char[] passwd) throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException;
}
