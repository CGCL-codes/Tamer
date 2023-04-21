package net.sourceforge.tile3d.model;

import java.awt.Color;

public class KindColor {

    double green;

    double blue;

    double red;

    public KindColor(double g, double b, double r) {
        super();
        green = g;
        blue = b;
        red = r;
    }

    public KindColor(Color color) {
        super();
        green = color.getGreen();
        blue = color.getBlue();
        red = color.getRed();
    }

    public KindColor() {
        super();
        green = 1.0;
        blue = 1.0;
        red = 1.0;
    }

    public double getBlue() {
        return blue;
    }

    public void setBlue(double p_blue) {
        blue = p_blue;
    }

    public double getGreen() {
        return green;
    }

    public void setGreen(double p_green) {
        green = p_green;
    }

    public double getRed() {
        return red;
    }

    public void setRed(double p_red) {
        red = p_red;
    }

    /**
         * toString methode: creates a String representation of the object
         * @return the String representation
         * @author info.vancauwenberge.tostring plugin

         */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("KindColor[");
        buffer.append("green = ").append(green);
        buffer.append(", blue = ").append(blue);
        buffer.append(", red = ").append(red);
        buffer.append("]");
        return buffer.toString();
    }
}
