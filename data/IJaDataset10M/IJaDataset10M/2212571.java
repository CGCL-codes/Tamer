package org.kubiki.base;

import java.awt.*;
import org.kubiki.xml.*;
import org.kubiki.base.*;
import org.kubiki.gui.*;
import org.kubiki.ide.*;
import org.kubiki.graphic.*;

public class ColorProperty extends Property {

    Color c;

    public ColorProperty(String name, String type, Color c) {
        super(name, type, "");
        this.name = name;
        this.type = type;
        this.c = c;
    }

    public Color getColorValue() {
        return c;
    }

    public void setColorValue(Color c) {
        this.c = c;
        ;
    }
}
