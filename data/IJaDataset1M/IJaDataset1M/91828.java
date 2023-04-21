package com.ivis.xprocess.ui.dialogs;

import java.util.ArrayList;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import com.ivis.xprocess.ui.UIType;
import com.ivis.xprocess.ui.datawrappers.IElementWrapper;
import com.ivis.xprocess.ui.properties.SelectUserMessages;
import com.ivis.xprocess.ui.util.ElementUtil;
import com.ivis.xprocess.ui.util.TestHarness;
import com.ivis.xprocess.util.License;

/**
 * This dialog is used when xProcess RCP cannot
 * associated a Person with the user in the license.
 *
 * This dialog allows the user to select an existing
 * user or to create a new one for the licensed user.
 *
 */
public class SelectUserDialog extends Dialog {

    private List existingUsersList;

    private ArrayList<IElementWrapper> existingUsers = new ArrayList<IElementWrapper>();

    private Button createDefaultUserRadioButton;

    private Button useExistingUserRadioButton;

    private Composite container;

    private boolean useExistingUser = false;

    private IElementWrapper personWrapper;

    private Label messageLabel;

    private IElementWrapper existsInDatasource = null;

    public SelectUserDialog(Shell parentShell) {
        super(parentShell);
        this.setShellStyle(SWT.APPLICATION_MODAL | SWT.BORDER | SWT.TITLE | SWT.RESIZE);
    }

    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText(SelectUserMessages.title);
    }

    @Override
    protected Point getInitialSize() {
        Point point = super.getInitialSize();
        if (point.y < 440) {
            point.y = 440;
        }
        if (point.x < 440) {
            point.x = 440;
        }
        return point;
    }

    protected Control createDialogArea(Composite parent) {
        container = (Composite) super.createDialogArea(parent);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        container.setLayout(gridLayout);
        messageLabel = new Label(container, SWT.WRAP);
        messageLabel.setText(SelectUserMessages.description);
        GridData gridData = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
        gridData.horizontalSpan = 3;
        gridData.verticalSpan = 2;
        messageLabel.setLayoutData(gridData);
        createDefaultUserRadioButton = new Button(container, SWT.RADIO);
        createDefaultUserRadioButton.setText(SelectUserMessages.create_new_person + License.getLicense().getFirstName() + " " + License.getLicense().getLastName());
        createDefaultUserRadioButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                checkData();
            }
        });
        useExistingUserRadioButton = new Button(container, SWT.RADIO);
        useExistingUserRadioButton.setText(SelectUserMessages.use_exsiting_person);
        gridData = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
        gridData.horizontalSpan = 3;
        useExistingUserRadioButton.setLayoutData(gridData);
        useExistingUserRadioButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                checkData();
            }
        });
        existingUsersList = new List(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.SINGLE);
        gridData = new GridData(GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL | GridData.FILL_BOTH);
        gridData.horizontalSpan = 3;
        existingUsersList.setLayoutData(gridData);
        existingUsersList.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                useExistingUserRadioButton.setSelection(true);
                createDefaultUserRadioButton.setSelection(false);
                checkData();
            }
        });
        existingUsersList.addMouseListener(new MouseListener() {

            public void mouseDoubleClick(MouseEvent e) {
                if (existingUsersList.getSelectionCount() > 0) {
                    buttonPressed(IDialogConstants.OK_ID);
                }
            }

            public void mouseDown(MouseEvent e) {
            }

            public void mouseUp(MouseEvent e) {
            }
        });
        Label separatorLabel = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
        gridData = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
        gridData.horizontalSpan = 3;
        separatorLabel.setLayoutData(gridData);
        setupData();
        if (existsInDatasource != null) {
            useExistingUserRadioButton.setSelection(true);
            createDefaultUserRadioButton.setSelection(false);
            existingUsersList.select(existingUsersList.indexOf(existsInDatasource.getLabel()));
        }
        setupTestHarness();
        checkData();
        return container;
    }

    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
    }

    protected void setupData() {
        createDefaultUserRadioButton.setSelection(true);
        for (IElementWrapper elementWrapper : ElementUtil.getAllElementsOfType(UIType.person)) {
            existingUsers.add(elementWrapper);
            existingUsersList.add(elementWrapper.getLabel());
            if (elementWrapper.getLabel().equals(License.getLicense().getFirstName() + " " + License.getLicense().getLastName())) {
                existsInDatasource = elementWrapper;
            }
        }
        checkData();
    }

    private void checkData() {
        messageLabel.setText("");
        if (useExistingUserRadioButton.getSelection()) {
            if (existingUsersList.getSelection().length == 0) {
                messageLabel.setText(SelectUserMessages.select_from_list);
            }
        }
        if (messageLabel.getText().length() > 0) {
            messageLabel.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
            messageLabel.setVisible(true);
            if (getButton(IDialogConstants.OK_ID) != null) {
                getButton(IDialogConstants.OK_ID).setEnabled(false);
            }
        } else {
            messageLabel.setText(SelectUserMessages.description);
            messageLabel.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
            if (getButton(IDialogConstants.OK_ID) != null) {
                getButton(IDialogConstants.OK_ID).setEnabled(true);
            }
        }
        container.layout(true, true);
    }

    /**
     * Setting up the wizard page for Abbot
     */
    private void setupTestHarness() {
        TestHarness.name(createDefaultUserRadioButton, TestHarness.SELECTUSERDIALOG_DEFAULT_BUTTON);
        TestHarness.name(useExistingUserRadioButton, TestHarness.SELECTUSERDIALOG_EXISTING_BUTTON);
        TestHarness.name(existingUsersList, TestHarness.SELECTUSERDIALOG_EXISTING_LIST);
    }

    /**
     * @return true if the user has selected an existing user
     */
    public boolean useExistingUser() {
        return useExistingUser;
    }

    /**
     * @return the existing Person that the user has chosen to use
     */
    public IElementWrapper getExistingUser() {
        return personWrapper;
    }

    @Override
    protected void buttonPressed(int buttonId) {
        if (buttonId == IDialogConstants.OK_ID) {
            useExistingUser = useExistingUserRadioButton.getSelection();
            if (useExistingUser) {
                personWrapper = existingUsers.get(existingUsersList.getSelectionIndex());
            }
        }
        super.buttonPressed(buttonId);
    }

    /**
     * This is a simple way to stop SWT.ESC from closing the dialog,
     * by setting the event.doit to false
     *
     * @see org.eclipse.jface.window.Window#getShellListener()
     */
    protected ShellListener getShellListener() {
        return new ShellAdapter() {

            public void shellClosed(ShellEvent event) {
                event.doit = false;
            }
        };
    }
}
