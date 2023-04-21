package info.wisl;

import javax.swing.JTextArea;

/**
 * Implements a cyclic buffer for logging
 *
 * @author Thomas Hildebrandt
 */
public class CyclicTextArea extends JTextArea {

    private int maxLines;

    public CyclicTextArea(int maxLines) throws IllegalArgumentException {
        super();
        if (maxLines < 1) {
            throw new IllegalArgumentException("max lines must be positive: " + maxLines);
        }
        this.maxLines = maxLines;
    }

    public void setMaxLines(int maxLines) throws IllegalArgumentException {
        if (maxLines < 1) {
            throw new IllegalArgumentException("max lines must be positive: " + maxLines);
        }
        this.maxLines = maxLines;
    }

    public int getMaxLines() {
        return maxLines;
    }

    /**
     * Not thread safe.
     */
    public void appendLine(String text) {
        while (getLineCount() >= maxLines) {
            try {
                replaceRange(null, 0, getLineEndOffset(0));
            } catch (javax.swing.text.BadLocationException ble) {
            }
            ;
        }
        append(text + "\n");
        setCaretPosition(getText().length());
    }
}
