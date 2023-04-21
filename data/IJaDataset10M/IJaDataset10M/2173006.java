package br.gov.demoiselle.eclipse.main.ui.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class DialogMessage extends Dialog {

    private String[] messages;

    ;

    private String title = "";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public DialogMessage(Shell parent) {
        this(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
    }

    public DialogMessage(Shell parent, int style) {
        super(parent, style);
    }

    public String[] getMessage() {
        return messages;
    }

    public void setMessages(String[] messages) {
        this.messages = messages;
    }

    public String open() {
        Shell shell = new Shell(getParent(), getStyle());
        shell.setText(getText());
        createContents(shell);
        shell.setBounds(400, 300, 0, 5000);
        shell.pack();
        shell.open();
        Display display = getParent().getDisplay();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        return null;
    }

    private void createContents(final Shell shell) {
        shell.setLayout(new GridLayout());
        shell.setText(getTitle());
        Group group = new Group(shell, SWT.NONE);
        group.setLayout(new GridLayout());
        for (int i = 0; i < messages.length; i++) {
            if (messages[i] != null && !messages[i].equals("")) {
                Label label = new Label(group, SWT.CENTER);
                label.setText(messages[i]);
            }
        }
        Button okButton = new Button(group, SWT.NONE);
        final GridData gd_buttonGroup = new GridData(SWT.FILL, SWT.CENTER, true, false);
        okButton.setLayoutData(gd_buttonGroup);
        okButton.setText("OK");
        okButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent event) {
                shell.close();
            }
        });
        shell.setDefaultButton(okButton);
    }
}
