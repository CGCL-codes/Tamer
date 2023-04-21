package com.bluemarsh.jswat.view;

import java.awt.Color;
import javax.swing.JTextField;

/**
 * SelectionDrawLayer is responsible for showing the current text
 * selection in the text area. It also keeps track of the current text
 * selection.
 *
 * @author  Nathan Fiedler
 */
public class SelectionDrawLayer extends HighlightDrawLayer {

    /** Our draw layer priority. */
    private static final int PRIORITY = 64;

    /**
     * Constructs a SelectionDrawLayer using the default color.
     */
    public SelectionDrawLayer() {
        super(new JTextField().getSelectionColor());
    }

    /**
     * Gets the priority level of this particular draw layer. Typically
     * each type of draw layer has its own priority. Lower values are
     * higher priority.
     *
     * @return  priority level.
     */
    public int getPriority() {
        return PRIORITY;
    }

    /**
     * Returns the selected text's end position. Return 0 if the
     * document is empty, or the value of dot if there is no selection.
     *
     * @return  the end position >= 0
     */
    public int getSelectionEnd() {
        return getHighlightEnd();
    }

    /**
     * Returns the selected text's start position. Return 0 if the
     * document is empty, or the value of dot if there is no selection.
     *
     * @return  the start position >= 0
     */
    public int getSelectionStart() {
        return getHighlightStart();
    }

    /**
     * Selects the text found between the specified start and end
     * locations. If the start location is after the end location, the
     * values will be swapped before the selection is made.
     *
     * @param  start  start offset of the selection.
     * @param  end    end offset of the selection.
     */
    public void setSelection(int start, int end) {
        if (end < start) {
            int t = start;
            start = end;
            end = t;
        }
        setHighlight(start, end);
    }
}
