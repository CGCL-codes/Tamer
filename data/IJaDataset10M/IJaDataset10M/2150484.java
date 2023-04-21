package org.eclipse.swt.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet49 {

    public static void main(String[] args) {
        Display display = new Display();
        final Shell shell = new Shell(display);
        final ToolBar toolBar = new ToolBar(shell, SWT.WRAP);
        for (int i = 0; i < 12; i++) {
            ToolItem item = new ToolItem(toolBar, SWT.PUSH);
            item.setText("Item " + i);
        }
        shell.addListener(SWT.Resize, new Listener() {

            public void handleEvent(Event e) {
                Rectangle rect = shell.getClientArea();
                Point size = toolBar.computeSize(rect.width, SWT.DEFAULT);
                toolBar.setSize(size);
            }
        });
        toolBar.pack();
        shell.pack();
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        display.dispose();
    }
}
