package net.tileeditor;

import java.awt.*;

public class Held {

    public static final int MAP_PAINTED = 1;

    public static final int BRUSH_IMPORTED = 2;

    public static final int LAYERS_CHANGED = 3;

    public static final int MAPS_CHANGED = 4;

    public static final int BRUSH_FIELDS_CHANGED = 5;

    public static final int BRUSH_FIELD_CHANGED = 6;

    public static final int BRUSH_SELECTED = 7;

    public static final int MAP_SELECTED = 8;

    public static final int LAYER_SELECTED = 9;

    public static final int BRUSH_BOUND = 10;

    public static final int PROJECT_LOADED = 11;

    public static final int BRUSH_NAME_CHANGED = 12;

    public static final int UPDATE_VALUES = 1;

    public static final int CENTER_BRUSH = 2;

    public static final Color[] colours = new Color[] { Color.black, Color.green, Color.red, Color.yellow, Color.orange, Color.blue, Color.magenta, Color.cyan, Color.pink, Color.pink, Color.cyan, Color.yellow, Color.blue };

    public Source source;

    public int reason;

    public Held(Source source, int reason) {
        this.source = source;
        this.reason = reason;
    }

    public Color getColourForChange() {
        return colours[reason];
    }

    public int updatability() {
        int result = 0;
        if ((reason == BRUSH_IMPORTED) || (reason == BRUSH_SELECTED)) {
            result = CENTER_BRUSH;
        }
        if ((reason == LAYERS_CHANGED) || (reason == MAPS_CHANGED) || (reason == MAP_SELECTED) || (reason == LAYER_SELECTED)) {
            result |= UPDATE_VALUES;
        }
        return result;
    }
}
