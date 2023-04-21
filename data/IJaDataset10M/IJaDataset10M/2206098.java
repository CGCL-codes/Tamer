package net.sf.planofattack.io.xstream;

import java.awt.Color;
import net.sf.planofattack.util.ColorUtil;
import com.thoughtworks.xstream.converters.SingleValueConverter;

public class ColorConverter implements SingleValueConverter {

    public Object fromString(String hex) {
        return ColorUtil.convertFromHex(hex);
    }

    public String toString(Object obj) {
        Color color = (Color) obj;
        return ColorUtil.convertToHex(color);
    }

    @SuppressWarnings("unchecked")
    public boolean canConvert(Class clazz) {
        return Color.class.equals(clazz);
    }
}
