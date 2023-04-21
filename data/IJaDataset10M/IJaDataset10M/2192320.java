package net.sourceforge.jacuda;

/**
 * array functions
 * @author wohlgemuth
 *
 */
public interface Array {

    /**
	 * adds two arrays
	 * @param a
	 * @param b
	 * @return
	 */
    float[] add(float[] a, float b[]);

    /**
	 * substract two arrays
	 * @param a
	 * @param b
	 * @return
	 */
    float[] substract(float[] a, float b[]);

    /**
	 * multiply two arrays
	 * @param a
	 * @param b
	 * @return
	 */
    float[] multiply(float[] a, float b[]);

    /**
	 * divides two arrays
	 * @param a
	 * @param b
	 * @return
	 */
    float[] divide(float[] a, float b[]);
}
