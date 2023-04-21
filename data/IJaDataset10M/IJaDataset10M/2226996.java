package org.dpr.mykeys.app.test;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.TreePath;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dpr.mykeys.app.CertificateInfo;

public class ExplorerTreeUI extends BasicTreeUI {

    public static final Log log = LogFactory.getLog(ExplorerTreeUI.class);

    private Color backgroundSelectionColor = null;

    private Color backgroundNonSelectionColor = null;

    private RowSelectionListener sf = new RowSelectionListener();

    protected void installDefaults() {
        super.installDefaults();
        backgroundSelectionColor = Color.RED;
        backgroundNonSelectionColor = Color.BLUE;
    }

    protected void paintRow(Graphics g, Rectangle clipBounds, Insets insets, Rectangle bounds, TreePath path, int row, boolean isExpanded, boolean hasBeenExpanded, boolean isLeaf) {
        if (editingComponent != null && editingRow == row) return;
        if (!tree.isRowSelected(row) && path.getPath().length == 2) {
        }
        int h = tree.getRowHeight();
        Graphics2D g2d = (Graphics2D) g;
        final Color color1 = Color.WHITE;
        final Color color2 = Color.RED;
        GradientPaint gp = new GradientPaint(0, h * row, backgroundSelectionColor, 0, h, backgroundNonSelectionColor);
        g2d.setPaint(gp);
        g2d.fillRect(clipBounds.x, h * row, clipBounds.width, h);
        log.trace(h);
        super.paintRow(g, clipBounds, insets, bounds, path, row, isExpanded, hasBeenExpanded, isLeaf);
    }

    protected void installListeners() {
        super.installListeners();
        tree.addMouseListener(sf);
    }

    protected void uninstallListeners() {
        tree.removeMouseListener(sf);
        super.uninstallListeners();
    }

    private class RowSelectionListener extends MouseAdapter {

        /**
		 * Listener for selecting the entire rows.
		 * 
		 * @author Kirill Grouchnikov
		 */
        @Override
        public void mousePressed(MouseEvent e) {
            if (!tree.isEnabled()) return;
            TreePath closestPath = tree.getClosestPathForLocation(e.getX(), e.getY());
            if (closestPath == null) return;
            Rectangle bounds = tree.getPathBounds(closestPath);
            if ((e.getY() >= bounds.y) && (e.getY() < (bounds.y + bounds.height)) && ((e.getX() < bounds.x) || (e.getX() > (bounds.x + bounds.width)))) {
                if (isLocationInExpandControl(closestPath, e.getX(), e.getY())) {
                    return;
                }
                selectPathForEvent(closestPath, e);
            }
        }
    }
}
