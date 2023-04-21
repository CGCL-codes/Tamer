package com.qasystems.swing;

import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.JPanel;
import javax.swing.Scrollable;

/**
 * This class implements a scrollable panel that can be placed in a JScrollPane.
 */
public class ScrollablePanel extends JPanel implements Scrollable {

    private int blockIncrement = 10;

    private int unitIncrement = 1;

    private boolean tracksHeight = false;

    private boolean tracksWidth = false;

    private Dimension viewportSize = null;

    /**
   * Default constructor.
   */
    public ScrollablePanel() {
        super();
    }

    /**
   * Default constructor.
   */
    public ScrollablePanel(int incBlock, int incUnit, boolean heightTrack, boolean widthTrack) {
        this();
        setScrollableBlockIncrement(incBlock);
        setScrollableUnitIncrement(incUnit);
        setScrollableTracksViewportHeight(heightTrack);
        setScrollableTracksViewportWidth(widthTrack);
    }

    /**
   * Gets the preferred viewport size.
   * Implements <tt>javax.swing.Scrollable</tt> interface.
   *
   * @return the preferred viewport size
   */
    public Dimension getPreferredScrollableViewportSize() {
        return ((viewportSize != null) ? viewportSize : getPreferredSize());
    }

    /**
   * Sets the preferred viewport size. If <tt>size</tt> is <tt>null</tt>, the
   * panel's preferred size is returned when calling
   * <tt>getPreferredScrollableViewportSize</tt>
   *
   * @param size the preferred viewport size
   */
    public synchronized void setPreferredScrollableViewportSize(Dimension size) {
        viewportSize = size;
    }

    /**
   * Ignores it's arguments.
   * Implements <tt>javax.swing.Scrollable</tt> interface.
   *
   * @return the block increment
   */
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return (blockIncrement);
    }

    /**
   * @param increment the block increment
   */
    public synchronized void setScrollableBlockIncrement(int increment) {
        blockIncrement = increment;
    }

    /**
   * Implements <tt>javax.swing.Scrollable</tt> interface.
   */
    public boolean getScrollableTracksViewportHeight() {
        return (tracksHeight);
    }

    /**
   * DOCUMENT ME!
   *
   * @param track DOCUMENT ME!
   */
    public synchronized void setScrollableTracksViewportHeight(boolean track) {
        tracksHeight = track;
    }

    /**
   * Implements <tt>javax.swing.Scrollable</tt> interface.
   */
    public boolean getScrollableTracksViewportWidth() {
        return (tracksWidth);
    }

    /**
   * DOCUMENT ME!
   *
   * @param track DOCUMENT ME!
   */
    public synchronized void setScrollableTracksViewportWidth(boolean track) {
        tracksWidth = track;
    }

    /**
   * Ignores it's arguments.
   * Implements <tt>javax.swing.Scrollable</tt> interface.
   *
   * @return the unit increment
   */
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return (unitIncrement);
    }

    /**
   * @param increment the unit increment
   */
    public synchronized void setScrollableUnitIncrement(int increment) {
        unitIncrement = increment;
    }
}
