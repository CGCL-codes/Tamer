package uvw.gui.monitor;

import org.eclipse.swt.widgets.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.events.*;
import uvw.gui.*;
import uvw.virtual.monitor.MonitorManager;

public class MonitorDialog {

    public Shell dialogShell;

    class AlertBox {

        public AlertBox(String msg) {
            MessageBox errorBox = new MessageBox(dialogShell, SWT.ICON_ERROR | SWT.RETRY);
            errorBox.setMessage(msg);
            errorBox.open();
        }
    }

    public MonitorDialog(final Shell parentShell, final MonitorManager monMgr) {
        dialogShell = new Shell(parentShell, SWT.DIALOG_TRIM);
        dialogShell.setText("Monitoring");
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 1;
        dialogShell.setLayout(gridLayout);
        Group typeComp = new Group(dialogShell, SWT.BORDER);
        typeComp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        typeComp.setText("Choose a monitoring type:");
        typeComp.setLayout(new FillLayout(SWT.VERTICAL));
        final Button oneFileTypeBtn = new Button(typeComp, SWT.RADIO);
        oneFileTypeBtn.setText("one file");
        final Button allFileTypeBtn = new Button(typeComp, SWT.RADIO);
        allFileTypeBtn.setText("all files");
        oneFileTypeBtn.setSelection(true);
        final Composite oneFileComp = new Composite(dialogShell, SWT.BORDER);
        gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        oneFileComp.setLayout(gridLayout);
        oneFileComp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        Label oneFilenameLabel = new Label(oneFileComp, SWT.NULL);
        oneFilenameLabel.setText("Filename");
        GridData gridData = new GridData();
        gridData.widthHint = 80;
        oneFilenameLabel.setLayoutData(gridData);
        final Text oneFilenameText = new Text(oneFileComp, SWT.BORDER | SWT.SINGLE);
        oneFilenameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        final Button oneProcessChkbox = new Button(oneFileComp, SWT.CHECK);
        oneProcessChkbox.setText("Limit monitoring to one process");
        gridData = new GridData();
        gridData.horizontalSpan = 2;
        oneProcessChkbox.setLayoutData(gridData);
        final Combo oneProcessCombo = new Combo(oneFileComp, SWT.DROP_DOWN | SWT.READ_ONLY);
        oneProcessCombo.setItems(new String[] { "process name", "PID #" });
        oneProcessCombo.select(0);
        gridData = new GridData();
        gridData.widthHint = 120;
        oneProcessCombo.setLayoutData(gridData);
        final Text oneProcessText = new Text(oneFileComp, SWT.BORDER | SWT.SINGLE);
        oneProcessText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        final Button oneProcessChildrenChkbox = new Button(oneFileComp, SWT.CHECK);
        oneProcessChildrenChkbox.setText("Include process children");
        gridData = new GridData();
        gridData.horizontalSpan = 2;
        oneProcessChildrenChkbox.setLayoutData(gridData);
        oneProcessChkbox.setSelection(false);
        oneProcessCombo.setEnabled(false);
        oneProcessText.setEnabled(false);
        oneProcessChildrenChkbox.setEnabled(false);
        final Composite allFileComp = new Composite(dialogShell, SWT.BORDER);
        gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        allFileComp.setLayout(gridLayout);
        allFileComp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        final Combo processCombo = new Combo(allFileComp, SWT.DROP_DOWN | SWT.READ_ONLY);
        processCombo.setItems(new String[] { "process name", "PID #" });
        processCombo.select(0);
        gridData = new GridData();
        gridData.widthHint = 120;
        processCombo.setLayoutData(gridData);
        final Text processText = new Text(allFileComp, SWT.BORDER | SWT.SINGLE);
        processText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        final Button processChildrenChkbox = new Button(allFileComp, SWT.CHECK);
        processChildrenChkbox.setText("Include process children");
        gridData = new GridData();
        gridData.horizontalSpan = 2;
        processChildrenChkbox.setLayoutData(gridData);
        allFileComp.setVisible(false);
        Composite buttonsComp = new Composite(dialogShell, SWT.NULL);
        gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        buttonsComp.setLayout(gridLayout);
        Button okButton = new Button(buttonsComp, SWT.PUSH);
        okButton.setText("Ok");
        Button cancelButton = new Button(buttonsComp, SWT.PUSH);
        cancelButton.setText("Cancel");
        oneProcessChkbox.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                if (oneProcessChkbox.getSelection()) {
                    oneProcessCombo.setEnabled(true);
                    oneProcessText.setEnabled(true);
                    oneProcessChildrenChkbox.setEnabled(true);
                } else {
                    oneProcessCombo.setEnabled(false);
                    oneProcessText.setEnabled(false);
                    oneProcessChildrenChkbox.setEnabled(false);
                }
            }
        });
        allFileTypeBtn.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                oneFileComp.setVisible(false);
                allFileComp.setVisible(true);
                dialogShell.layout();
            }
        });
        oneFileTypeBtn.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                allFileComp.setVisible(false);
                oneFileComp.setVisible(true);
                dialogShell.layout();
            }
        });
        okButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                if (oneFileTypeBtn.getSelection()) {
                    if (oneFilenameText.getCharCount() == 0) {
                        new AlertBox("You must specify a file name");
                        return;
                    }
                    if (oneProcessChkbox.getSelection()) {
                        if (oneProcessCombo.getSelectionIndex() == 0) {
                            MonitorContainer mc = new MonitorContainer(parentShell, new String("Monitoring " + oneFilenameText.getText() + " from process " + oneProcessText.getText()));
                            monMgr.newMonitor1File1Proc(mc, oneFilenameText.getText(), oneProcessText.getText(), oneProcessChildrenChkbox.getSelection());
                        } else {
                            Long pid;
                            try {
                                pid = new Long(oneProcessText.getText());
                            } catch (NumberFormatException exc) {
                                new AlertBox("You choose to enter a pid number, please use only integer values");
                                return;
                            }
                            MonitorContainer mc = new MonitorContainer(parentShell, new String("Monitoring " + oneFilenameText.getText() + " from pid #" + pid));
                            monMgr.newMonitor1File1Pid(mc, oneFilenameText.getText(), pid, oneProcessChildrenChkbox.getSelection());
                        }
                    } else {
                        MonitorContainer mc = new MonitorContainer(parentShell, new String("Monitoring " + oneFilenameText.getText()));
                        monMgr.newMonitor1FileAllProc(mc, oneFilenameText.getText());
                    }
                } else {
                    if (processCombo.getSelectionIndex() == 0) {
                        MonitorContainer mc = new MonitorContainer(parentShell, new String("Monitoring " + processText.getText()));
                        monMgr.newMonitorAllFile1Proc(mc, processText.getText(), oneProcessChildrenChkbox.getSelection());
                    } else {
                        Long pid;
                        try {
                            pid = new Long(processText.getText());
                        } catch (NumberFormatException exc) {
                            new AlertBox("You choose to enter a pid number, please use only integer values");
                            return;
                        }
                        MonitorContainer mc = new MonitorContainer(parentShell, new String("Monitoring pid #" + pid));
                        monMgr.newMonitorAllFile1Pid(mc, pid, oneProcessChildrenChkbox.getSelection());
                    }
                }
                dialogShell.dispose();
            }
        });
        cancelButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                dialogShell.dispose();
            }
        });
        dialogShell.pack();
        dialogShell.open();
    }
}
