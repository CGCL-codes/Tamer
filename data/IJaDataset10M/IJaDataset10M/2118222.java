package org.dllearner.tools.protege;

import java.awt.Color;

/**
 * This Class represents an entry of the suggest list.
 * @author Christian Koetteritzsch
 *
 */
public class SuggestListItem {

    private final Color color;

    private final String value;

    private final double accuracy;

    /**
     * Constructor for the SuggestListItem.
     * @param c Color Color in which the text is painted.
     * @param s String text that is shown.
     * @param acc Accuracy of the concept
     */
    public SuggestListItem(Color c, String s, double acc) {
        this.color = c;
        this.value = s;
        this.accuracy = acc;
    }

    /**
     * This method returns the color of the current list item.
     * @return Color Color of the current list item
     */
    public Color getColor() {
        return color;
    }

    /**
     * This Method returns the text of the current list item.
     * @return String Text of the current list item
     */
    public String getValue() {
        return value;
    }

    /**
	 * This method returns the accuracy of the current list item.
	 * @return accuracy
	 */
    public double getAccuracy() {
        return accuracy;
    }
}
