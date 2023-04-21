package org.eclipse.swt.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.browser.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet303 {

    public static void main(String[] args) {
        final String SCRIPT = "document.onmousedown = function(e) {if (!e) {e = window.event;} if (e) {window.status = 'MOUSEDOWN: ' + e.clientX + ',' + e.clientY;}}";
        Display display = new Display();
        final Shell shell = new Shell(display);
        shell.setLayout(new FillLayout());
        final Browser browser;
        try {
            browser = new Browser(shell, SWT.NONE);
        } catch (SWTError e) {
            System.out.println("Could not instantiate Browser: " + e.getMessage());
            display.dispose();
            return;
        }
        browser.addProgressListener(new ProgressAdapter() {

            public void completed(ProgressEvent event) {
                browser.execute(SCRIPT);
            }
        });
        browser.addStatusTextListener(new StatusTextListener() {

            public void changed(StatusTextEvent event) {
                if (event.text.startsWith("MOUSEDOWN: ")) {
                    System.out.println(event.text);
                    browser.execute("window.status = '';");
                }
            }
        });
        browser.setUrl("eclipse.org");
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        display.dispose();
    }
}
