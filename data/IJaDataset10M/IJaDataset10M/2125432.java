package wotlas.libs.graphics2d.filter.color;

import wotlas.libs.graphics2d.filter.ColorType;

/** Represents the "brown" colors.
 *
 * @author Aldiss
 */
public class BrownColor implements ColorType {

    /** Return true if the given color is of our Color Type.
     * @param r red component
     * @param b blue component
     * @param g green component
     * @return true if it's of this color type.
     */
    public boolean isFromThisColorType(short r, short g, short b) {
        if (r > g && r > b && r < 130) return true;
        return false;
    }

    /** Given three level of luminosity we return a color of our color type.
     * @param min min luminosity.
     * @param mid medium luminosity.
     * @param max maximum luminosity.
     * @return a rgb integer with an alpha set to 0.
     */
    public int setToColorType(short min, short mid, short max) {
        return ((short) (max / 1.3f) << 16) | ((max / 2) << 8) | (min / 2);
    }
}
