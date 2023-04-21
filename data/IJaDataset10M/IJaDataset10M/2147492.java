package sceneMatch;

import java.awt.*;
import java.lang.Math;

class Natural_Number_Colour extends Object {

    public static Color convert(short natural_number_colour) {
        Color c = new Color(Math.abs(187 - (natural_number_colour - 1) * 113) % 255, Math.abs((natural_number_colour - 1) * (205 - (natural_number_colour - 2)) * 31) % 255, Math.abs((105 - (natural_number_colour - 1)) * 41) % 255);
        return (c);
    }
}
