package org.eclipse.swt.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

public class Snippet238 {

    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        Composite composite = new Composite(shell, SWT.BORDER);
        composite.setSize(100, 100);
        Menu menu = new Menu(shell, SWT.POP_UP);
        MenuItem item1 = new MenuItem(menu, SWT.PUSH);
        item1.setText("Push Item");
        MenuItem item2 = new MenuItem(menu, SWT.CASCADE);
        item2.setText("Cascade Item");
        Menu subMenu = new Menu(menu);
        item2.setMenu(subMenu);
        MenuItem subItem1 = new MenuItem(subMenu, SWT.PUSH);
        subItem1.setText("Subitem 1");
        MenuItem subItem2 = new MenuItem(subMenu, SWT.PUSH);
        subItem2.setText("Subitem 2");
        composite.setMenu(menu);
        shell.setMenu(menu);
        shell.setSize(300, 300);
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        display.dispose();
    }
}
