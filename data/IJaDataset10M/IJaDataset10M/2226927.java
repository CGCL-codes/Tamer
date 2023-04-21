package eg.nileu.cis.nilestore.uri;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bitpedia.util.Base32;
import eg.nileu.cis.nilestore.interfaces.uri.IMutableFileURI;
import eg.nileu.cis.nilestore.interfaces.uri.IVerifierURI;
import eg.nileu.cis.nilestore.utils.hashutils.Hash;

/**
 * The Class ReadonlySSKFileURI.
 * 
 * @author Mahmoud Ismail <mahmoudahmedismail@gmail.com>
 */
public class ReadonlySSKFileURI implements IMutableFileURI {

    /** The Constant BASE_STRING. */
    public static final String BASE_STRING = "URI:SSK-RO:";

    /** The pattern. */
    private final Pattern pattern = Pattern.compile(BASE_STRING + uri.BASE32_CHARS + ":" + uri.BASE32_CHARS);

    /** The readkey. */
    private final byte[] readkey;

    /** The fingerprint. */
    private final byte[] fingerprint;

    /** The storage index. */
    private final byte[] storageIndex;

    /**
	 * Instantiates a new readonly ssk file uri.
	 * 
	 * @param readkey
	 *            the readkey
	 * @param fingerprint
	 *            the fingerprint
	 */
    public ReadonlySSKFileURI(byte[] readkey, byte[] fingerprint) {
        this.readkey = readkey;
        this.storageIndex = Hash.ssk_storage_index_hash(readkey);
        this.fingerprint = fingerprint;
    }

    /**
	 * Instantiates a new readonly ssk file uri.
	 * 
	 * @param cap
	 *            the cap
	 */
    public ReadonlySSKFileURI(String cap) {
        Matcher matcher = pattern.matcher(cap);
        if (matcher.matches()) {
            this.readkey = Base32.decode(matcher.group(1));
            this.storageIndex = Hash.ssk_storage_index_hash(readkey);
            this.fingerprint = Base32.decode(matcher.group(2));
        } else {
            readkey = null;
            storageIndex = null;
            fingerprint = null;
        }
    }

    /**
	 * Gets the readkey.
	 * 
	 * @return the readkey
	 */
    public byte[] getReadkey() {
        return readkey;
    }

    /**
	 * Gets the fingerprint.
	 * 
	 * @return the fingerprint
	 */
    public byte[] getFingerprint() {
        return fingerprint;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public boolean isReadonly() {
        return true;
    }

    @Override
    public IMutableFileURI getReadonlyCap() {
        return this;
    }

    @Override
    public IVerifierURI getVerifyCap() {
        return null;
    }

    @Override
    public byte[] getStorageIndex() {
        return storageIndex;
    }

    @Override
    public String toString() {
        return String.format("%s%s:%s", BASE_STRING, Base32.encode(readkey), Base32.encode(fingerprint));
    }
}
