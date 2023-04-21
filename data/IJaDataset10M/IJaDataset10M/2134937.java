package org.eclipse.swt.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

public class Snippet35 {

    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        Table table = new Table(shell, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        for (int i = 0; i < 12; i++) {
            TableItem item = new TableItem(table, 0);
            item.setText("Item " + i);
        }
        table.setSize(100, 100);
        shell.setSize(200, 200);
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        display.dispose();
    }
}
