package org.freelords.util;

import java.util.LinkedList;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class EdiTable {

    /** The underlying swt table */
    private Table table;

    /** The preferred minimal width of a column, also initial width */
    private static final int PREF_MIN_WIDTH = 200;

    /** Constructor setting the table as a child of a Composite and initial
	 * column's names (number of names determines the number of columns) */
    public EdiTable(Composite parent, List<String> columns) {
        table = new Table(parent, SWT.BORDER);
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        for (String s : columns) {
            TableColumn column = new TableColumn(table, SWT.NONE);
            column.setText(s);
            column.setWidth(PREF_MIN_WIDTH);
        }
        table.setBounds(table.getParent().getClientArea());
    }

    /** Fills the table with data from a TableList */
    public void fromTableList(TableList<String> data) {
        if (data.numColumns() != table.getColumnCount()) {
            return;
        }
        clear();
        for (int i = 0; i < data.numRows(); i++) {
            TableItem item = new TableItem(table, SWT.NONE);
            int j = 0;
            for (String s : data.getRow(i)) {
                item.setText(j, s);
                j++;
            }
        }
        for (TableColumn column : table.getColumns()) {
            column.pack();
            column.setWidth(Math.max(column.getWidth(), PREF_MIN_WIDTH));
        }
    }

    /** Extracts data from the table into a TableList */
    public TableList<String> toTableList() {
        TableList<String> list = new TableList<String>();
        int nc = table.getColumnCount();
        LinkedList<String> row;
        for (TableItem item : table.getItems()) {
            row = new LinkedList<String>();
            for (int i = 0; i < nc; i++) {
                row.add(item.getText(i));
            }
            list.addRow(row);
        }
        return list;
    }

    /** Returns the widget */
    public Composite getWidget() {
        return table;
    }

    /** Removes all items */
    private void clear() {
        table.removeAll();
    }

    /** Disposes the table */
    public void dispose() {
        table.dispose();
    }

    /** */
    public void activateListeners() {
        final TableEditor editor = new TableEditor(table);
        editor.horizontalAlignment = SWT.LEFT;
        editor.grabHorizontal = true;
        table.addMouseListener(new MouseListener() {

            public void mouseDoubleClick(MouseEvent e) {
            }

            public void mouseDown(MouseEvent e) {
                int mouseButton = MouseButtons.getVirtualMouseButton(e);
                if (mouseButton == 2) {
                    final int y = e.y;
                    final Menu menu = new Menu(table);
                    final MenuItem insert = new MenuItem(menu, SWT.PUSH);
                    insert.setText("Insert row");
                    final MenuItem delete = new MenuItem(menu, SWT.PUSH);
                    delete.setText("Delete row");
                    SelectionListener listener = new SelectionListener() {

                        public void widgetDefaultSelected(SelectionEvent e) {
                        }

                        public void widgetSelected(SelectionEvent e) {
                            int index = table.getTopIndex();
                            int i = index;
                            while (i < table.getItemCount()) {
                                final TableItem item = table.getItem(i);
                                Rectangle rect = item.getBounds(0);
                                if (y > rect.y && y < rect.y + rect.height) {
                                    index = i;
                                    if (e.widget == delete) {
                                        table.remove(index);
                                    }
                                    break;
                                }
                                i++;
                            }
                            if (e.widget == insert) {
                                TableItem line = new TableItem(table, SWT.NONE, index);
                                line.setText(0, "key");
                                line.setText(1, "value");
                            }
                        }
                    };
                    insert.addSelectionListener(listener);
                    delete.addSelectionListener(listener);
                    table.setMenu(menu);
                }
                if (mouseButton == 1) {
                    Rectangle clientArea = table.getClientArea();
                    Point pt = new Point(e.x, e.y);
                    int index = table.getTopIndex();
                    while (index < table.getItemCount()) {
                        boolean visible = false;
                        final TableItem item = table.getItem(index);
                        for (int i = 0; i < table.getColumnCount(); i++) {
                            Rectangle rect = item.getBounds(i);
                            if (rect.contains(pt)) {
                                final int column = i;
                                final Text text = new Text(table, SWT.NONE);
                                Listener textListener = new Listener() {

                                    public void handleEvent(final Event e) {
                                        switch(e.type) {
                                            case SWT.FocusOut:
                                                item.setText(column, text.getText());
                                                text.dispose();
                                                break;
                                            case SWT.Traverse:
                                                switch(e.detail) {
                                                    case SWT.TRAVERSE_RETURN:
                                                        item.setText(column, text.getText());
                                                        table.getColumn(column).pack();
                                                    case SWT.TRAVERSE_ESCAPE:
                                                        text.dispose();
                                                        e.doit = false;
                                                }
                                                break;
                                        }
                                    }
                                };
                                text.addListener(SWT.FocusOut, textListener);
                                text.addListener(SWT.Traverse, textListener);
                                editor.setEditor(text, item, i);
                                text.setText(item.getText(i));
                                text.selectAll();
                                text.setFocus();
                                return;
                            }
                            if (!visible && rect.intersects(clientArea)) {
                                visible = true;
                            }
                        }
                        if (!visible) {
                            return;
                        }
                        index++;
                    }
                }
            }

            public void mouseUp(MouseEvent e) {
            }
        });
    }
}
