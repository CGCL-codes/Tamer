package gnu.javax.crypto.cipher;

import gnu.java.security.Configuration;
import java.security.InvalidKeyException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A basic abstract class to facilitate implementing symmetric key block
 * ciphers.
 */
public abstract class BaseCipher implements IBlockCipher, IBlockCipherSpi {

    private static final Logger log = Logger.getLogger(BaseCipher.class.getName());

    /** The canonical name prefix of the cipher. */
    protected String name;

    /** The default block size, in bytes. */
    protected int defaultBlockSize;

    /** The default key size, in bytes. */
    protected int defaultKeySize;

    /** The current block size, in bytes. */
    protected int currentBlockSize;

    /** The session key for this instance. */
    protected transient Object currentKey;

    /** The instance lock. */
    protected Object lock = new Object();

    /**
   * Trivial constructor for use by concrete subclasses.
   * 
   * @param name the canonical name prefix of this instance.
   * @param defaultBlockSize the default block size in bytes.
   * @param defaultKeySize the default key size in bytes.
   */
    protected BaseCipher(String name, int defaultBlockSize, int defaultKeySize) {
        super();
        this.name = name;
        this.defaultBlockSize = defaultBlockSize;
        this.defaultKeySize = defaultKeySize;
    }

    public abstract Object clone();

    public String name() {
        StringBuffer sb = new StringBuffer(name).append('-');
        if (currentKey == null) sb.append(String.valueOf(8 * defaultBlockSize)); else sb.append(String.valueOf(8 * currentBlockSize));
        return sb.toString();
    }

    public int defaultBlockSize() {
        return defaultBlockSize;
    }

    public int defaultKeySize() {
        return defaultKeySize;
    }

    public void init(Map attributes) throws InvalidKeyException {
        synchronized (lock) {
            if (currentKey != null) throw new IllegalStateException();
            Integer bs = (Integer) attributes.get(CIPHER_BLOCK_SIZE);
            if (bs == null) {
                if (currentBlockSize == 0) currentBlockSize = defaultBlockSize;
            } else {
                currentBlockSize = bs.intValue();
                Iterator it;
                boolean ok = false;
                for (it = blockSizes(); it.hasNext(); ) {
                    ok = (currentBlockSize == ((Integer) it.next()).intValue());
                    if (ok) break;
                }
                if (!ok) throw new IllegalArgumentException(IBlockCipher.CIPHER_BLOCK_SIZE);
            }
            byte[] k = (byte[]) attributes.get(KEY_MATERIAL);
            currentKey = makeKey(k, currentBlockSize);
        }
    }

    public int currentBlockSize() {
        if (currentKey == null) throw new IllegalStateException();
        return currentBlockSize;
    }

    public void reset() {
        synchronized (lock) {
            currentKey = null;
        }
    }

    public void encryptBlock(byte[] in, int inOffset, byte[] out, int outOffset) throws IllegalStateException {
        synchronized (lock) {
            if (currentKey == null) throw new IllegalStateException();
            encrypt(in, inOffset, out, outOffset, currentKey, currentBlockSize);
        }
    }

    public void decryptBlock(byte[] in, int inOffset, byte[] out, int outOffset) throws IllegalStateException {
        synchronized (lock) {
            if (currentKey == null) throw new IllegalStateException();
            decrypt(in, inOffset, out, outOffset, currentKey, currentBlockSize);
        }
    }

    public boolean selfTest() {
        int ks;
        Iterator bit;
        for (Iterator kit = keySizes(); kit.hasNext(); ) {
            ks = ((Integer) kit.next()).intValue();
            for (bit = blockSizes(); bit.hasNext(); ) if (!testSymmetry(ks, ((Integer) bit.next()).intValue())) return false;
        }
        return true;
    }

    private boolean testSymmetry(int ks, int bs) {
        try {
            byte[] kb = new byte[ks];
            byte[] pt = new byte[bs];
            byte[] ct = new byte[bs];
            byte[] cpt = new byte[bs];
            int i;
            for (i = 0; i < ks; i++) kb[i] = (byte) i;
            for (i = 0; i < bs; i++) pt[i] = (byte) i;
            Object k = makeKey(kb, bs);
            encrypt(pt, 0, ct, 0, k, bs);
            decrypt(ct, 0, cpt, 0, k, bs);
            return Arrays.equals(pt, cpt);
        } catch (Exception x) {
            if (Configuration.DEBUG) log.log(Level.FINE, "Exception in testSymmetry() for " + name(), x);
            return false;
        }
    }

    protected boolean testKat(byte[] kb, byte[] ct) {
        return testKat(kb, ct, new byte[ct.length]);
    }

    protected boolean testKat(byte[] kb, byte[] ct, byte[] pt) {
        try {
            int bs = pt.length;
            byte[] t = new byte[bs];
            Object k = makeKey(kb, bs);
            encrypt(pt, 0, t, 0, k, bs);
            if (!Arrays.equals(t, ct)) return false;
            decrypt(t, 0, t, 0, k, bs);
            return Arrays.equals(t, pt);
        } catch (Exception x) {
            if (Configuration.DEBUG) log.log(Level.FINE, "Exception in testKat() for " + name(), x);
            return false;
        }
    }
}
