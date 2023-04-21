package net.sf.vat4net.gui.animationpanel;

import java.awt.Color;
import java.util.HashMap;

public class TraceColor {

    private HashMap color = new HashMap();

    public TraceColor() {
        color.put("yellow", Color.YELLOW);
        color.put("green", Color.GREEN);
        color.put("orange", Color.ORANGE);
        color.put("chocolate", new Color(159, 61, 47));
        color.put("magenta", Color.MAGENTA);
        color.put("gold", new Color(255, 216, 98));
        color.put("red", Color.RED);
        color.put("purple", Color.PINK);
        color.put("cyan", Color.CYAN);
        color.put("black", Color.BLACK);
        color.put("grey", Color.GRAY);
    }

    public HashMap getColorTable() {
        return color;
    }
}
