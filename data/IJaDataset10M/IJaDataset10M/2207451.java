package pcgen.gui2.util;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import pcgen.gui2.util.event.DynamicTableColumnModelListener;
import pcgen.gui2.util.table.DefaultDynamicTableColumnModel;
import pcgen.gui2.util.table.DynamicTableColumnModel;

/**
 *
 * @author Connor Petty <cpmeister@users.sourceforge.net>
 */
public class JDynamicTable extends JTableEx {

    private final DynamicTableColumnModelListener listener = new DynamicTableColumnModelListener() {

        public void availableColumnAdded(TableColumnModelEvent event) {
            int index = event.getToIndex();
            TableColumn column = dynamicColumnModel.getAvailableColumns().get(index);
            menu.insert(createMenuItem(column), index);
            cornerButton.setVisible(true);
        }

        public void availableColumnRemove(TableColumnModelEvent event) {
            menu.remove(event.getFromIndex());
            if (menu.getComponentCount() == 0) {
                cornerButton.setVisible(false);
            }
        }
    };

    private final JButton cornerButton = new JButton(new CornerAction());

    private DynamicTableColumnModel dynamicColumnModel = null;

    private JPopupMenu menu = new JPopupMenu();

    @Override
    protected void configureEnclosingScrollPane() {
        super.configureEnclosingScrollPane();
        Container p = getParent();
        if (p instanceof JViewport) {
            Container gp = p.getParent();
            if (gp instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) gp;
                JViewport viewport = scrollPane.getViewport();
                if (viewport == null || viewport.getView() != this) {
                    return;
                }
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                scrollPane.setCorner(JScrollPane.UPPER_RIGHT_CORNER, cornerButton);
            }
        }
    }

    @Override
    protected void unconfigureEnclosingScrollPane() {
        super.unconfigureEnclosingScrollPane();
        Container p = getParent();
        if (p instanceof JViewport) {
            Container gp = p.getParent();
            if (gp instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) gp;
                JViewport viewport = scrollPane.getViewport();
                if (viewport == null || viewport.getView() != this) {
                    return;
                }
                scrollPane.setCorner(JScrollPane.UPPER_RIGHT_CORNER, null);
            }
        }
    }

    protected DynamicTableColumnModel createDefaultDynamicTableColumnModel() {
        return new DefaultDynamicTableColumnModel(getColumnModel(), 1);
    }

    private JCheckBoxMenuItem createMenuItem(TableColumn column) {
        JCheckBoxMenuItem item = new JCheckBoxMenuItem();
        boolean visible = dynamicColumnModel.isVisible(column);
        item.setSelected(visible);
        item.setAction(new MenuAction(column, visible));
        return item;
    }

    @Override
    public void setColumnModel(TableColumnModel columnModel) {
        if (this.dynamicColumnModel != null) {
            this.dynamicColumnModel.removeDynamicTableColumnModelListener(listener);
            cornerButton.setVisible(false);
        }
        super.setColumnModel(columnModel);
    }

    public void setColumnModel(DynamicTableColumnModel columnModel) {
        if (this.dynamicColumnModel != null) {
            this.dynamicColumnModel.removeDynamicTableColumnModelListener(listener);
        }
        this.dynamicColumnModel = columnModel;
        columnModel.addDynamicTableColumnModelListener(listener);
        super.setColumnModel(columnModel);
        List<TableColumn> columns = columnModel.getAvailableColumns();
        menu.removeAll();
        if (!columns.isEmpty()) {
            for (TableColumn column : columns) {
                menu.add(createMenuItem(column));
            }
            cornerButton.setVisible(true);
        } else {
            cornerButton.setVisible(false);
        }
    }

    private class CornerAction extends AbstractAction {

        public CornerAction() {
            super("...");
        }

        public void actionPerformed(ActionEvent e) {
            Container parent = getParent();
            menu.setVisible(true);
            menu.show(parent, parent.getWidth() - menu.getWidth(), 0);
        }
    }

    private class MenuAction extends AbstractAction {

        private boolean visible;

        private TableColumn column;

        public MenuAction(TableColumn column, boolean visible) {
            super(column.getHeaderValue().toString());
            this.visible = visible;
            this.column = column;
        }

        public void actionPerformed(ActionEvent e) {
            dynamicColumnModel.setVisible(column, visible = !visible);
        }
    }
}
