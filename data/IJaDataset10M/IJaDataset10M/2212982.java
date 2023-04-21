package org.eclipse.swt.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

public class Snippet14 {

    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setSize(100, 100);
        shell.addListener(SWT.MouseEnter, new Listener() {

            public void handleEvent(Event e) {
                System.out.println("ENTER");
            }
        });
        shell.addListener(SWT.MouseExit, new Listener() {

            public void handleEvent(Event e) {
                System.out.println("EXIT");
            }
        });
        shell.addListener(SWT.MouseHover, new Listener() {

            public void handleEvent(Event e) {
                System.out.println("HOVER");
            }
        });
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        display.dispose();
    }
}
