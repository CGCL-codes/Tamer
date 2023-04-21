package gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;

public class ContinueDialog {

    private final Shell parentShell;

    private Shell continueDialogShell;

    private boolean continueFlag;

    public ContinueDialog(Shell parentShell) {
        this.parentShell = parentShell;
        createSShell();
    }

    private void createSShell() {
        GridData gridData1 = new org.eclipse.swt.layout.GridData();
        gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        gridData1.grabExcessHorizontalSpace = false;
        gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        GridData gridData = new org.eclipse.swt.layout.GridData();
        gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        gridData.grabExcessHorizontalSpace = false;
        gridData.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        gridLayout.verticalSpacing = 5;
        gridLayout.marginWidth = 40;
        gridLayout.marginHeight = 40;
        gridLayout.makeColumnsEqualWidth = true;
        gridLayout.horizontalSpacing = 20;
        continueDialogShell = new Shell(SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
        continueDialogShell.setText("Continue tournament?");
        continueDialogShell.setLayout(gridLayout);
        continueDialogShell.setSize(new org.eclipse.swt.graphics.Point(404, 166));
        final Button btnContinue = new Button(continueDialogShell, SWT.NONE);
        btnContinue.setText("Continue tournament");
        btnContinue.setLayoutData(gridData1);
        Button btnStartFromBeginning = new Button(continueDialogShell, SWT.PUSH);
        btnStartFromBeginning.setText("Start from beginning");
        btnStartFromBeginning.setLayoutData(gridData);
        Listener listener = new Listener() {

            public void handleEvent(Event event) {
                continueFlag = event.widget == btnContinue;
                continueDialogShell.close();
            }
        };
        btnContinue.addListener(SWT.Selection, listener);
        btnStartFromBeginning.addListener(SWT.Selection, listener);
        continueDialogShell.setSize(continueDialogShell.computeSize(SWT.DEFAULT, SWT.DEFAULT));
    }

    public boolean doContinue() {
        continueDialogShell.open();
        while (!continueDialogShell.isDisposed()) {
            if (!parentShell.getDisplay().readAndDispatch()) {
                parentShell.getDisplay().sleep();
            }
        }
        continueDialogShell.dispose();
        return continueFlag;
    }
}
