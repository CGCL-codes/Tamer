package org.gamesroom.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.gamesroom.l10n.Messages;
import org.gamesroom.logic.GamesroomException;
import org.gamesroom.logic.Mediator;
import org.gamesroom.util.GuiUtils;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class AddGameDialog extends org.eclipse.swt.widgets.Dialog {

    private Shell dialogShell;

    private Button buttonAdd;

    private Button buttonExit;

    private Label labelGameName;

    private Text textGameName;

    private boolean success = false;

    public boolean isSuccess() {
        return success;
    }

    /**
	 * Auto-generated main method to display this org.eclipse.swt.widgets.Dialog
	 * inside a new Shell.
	 */
    public static void main(String[] args) {
        try {
            Display display = Display.getDefault();
            Shell shell = new Shell(display);
            AddGameDialog inst = new AddGameDialog(shell, SWT.NULL);
            inst.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public AddGameDialog(Shell parent, int style) {
        super(parent, style);
    }

    public void open() {
        try {
            Shell parent = getParent();
            dialogShell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
            dialogShell.setLayout(new FormLayout());
            dialogShell.layout();
            dialogShell.pack();
            dialogShell.setSize(461, 120);
            {
                labelGameName = new Label(dialogShell, SWT.NONE);
                FormData labelGameNameLData = new FormData();
                labelGameNameLData.width = 102;
                labelGameNameLData.height = 21;
                labelGameNameLData.left = new FormAttachment(16, 1000, 0);
                labelGameNameLData.top = new FormAttachment(0, 1000, 14);
                labelGameNameLData.right = new FormAttachment(240, 1000, 0);
                labelGameName.setLayoutData(labelGameNameLData);
                labelGameName.setText(Messages.getString("AddGameDialog.LBL_GAME_NAME"));
                labelGameName.setAlignment(SWT.CENTER);
                labelGameName.setDragDetect(false);
            }
            {
                textGameName = new Text(dialogShell, SWT.SINGLE | SWT.BORDER);
                FormData text1LData = new FormData();
                text1LData.width = 313;
                text1LData.height = 15;
                text1LData.left = new FormAttachment(labelGameName, 0, SWT.RIGHT);
                text1LData.top = new FormAttachment(0, 1000, 13);
                text1LData.right = new FormAttachment(974, 1000, 0);
                textGameName.setLayoutData(text1LData);
                textGameName.addKeyListener(new KeyAdapter() {

                    public void keyPressed(KeyEvent evt) {
                        textGameNameKeyPressed(evt);
                    }
                });
            }
            {
                buttonExit = new Button(dialogShell, SWT.PUSH | SWT.CENTER);
                FormData buttonExitLData = new FormData();
                buttonExitLData.width = 210;
                buttonExitLData.height = 28;
                buttonExitLData.right = new FormAttachment(970, 1000, 0);
                buttonExitLData.bottom = new FormAttachment(1000, 1000, -14);
                buttonExitLData.left = new FormAttachment(508, 1000, 0);
                buttonExit.setLayoutData(buttonExitLData);
                buttonExit.setText(Messages.getString("AddGameDialog.BTN_CANCEL"));
                buttonExit.addSelectionListener(new SelectionAdapter() {

                    public void widgetSelected(SelectionEvent evt) {
                        buttonExitWidgetSelected(evt);
                    }
                });
            }
            {
                buttonAdd = new Button(dialogShell, SWT.PUSH | SWT.CENTER);
                FormData buttonAddLData = new FormData();
                buttonAddLData.width = 210;
                buttonAddLData.height = 28;
                buttonAddLData.left = new FormAttachment(16, 1000, 0);
                buttonAddLData.right = new FormAttachment(478, 1000, 0);
                buttonAddLData.bottom = new FormAttachment(1000, 1000, -14);
                buttonAdd.setLayoutData(buttonAddLData);
                buttonAdd.setText(Messages.getString("AddGameDialog.BTN_ADD_GAME"));
                buttonAdd.addSelectionListener(new SelectionAdapter() {

                    public void widgetSelected(SelectionEvent evt) {
                        buttonAddWidgetSelected(evt);
                    }
                });
            }
            dialogShell.setLocation(getParent().toDisplay(100, 100));
            dialogShell.open();
            Display display = dialogShell.getDisplay();
            while (!dialogShell.isDisposed()) {
                if (!display.readAndDispatch()) display.sleep();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buttonExitWidgetSelected(SelectionEvent evt) {
        dialogShell.dispose();
    }

    private void buttonAddWidgetSelected(SelectionEvent evt) {
        addGame();
    }

    private void addGame() {
        String title = textGameName.getText();
        if (title != null && !title.isEmpty() && !title.matches(Messages.getString("AddGameDialog.REGEXP_SPACES"))) {
            try {
                Mediator.getInstance().addGame(title);
                success = true;
                dialogShell.dispose();
            } catch (GamesroomException e) {
                GuiUtils.showMessage(dialogShell, Messages.getString("AddGameDialog.ERROR_ADD_GAME"), e.getMessage(), SWT.ICON_ERROR | SWT.OK);
            }
        } else {
            GuiUtils.showMessage(dialogShell, Messages.getString("AddGameDialog.ERROR_CREATE_GAME"), Messages.getString("AddGameDialog.DIALOG_ENTER_GAME_NAME"), SWT.ICON_WARNING | SWT.OK);
        }
    }

    private void textGameNameKeyPressed(KeyEvent evt) {
        if (evt.keyCode == SWT.CR) {
            addGame();
        }
    }
}

;
