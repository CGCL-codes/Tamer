package com.richclientgui.toolbox.samples.googlemap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import com.richclientgui.toolbox.googleMap.GoogleMapComposite;

public class GoogleMapDemo {

    public static void main(String[] args) {
        final Display display = new Display();
        final Shell shell = getShell(display);
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    public static Shell getShell(Display display) {
        final Shell shell = new Shell(display);
        shell.setText("Google Map Demo");
        shell.setLayout(new GridLayout());
        new GoogleMapDemo(shell);
        shell.setLocation(Display.getDefault().getBounds().width / 2 - 100, Display.getDefault().getBounds().height / 2 - 50);
        shell.open();
        shell.pack();
        shell.setSize(230, 300);
        return shell;
    }

    public GoogleMapDemo(Shell shell) {
        final Composite composite = new Composite(shell, SWT.NONE);
        composite.setLayout(new GridLayout(1, false));
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        composite.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
        createMapRegion(composite);
    }

    private void createMapRegion(Composite parent) {
        final GoogleMapComposite map = new GoogleMapComposite(parent, SWT.NONE);
        map.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    }
}
