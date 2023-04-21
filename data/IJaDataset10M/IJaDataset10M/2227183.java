package jfaceDialog;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.jface.dialogs.*;

public class MessageDialog2Class {

    private Text text;

    private Label label;

    public MessageDialog2Class() {
        final Display display = Display.getDefault();
        final Shell shell = new Shell();
        shell.setSize(465, 200);
        shell.setText("MessageDialogʵ��");
        shell.setLayout(new GridLayout(5, false));
        text = new Text(shell, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        GridData data = new GridData(GridData.FILL_BOTH);
        data.horizontalSpan = 5;
        text.setLayoutData(data);
        Button confirm = new Button(shell, SWT.NONE);
        confirm.setText("Confirm");
        GridData gridconfirm = new GridData();
        gridconfirm.widthHint = 85;
        gridconfirm.heightHint = 25;
        confirm.setLayoutData(gridconfirm);
        confirm.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent event) {
                boolean b = MessageDialog.openConfirm(shell, "Confirm", text.getText());
                label.setText("Returned " + Boolean.toString(b));
            }
        });
        Button information = new Button(shell, SWT.NONE);
        information.setText("Information");
        GridData gridinformation = new GridData();
        gridinformation.widthHint = 85;
        gridinformation.heightHint = 25;
        information.setLayoutData(gridinformation);
        information.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent event) {
                MessageDialog.openInformation(shell, "Information", text.getText());
                label.setText("product prompt information");
            }
        });
        Button question = new Button(shell, SWT.NONE);
        question.setText("Question");
        GridData gridquestion = new GridData();
        gridquestion.widthHint = 85;
        gridquestion.heightHint = 25;
        question.setLayoutData(gridquestion);
        question.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent event) {
                boolean b = MessageDialog.openQuestion(shell, "Question", text.getText());
                label.setText("Returned " + Boolean.toString(b));
            }
        });
        Button error = new Button(shell, SWT.NONE);
        error.setText("Error");
        GridData griderror = new GridData();
        griderror.widthHint = 85;
        griderror.heightHint = 25;
        error.setLayoutData(griderror);
        error.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent event) {
                MessageDialog.openError(shell, "Error", text.getText());
                label.setText("product error information");
            }
        });
        Button warning = new Button(shell, SWT.NONE);
        warning.setText("Warning");
        GridData gridwarning = new GridData();
        gridwarning.widthHint = 85;
        gridwarning.heightHint = 25;
        warning.setLayoutData(gridwarning);
        warning.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent event) {
                MessageDialog.openWarning(shell, "Warning", text.getText());
                label.setText("product waring information");
            }
        });
        label = new Label(shell, SWT.NONE);
        label.setText("It is not Information");
        GridData datalabel = new GridData(GridData.FILL_HORIZONTAL);
        datalabel.horizontalSpan = 5;
        datalabel.heightHint = 20;
        label.setLayoutData(datalabel);
        shell.open();
        shell.layout();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
    }

    public static void main(String[] args) {
        new MessageDialog2Class();
    }
}
