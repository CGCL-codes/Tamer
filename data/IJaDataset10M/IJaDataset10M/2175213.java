package org.chaoticengine.cgll.serialization;

import org.newdawn.slick.Color;
import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

/**
 * Simple XML converter for the Slick2D class Color.
 *
 * @author Matt v.d. Westhuizen
 */
public class ColorConverter implements Converter<Color> {

    public Color read(InputNode in) throws Exception {
        Color result = Color.white;
        result = Color.decode(in.getValue());
        return (result);
    }

    public void write(OutputNode on, Color c) throws Exception {
        Long result = 0L;
        result = result | (c.getRed() << 24);
        result = result | (c.getGreen() << 16);
        result = result | (c.getBlue() << 8);
        result = result | (c.getAlpha());
        on.setValue("#" + Long.toHexString(result));
    }
}
