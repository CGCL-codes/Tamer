package raiser.util;

import javax.swing.Icon;

/**
 *Util class.
 */
public class Randomizer {

    public static String generateWord(int minSize, int maxSize) {
        return "randomWord" + Math.random();
    }

    public static Icon generateIcon(int width, int height, int shapeCount, int colorCount) {
        return new RandomizeIcon(width, height, shapeCount, colorCount);
    }
}
