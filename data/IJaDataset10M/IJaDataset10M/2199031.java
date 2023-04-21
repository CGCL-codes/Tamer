package com.webcamtracker.image.color.rgb;

import com.webcamtracker.image.Color;
import java.awt.image.BufferedImage;

/**
 * Created by IntelliJ IDEA.
 * User: Pachax
 * Date: 23/03/2006
 * Time: 20:19:50
 * To change this template use File | Settings | File Templates.
 */
public interface ColorSampler {

    public Color getColorAt(BufferedImage image, int x, int y);

    public Color getColorFrom(RGBColor color);
}
