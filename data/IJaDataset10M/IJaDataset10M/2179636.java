package org.eclipse.swt.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;

public class Snippet89 {

    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        Menu menu = new Menu(shell, SWT.POP_UP);
        for (int i = 0; i < 4; i++) {
            MenuItem item = new MenuItem(menu, SWT.RADIO);
            item.setText("Item " + i);
            item.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent e) {
                    MenuItem item = (MenuItem) e.widget;
                    if (item.getSelection()) {
                        System.out.println(item + " selected");
                    } else {
                        System.out.println(item + " unselected");
                    }
                }
            });
        }
        shell.setMenu(menu);
        shell.setSize(300, 300);
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        display.dispose();
    }
}
