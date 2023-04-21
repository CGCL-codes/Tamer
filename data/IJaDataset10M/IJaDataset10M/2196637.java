package org.gjt.sp.jedit.browser;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import org.gjt.sp.jedit.jEdit;

/**
 * Adds tool-tip helpers for obscured list items.
 *
 * @author Jason Ginchereau
 */
public class HelpfulJList extends JList implements MouseListener {

    public HelpfulJList() {
        ToolTipManager.sharedInstance().registerComponent(this);
        addMouseListener(this);
    }

    public final String getToolTipText(MouseEvent evt) {
        int index = locationToIndex(evt.getPoint());
        if (index >= 0) {
            Object item = getModel().getElementAt(index);
            Component renderer = getCellRenderer().getListCellRendererComponent(this, item, index, isSelectedIndex(index), false);
            Dimension cellSize = renderer.getPreferredSize();
            Rectangle cellBounds = getCellBounds(index, index);
            if (cellBounds != null) {
                Rectangle cellRect = new Rectangle(0, cellBounds.y, cellSize.width, cellBounds.height);
                if (!cellRectIsVisible(cellRect)) return item.toString();
            }
        }
        return null;
    }

    public final Point getToolTipLocation(MouseEvent evt) {
        int index = locationToIndex(evt.getPoint());
        if (index >= 0) {
            Object item = getModel().getElementAt(index);
            Component renderer = getCellRenderer().getListCellRendererComponent(this, item, index, isSelectedIndex(index), false);
            Dimension cellSize = renderer.getPreferredSize();
            Rectangle cellBounds = getCellBounds(index, index);
            if (cellBounds != null) {
                Rectangle cellRect = new Rectangle(cellBounds.x, cellBounds.y, cellSize.width, cellBounds.height);
                if (!cellRectIsVisible(cellRect)) return new Point(cellRect.x + 20, cellRect.y);
            }
        }
        return null;
    }

    private final boolean cellRectIsVisible(Rectangle cellRect) {
        Rectangle vr = getVisibleRect();
        return vr.contains(cellRect.x, cellRect.y) && vr.contains(cellRect.x + cellRect.width, cellRect.y + cellRect.height);
    }

    public void mouseClicked(MouseEvent evt) {
    }

    public void mousePressed(MouseEvent evt) {
    }

    public void mouseReleased(MouseEvent evt) {
    }

    public void mouseEntered(MouseEvent evt) {
        ToolTipManager ttm = ToolTipManager.sharedInstance();
        toolTipInitialDelay = ttm.getInitialDelay();
        toolTipReshowDelay = ttm.getReshowDelay();
        ttm.setInitialDelay(200);
        ttm.setReshowDelay(0);
    }

    public void mouseExited(MouseEvent evt) {
        ToolTipManager ttm = ToolTipManager.sharedInstance();
        ttm.setInitialDelay(toolTipInitialDelay);
        ttm.setReshowDelay(toolTipReshowDelay);
    }

    private int toolTipInitialDelay = -1;

    private int toolTipReshowDelay = -1;
}
