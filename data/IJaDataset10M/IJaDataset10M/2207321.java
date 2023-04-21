package org.databene.gui.swing.table.item.adapter;

import org.databene.gui.swing.table.item.FieldConnector;
import javax.swing.table.TableColumn;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.*;

/**
 * Created: 27.06.2005 08:59:55
 * @since 0.1.6
 * @author Volker Bergmann
 */
public class ConnectorTableColumn extends TableColumn {

    private static final long serialVersionUID = 7889497773878324784L;

    private FieldConnector connector;

    public ConnectorTableColumn(FieldConnector connector, int index) {
        super(index);
        this.connector = connector;
        TableCellEditor editor = connector.getEditor();
        if (editor instanceof DefaultCellEditor) {
            ((DefaultCellEditor) editor).setClickCountToStart(1);
        }
    }

    @Override
    public Object getHeaderValue() {
        return connector.getDisplayName();
    }

    @Override
    public TableCellRenderer getCellRenderer() {
        return connector.getRenderer();
    }

    @Override
    public TableCellEditor getCellEditor() {
        return connector.getEditor();
    }

    @Override
    public String toString() {
        return getClass().getName() + "['" + getHeaderValue() + "', " + getModelIndex() + "]";
    }
}
