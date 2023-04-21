package be.lassi.ui.log;

import java.awt.Dimension;
import javax.swing.JTextPane;

/**
 * Modified <code>JTextPane</code> that does not wrap the text.
 */
public class NoWrapTextPane extends JTextPane {

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSize(final Dimension newSize) {
        Dimension size = newSize;
        if (size.width < getParent().getSize().width) {
            size.width = getParent().getSize().width;
        }
        super.setSize(size);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getScrollableTracksViewportWidth() {
        return false;
    }
}
