package org.eclipse.swt.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;

public class Snippet183 {

    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        Link link = new Link(shell, SWT.NONE);
        String text = "The SWT component is designed to provide <a>efficient</a>, <a>portable</a> <a href=\"native\">access to the user-interface facilities of the operating systems</a> on which it is implemented.";
        link.setText(text);
        link.setSize(400, 400);
        link.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                System.out.println("Selection: " + event.text);
            }
        });
        shell.pack();
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        display.dispose();
    }
}
