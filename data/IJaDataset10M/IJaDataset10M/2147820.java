package gui.editors.color;

import java.awt.Color;

public class ColorRepr {

    Color color;

    String name;

    public ColorRepr(Color color, String name) {
        super();
        this.color = color;
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public String getName() {
        return name;
    }
}
