package grammarscope.browser.utils;

import java.awt.Color;
import java.util.Random;

/**
 * Random color
 * 
 * @author Bernard Bou
 */
public class RandomColor extends Color {

    private static final long serialVersionUID = 1L;

    /**
	 * Randomizer
	 */
    private static Random theRandomizer = new Random();

    /**
	 * Variation range
	 */
    private static final int theRange = 32;

    /**
	 * Minimum delta
	 */
    private static final int theMinDelta = 16;

    /**
	 * Clue
	 */
    private enum Rgb {

        R, G, B
    }

    /**
	 * Random color
	 */
    public RandomColor() {
        super(RandomColor.getValues());
    }

    /**
	 * Random values (for colors)
	 * 
	 * @return values combined into one integer
	 */
    private static int getValues() {
        final double FACTOR = 1.2;
        int r = RandomColor.getValue();
        int g = RandomColor.getValue();
        int b = RandomColor.getValue();
        while (r + g + b < 4 * 128) {
            r = Math.min((int) (r * FACTOR), 255);
            g = Math.min((int) (g * FACTOR), 255);
            b = Math.min((int) (b * FACTOR), 255);
        }
        final int thisResult = r << 16 & 0xFF0000 | g << 8 & 0xFF00 | b & 0xFF;
        return thisResult;
    }

    /**
	 * Get random value
	 * 
	 * @return random value
	 */
    private static int getValue() {
        return RandomColor.theRandomizer.nextInt(256);
    }

    /**
	 * Random color near reference color
	 * 
	 * @param thisColor
	 *            reference color
	 */
    public RandomColor(final Color thisColor) {
        super(RandomColor.getValue(thisColor, Rgb.R), RandomColor.getValue(thisColor, Rgb.G), RandomColor.getValue(thisColor, Rgb.B));
    }

    /**
	 * Get random value near reference color
	 * 
	 * @param thisColor
	 *            reference color
	 * @param rgb
	 *            clue
	 * @return color value
	 */
    private static int getValue(final Color thisColor, final Rgb rgb) {
        if (thisColor == null) return RandomColor.getValue();
        switch(rgb) {
            case R:
                return RandomColor.getValue(thisColor.getRed());
            case G:
                return RandomColor.getValue(thisColor.getGreen());
            case B:
                return RandomColor.getValue(thisColor.getBlue());
        }
        return 0;
    }

    /**
	 * Get value near reference value
	 * 
	 * @param thatValue
	 *            reference value
	 * @return color value
	 */
    private static int getValue(final int thatValue) {
        int thisValue = thatValue;
        int thisDelta = RandomColor.theRandomizer.nextInt(RandomColor.theRange * 2);
        thisDelta -= RandomColor.theRange;
        if (thisDelta > 0) {
            thisDelta += RandomColor.theMinDelta;
        } else {
            thisDelta -= RandomColor.theMinDelta;
        }
        thisValue += thisDelta;
        return thisValue > 255 ? 255 : thisValue < 0 ? 0 : thisValue;
    }
}
