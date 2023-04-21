package gnu.javax.crypto.jce.prng;

import gnu.java.security.prng.LimitReachedException;
import gnu.javax.crypto.prng.Fortuna;
import java.security.SecureRandomSpi;
import java.util.Collections;

public final class FortunaImpl extends SecureRandomSpi {

    private final Fortuna adaptee;

    public FortunaImpl() {
        adaptee = new Fortuna();
        adaptee.init(Collections.singletonMap(Fortuna.SEED, new byte[0]));
    }

    protected void engineSetSeed(byte[] seed) {
        synchronized (adaptee) {
            adaptee.addRandomBytes(seed);
        }
    }

    protected void engineNextBytes(byte[] buffer) {
        synchronized (adaptee) {
            try {
                adaptee.nextBytes(buffer);
            } catch (LimitReachedException shouldNotHappen) {
                throw new Error(shouldNotHappen);
            }
        }
    }

    protected byte[] engineGenerateSeed(int numbytes) {
        byte[] seed = new byte[numbytes];
        engineNextBytes(seed);
        return seed;
    }
}
