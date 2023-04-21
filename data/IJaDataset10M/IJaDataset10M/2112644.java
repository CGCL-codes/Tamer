package com.bluebrim.swing.client;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneLayout;
import javax.swing.Scrollable;
import javax.swing.border.Border;

/**
 * Subclass to ScrollPaneLayout that can handle CoScrollable:s,
 * that is Components whose dimensions are related. Typically,
 * the height increases when the width decreases.
 *
 * @see CoViewportLayout
 * @see CoScrollable
 * @author Markus Persson 1999-09-15
 */
public class CoScrollPaneLayout extends ScrollPaneLayout {

    /** 
	 * Lay out the scrollpane. The positioning of components depends on
	 * the following constraints:
	 * <ul>
	 * <li> The row header, if present and visible, gets its preferred
	 * height and the viewports width.
	 * 
	 * <li> The column header, if present and visible, gets its preferred
	 * width and the viewports height.
	 * 
	 * <li> If a vertical scrollbar is needed, i.e. if the viewports extent
	 * height is smaller than its view height or if the displayPolicy
	 * is ALWAYS, it's treated like the row header wrt it's dimensions and
	 * it's made visible.
	 * 
	 * <li> If a horizontal scrollbar is needed it's treated like the
	 * column header (and see the vertical scrollbar item).
	 * 
	 * <li> If the scrollpane has a non-null viewportBorder, then space
	 * is allocated for that.
	 * 
	 * <li> The viewport gets the space available after accounting for
	 * the previous constraints.
	 * 
	 * <li> The corner components, if provided, are aligned with the 
	 * ends of the scrollbars and headers. If there's a vertical
	 * scrollbar the right corners appear, if there's a horizontal
	 * scrollbar the lower corners appear, a row header gets left
	 * corners and a column header gets upper corners.
	 * </ul>
	 *
	 * @param parent the Container to lay out
	 */
    public void layoutContainer(Container parent) {
        JScrollPane scrollPane = (JScrollPane) parent;
        vsbPolicy = scrollPane.getVerticalScrollBarPolicy();
        hsbPolicy = scrollPane.getHorizontalScrollBarPolicy();
        Rectangle availR = new Rectangle(scrollPane.getSize());
        Insets insets = parent.getInsets();
        availR.x = insets.left;
        availR.y = insets.top;
        availR.width -= insets.left + insets.right;
        availR.height -= insets.top + insets.bottom;
        Rectangle colHeadR = new Rectangle(0, availR.y, 0, 0);
        if ((colHead != null) && (colHead.isVisible())) {
            int colHeadHeight = colHead.getPreferredSize().height;
            colHeadR.height = colHeadHeight;
            availR.y += colHeadHeight;
            availR.height -= colHeadHeight;
        }
        Rectangle rowHeadR = new Rectangle(availR.x, 0, 0, 0);
        if ((rowHead != null) && (rowHead.isVisible())) {
            int rowHeadWidth = rowHead.getPreferredSize().width;
            rowHeadR.width = rowHeadWidth;
            availR.x += rowHeadWidth;
            availR.width -= rowHeadWidth;
        }
        Border viewportBorder = scrollPane.getViewportBorder();
        Insets vpbInsets;
        if (viewportBorder != null) {
            vpbInsets = viewportBorder.getBorderInsets(parent);
            availR.x += vpbInsets.left;
            availR.y += vpbInsets.top;
            availR.width -= vpbInsets.left + vpbInsets.right;
            availR.height -= vpbInsets.top + vpbInsets.bottom;
        } else {
            vpbInsets = new Insets(0, 0, 0, 0);
        }
        colHeadR.x = availR.x;
        rowHeadR.y = availR.y;
        Component view = (viewport != null) ? viewport.getView() : null;
        Dimension viewPrefSize = (view != null) ? view.getPreferredSize() : new Dimension(0, 0);
        Dimension extentSize = (viewport != null) ? viewport.toViewCoordinates(availR.getSize()) : new Dimension(0, 0);
        boolean viewTracksViewportWidth = false;
        boolean viewTracksViewportHeight = false;
        if (view instanceof Scrollable) {
            Scrollable sv = ((Scrollable) view);
            viewTracksViewportWidth = sv.getScrollableTracksViewportWidth();
            viewTracksViewportHeight = sv.getScrollableTracksViewportHeight();
        }
        Rectangle vsbR = new Rectangle(0, availR.y - vpbInsets.top, 0, 0);
        boolean vsbNeeded;
        if (vsbPolicy == VERTICAL_SCROLLBAR_ALWAYS) {
            vsbNeeded = true;
        } else if (vsbPolicy == VERTICAL_SCROLLBAR_NEVER) {
            vsbNeeded = false;
        } else {
            vsbNeeded = !viewTracksViewportHeight && (viewPrefSize.height > extentSize.height);
        }
        if ((vsb != null) && vsbNeeded) {
            int vsbWidth = vsb.getPreferredSize().width;
            availR.width -= vsbWidth;
            vsbR.x = availR.x + availR.width + vpbInsets.right;
            vsbR.width = vsbWidth;
        }
        Rectangle hsbR = new Rectangle(availR.x - vpbInsets.left, 0, 0, 0);
        boolean hsbNeeded;
        if (hsbPolicy == HORIZONTAL_SCROLLBAR_ALWAYS) {
            hsbNeeded = true;
        } else if (hsbPolicy == HORIZONTAL_SCROLLBAR_NEVER) {
            hsbNeeded = false;
        } else {
            hsbNeeded = !viewTracksViewportWidth && (viewPrefSize.width > extentSize.width);
        }
        if ((hsb != null) && hsbNeeded) {
            int hsbHeight = hsb.getPreferredSize().height;
            availR.height -= hsbHeight;
            hsbR.y = availR.y + availR.height + vpbInsets.bottom;
            hsbR.height = hsbHeight;
            if ((vsb != null) && !vsbNeeded && (vsbPolicy != VERTICAL_SCROLLBAR_NEVER)) {
                extentSize = viewport.toViewCoordinates(availR.getSize());
                vsbNeeded = viewPrefSize.height > extentSize.height;
                if (vsbNeeded) {
                    int vsbWidth = vsb.getPreferredSize().width;
                    availR.width -= vsbWidth;
                    vsbR.x = availR.x + availR.width + vpbInsets.right;
                    vsbR.width = vsbWidth;
                }
            }
        }
        vsbR.height = availR.height + vpbInsets.top + vpbInsets.bottom;
        hsbR.width = availR.width + vpbInsets.left + vpbInsets.right;
        rowHeadR.height = availR.height;
        colHeadR.width = availR.width;
        if (viewport != null) {
            viewport.setBounds(availR);
        }
        if (rowHead != null) {
            rowHead.setBounds(rowHeadR);
        }
        if (colHead != null) {
            colHead.setBounds(colHeadR);
        }
        if (vsb != null) {
            if (vsbNeeded) {
                vsb.setVisible(true);
                vsb.setBounds(vsbR);
            } else {
                vsb.setVisible(false);
            }
        }
        if (hsb != null) {
            if (hsbNeeded) {
                hsb.setVisible(true);
                hsb.setBounds(hsbR);
            } else {
                hsb.setVisible(false);
            }
        }
        if (lowerLeft != null) {
            lowerLeft.setBounds(rowHeadR.x, hsbR.y, rowHeadR.width, hsbR.height);
        }
        if (lowerRight != null) {
            lowerRight.setBounds(vsbR.x, hsbR.y, vsbR.width, hsbR.height);
        }
        if (upperLeft != null) {
            upperLeft.setBounds(rowHeadR.x, colHeadR.y, rowHeadR.width, colHeadR.height);
        }
        if (upperRight != null) {
            upperRight.setBounds(vsbR.x, colHeadR.y, vsbR.width, colHeadR.height);
        }
    }
}
