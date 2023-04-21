package net.engine.picture;

import net.engine.file.xml.XMLBody;
import java.awt.*;

public class ColorUtil {

    public static Color fromXML(XMLBody body) {
        int red = Integer.parseInt((String) body.getData("Red"));
        int green = Integer.parseInt((String) body.getData("Green"));
        int blue = Integer.parseInt((String) body.getData("Blue"));
        String s = (String) body.getData("Alpha");
        int alpha = 255;
        if (s != null) {
            alpha = Integer.parseInt(s);
        }
        return new Color(red, green, blue, alpha);
    }

    public static void toXML(XMLBody body, Color colour) {
        body.put("Red", ((Integer) colour.getRed()).toString());
        body.put("Green", ((Integer) colour.getGreen()).toString());
        body.put("Blue", ((Integer) colour.getBlue()).toString());
        body.put("Alpha", ((Integer) colour.getAlpha()).toString());
    }

    public static Color copy(Color colour) {
        if (colour == null) {
            return null;
        }
        return new Color(colour.getRed(), colour.getGreen(), colour.getBlue(), colour.getAlpha());
    }
}
