package antsnest.theme;

import java.awt.*;
import com.jgoodies.looks.plastic.*;

/**
 * Class representing a set of colours and styles
 * @author Chris Clohosy
 */
public class Theme {

    /**
	 * The name of the theme
	 */
    private String name;

    /**
	 * The JGoodies theme to use
	 */
    private PlasticTheme goodieTheme;

    /**
	 * The colour to use for the project XML tag
	 */
    private Color projectColour;

    /**
	 * The colour to use for the target XML tag
	 */
    private Color targetColour;

    /**
	 * The colour to use for the property XML tag
	 */
    private Color propertyColour;

    /**
	 * The colour to use for highlighting
	 */
    private Color highlightColour;

    /**
	 * Constructs a theme
	 * @param name the name of the theme
	 * @param goodieTheme the JGoodies theme to use
	 * @param projectColour the colour to use for the project XML tag
	 * @param targetColour the colour to use for the target XML tag
	 * @param propertyColour the colour to use for the property XML tag
	 * @param highlightColour the colour to use for highlighting
	 */
    public Theme(String name, PlasticTheme goodieTheme, Color projectColour, Color targetColour, Color propertyColour, Color highlightColour) {
        this.name = name;
        this.goodieTheme = goodieTheme;
        this.projectColour = projectColour;
        this.targetColour = targetColour;
        this.propertyColour = propertyColour;
        this.highlightColour = highlightColour;
    }

    /**
	 * Gets the name of the theme
	 * @return a String, the name
	 */
    public String getName() {
        return name;
    }

    /**
	 * Gets the JGoodies theme being used
	 * @return a PlasticTheme, the theme
	 */
    public PlasticTheme getJGoodiesTheme() {
        return goodieTheme;
    }

    /**
	 * Gets the colour to use for the project XML tag
	 * @return a Color, the colour
	 */
    public Color getProjectColour() {
        return projectColour;
    }

    /**
	 * Gets the colour to use for the target XML tag
	 * @return a Color, the colour
	 */
    public Color getTargetColour() {
        return targetColour;
    }

    /**
	 * Gets the colour to use for the property XML tag
	 * @return a Color, the colour
	 */
    public Color getPropertyColour() {
        return propertyColour;
    }

    /**
	 * Gets the colour to use for highlighting
	 * @return a Color, the colour
	 */
    public Color getHighlightColour() {
        return highlightColour;
    }

    /**
	 * Gets a string representation of this theme
	 * @return a String, the theme as a string
	 */
    public String toString() {
        return name;
    }
}
