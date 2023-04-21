package gui;

import java.io.File;
import java.io.FileNotFoundException;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import auxiliary_classes.Notifier;

/**
 * Provides a simple GUI for collecting error information from the user and send it to the developers.
 * 
 * This GUI is meant to be used just after an error occurs.
 * It has two main input components: a styled text box (for multi-lined, styled explanations), and a smaller, non-styled text box for a return address.
 * In addition to this, each errorReportGUI has three control buttons: "Show me the error!", "Don't Send", and "Send Error Report".
 * 		"Show me the error!": Instantiates a SimpleDialog object with the error information, which is passed into the constructor. This option is mainly for savvy programmers who have access to the source code.
 * 		"Don't Send": Fairly self-explanatory; just closes the shell.
 * 		"Send Error Report": Compiles an error report with a UUID from the notifier, version, error itself, and the user-provided explanation and email address.
 * After "Send Error Report" is selected and executes, a SimpleDialog containing a confirmation or error message is instantiated.
 * In the former case, this dialog contains a UUID for future reference of the issue.
 * On the other hand, the latter case provides simple troubleshooting tips, an error message, and the developer email addresses for manual error transmission.
 * 
 * @author Lane Aasen
 */
public class ErrorReportGUI extends Dialog {

    protected Object result;

    protected Shell shlErrorReport;

    private Text txtEmailAddress;

    private Notifier notifier;

    private String subjectStem;

    private File errorFile;

    private String version;

    /**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
    public ErrorReportGUI(Shell parent, int style, Notifier notifier, String subjectStem, File errorFile, String version) {
        super(parent, style);
        this.notifier = notifier;
        this.subjectStem = subjectStem;
        this.errorFile = errorFile;
        this.version = version;
        setText("Error Report");
    }

    /**
	 * Open the dialog.
	 * @return the result
	 */
    public Object open() {
        createContents();
        shlErrorReport.open();
        shlErrorReport.layout();
        Display display = getParent().getDisplay();
        while (!shlErrorReport.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        return result;
    }

    /**
	 * Create contents of the dialog.
	 */
    private void createContents() {
        shlErrorReport = new Shell(getParent(), SWT.DIALOG_TRIM);
        shlErrorReport.setSize(450, 310);
        shlErrorReport.setText("Error Report");
        Group grpReportError = new Group(shlErrorReport, SWT.NONE);
        grpReportError.setText("Report An Error");
        grpReportError.setBounds(10, 10, 428, 261);
        Label lblPrompt = new Label(grpReportError, SWT.NONE);
        lblPrompt.setFont(SWTResourceManager.getFont("Sans", 8, SWT.NORMAL));
        lblPrompt.setBounds(10, 20, 395, 26);
        lblPrompt.setText("Please precisely describle the error, i.e. what were you doing at the time \nand what exactly happened.");
        final StyledText stxtExplanation = new StyledText(grpReportError, SWT.BORDER);
        stxtExplanation.setBounds(10, 52, 408, 132);
        txtEmailAddress = new Text(grpReportError, SWT.BORDER);
        txtEmailAddress.setText("Email Address [Optional]");
        txtEmailAddress.setBounds(10, 190, 408, 26);
        Button btnSendReport = new Button(grpReportError, SWT.NONE);
        btnSendReport.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent arg0) {
                int lines = stxtExplanation.getLineCount();
                String UUID = Notifier.generateUUID();
                String errorReport = "UUID: " + UUID + "\nVersion: " + version;
                if (txtEmailAddress.getText().contains("@")) {
                    errorReport = errorReport + "\nReturn Address: " + txtEmailAddress.getText().trim();
                }
                errorReport = errorReport + "\n\nExplanation: \n";
                for (int i = 0; i < lines; i++) {
                    errorReport = errorReport + "    " + stxtExplanation.getLine(i) + "\n";
                }
                try {
                    errorReport = errorReport + "\nError: \n" + Notifier.fileToString(errorFile);
                } catch (FileNotFoundException fnfe) {
                    fnfe.printStackTrace();
                }
                boolean errored = false;
                try {
                    notifier.sendGmailSSL(subjectStem + ": " + UUID, errorReport);
                } catch (Exception e) {
                    errored = true;
                    String developers = "";
                    for (int i = 0; i < notifier.getRecipients().length; i++) developers = developers + notifier.getRecipients()[i] + "\n";
                    String errorMessage = "Transmission of your error report has failed.\n" + "Please check your internet and try again.\n" + "If this does not work, send the below error\n" + "message to the provided email addresses.\n\n" + developers + "\n\nError: " + "\nWe are very sorry for the inconvenience";
                    SimpleDialog mailError = new SimpleDialog(shlErrorReport, shlErrorReport.getStyle(), "Crash Report", errorMessage, 455, 200, 10);
                    mailError.open();
                }
                if (!errored) {
                    SimpleDialog reportConfirmation = new SimpleDialog(shlErrorReport, shlErrorReport.getStyle(), "Report Confirmation", "Thanks for reporting that bug, we will recifty it ASAP!\n" + "Here's a UUID for referencing this issue in the future:\n" + "    " + UUID, 370, 130, 10);
                    reportConfirmation.open();
                }
            }
        });
        btnSendReport.setBounds(293, 222, 125, 29);
        btnSendReport.setText("Send Error Report");
        Button btnDontSend = new Button(grpReportError, SWT.NONE);
        btnDontSend.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent arg0) {
                shlErrorReport.close();
            }
        });
        btnDontSend.setBounds(204, 222, 83, 29);
        btnDontSend.setText("Don't Send");
        Button btnShowError = new Button(grpReportError, SWT.NONE);
        btnShowError.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent arg0) {
                SimpleDialog showError;
                try {
                    showError = new SimpleDialog(shlErrorReport, shlErrorReport.getStyle(), "Error Log", Notifier.fileToString(errorFile), 200, 200, 10);
                    showError.open();
                } catch (FileNotFoundException fnfe) {
                    fnfe.printStackTrace();
                }
            }
        });
        btnShowError.setBounds(61, 222, 137, 29);
        btnShowError.setText("Show me the error!");
    }
}
