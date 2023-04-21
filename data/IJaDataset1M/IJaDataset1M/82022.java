package org.tranche.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.ListSelectionModel;
import org.apache.commons.httpclient.NameValuePair;

/**
 *
 * @author James "Augie" Hill - augman85@gmail.com
 */
public class AnnotationPanel extends JPanel {

    public GenericMenuBar menuBar = new GenericMenuBar();

    private GenericMenu addMenu = new GenericMenu("Add Tag");

    private GenericMenu selectionMenu = new GenericMenu("Selection");

    private GenericTable table;

    private TagTableModel model;

    public AnnotationPanel() {
        setSize(400, 400);
        setLayout(new BorderLayout());
        {
            GenericMenuItem blankMenuItem = new GenericMenuItem("Blank");
            blankMenuItem.setMnemonic('b');
            blankMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    Thread t = new Thread() {

                        @Override
                        public void run() {
                            model.addRow("", "");
                        }
                    };
                    t.setDaemon(true);
                    t.start();
                }
            });
            addMenu.add(blankMenuItem);
            JSeparator separator = new JSeparator();
            separator.setForeground(blankMenuItem.getForeground());
            addMenu.add(separator);
            GenericMenuItem removeMenuItem = new GenericMenuItem("Remove");
            removeMenuItem.setMnemonic('r');
            removeMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    Thread t = new Thread() {

                        @Override
                        public void run() {
                            int[] rows = table.getSelectedRows();
                            for (int i = rows.length - 1; i >= 0; i--) {
                                model.rows.remove(rows[i]);
                            }
                            model.fireTableDataChanged();
                        }
                    };
                    t.setDaemon(true);
                    t.start();
                }
            });
            selectionMenu.add(removeMenuItem);
            addMenu.setMnemonic('a');
            menuBar.add(addMenu);
            selectionMenu.setMnemonic('s');
            menuBar.add(selectionMenu);
            add(menuBar, BorderLayout.NORTH);
        }
        model = new TagTableModel();
        table = new GenericTable(model, ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        GenericScrollPane scrollPane = new GenericScrollPane(table);
        scrollPane.setBorder(Styles.BORDER_NONE);
        scrollPane.setBackground(table.getTableHeader().getBackground());
        scrollPane.setVerticalScrollBar(new GenericScrollBar());
        scrollPane.setHorizontalScrollBar(new GenericScrollBar());
        add(scrollPane, BorderLayout.CENTER);
    }

    public void add(String name, String value) {
        model.addRow(name, value);
    }

    public List<NameValuePair> getNameValuePairs() {
        List<NameValuePair> returnList = new ArrayList<NameValuePair>();
        for (NameValuePair pair : model.rows) {
            if (!pair.getName().trim().equals("") && !pair.getValue().trim().equals("")) {
                returnList.add(pair);
            }
        }
        return returnList;
    }

    private class TagTableModel extends SortableTableModel {

        private final String[] headers = new String[] { "Name", "Value" };

        private final List<NameValuePair> rows = new LinkedList<NameValuePair>();

        public void addRow(String name, String value) {
            rows.add(new NameValuePair(name, value));
            sort(table.getPressedColumn());
            fireTableDataChanged();
        }

        public int getColumnCount() {
            return headers.length;
        }

        @Override
        public String getColumnName(int column) {
            if (column < getColumnCount()) {
                return headers[column];
            } else {
                return "";
            }
        }

        public NameValuePair getRow(int row) {
            return rows.get(row);
        }

        public int getRowCount() {
            return rows.size();
        }

        public Object getValueAt(int row, int column) {
            switch(column) {
                case 0:
                    return rows.get(row).getName();
                case 1:
                    return rows.get(row).getValue();
                default:
                    return null;
            }
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return true;
        }

        @Override
        public void setValueAt(Object value, int row, int col) {
            if (col == table.getColumnModel().getColumnIndex("Name")) {
                rows.get(row).setName((String) value);
                fireTableCellUpdated(row, col);
            } else if (col == table.getColumnModel().getColumnIndex("Value")) {
                rows.get(row).setValue((String) value);
                fireTableCellUpdated(row, col);
            }
        }

        public void sort(int column) {
            table.setPressedColumn(column);
            Collections.sort(rows, new StringComparator(column));
        }

        private class StringComparator implements Comparator {

            private int column;

            public StringComparator(int column) {
                this.column = column;
            }

            public int compare(Object o1, Object o2) {
                if (table.getDirection()) {
                    Object temp = o1;
                    o1 = o2;
                    o2 = temp;
                }
                if (o1 == null && o2 == null) {
                    return 0;
                } else if (o1 == null) {
                    return 1;
                } else if (o1 instanceof NameValuePair && o2 instanceof NameValuePair) {
                    if (column == 0) {
                        return ((NameValuePair) o1).getName().toLowerCase().compareTo(((NameValuePair) o2).getName().toLowerCase());
                    } else {
                        return ((NameValuePair) o1).getValue().toLowerCase().compareTo(((NameValuePair) o2).getValue().toLowerCase());
                    }
                } else {
                    return 1;
                }
            }
        }
    }
}
