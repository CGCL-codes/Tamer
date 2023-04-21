package ase.eleitweg.wfeditor.gui;

import java.util.Date;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import ase.eleitweg.common.Workflow;

public class EditWorkflowProperties extends Dialog {

    private Text beschreibungsText;

    private Text nameText;

    protected Shell shell;

    protected Workflow workflow;

    /**
	 * Create the dialog
	 * @param parent
	 * @param style
	 */
    public EditWorkflowProperties(Shell parent, int style) {
        super(parent, style);
    }

    /**
	 * Create the dialog
	 * @param parent
	 */
    public EditWorkflowProperties(Shell parent) {
        this(parent, SWT.NONE);
    }

    /**
	 * Open the dialog
	 * @return the result
	 */
    public void open(Workflow workflow) {
        this.workflow = workflow;
        createContents();
        shell.open();
        shell.layout();
        Display display = getParent().getDisplay();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
    }

    /**
	 * Create contents of the dialog
	 */
    @SuppressWarnings("deprecation")
    protected void createContents() {
        shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        shell.setSize(500, 243);
        shell.setText("Workflow Eigenschaften");
        final Label workflowNameLabel = new Label(shell, SWT.NONE);
        workflowNameLabel.setText("Name:");
        workflowNameLabel.setBounds(10, 10, 91, 20);
        final DateTime dateTime = new DateTime(shell, SWT.NONE);
        dateTime.setBounds(107, 132, 91, 20);
        final Label beschreibungLabel = new Label(shell, SWT.NONE);
        beschreibungLabel.setText("Beschreibung:");
        beschreibungLabel.setBounds(10, 36, 91, 20);
        nameText = new Text(shell, SWT.BORDER);
        nameText.setBounds(107, 7, 239, 20);
        beschreibungsText = new Text(shell, SWT.WRAP | SWT.BORDER);
        beschreibungsText.setBounds(107, 36, 377, 90);
        final Label datumLabel = new Label(shell, SWT.NONE);
        datumLabel.setText("Datum:");
        datumLabel.setBounds(10, 132, 91, 20);
        final Button abbrechenButton = new Button(shell, SWT.NONE);
        abbrechenButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                shell.dispose();
            }
        });
        abbrechenButton.setText("Abbrechen");
        abbrechenButton.setBounds(375, 186, 109, 23);
        final Button okButton = new Button(shell, SWT.NONE);
        okButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                if (!beschreibungsText.getText().isEmpty() && !nameText.getText().isEmpty()) {
                    workflow.setName(nameText.getText());
                    workflow.setDesc(beschreibungsText.getText());
                    workflow.setDate(new Date(dateTime.getYear(), dateTime.getMonth(), dateTime.getDay()));
                    shell.dispose();
                } else {
                    MessageBox box = new MessageBox(shell, SWT.ICON_ERROR);
                    box.setText("Fehler");
                    box.setMessage("Der Name und Beschreibung eines Workflows sollen dürfen nicht leer sein!");
                    box.open();
                }
            }
        });
        okButton.setText("OK");
        okButton.setBounds(261, 186, 109, 23);
        dateTime.setYear(workflow.getDate().getYear());
        dateTime.setMonth(workflow.getDate().getMonth());
        dateTime.setDay(workflow.getDate().getDay());
        fillInContent();
    }

    protected void fillInContent() {
        if (workflow.getName() != null) nameText.setText(workflow.getName());
        if (workflow.getDesc() != null) beschreibungsText.setText(workflow.getDesc());
    }
}
