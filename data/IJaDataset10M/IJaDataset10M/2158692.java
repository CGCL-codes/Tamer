package be.vanvlerken.bert.logmonitor.gui;

import java.awt.Color;
import be.vanvlerken.bert.logmonitor.logging.ILogEntry;

/**
 * Container for Identifier with some extra properties
 */
public class IdentifierCell implements IColoredTextCell {

    private int identifier;

    private Color color;

    public IdentifierCell() {
        this.identifier = 0;
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
	 * Returns the identifier.
	 * @return int
	 */
    public int getIdentifier() {
        return identifier;
    }

    /**
	 * Sets the color.
	 * @param color The color to set
	 */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
	 * Sets the identifier.
	 * @param identifier The identifier to set
	 */
    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }

    public String toString() {
        String errorLevel;
        switch(identifier) {
            case ILogEntry.ERROR:
                errorLevel = "ERROR";
                break;
            case ILogEntry.WARNING:
                errorLevel = "WARNING";
                break;
            case ILogEntry.INFO:
                errorLevel = "INFO";
                break;
            case ILogEntry.VERBOSE:
                errorLevel = "VERBOSE";
                break;
            default:
                errorLevel = "UNKNOWN";
        }
        return errorLevel;
    }

    /**
     * @see be.vanvlerken.bert.logmonitor.gui.IColoredTextCell#getText()
     */
    public String getText() {
        return toString();
    }
}
