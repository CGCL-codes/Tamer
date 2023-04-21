package org.viewaframework.widget.swing.treetable.single;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.CompoundHighlighter;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.treetable.TreeTableModel;
import org.viewaframework.widget.swing.table.DynamicTableColumn;
import org.viewaframework.widget.swing.treetable.single.ui.SingleGroupingPopup;

/**
 * @author mgg
 *
 * @param <T>
 */
public class DynamicTreeTable<T> extends JXTreeTable {

    private static final long serialVersionUID = 4240778318196874588L;

    /**
	 * @author mgg
	 *
	 */
    class PopupListener extends MouseAdapter {

        public void mousePressed(MouseEvent e) {
            showPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            showPopup(e);
        }

        private void showPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                int height = getTableHeader().getY() + getTableHeader().getHeight();
                groupingPopup.show(e.getComponent(), e.getX(), height);
            }
        }
    }

    private SingleGroupingPopup<T> groupingPopup;

    private DynamicTreeTableModel<T> notAdaptedModel;

    /**
	 * @param model
	 */
    public DynamicTreeTable(DynamicTreeTableModel<T> model) {
        super(model);
        this.notAdaptedModel = model;
        this.groupingPopup = new SingleGroupingPopup<T>(this);
        this.getTableHeader().addMouseListener(new PopupListener());
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setTreeTableModel(TreeTableModel treeModel) {
        if (DynamicTreeTableModel.class.isInstance(treeModel)) {
            this.notAdaptedModel = DynamicTreeTableModel.class.cast(treeModel);
        }
        super.setTreeTableModel(treeModel);
    }

    /**
     * Paints empty rows too, after letting the UI delegate do
     * its painting.
     */
    public void paint(Graphics g) {
        super.paint(g);
        paintEmptyRows(g);
    }

    /**
     * Paints the backgrounds of the implied empty rows when the
     * table model is insufficient to fill all the visible area
     * available to us. We don't involve cell renderers, because
     * we have no data.
     */
    protected void paintEmptyRows(Graphics g) {
        Highlighter[] highlighters = this.getCompoundHighlighter().getHighlighters();
        if (highlighters.length == 1) {
            final int rowCount = getRowCount();
            final Rectangle clip = g.getClipBounds();
            if (rowCount * rowHeight < clip.height) {
                for (int i = rowCount; i <= clip.height / rowHeight; ++i) {
                    g.setColor(colorForRow(i));
                    g.fillRect(clip.x, i * rowHeight, clip.width, rowHeight);
                }
            }
        }
    }

    /**
     * Returns the appropriate background color for the given row.
     */
    protected Color colorForRow(int row) {
        CompoundHighlighter ch = null;
        int rowx = 1;
        if (row % 2 == 0) {
            rowx = 0;
        }
        ch = CompoundHighlighter.class.cast(this.getCompoundHighlighter().getHighlighters()[0]);
        return ColorHighlighter.class.cast(ch.getHighlighters()[rowx]).getBackground();
    }

    /**
	 * @return
	 */
    public DynamicTreeTableModel<T> getNotAdaptedModel() {
        return this.notAdaptedModel;
    }

    /**
	 * @return
	 */
    public List<DynamicTableColumn> getTreeTableColumnInfoList() {
        return this.notAdaptedModel.getTreeTableColumnInfoList();
    }
}
