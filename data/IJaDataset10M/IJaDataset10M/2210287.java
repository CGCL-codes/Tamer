package org.isistan.flabot.edit.editor.dialogs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.isistan.flabot.messages.Messages;
import org.isistan.flabot.util.edition.EditionItem;
import org.isistan.flabot.util.edition.EditionItemChangeListener;
import org.isistan.flabot.util.edition.EditionItemStatus;
import org.isistan.flabot.util.edition.tab.CompositeEditionTabItem;
import org.isistan.flabot.util.edition.tab.EditionTabItem;

/**
 * This dialog takes EditionTabItems as arguments and permits the edition
 * automatically providing a status bar and an auto-enabled/disabled ok button
 * 
 * @author da Costa Cambio
 *
 */
public class StandardEditionDialog<T> extends Dialog {

    /**
	 * the dialog's shell
	 */
    private Shell shell = null;

    private Command command = null;

    private T element;

    private CompositeEditionTabItem<T> editionItem;

    private Collection<? extends EditionTabItem<T>> editionItems;

    private Button buttonOK;

    private Button buttonProblems;

    private Label status;

    private String title;

    private String commandName;

    public StandardEditionDialog(Shell parent, String title, String commandName, Collection<? extends EditionTabItem<T>> editionItems) {
        super(parent, 0);
        this.editionItems = editionItems;
        this.title = title;
        this.commandName = commandName;
    }

    public StandardEditionDialog(Shell parent, String title, String commandName, EditionTabItem<T>[] editionItems) {
        super(parent, 0);
        List<EditionTabItem<T>> list = new ArrayList<EditionTabItem<T>>(editionItems.length);
        for (EditionTabItem<T> item : editionItems) {
            list.add(item);
        }
        this.editionItems = list;
        this.title = title;
        this.commandName = commandName;
    }

    public Command open(T element) {
        this.element = element;
        createShell();
        createEdition();
        createFinalButtons();
        createStatus();
        setStatus(editionItem);
        return show();
    }

    private void createShell() {
        shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        shell.setText(title);
        shell.setLayout(new GridLayout(1, true));
        shell.addShellListener(new org.eclipse.swt.events.ShellAdapter() {

            public void shellClosed(org.eclipse.swt.events.ShellEvent e) {
                handleCancel();
            }
        });
    }

    private void createFinalButtons() {
        Composite buttonsComposite = new Composite(shell, SWT.NONE);
        buttonsComposite.setLayout(new RowLayout());
        buttonsComposite.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
        buttonOK = new Button(buttonsComposite, SWT.NONE);
        buttonOK.setText(Messages.getString("org.isistan.flabot.edit.editor.okButton"));
        buttonOK.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                handleOk();
            }
        });
        Button buttonCancel = new Button(buttonsComposite, SWT.NONE);
        buttonCancel.setText(Messages.getString("org.isistan.flabot.edit.editor.cancelButton"));
        buttonCancel.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                handleCancel();
            }
        });
        buttonProblems = new Button(buttonsComposite, SWT.NONE);
        buttonProblems.setText(Messages.getString("org.isistan.flabot.edit.editor.problemsButton"));
        buttonProblems.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                handleViewProblems();
            }
        });
    }

    private void createStatus() {
        status = new Label(shell, SWT.NONE);
        status.setText("");
    }

    private boolean statusMatters(EditionItemStatus status) {
        return status.getType() != EditionItemStatus.Type.DEFAULT_OK;
    }

    private List<String> getMessages(EditionItemStatus status) {
        EditionItemStatus[] statuses = status.getSubStatuses();
        List<String> list = new LinkedList<String>();
        if (statuses.length > 0) {
            for (EditionItemStatus childStatus : statuses) {
                if (statusMatters(childStatus)) {
                    list.addAll(getMessages(childStatus));
                }
            }
            return list;
        } else {
            if (statusMatters(status)) {
                list.add(" - " + status.getDescription());
            }
            return list;
        }
    }

    private String getMessage(EditionItemStatus status) {
        List<String> list = getMessages(status);
        StringBuffer buffer = new StringBuffer();
        for (String string : list) {
            buffer.append(string);
            buffer.append('\n');
        }
        return buffer.toString();
    }

    protected void handleOk() {
        EditionItemStatus status = editionItem.getStatus();
        String message = getMessage(status);
        boolean canCreateCommand = editionItem.canCreateCommand();
        if (canCreateCommand) {
            if (message.length() > 0) {
                boolean result = MessageDialog.openConfirm(shell, title, Messages.getString("org.isistan.flabot.edit.editor.dialogs.StandardEditionDialog.problemsFoundCanAccept", message));
                if (result) {
                    command = editionItem.getCommand();
                    shell.dispose();
                }
            } else {
                command = editionItem.getCommand();
                shell.dispose();
            }
        } else {
            MessageDialog.openError(shell, title, Messages.getString("org.isistan.flabot.edit.editor.dialogs.StandardEditionDialog.problemsFoundCanNotAccept", message));
        }
    }

    protected void handleViewProblems() {
        EditionItemStatus status = editionItem.getStatus();
        String message = getMessage(status);
        boolean canCreateCommand = editionItem.canCreateCommand();
        if (canCreateCommand) {
            if (message.length() > 0) {
                MessageDialog.openWarning(shell, title, Messages.getString("org.isistan.flabot.edit.editor.dialogs.StandardEditionDialog.problemsFoundCanAcceptWarning", message));
            } else {
                MessageDialog.openInformation(shell, title, Messages.getString("org.isistan.flabot.edit.editor.dialogs.StandardEditionDialog.noProblemsFound", message));
            }
        } else {
            MessageDialog.openError(shell, title, Messages.getString("org.isistan.flabot.edit.editor.dialogs.StandardEditionDialog.problemsFoundCanNotAccept", message));
        }
    }

    private Command show() {
        shell.pack();
        Display display = getParent().getDisplay();
        Rectangle r = display.getClientArea();
        int centerX = r.width / 2 - shell.getSize().x / 2;
        int centerY = r.height / 2 - shell.getSize().y / 2;
        shell.setLocation(centerX, centerY);
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        return command;
    }

    private void handleCancel() {
        command = null;
        shell.dispose();
    }

    private void createEdition() {
        editionItem = new CompositeEditionTabItem<T>(commandName);
        for (EditionTabItem<T> aEditionItem : editionItems) {
            editionItem.addEditionItem(aEditionItem);
        }
        editionItem.initialize(shell, element);
        editionItem.addListener(new EditionItemChangeListener<T>() {

            public void changed(EditionItem<T> notifier, EditionItem<T> originator) {
                setStatus(notifier);
            }
        });
    }

    private void setStatus(EditionItem<T> notifier) {
        EditionItemStatus eiStatus = notifier.getStatus();
        status.setText(eiStatus.getDescription());
        status.pack();
        buttonOK.setEnabled(notifier.canCreateCommand());
    }
}
