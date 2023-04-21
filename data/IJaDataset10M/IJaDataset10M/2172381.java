package be.vanvlerken.bert.logmonitor.gui;

import java.awt.Color;

/**
 * Container for a text with some extra properties
 */
public class ModuleNameCell implements IColoredTextCell {

    private String moduleName;

    private Color color;

    public ModuleNameCell() {
        this.moduleName = "";
        this.color = Color.white;
    }

    /**
	 * Returns the color.
	 * @return Color
	 */
    public Color getColor() {
        return color;
    }

    /**
	 * Returns the text.
	 * @return String
	 */
    public String getText() {
        return moduleName;
    }

    /**
	 * Sets the color.
	 * @param color The color to set
	 */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
	 * Sets the text.
	 * @param text The text to set
	 */
    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String toString() {
        return moduleName;
    }
}
