package org.ncsa.foodlog.rcp.daily.records;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet96 {

    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setLayout(new GridLayout());
        final Table table = new Table(shell, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
        table.setLayoutData(new GridData(GridData.FILL_BOTH));
        TableColumn column1 = new TableColumn(table, SWT.NONE);
        TableColumn column2 = new TableColumn(table, SWT.NONE);
        TableColumn column3 = new TableColumn(table, SWT.NONE);
        for (int i = 0; i < 100; i++) {
            TableItem item = new TableItem(table, SWT.NONE);
            item.setText(new String[] { "cell " + i + " 0", "cell " + i + " 1", "cell " + i + " 2" });
        }
        column1.pack();
        column2.pack();
        column3.pack();
        final TableCursor cursor = new TableCursor(table, SWT.NONE);
        final ControlEditor editor = new ControlEditor(cursor);
        editor.grabHorizontal = true;
        editor.grabVertical = true;
        cursor.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                table.setSelection(new TableItem[] { cursor.getRow() });
            }

            public void widgetDefaultSelected(SelectionEvent e) {
                final Text text = new Text(cursor, SWT.NONE);
                TableItem row = cursor.getRow();
                int column = cursor.getColumn();
                text.setText(row.getText(column));
                text.addKeyListener(new KeyAdapter() {

                    public void keyPressed(KeyEvent e) {
                        if (e.character == SWT.CR) {
                            TableItem row = cursor.getRow();
                            int column = cursor.getColumn();
                            row.setText(column, text.getText());
                            text.dispose();
                        }
                        if (e.character == SWT.ESC) {
                            text.dispose();
                        }
                    }
                });
                text.addFocusListener(new FocusAdapter() {

                    public void focusLost(FocusEvent e) {
                        text.dispose();
                    }
                });
                editor.setEditor(text);
                text.setFocus();
            }
        });
        cursor.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                if (e.keyCode == SWT.CTRL || e.keyCode == SWT.SHIFT || (e.stateMask & SWT.CONTROL) != 0 || (e.stateMask & SWT.SHIFT) != 0) {
                    cursor.setVisible(false);
                }
            }
        });
        cursor.addMouseListener(new MouseAdapter() {

            public void mouseDown(MouseEvent e) {
                final Text text = new Text(cursor, SWT.NONE);
                TableItem row = cursor.getRow();
                int column = cursor.getColumn();
                text.setText(row.getText(column));
                text.addKeyListener(new KeyAdapter() {

                    public void keyPressed(KeyEvent e) {
                        if (e.character == SWT.CR) {
                            TableItem row = cursor.getRow();
                            int column = cursor.getColumn();
                            row.setText(column, text.getText());
                            text.dispose();
                        }
                        if (e.character == SWT.ESC) {
                            text.dispose();
                        }
                    }
                });
                text.addFocusListener(new FocusAdapter() {

                    public void focusLost(FocusEvent e) {
                        text.dispose();
                    }
                });
                editor.setEditor(text);
                text.setFocus();
            }
        });
        table.addKeyListener(new KeyAdapter() {

            public void keyReleased(KeyEvent e) {
                if (e.keyCode == SWT.CONTROL && (e.stateMask & SWT.SHIFT) != 0) return;
                if (e.keyCode == SWT.SHIFT && (e.stateMask & SWT.CONTROL) != 0) return;
                if (e.keyCode != SWT.CONTROL && (e.stateMask & SWT.CONTROL) != 0) return;
                if (e.keyCode != SWT.SHIFT && (e.stateMask & SWT.SHIFT) != 0) return;
                TableItem[] selection = table.getSelection();
                TableItem row = (selection.length == 0) ? table.getItem(table.getTopIndex()) : selection[0];
                table.showItem(row);
                cursor.setSelection(row, 0);
                cursor.setVisible(true);
                cursor.setFocus();
            }
        });
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        display.dispose();
    }
}
