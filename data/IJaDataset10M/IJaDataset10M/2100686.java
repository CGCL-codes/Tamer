package net.galluzzo.wave.orthopermubot.permutation;

/**
 * An individual type of permutation that can be applied to a string to generate
 * a new string.
 * 
 * @author Eric Galluzzo
 */
public interface Permutation {

    /**
	 * Permutes the given string.
	 * 
	 * @param str  The string to permute
	 * 
	 * @return  The permutation of the string
	 */
    String permute(String str);
}
