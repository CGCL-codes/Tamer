package org.statefive.feedstate.ui.swing;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.util.LinkedHashMap;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import org.statefive.feedstate.feed.CommonMessageFormat;
import org.statefive.feedstate.feed.column.Column;
import org.statefive.feedstate.feed.column.ColumnGroup;
import org.statefive.feedstate.feed.view.FeedDataListener;
import org.statefive.feedstate.feed.view.ViewSelectionListener;
import org.statefive.feedstate.feed.view.Viewable;

/**
Table for containing feed messages.

@author rmeeking
 */
public class TableViewPanel extends JPanel implements FeedDataListener, ViewSelectionListener {

    /** The table for maintaining messages. */
    private JTable table;

    /** */
    private JScrollPane scrollPane;

    /** Default Table model for CMFs. */
    private DefaultTableModel tableModel;

    /** Provides information based on the column identifier of the CMFs that
  this view stores. */
    private DefaultTableColumnModel columnModel;

    /** For the moment we're storing the column group here. Since feeds can
  be remote, perhaps it makes sense to register all column groups (and
  thus removing this instance variable) against the name of the feed they
  are dealing with. */
    private ColumnGroup columnGroup;

    /** The underlying view that this table obtains data from. */
    private Viewable view;

    /**
   * Creates a new view panel for displaying feed items in a table.
   *
   * @param view the underlying view that this tablewill get it's data
   * from.
   */
    public TableViewPanel(Viewable view) {
        this.view = view;
        this.setLayout(new BorderLayout());
        view.addViewSelectionListener(this);
    }

    /**
   * Creates a new table panel with no underlying view.
   */
    public TableViewPanel() {
        this.setLayout(new BorderLayout());
    }

    /**
   * Marks that a new selection has been made within the view.
   *
   * @param messages The newly selected messages. The key of the map
   * determines the position in the view (indexed from 0) of the selected messages.
   */
    @Override
    public void selectionChanged(LinkedHashMap<Integer, CommonMessageFormat> messages) {
        if (messages.keySet().iterator().hasNext()) {
            int pos = messages.keySet().iterator().next();
            table.setRowSelectionInterval(pos, pos);
            Rectangle r = table.getCellRect(pos, 0, true);
            table.scrollRectToVisible(r);
        }
    }

    /**
   * When a new feed data message arrives, it is immediately added
   * to the table.
   *
   * @param feedMessage the message to add to the table.
   */
    @Override
    public void feedDataUpdate(CommonMessageFormat feedMessage) {
        Column[] cols = columnGroup.getColumns();
        Object[] o = new Object[cols.length];
        for (int i = 0; i < cols.length; i++) {
            o[i] = feedMessage.getField(i);
        }
        tableModel.insertRow(tableModel.getRowCount(), o);
        if (view.isSelectRecentMessage()) {
            int pos = table.getModel().getRowCount() - 1;
            table.setRowSelectionInterval(pos, pos);
            Rectangle r = table.getCellRect(pos, 0, true);
            table.scrollRectToVisible(r);
        }
    }

    /**
   * Sets the column group for this table view - the table cannot
   * instantiate any columns until this information is received.
   *
   * @param columnGroup the non-<tt>null</tt> column group.
   */
    @Override
    public void setColumnGroup(ColumnGroup columnGroup) {
        if (this.columnGroup == null) {
            this.columnGroup = columnGroup;
            columnModel = new DefaultTableColumnModel();
            Column[] cols = columnGroup.getColumns();
            for (int i = 0; i < cols.length; i++) {
                TableColumn column = new TableColumn(i);
                column.setIdentifier(cols[i].getDisplayName());
                columnModel.addColumn(column);
            }
            tableModel = new FeedTableModel(cols);
            table = new JTable(tableModel);
            SelectionListener listener = new SelectionListener(view, table);
            table.getSelectionModel().addListSelectionListener(listener);
            scrollPane = new JScrollPane(table);
            add(scrollPane, BorderLayout.CENTER);
        }
    }
}

/**
 * Simple class to get the selected message from the view.
 * @author rich
 */
class SelectionListener implements ListSelectionListener {

    Viewable view;

    JTable table;

    SelectionListener(Viewable view, JTable table) {
        this.view = view;
        this.table = table;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            for (int i = 0; i < table.getSelectedRows().length; i++) {
                Rectangle r = table.getCellRect(table.getSelectedRows()[i], 0, true);
                table.scrollRectToVisible(r);
            }
            LinkedHashMap<Integer, CommonMessageFormat> messages = new LinkedHashMap<Integer, CommonMessageFormat>();
            messages.put(table.getSelectedRows()[0], view.getMessages().get(table.getSelectedRows()[0]));
            view.setSelectedMessages(messages);
        }
    }

    public void showCell(int row, int column) {
        Rectangle rect = table.getCellRect(row, column, true);
        table.scrollRectToVisible(rect);
        table.clearSelection();
        table.setRowSelectionInterval(row, row);
        table.getModel().notifyAll();
    }
}
