package org.openeccos.table.renderer;

import org.openeccos.table.BaseTableModel;
import org.openeccos.widgets.PDCombo;
import nextapp.echo2.app.Component;
import nextapp.echo2.app.Extent;
import nextapp.echo2.app.Table;
import nextapp.echo2.app.event.ActionEvent;
import nextapp.echo2.app.event.ActionListener;
import nextapp.echo2.app.table.TableCellRenderer;

/**
 * @author cgspinner
 * Created on Oct 5, 2007
 */
public class ComboRenderer implements TableCellRenderer {

    private Object[] data;

    public ComboRenderer(Object[] data) {
        this.data = data;
    }

    public Component getTableCellRendererComponent(Table table, Object value, final int col, final int row) {
        final BaseTableModel tableModel = (BaseTableModel) table.getModel();
        final PDCombo cbo = new PDCombo(data);
        cbo.setWidth(new Extent(100, Extent.PERCENT));
        cbo.setSelectedItem(value);
        cbo.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                tableModel.setValueAt(cbo.getSelectedItem(), col, row);
            }
        });
        return cbo;
    }
}
