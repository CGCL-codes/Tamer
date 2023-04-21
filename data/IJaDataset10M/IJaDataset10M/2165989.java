package org.argouml.cognitive;

/**
 * Interface to be implementable by figures which can be highlighted to visually
 * identify them.
 * 
 * @author Bob Tarling
 */
public interface Highlightable {

    /**
     * Set the highlighted state.
     * @param highlighted true to highlight the fig
     */
    void setHighlight(boolean highlighted);

    /**
     * @return the current highlight state
     */
    boolean getHighlight();
}
