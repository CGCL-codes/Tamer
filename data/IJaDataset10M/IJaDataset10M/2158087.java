package org.eclipse.mylyn.internal.tasks.ui.views;

import java.util.Date;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.mylyn.internal.tasks.ui.RetrieveTitleFromUrlJob;
import org.eclipse.mylyn.monitor.core.StatusHandler;
import org.eclipse.mylyn.tasks.ui.DatePicker;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @author Ken Sueda
 * @author Wesley Coelho (Extended to allow URL input)
 * @author Mik Kersten
 */
public class TaskInputDialog extends Dialog {

    public static final String LABEL_SHELL = "New Task";

    private static final String LABEL_DESCRIPTION = "Description:";

    private String taskName = "";

    private String priority = "P3";

    private String taskURL = "http://";

    private Date reminderDate = null;

    Text taskNameTextWidget = null;

    private Text issueURLTextWidget = null;

    private Button getDescButton = null;

    public TaskInputDialog(Shell parentShell) {
        super(parentShell);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        GridLayout gl = new GridLayout(5, false);
        composite.setLayout(gl);
        GridData data = new GridData(GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_CENTER);
        data.widthHint = convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH + 180);
        composite.setLayoutData(data);
        Label taskNameLabel = new Label(composite, SWT.WRAP);
        taskNameLabel.setText(LABEL_DESCRIPTION);
        taskNameLabel.setFont(parent.getFont());
        taskNameTextWidget = new Text(composite, SWT.SINGLE | SWT.BORDER);
        GridData taskNameGD = new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL);
        taskNameGD.widthHint = 200;
        taskNameGD.horizontalSpan = 1;
        taskNameTextWidget.setLayoutData(taskNameGD);
        final Combo c = new Combo(composite, SWT.NO_BACKGROUND | SWT.MULTI | SWT.V_SCROLL | SWT.READ_ONLY | SWT.DROP_DOWN);
        c.setItems(TaskListView.PRIORITY_LEVELS);
        c.setText(priority);
        c.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {
                priority = c.getText();
            }

            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }
        });
        final DatePicker datePicker = new DatePicker(composite, SWT.BORDER, DatePicker.LABEL_CHOOSE);
        datePicker.addPickerSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent arg0) {
                if (datePicker.getDate() != null) {
                    reminderDate = datePicker.getDate().getTime();
                }
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
            }
        });
        Button removeReminder = new Button(composite, SWT.PUSH | SWT.CENTER);
        removeReminder.setText("Clear");
        removeReminder.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                datePicker.setDate(null);
                reminderDate = null;
            }
        });
        Label urlLabel = new Label(composite, SWT.WRAP);
        urlLabel.setText("Web Link:");
        urlLabel.setFont(parent.getFont());
        issueURLTextWidget = new Text(composite, SWT.SINGLE | SWT.BORDER);
        issueURLTextWidget.setText(getDefaultIssueUrl());
        GridData urlData = new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL);
        urlData.horizontalSpan = 3;
        urlData.grabExcessHorizontalSpace = true;
        issueURLTextWidget.setLayoutData(urlData);
        getDescButton = new Button(composite, SWT.PUSH);
        getDescButton.setText("Get Description");
        getDescButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
        setButtonStatus();
        issueURLTextWidget.addKeyListener(new KeyListener() {

            public void keyPressed(KeyEvent e) {
                setButtonStatus();
            }

            public void keyReleased(KeyEvent e) {
                setButtonStatus();
            }
        });
        getDescButton.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {
                retrieveTaskDescription(issueURLTextWidget.getText());
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
        return composite;
    }

    /**
	 * Sets the Get Description button enabled or not depending on whether there is a URL specified
	 */
    protected void setButtonStatus() {
        String url = issueURLTextWidget.getText();
        if (url.length() > 10 && (url.startsWith("http://") || url.startsWith("https://"))) {
            getDescButton.setEnabled(true);
        } else {
            getDescButton.setEnabled(false);
        }
    }

    /**
	 * Returns the default URL text for the task by first checking the contents of the clipboard and then using the
	 * default prefix preference if that fails
	 */
    protected String getDefaultIssueUrl() {
        String clipboardText = getClipboardText();
        if ((clipboardText.startsWith("http://") || clipboardText.startsWith("https://") && clipboardText.length() > 10)) {
            return clipboardText;
        } else {
            return taskURL;
        }
    }

    /**
	 * Attempts to set the task pageTitle to the title from the specified url
	 */
    protected void retrieveTaskDescription(final String url) {
        try {
            RetrieveTitleFromUrlJob job = new RetrieveTitleFromUrlJob(issueURLTextWidget.getText()) {

                @Override
                protected void setTitle(final String pageTitle) {
                    taskNameTextWidget.setText(pageTitle);
                }
            };
            job.schedule();
        } catch (RuntimeException e) {
            StatusHandler.fail(e, "could not open task web page", false);
        }
    }

    /**
	 * Returns the contents of the clipboard or "" if no text content was available
	 */
    protected String getClipboardText() {
        Clipboard clipboard = new Clipboard(Display.getDefault());
        TextTransfer transfer = TextTransfer.getInstance();
        String contents = (String) clipboard.getContents(transfer);
        if (contents != null) {
            return contents;
        } else {
            return "";
        }
    }

    public String getSelectedPriority() {
        return priority;
    }

    public String getTaskname() {
        return taskName;
    }

    public Date getReminderDate() {
        return reminderDate;
    }

    public String getIssueURL() {
        return taskURL;
    }

    @Override
    protected void buttonPressed(int buttonId) {
        if (buttonId == IDialogConstants.OK_ID) {
            taskName = taskNameTextWidget.getText();
            taskURL = issueURLTextWidget.getText();
        } else {
            taskName = null;
        }
        super.buttonPressed(buttonId);
    }

    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText(LABEL_SHELL);
    }
}
