package org.eclipse.swt.snippets;

import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;

public class Snippet290 {

    public static void main(String[] args) {
        final Display display = new Display();
        final Shell shell = new Shell(display);
        shell.addMouseListener(new MouseAdapter() {

            public void mouseUp(MouseEvent e) {
                if (e.count == 1) {
                    System.out.println("Mouse up");
                }
            }

            public void mouseDoubleClick(MouseEvent e) {
                System.out.println("Double-click");
            }
        });
        shell.setBounds(10, 10, 200, 200);
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        display.dispose();
    }
}
