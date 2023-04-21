package org.designerator.color.test;

import org.designerator.color.dialog.ColorPopup;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class ColorPopupTest {

    public static void main(String[] args) {
        final Display display = new Display();
        final Shell shell = new Shell(display);
        shell.setLayout(new GridLayout(1, false));
        final Button button = new Button(shell, SWT.PUSH);
        button.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        button.setText("color");
        button.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                ColorPopup shapesPopup = new ColorPopup(shell, SWT.Resize, true, true, true, false, false, null, null);
                shapesPopup.open();
                shapesPopup.setBackgroundColor(display.getSystemColor(SWT.COLOR_BLACK));
            }
        });
        shell.setSize(400, 400);
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        display.dispose();
    }
}
