package mathgame.editor;

import mathgame.common.*;
import java.awt.*;
import javax.swing.*;

/**
 * Simply just a JDesktopPane that implements Scrollable.
 */
public class ScrollableDesktopPane extends JDesktopPane implements Scrollable {

    private Dimension contentDimension;

    /**
     * Constructs a ScrollableDesktopPane.
     */
    public ScrollableDesktopPane(Dimension contentDimension) {
        this.contentDimension = contentDimension;
    }

    public Dimension getPreferredSize() {
        return contentDimension;
    }

    public void setContentDimension(Dimension d) {
        contentDimension = d;
    }

    /**
     * Scrollable implementation. Returns the dimension of the Board.
     */
    public Dimension getPreferredScrollableViewportSize() {
        return contentDimension;
    }

    /**
     * Scrollable implementation. Returns 10.
     */
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 10;
    }

    /**
     * Scrollable implementation. Returns 100.
     */
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 100;
    }

    /**
     * Scrollable implementation. Returns true.
     */
    public boolean getScrollableTracksViewportWidth() {
        return true;
    }

    /**
     * Scrollable implementation. Returns true.
     */
    public boolean getScrollableTracksViewportHeight() {
        return true;
    }
}
