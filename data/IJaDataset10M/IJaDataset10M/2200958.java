package gnu.javax.crypto.prng;

import gnu.java.security.Registry;
import gnu.java.security.prng.BasePRNG;
import gnu.java.security.prng.LimitReachedException;
import gnu.javax.crypto.cipher.CipherFactory;
import gnu.javax.crypto.cipher.IBlockCipher;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.security.InvalidKeyException;

/**
 * <i>KDF</i>s (Key Derivation Functions) are used to stretch user-supplied key
 * material to specific size(s) required by high level cryptographic primitives.
 * Described in the <A
 * HREF="http://www.ietf.org/internet-drafts/draft-krovetz-umac-01.txt">UMAC</A>
 * paper, this function basically operates an underlying <em>symmetric key block
 * cipher</em> instance in output feedback mode (OFB), as a <b>strong</b>
 * pseudo-random number generator.
 * <p>
 * <code>UMacGenerator</code> requires an <em>index</em> parameter
 * (initialisation parameter <code>gnu.crypto.prng.umac.kdf.index</code> taken
 * to be an instance of {@link Integer} with a value between <code>0</code> and
 * <code>255</code>). Using the same key, but different indices, generates
 * different pseudorandom outputs.
 * <p>
 * This implementation generalises the definition of the
 * <code>UmacGenerator</code> algorithm to allow for other than the AES
 * symetric key block cipher algorithm (initialisation parameter
 * <code>gnu.crypto.prng.umac.cipher.name</code> taken to be an instance of
 * {@link String}). If such a parameter is not defined/included in the
 * initialisation <code>Map</code>, then the "Rijndael" algorithm is used.
 * Furthermore, if the initialisation parameter
 * <code>gnu.crypto.cipher.block.size</code> (taken to be a instance of
 * {@link Integer}) is missing or undefined in the initialisation
 * <code>Map</code>, then the cipher's <em>default</em> block size is used.
 * <p>
 * <b>NOTE</b>: Rijndael is used as the default symmetric key block cipher
 * algorithm because, with its default block and key sizes, it is the AES. Yet
 * being Rijndael, the algorithm offers more versatile block and key sizes which
 * may prove to be useful for generating "longer" key streams.
 * <p>
 * References:
 * <ol>
 * <li><a href="http://www.ietf.org/internet-drafts/draft-krovetz-umac-01.txt">
 * UMAC</a>: Message Authentication Code using Universal Hashing.<br>
 * T. Krovetz, J. Black, S. Halevi, A. Hevia, H. Krawczyk, and P. Rogaway.</li>
 * </ol>
 */
public class UMacGenerator extends BasePRNG implements Cloneable {

    /**
   * Property name of the KDF <code>index</code> value to use in this
   * instance. The value is taken to be an {@link Integer} less than
   * <code>256</code>.
   */
    public static final String INDEX = "gnu.crypto.prng.umac.index";

    /** The name of the underlying symmetric key block cipher algorithm. */
    public static final String CIPHER = "gnu.crypto.prng.umac.cipher.name";

    /** The generator's underlying block cipher. */
    private IBlockCipher cipher;

    /** Trivial 0-arguments constructor. */
    public UMacGenerator() {
        super(Registry.UMAC_PRNG);
    }

    public void setup(Map attributes) {
        boolean newCipher = true;
        String cipherName = (String) attributes.get(CIPHER);
        if (cipherName == null) if (cipher == null) cipher = CipherFactory.getInstance(Registry.RIJNDAEL_CIPHER); else newCipher = false; else cipher = CipherFactory.getInstance(cipherName);
        int cipherBlockSize = 0;
        Integer bs = (Integer) attributes.get(IBlockCipher.CIPHER_BLOCK_SIZE);
        if (bs != null) cipherBlockSize = bs.intValue(); else {
            if (newCipher) cipherBlockSize = cipher.defaultBlockSize();
        }
        byte[] key = (byte[]) attributes.get(IBlockCipher.KEY_MATERIAL);
        if (key == null) throw new IllegalArgumentException(IBlockCipher.KEY_MATERIAL);
        int keyLength = key.length;
        boolean ok = false;
        for (Iterator it = cipher.keySizes(); it.hasNext(); ) {
            ok = (keyLength == ((Integer) it.next()).intValue());
            if (ok) break;
        }
        if (!ok) throw new IllegalArgumentException("key length");
        int index = -1;
        Integer i = (Integer) attributes.get(INDEX);
        if (i != null) {
            index = i.intValue();
            if (index < 0 || index > 255) throw new IllegalArgumentException(INDEX);
        }
        Map map = new HashMap();
        if (cipherBlockSize != 0) map.put(IBlockCipher.CIPHER_BLOCK_SIZE, Integer.valueOf(cipherBlockSize));
        map.put(IBlockCipher.KEY_MATERIAL, key);
        try {
            cipher.init(map);
        } catch (InvalidKeyException x) {
            throw new IllegalArgumentException(IBlockCipher.KEY_MATERIAL);
        }
        buffer = new byte[cipher.currentBlockSize()];
        buffer[cipher.currentBlockSize() - 1] = (byte) index;
        try {
            fillBlock();
        } catch (LimitReachedException impossible) {
        }
    }

    public void fillBlock() throws LimitReachedException {
        cipher.encryptBlock(buffer, 0, buffer, 0);
    }
}
