package vrml.field;

import vrml.*;

public class SFColor extends Field {

    float red, green, blue;

    public SFColor() {
    }

    public SFColor(float red, float green, float blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public void getValue(float colors[]) {
        colors[0] = red;
        colors[1] = green;
        colors[2] = blue;
    }

    public float getRed() {
        return red;
    }

    public float getGreen() {
        return green;
    }

    public float getBlue() {
        return blue;
    }

    public void setValue(float colors[]) {
        red = colors[0];
        green = colors[1];
        blue = colors[2];
    }

    public void setValue(float red, float green, float blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public void setValue(ConstSFColor color) {
        this.red = color.red;
        this.green = color.green;
        this.blue = color.blue;
    }

    public void setValue(SFColor color) {
        this.red = color.red;
        this.green = color.green;
        this.blue = color.blue;
    }

    public String toString() {
        return red + " " + green + " " + blue;
    }
}
