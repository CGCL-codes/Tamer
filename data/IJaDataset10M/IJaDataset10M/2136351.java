package org.eclipse.swt.snippets;

import org.eclipse.swt.widgets.*;

public class Snippet27 {

    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setMinimized(true);
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        display.dispose();
    }
}
