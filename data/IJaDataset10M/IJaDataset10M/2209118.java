package net.sf.orexio.jdcp.security;

import java.net.UnknownHostException;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;
import net.sf.orexio.jdcp.common.ItemIdentifier;
import net.sf.orexio.jdcp.common.ItemsFileStore;

/**
 * Handle key pair store and key pair generation (SecurityKeyPair DAO).
 * @author alois.cochard@gmail.com
 *
 */
public class SecurityKeyPairProvider {

    /**
	 * The security service.
	 */
    private SecurityService service = null;

    /**
	 * Keys file store, read and write keys to file system.
	 */
    private ItemsFileStore store = null;

    /**
	 * Default package restricted constructor.
	 * @param service the security service
	 * @param store the item file store used to provide security key pair
	 */
    SecurityKeyPairProvider(final SecurityService service, final ItemsFileStore store) {
        this.service = service;
        this.store = store;
    }

    /**
	 * Generate a new security key pair of defined type.
	 * @param type the security key pair type,
	 * can be {@link SecurityKeyPair#TYPE_PUBLIC_READING}
	 * or {@link SecurityKeyPair#TYPE_PUBLIC_WRITING}.
	 * @param strength the security key pair strength,
	 * can be {@link SecurityKeyPair#STRENGTH_LOW}
	 * or {@link SecurityKeyPair#STRENGTH_HIGH}. 
	 * @return the new generated key pair
	 * @throws UnknownHostException
	 * if unable to create a new key pair
	 * @throws SecurityServiceException
	 * if key pair type or strength is invalid
	 */
    public final synchronized SecurityKeyPair create(final int type, final int strength) throws UnknownHostException, SecurityServiceException {
        SecurityKeyPair key = null;
        service.checkKeyPairType(type);
        service.checkKeyPairStrength(strength);
        AsymmetricCipherKeyPair keyPair = service.generateKeyPair(strength);
        RSAPrivateCrtKeyParameters keyPrivate = (RSAPrivateCrtKeyParameters) keyPair.getPrivate();
        RSAKeyParameters keyPublic = (RSAKeyParameters) keyPair.getPublic();
        key = new SecurityKeyPair(type, strength, keyPrivate, keyPublic);
        return key;
    }

    /**
	 * Store a key pair to the store.
	 * @param keyPair the key pair
	 */
    public final void store(final SecurityKeyPair keyPair) {
        SecurityKeyPair storeKeyPair = getKeyPairByIdentifier(keyPair.getIdentifier());
        if (storeKeyPair != null) {
            if (keyPair.getKeyPrivate() == null && storeKeyPair.getKeyPrivate() != null) {
                return;
            }
        }
        store.set(keyPair);
    }

    /**
	 * Remove a key pair from the store.
	 * @param keyPair the key pair
	 */
    public final void remove(final SecurityKeyPair keyPair) {
        store.remove(keyPair.getIdentifier());
    }

    /**
	 * Retrieve a key from the keys store.
	 * @param identifier the identifier of the requested key
	 * @return the requested key if found, <b>null</b> otherwise
	 */
    public final SecurityKeyPair getKeyPairByIdentifier(final ItemIdentifier identifier) {
        return (SecurityKeyPair) store.get(identifier);
    }
}
