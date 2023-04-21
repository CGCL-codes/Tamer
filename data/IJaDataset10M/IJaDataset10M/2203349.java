package com.arturkula.nauka.sashki;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class SashFormSticker {

    public static void main(final String[] args) {
        final Display display = new Display();
        final Shell shell = new Shell(display);
        shell.setText("Sash One");
        shell.setLayout(new FormLayout());
        final Sash sash = new Sash(shell, SWT.VERTICAL);
        FormData data = new FormData();
        data.top = new FormAttachment(0, 0);
        data.bottom = new FormAttachment(100, 0);
        data.left = new FormAttachment(50, 0);
        sash.setLayoutData(data);
        final Text one = new Text(shell, SWT.BORDER);
        data = new FormData();
        data.top = new FormAttachment(0, 0);
        data.bottom = new FormAttachment(100, 0);
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(sash, 0);
        one.setLayoutData(data);
        final Text two = new Text(shell, SWT.BORDER);
        data = new FormData();
        data.top = new FormAttachment(0, 0);
        data.bottom = new FormAttachment(100, 0);
        data.left = new FormAttachment(sash, 0);
        data.right = new FormAttachment(100, 0);
        two.setLayoutData(data);
        sash.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent event) {
                ((FormData) sash.getLayoutData()).left = new FormAttachment(0, event.x);
                sash.getParent().layout();
            }
        });
        shell.pack();
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }
}
