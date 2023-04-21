package org.coode.matrix.ui.action;

import org.protege.editor.core.ui.view.DisposableAction;
import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Author: drummond<br>
 * http://www.cs.man.ac.uk/~drummond/<br><br>
 * <p/>
 * The University Of Manchester<br>
 * Bio Health Informatics Group<br>
 * Date: Sep 9, 2008<br><br>
 */
public class FitColumnsToContentAction extends DisposableAction {

    private JTable table;

    public FitColumnsToContentAction(JTable table, String name, Icon icon) {
        super(name, icon);
        this.table = table;
    }

    public void actionPerformed(ActionEvent event) {
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        packColumns(table);
    }

    private void packColumns(JTable table) {
        for (int i = 0; i < table.getColumnCount(); i++) {
            final TableColumn col = table.getColumnModel().getColumn(i);
            int w = getPreferredColumnWidth(table, i, col);
            col.setPreferredWidth(w);
        }
    }

    private int getPreferredColumnWidth(JTable table, int index, TableColumn col) {
        int width = 0;
        TableCellRenderer renderer = col.getHeaderRenderer();
        if (renderer == null) {
            final JTableHeader header = table.getTableHeader();
            if (header != null) {
                renderer = header.getDefaultRenderer();
            }
        }
        if (renderer != null) {
            Component comp = renderer.getTableCellRendererComponent(table, col.getHeaderValue(), false, false, 0, 0);
            width = comp.getPreferredSize().width;
        }
        for (int r = 0; r < table.getRowCount(); r++) {
            renderer = table.getCellRenderer(r, index);
            Component comp = renderer.getTableCellRendererComponent(table, table.getValueAt(r, index), false, false, r, index);
            width = Math.max(width, comp.getPreferredSize().width);
        }
        width = width + (table.getColumnModel().getColumnMargin());
        return width;
    }

    public void dispose() {
        table = null;
    }
}
