package org.systemsEngineering.workbench.core.ui.editors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.custom.TableEditor;

/**
 * This class provides edit functionality for the data table enabling the user
 * to open cells for keyboard input by double clicking the corresponding cell
 * with the mouse. The input will be stored as regular string within the table
 * by pressing the enter key.
 */
class WgaTableMouseListener implements Listener {

    private Table wgaDataTable;

    private Table wgaResultTable;

    private Composite wgaComposite;

    private final TableEditor wgaDataTableEditor;

    static int selectedColumn;

    /**
         * @param table
         *                The table to which the new
         *                WgaTableMouseDoubleClickListener should be added to.
         */
    WgaTableMouseListener(Table table, Table resultTable, Composite composite) {
        wgaDataTable = table;
        wgaResultTable = resultTable;
        wgaComposite = composite;
        wgaDataTableEditor = new TableEditor(wgaDataTable);
        wgaDataTableEditor.horizontalAlignment = SWT.LEFT;
        wgaDataTableEditor.grabHorizontal = true;
    }

    public void handleEvent(Event event) {
        Rectangle clientArea = wgaDataTable.getClientArea();
        Point pt = new Point(event.x, event.y);
        int index = wgaDataTable.getTopIndex();
        while (index < wgaDataTable.getItemCount()) {
            boolean visible = false;
            final TableItem wgaDataTableItem = wgaDataTable.getItem(index);
            for (int i = 0; i < wgaDataTable.getColumnCount(); i++) {
                Rectangle rect = wgaDataTableItem.getBounds(i);
                if (rect.contains(pt)) {
                    selectedColumn = i;
                    if ((i > 0 && i % 2 == 1 && index < 2) || index > 2) {
                        switch(event.type) {
                            case (SWT.MouseDown):
                                return;
                            case (SWT.MouseDoubleClick):
                                {
                                    final Text wgaText = new Text(wgaDataTable, SWT.NONE);
                                    Listener wgaTextListener = new Listener() {

                                        public void handleEvent(final Event e) {
                                            switch(e.type) {
                                                case SWT.FocusOut:
                                                    wgaDataTableItem.setText(selectedColumn, wgaText.getText());
                                                    if (selectedColumn == 0) wgaResultTable.getItem(wgaDataTable.indexOf(wgaDataTableItem) - 2).setText(selectedColumn, wgaText.getText());
                                                    wgaText.dispose();
                                                    break;
                                                case SWT.Traverse:
                                                    switch(e.detail) {
                                                        case SWT.TRAVERSE_RETURN:
                                                            wgaDataTableItem.setText(selectedColumn, wgaText.getText());
                                                            if (selectedColumn == 0) wgaResultTable.getItem(wgaDataTable.indexOf(wgaDataTableItem) - 2).setText(selectedColumn, wgaText.getText());
                                                        case SWT.TRAVERSE_ESCAPE:
                                                            wgaText.dispose();
                                                            e.doit = false;
                                                    }
                                                    break;
                                            }
                                            wgaDataTable.getColumn(selectedColumn).pack();
                                            wgaComposite.setSize(wgaComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
                                        }
                                    };
                                    wgaText.addListener(SWT.FocusOut, wgaTextListener);
                                    wgaText.addListener(SWT.Traverse, wgaTextListener);
                                    wgaDataTableEditor.setEditor(wgaText, wgaDataTableItem, i);
                                    wgaText.setText(wgaDataTableItem.getText(i));
                                    wgaText.selectAll();
                                    wgaText.setFocus();
                                    return;
                                }
                        }
                    }
                }
                if (!visible && rect.intersects(clientArea)) {
                    visible = true;
                }
            }
            if (!visible) return;
            index++;
        }
    }
}
