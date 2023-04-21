package net.sf.passwordpurse.crypto;

public abstract class AbstractSaltManager implements SaltManager {

    /**
 * for real life! Mix a seed into the random number generator. For fake
 * randoms, simply ignore the new seed data and preserve the original.
 */
    public void addSeedMaterial(long seed) {
        byte[] seedBytes = new byte[4];
        seedBytes[3] = (byte) (seed & 0x000000ff);
        seedBytes[2] = (byte) ((seed & 0x0000ff00) >> 8);
        seedBytes[1] = (byte) ((seed & 0x00ff0000) >> 16);
        seedBytes[0] = (byte) ((seed & 0xff000000) >> 24);
        addSeedMaterial(seedBytes);
    }

    /**
 * clone means different things to the salt generators
 */
    public abstract Object clone();
}
