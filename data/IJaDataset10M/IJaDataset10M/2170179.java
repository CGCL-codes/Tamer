package com.eric.ref.browser;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.browser.*;

public class Snippet173 {

    static final int BROWSER_STYLE = SWT.NONE;

    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("Main Window");
        shell.setLayout(new FillLayout());
        final Browser browser;
        try {
            browser = new Browser(shell, BROWSER_STYLE);
        } catch (SWTError e) {
            System.out.println("Could not instantiate Browser: " + e.getMessage());
            display.dispose();
            return;
        }
        initialize(display, browser);
        shell.open();
        browser.setUrl("http://www.cnn.com");
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        display.dispose();
    }

    static void initialize(final Display display, Browser browser) {
        browser.addOpenWindowListener(new OpenWindowListener() {

            public void open(WindowEvent event) {
                Shell shell = new Shell(display);
                shell.setText("New Window");
                shell.setLayout(new FillLayout());
                Browser browser = new Browser(shell, BROWSER_STYLE);
                initialize(display, browser);
                event.browser = browser;
            }
        });
        browser.addVisibilityWindowListener(new VisibilityWindowListener() {

            public void hide(WindowEvent event) {
                Browser browser = (Browser) event.widget;
                Shell shell = browser.getShell();
                shell.setVisible(false);
            }

            public void show(WindowEvent event) {
                Browser browser = (Browser) event.widget;
                final Shell shell = browser.getShell();
                boolean isOSX = SWT.getPlatform().equals("cocoa") || SWT.getPlatform().equals("carbon");
                if (!event.addressBar && !event.statusBar && !event.toolBar && (!event.menuBar || isOSX)) {
                    System.out.println("Popup blocked.");
                    event.display.asyncExec(new Runnable() {

                        public void run() {
                            shell.close();
                        }
                    });
                    return;
                }
                if (event.location != null) shell.setLocation(event.location);
                if (event.size != null) {
                    Point size = event.size;
                    shell.setSize(shell.computeSize(size.x, size.y));
                }
                shell.open();
            }
        });
        browser.addCloseWindowListener(new CloseWindowListener() {

            public void close(WindowEvent event) {
                Browser browser = (Browser) event.widget;
                Shell shell = browser.getShell();
                shell.close();
            }
        });
    }
}
