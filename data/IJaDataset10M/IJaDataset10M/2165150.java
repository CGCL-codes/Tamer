package org.xith3d.utility.image;

import org.openmali.vecmath2.Colorf;

/**
 * Mixes colors.
 * 
 * @author Amos Wenger (aka BlueSky)
 */
public class ColorfMixer {

    /**
     * Mixes two colors.
     * 
     * @param c1 First color
     * @param c2 Second color
     * @param ratio If 0, only the first color, if 1 only the second color
     * 
     * @return The mixed color
     */
    public static Colorf mix(Colorf c1, Colorf c2, float ratio) {
        if (ratio < 0f) {
            ratio = 0f;
        } else if (ratio > 1f) {
            ratio = 1f;
        }
        final float ratio2 = 1f - ratio;
        Colorf mixed = new Colorf();
        mixed.setRed(c1.getRed() * ratio2 + c2.getRed() * ratio);
        mixed.setGreen(c1.getGreen() * ratio2 + c2.getGreen() * ratio);
        mixed.setBlue(c1.getBlue() * ratio2 + c2.getBlue() * ratio);
        if (c1.hasAlpha() || c2.hasAlpha()) mixed.setAlpha(c1.getAlpha() * ratio2 + c2.getAlpha() * ratio);
        return (mixed);
    }
}
