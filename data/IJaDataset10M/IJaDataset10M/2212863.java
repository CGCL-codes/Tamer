package com.myapp.tools.media.renamer.view.swing;

import static com.myapp.tools.media.renamer.controller.Msg.msg;
import static javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION;
import static com.myapp.tools.media.renamer.view.swing.IActionCommands.*;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import com.myapp.tools.media.renamer.config.IRenamerConfiguration;
import com.myapp.tools.media.renamer.config.IConstants.ISysConstants;
import com.myapp.tools.media.renamer.controller.Msg;
import com.myapp.tools.media.renamer.controller.Util;
import com.myapp.tools.media.renamer.model.IProperty;
import com.myapp.tools.media.renamer.model.IRenamable;
import com.myapp.tools.media.renamer.view.IListView;

/**
 * gui object that helds the table component of the application.
 * 
 * @author andre
 * 
 */
@SuppressWarnings("serial")
final class ListView extends JPanel implements IListView, ISysConstants {

    /**
     * dumb class that helds the width of a column.
     * 
     * @author andre
     * 
     */
    static class ColumnWidth {

        final int width;

        /**
         * creates a new ColDef with the given width
         * 
         * @param width
         *            the width of the coldef
         */
        ColumnWidth(int width) {
            this.width = width;
        }
    }

    private JTable jtable;

    private SwingApplication app;

    /**
     * creates a table component for the given app
     * 
     * @param appFrame the appcontext
     */
    public ListView(SwingApplication appFrame) {
        super(new BorderLayout());
        this.app = appFrame;
        createTable();
        init();
    }

    /**
     * inits and sets up the components
     */
    private void init() {
        JScrollPane scrollPane = new JScrollPane(jtable);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * creates the jtable instance used to display the elements
     */
    private void createTable() {
        jtable = new JTable(app.getTableModel()) {

            {
                setFillsViewportHeight(true);
                ListSelectionModel lsm = getSelectionModel();
                lsm.setSelectionMode(SINGLE_INTERVAL_SELECTION);
                lsm.addListSelectionListener(new ListSelectionListener() {

                    public void valueChanged(ListSelectionEvent e) {
                        app.getListSelectionListener().valueChanged(new ListSelectionEvent(ListView.this, e.getFirstIndex(), e.getLastIndex(), e.getValueIsAdjusting()));
                    }
                });
                addMouseListener(new MouseAdapter() {

                    public void mouseClicked(MouseEvent e) {
                        if (e.getButton() == MouseEvent.BUTTON1) return;
                        int rowClicked = rowAtPoint(e.getPoint());
                        int[] selection = getSelectedRows();
                        boolean clickWasOnSelectedRow = false;
                        for (int i = 0, sz = selection.length; i < sz; i++) {
                            if (selection[i] == rowClicked) {
                                clickWasOnSelectedRow = true;
                                break;
                            }
                        }
                        if (!clickWasOnSelectedRow) return;
                        showContextMenu(e.getPoint());
                    }
                });
                RenamerDelegate wrapper = (RenamerDelegate) app.getRenamer();
                TableColumnModel colModel = getColumnModel();
                List<IProperty> propList = wrapper.getTableColumns();
                Class<? extends IProperty> clazz = null;
                TableColumn tableColumn = null;
                ColumnWidth colDef = null;
                for (int i = 0, size = propList.size(); i < size; i++) {
                    tableColumn = colModel.getColumn(i);
                    clazz = propList.get(i).getClass();
                    colDef = wrapper.getColDefs().get(clazz);
                    assert colDef != null : "" + clazz.getName();
                    tableColumn.setPreferredWidth(colDef.width);
                }
            }

            @Override
            public Component prepareRenderer(TableCellRenderer renderer, final int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!(c instanceof JComponent)) return c;
                JComponent jc = (JComponent) c;
                IRenamable rn = app.getRenamer().getElementAt(row);
                jc.setToolTipText(msg("Table.tableContextMenu.text").replace("#oldname#", rn.getOldName()).replace("#newname#", rn.getNewName()).replace("#oldparent#", rn.getOldParentAbsolutePath()).replace("#newparent#", rn.getNewParentAbsolutePath()).replace("#size#", Util.getHumanReadableFileSize(rn.getSourceObject().length())));
                return c;
            }
        };
    }

    private JMenuItem createMenuItem(String name, String msgId, String actionCommand) {
        JMenuItem i = new JMenuItem(msg(msgId));
        i.setName(name);
        i.addActionListener(app.getActionListener());
        i.setActionCommand(actionCommand);
        return i;
    }

    /**
     * displays a contextmenu with actions that are possible for the current
     * selection
     * 
     * @param pointInJTable
     */
    private void showContextMenu(Point pointInJTable) {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem rm = createMenuItem("ListView.showContextMenu.rm", "MenuBar.RemoveFiles", REMOVE_SELECTED_FILES);
        JMenuItem mv = createMenuItem("ListView.showContextMenu.mv", "MenuBar.MoveFiles", MOVE_SELECTED_FILES);
        JMenuItem num = createMenuItem("ListView.showContextMenu.num", "MenuBar.editNummerierung", SET_NUMMERIERUNG_TO_SELECTION);
        JMenuItem reset = createMenuItem("ListView.showContextMenu.reset", "MenuBar.discardSelChanges", DISCARD_SELECTIONS_CHANGES);
        popupMenu.add(mv);
        popupMenu.add(rm);
        popupMenu.add(num);
        popupMenu.add(reset);
        popupMenu.show((Component) app.getUIComponent(), pointInJTable.x, pointInJTable.y + 50);
    }

    @Override
    public int[] getSelection() {
        int[] selectedElements = jtable.getSelectedRows();
        Arrays.sort(selectedElements);
        return selectedElements;
    }

    @Override
    public void clearSelection() {
        jtable.clearSelection();
    }

    @Override
    public void setSelection(int from, int to) {
        jtable.getSelectionModel().setSelectionInterval(from, to);
    }

    @Override
    public void persistSettings(IRenamerConfiguration cfg) {
        TableColumnModel m = jtable.getTableHeader().getColumnModel();
        ;
        TableColumn col;
        String name, width;
        for (int i = 0, count = m.getColumnCount(); i < count; i++) {
            col = m.getColumn(i);
            name = col.getHeaderValue().toString();
            width = Integer.toString(col.getWidth());
            if (name.equals(Msg.msg(COLUMN_NAME_BESCHREIBUNG))) {
                cfg.setCustomProperty(COLUMN_WIDTH_BESCHREIBUNG, width);
            } else if (name.equals(Msg.msg(COLUMN_NAME_THEMA))) {
                cfg.setCustomProperty(COLUMN_WIDTH_THEMA, width);
            } else if (name.equals(Msg.msg(COLUMN_NAME_TITEL))) {
                cfg.setCustomProperty(COLUMN_WIDTH_TITEL, width);
            } else if (name.equals(Msg.msg(COLUMN_NAME_DATUM))) {
                cfg.setCustomProperty(COLUMN_WIDTH_DATUM, width);
            } else if (name.equals(Msg.msg(COLUMN_NAME_NUMMER))) {
                cfg.setCustomProperty(COLUMN_WIDTH_NUMMER, width);
            } else if (name.equals(Msg.msg(COLUMN_NAME_TYP))) {
                cfg.setCustomProperty(COLUMN_WIDTH_TYP, width);
            } else if (name.equals(Msg.msg(COLUMN_NAME_DATEIGROESSE))) {
                cfg.setCustomProperty(COLUMN_WIDTH_DATEIGROESSE, width);
            } else if (name.equals(Msg.msg(COLUMN_NAME_QUELLDATEI))) {
                cfg.setCustomProperty(COLUMN_WIDTH_QUELLDATEI, width);
            } else if (name.equals(Msg.msg(COLUMN_NAME_ZIELDATEI))) {
                cfg.setCustomProperty(COLUMN_WIDTH_ZIELDATEI, width);
            } else throw new RuntimeException("unknown name : " + name);
        }
    }

    @Override
    public Object getUIComponent() {
        return this;
    }
}
