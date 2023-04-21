package gui.dialog.SyncForum;

import java.awt.Component;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellRenderer;
import domain.Forum;
import exceptions.DatabaseConnectException;

class TRenderer extends JCheckBox implements TableCellRenderer {

    public TRenderer() {
        setHorizontalAlignment(JLabel.CENTER);
    }

    @Override
    public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column) {
        if (isSelected) {
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(table.getBackground());
        }
        return this;
    }
}

/**
 * @author Sergey
 */
public class SyncForumList extends JTable {

    public SyncForumList() {
        try {
            final SyncForumsModel model = new SyncForumsModel();
            Forum.data.addForumMapperListener(model);
            setModel(model);
            final CheckBoxHeader jb = new CheckBoxHeader(new MyItemListener(), Forum.data.getAllForums().size() == Forum.data.getSyncForums().size());
            getColumnModel().getColumn(0).setHeaderRenderer(jb);
            setAutoResizeMode(AUTO_RESIZE_ALL_COLUMNS);
            setFillsViewportHeight(true);
            setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            getColumnModel().getColumn(0).setMaxWidth(50);
            getColumnModel().getColumn(0).setMinWidth(50);
            getColumnModel().getColumn(2).setMaxWidth(50);
            getColumnModel().getColumn(2).setMinWidth(50);
            setDefaultEditor(JCheckBox.class, new DefaultCellEditor(new JCheckBox()));
        } catch (DatabaseConnectException ex) {
            Logger.getLogger(SyncForumList.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public TableCellRenderer getCellRenderer(final int row, final int column) {
        final Object value = getValueAt(row, column);
        if (value != null) {
            if (value instanceof JCheckBox) {
                return new TRenderer();
            }
        }
        return super.getCellRenderer(row, column);
    }
}
