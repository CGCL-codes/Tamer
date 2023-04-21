package darwevo.algo.array2d;

public class Fill {

    /**
	 * This fills a rectangle in the limits of left <= x < right and top <= y < bottom.
	 * @param value The value with which to fill the specified array.
	 * @param array Floating point array indexed by y * scanline + x
	 * @scan Used for array indexing.
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 */
    public static void fillSquare(final float value, final float[] array, final int scan, final int left, final int top, final int right, final int bottom) {
        for (int y = top; y < bottom; y++) {
            for (int x = left; x < right; x++) {
                array[y * scan + x] = value;
            }
        }
    }
}
