package a03.swing.plaf;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.util.Enumeration;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.TreePath;
import a03.swing.plugin.A03PluginManager;

public class A03TreeUI extends BasicTreeUI {

    public static ComponentUI createUI(JComponent c) {
        return new A03TreeUI();
    }

    private A03TreeDelegate delegate;

    @Override
    public void installUI(JComponent c) {
        this.delegate = (A03TreeDelegate) A03SwingUtilities.getDelegate(c, UIManager.get("Tree.delegate"));
        super.installUI(c);
        A03PluginManager.getInstance().registerComponent(c);
    }

    @Override
    public void uninstallUI(JComponent c) {
        A03PluginManager.getInstance().unregisterComponent(c);
        super.uninstallUI(c);
    }

    protected void paintRow(Graphics g, Rectangle clipBounds, Insets insets, Rectangle bounds, TreePath path, int row, boolean isExpanded, boolean hasBeenExpanded, boolean isLeaf) {
        if (editingComponent != null && editingRow == row) return;
        boolean hasFocus = tree.hasFocus() && row == getRowForPath(tree, tree.getLeadSelectionPath());
        Component component = currentCellRenderer.getTreeCellRendererComponent(tree, path.getLastPathComponent(), tree.isRowSelected(row), isExpanded, isLeaf, row, hasFocus);
        boolean isSelected = tree.isRowSelected(row);
        if (isSelected) {
            component.setForeground(delegate.getSelectionForeground());
        } else {
            component.setForeground(delegate.getForeground(tree, row));
        }
        rendererPane.paintComponent(g, component, tree, bounds.x, bounds.y, bounds.width, bounds.height, true);
    }

    public void paint(Graphics g, JComponent c) {
        if (treeState == null) {
            return;
        }
        Rectangle paintBounds = g.getClipBounds();
        TreePath initialPath = getClosestPathForLocation(tree, 0, paintBounds.y);
        Enumeration<?> paintingEnumerator = treeState.getVisiblePathsFrom(initialPath);
        int row = treeState.getRowForPath(initialPath);
        int endY = paintBounds.y + paintBounds.height;
        Graphics2D graphics = (Graphics2D) g.create();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        try {
            Rectangle bounds = treeState.getBounds(initialPath, new Rectangle());
            int y = bounds != null ? bounds.y : 0;
            Insets insets = tree.getInsets();
            if (insets != null) {
                if (y == 0 && insets.top > 0) {
                    graphics.setColor(delegate.getBackground(tree, 1));
                    graphics.fillRect(0, 0, tree.getWidth(), insets.top);
                    y += insets.top;
                }
            }
            if (initialPath != null && paintingEnumerator != null) {
                while (paintingEnumerator.hasMoreElements()) {
                    boolean isSelected = tree.isRowSelected(row);
                    if (isSelected) {
                        graphics.setColor(delegate.getSelectionBackground());
                    } else {
                        graphics.setColor(delegate.getBackground(tree, row));
                    }
                    int rowHeight = getRowHeight(row);
                    graphics.fillRect(0, y, tree.getWidth(), rowHeight);
                    y += rowHeight;
                    if (y >= endY) {
                        break;
                    }
                    row++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.paint(graphics, c);
        graphics.dispose();
    }

    private int getRowHeight(int row) {
        int rowHeight = tree.getRowHeight();
        if (rowHeight <= 0) {
            Rectangle rowBounds = tree.getRowBounds(row);
            if (rowBounds != null) {
                rowHeight = rowBounds.height;
            } else {
                rowHeight = 16;
            }
        }
        return rowHeight;
    }
}
