package color;

import java.awt.Color;

/**
 * A color wheel represent the 11 hue variation of a main color plus 2 lightness
 * variation for the main color and it's complement.
 * 
 * 
 * @author Desprez Jean-Marc
 * 
 */
public class ColorWheel {

    private static final double HALF_MONOCHROME_GAP = 0.10;

    private static final double MONOCHROME_GAP = 0.20;

    private static final int WHEEL_SIZE = 16;

    private HSLColor[] colors;

    private ColorWheel() {
        colors = new HSLColor[WHEEL_SIZE];
        for (int i = 0; i < WHEEL_SIZE; i++) {
            colors[i] = new HSLColor(0, 0, 0);
        }
    }

    /**
   * Create a new wheel with the given HSLColor.
   * 
   * @param mainColor
   *          the main color of the wheel.
   * 
   * @see HSLColor
   */
    public ColorWheel(final HSLColor mainColor) {
        this();
        setMainColor(mainColor.getRed(), mainColor.getGreen(), mainColor.getBlue());
    }

    /**
   * Create a new wheel with a RGB code. If a value is out of the range, the
   * value will be replaced by min or max.
   * 
   * @param red
   *          the red color [0..255]
   * @param green
   *          the green color [0..255]
   * @param blue
   *          the blue color [0..255]
   */
    public ColorWheel(final int red, final int green, final int blue) {
        this();
        setMainColor(red, green, blue);
    }

    private void updateWheel() {
        double hue, sat, bright;
        hue = colors[0].getHue();
        sat = colors[0].getSaturation();
        bright = colors[0].getLightness();
        for (int i = 1; i < WHEEL_SIZE - 4; i++) {
            colors[i].setHue(hue + i / 12.0);
            colors[i].setSaturation(sat);
            colors[i].setLightness(bright);
        }
        colors[12].setHue(hue);
        colors[12].setSaturation(sat);
        colors[13].setHue(hue);
        colors[13].setSaturation(sat);
        if (bright <= 0.70) {
            colors[12].setLightness(bright + HALF_MONOCHROME_GAP);
            colors[13].setLightness(bright + MONOCHROME_GAP);
        } else {
            colors[12].setLightness(bright - HALF_MONOCHROME_GAP);
            colors[13].setLightness(bright - MONOCHROME_GAP);
        }
        colors[14].setHue(colors[6].getHue());
        colors[14].setSaturation(sat);
        colors[15].setHue(colors[6].getHue());
        colors[15].setSaturation(sat);
        if (bright <= 0.70) {
            colors[14].setLightness(bright + HALF_MONOCHROME_GAP);
            colors[15].setLightness(bright + MONOCHROME_GAP);
        } else {
            colors[14].setLightness(bright - HALF_MONOCHROME_GAP);
            colors[15].setLightness(bright - MONOCHROME_GAP);
        }
    }

    /**
   * @return an array containing the main color and the 2 closest hue variation.
   */
    public HSLColor[] getAnalogous() {
        return new HSLColor[] { colors[0], colors[1], colors[11] };
    }

    /**
   * @return an array containing the main color and it's complement.
   */
    public HSLColor[] getComplement() {
        return new HSLColor[] { colors[0], colors[6] };
    }

    /**
   * @return the main color of the wheel.
   */
    public HSLColor getMainColor() {
        return colors[0];
    }

    /**
   * @return an array containing the main color and 2 ligthness variation.
   */
    public HSLColor[] getMonochrome() {
        return new HSLColor[] { colors[0], colors[12], colors[13] };
    }

    /**
   * @return an array containing the main color and it's split complement.
   */
    public HSLColor[] getSplitComplement() {
        return new HSLColor[] { colors[0], colors[5], colors[7] };
    }

    /**
   * @return an array containing the main color and the tetrad.
   */
    public HSLColor[] getTetrad() {
        return new HSLColor[] { colors[0], colors[3], colors[6], colors[9] };
    }

    /**
   * @return an array containing the main color and the triad.
   */
    public HSLColor[] getTriad() {
        return new HSLColor[] { colors[0], colors[4], colors[8] };
    }

    /**
   * @return the entire wheel.
   */
    public HSLColor[] getWheel() {
        return colors;
    }

    /**
   * Set the main color of the wheel.
   * 
   * @param color
   *          the main color of the wheel.
   */
    public void setMainColor(final Color color) {
        setMainColor(color.getRed(), color.getGreen(), color.getBlue());
    }

    /**
   * Set the main color with a color on the wheel.
   * 
   * @param squareNumber
   *          the position of the color.
   */
    public void setMainColor(final int squareNumber) {
        if (squareNumber > 0 && squareNumber < WHEEL_SIZE) {
            setMainColor(colors[squareNumber].getRed(), colors[squareNumber].getGreen(), colors[squareNumber].getBlue());
        }
    }

    /**
   * Set the main color with a RGB code. If a value is out of the range, the
   * value will be replaced by min or max.
   * 
   * @param red
   *          the red color [0..255]
   * @param green
   *          the green color [0..255]
   * @param blue
   *          the blue color [0..255]
   */
    public void setMainColor(final int red, final int green, final int blue) {
        colors[0].setRed(red);
        colors[0].setGreen(green);
        colors[0].setBlue(blue);
        updateWheel();
    }
}
