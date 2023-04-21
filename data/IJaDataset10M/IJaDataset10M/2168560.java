package com.lolcode.editors;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import com.lolcode.eclipselol.ui.Activator;

/**
 * This class Manages available colors and makes sure the resources are disposed.
 * 
 *
 */
public class ColorManager {

    /**
	 * Map of all colors in this manager.
	 */
    protected Map fColorTable = new HashMap(10);

    /**
	 * Disposes all colors creates so far.
	 */
    private void disposeCurrentColors() {
        Iterator e = fColorTable.values().iterator();
        while (e.hasNext()) {
            ((Color) e.next()).dispose();
        }
    }

    public Color getBackgroundColor() {
        if (!fColorTable.containsKey("BACKGROUND")) {
            fColorTable.put("BACKGROUND", new Color(Display.getCurrent(), new RGB(150, 150, 0)));
        }
        return (Color) fColorTable.get("BACKGROUND");
    }

    /**
	 * Clears the currently saved colors.
	 * 
	 * TODO - Updaet based on preferences...
	 */
    public void clearColors() {
        this.disposeCurrentColors();
        fColorTable.clear();
    }

    /**
	 * Removes the current colors in the manager.
	 */
    public void dispose() {
        disposeCurrentColors();
    }

    public Color getColor(String color_string) {
        Color color = (Color) fColorTable.get(color_string);
        if (color == null) {
            color = new Color(Display.getCurrent(), getColorFromPreferences(color_string));
            fColorTable.put(color_string, color);
        }
        return color;
    }

    /**
	 * Retreives the color from this plugin's preferences.
	 * @param name
	 *           The "name" of the color (from PerferenceConstants)
	 * @return
	 *           The RGB value of this color.
	 */
    private RGB getColorFromPreferences(String name) {
        return PreferenceConverter.getColor(Activator.getDefault().getPreferenceStore(), name);
    }

    /**
	 * This is sent when the preferences change.
	 */
    public void updateColor(String name) {
        if (fColorTable.containsKey(name)) {
            ((Color) fColorTable.get(name)).dispose();
        }
        fColorTable.put(name, new Color(Display.getCurrent(), getColorFromPreferences(name)));
    }
}
