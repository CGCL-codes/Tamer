package net.sourceforge.ivi.waveview.ui.tree;

import org.eclipse.swt.graphics.Point;

public interface IViewport {

    /**
	 * getVisibleViewPosition()
	 * 
	 * Returns the coordinates of the upper-left corner of the visible 
	 * portion of the view
	 */
    Point getVisibleViewPosition();

    /**
	 * getVisibleViewSize()
	 * 
	 * Returns the size of the visible portion of the view
	 */
    Point getVisibleViewSize();

    /**
	 * getTotalViewSize()
	 * 
	 * Returns the total size of the view (invisible+visible)
	 */
    Point getTotalViewSize();

    /**
	 * addViewChangeListener
	 */
    void addViewChangeListener(IViewportChangeListener l);
}
