package org.eclipse.swt.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

public class Snippet51 {

    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        Table table = new Table(shell, SWT.BORDER | SWT.MULTI);
        table.setSize(200, 200);
        for (int i = 0; i < 128; i++) {
            TableItem item = new TableItem(table, SWT.NONE);
            item.setText("Item " + i);
        }
        table.setTopIndex(95);
        shell.pack();
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        display.dispose();
    }
}
