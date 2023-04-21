package net.galluzzo.wave.orthopermubot.permutation;

import java.util.Random;

/**
 * A permutation that operates at a random index within a string.
 * 
 * @author Eric Galluzzo
 */
public abstract class RandomPermutation implements Permutation {

    protected Random random = new Random();

    public String permute(String str) {
        int index = random.nextInt(str.length());
        return permute(str, index);
    }

    /**
	 * Permutes the string at the given index.
	 * 
	 * @param str    The string
	 * @param index  The index at which to permute the string; must be between
	 *               0 and <code>str.length() - 1</code>
	 */
    protected abstract String permute(String str, int index);
}
