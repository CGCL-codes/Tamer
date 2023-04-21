package jasci.ui.text;

import jasci.util.Color;

public class ColorAttribute implements Attribute {

    private Color c;

    public ColorAttribute(Color c) {
        this.c = c;
    }

    public Color getColor() {
        return c;
    }

    public String toString() {
        return c.toString();
    }
}
