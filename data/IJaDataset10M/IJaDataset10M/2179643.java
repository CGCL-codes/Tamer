package net.sf.fileexchange.ui;

import java.awt.Component;
import java.net.InetAddress;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import net.sf.fileexchange.api.Transfers.Entry;

public class TransfersColumns {

    public static TableColumn createUserTableColumn() {
        TableColumn column = new TableColumn(0, 200, new UserCellRenderer(), new NoCellEditor());
        column.setHeaderValue("Requested By");
        return column;
    }

    public static TableColumn createRequestTableColumn() {
        TableColumn column = new TableColumn(0, 300, new RequestCellRenderer(), new NoCellEditor());
        column.setHeaderValue("Request");
        return column;
    }

    private static final class UserCellRenderer extends DefaultTableCellRenderer {

        private static final long serialVersionUID = 1L;

        @Override
        protected void setValue(Object value) {
            if (value == null) {
                setText("");
                return;
            }
            Entry entry = (Entry) value;
            final InetAddress ip = entry.getUserIP();
            final String ipString = ip == null ? "?" : ip.toString();
            String userAgent = entry.getUserAgent();
            if (userAgent == null) userAgent = "?";
            setText(String.format("%s (%s)", ipString, userAgent));
        }
    }

    private static final class RequestCellRenderer extends DefaultTableCellRenderer {

        private static final long serialVersionUID = 1L;

        @Override
        protected void setValue(Object value) {
            Entry entry = (Entry) value;
            if (entry == null) {
                setText("");
                return;
            }
            final String method = entry.getMethod();
            final String uriPath = entry.getURIPath();
            if (method == null || uriPath == null) {
                setText("?");
                return;
            }
            setText(entry.getMethod() + " " + entry.getURIPath());
        }
    }

    private static final class NoCellEditor extends AbstractCellEditor implements TableCellEditor {

        private static final long serialVersionUID = 1L;

        private Entry entry;

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.entry = (Entry) table.getValueAt(row, column);
            return null;
        }

        @Override
        public boolean isCellEditable(EventObject e) {
            return false;
        }

        @Override
        public Object getCellEditorValue() {
            return entry;
        }
    }
}
