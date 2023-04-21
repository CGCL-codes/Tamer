package org.horen.ui.dialogs;

import java.util.ResourceBundle;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.horen.ui.panes.composite.ImagedComposite;
import org.horen.ui.resources.Resources;
import org.horen.ui.util.NullShellProvider;

/**
 * An abstract class extending the Dialog-class for showing 
 * messages to the user. The dialog area consists of an
 * image on the left side and the message area on the right
 * side. 
 * @author Steffen
 */
public abstract class MessageDialog extends Dialog {

    public static final int OK = IDialogConstants.OK_ID;

    public static final int YES = IDialogConstants.YES_ID;

    public static final int NO = IDialogConstants.NO_ID;

    public static final int CANCEL = IDialogConstants.CANCEL_ID;

    public static final int REPORT = IDialogConstants.FINISH_ID;

    public static final int SUBMIT = IDialogConstants.CLIENT_ID + 0;

    public static final String KEY_PREFIX = "DIALOG_BUTTON_";

    private static final String BUTTON_NO_LABEL = "unknown";

    private String m_Title;

    private Image m_Icon;

    private String m_Message;

    private int[] m_Buttons;

    /**
	 * Constructs a generic message dialog.
	 * @param parentShell Provider of the parent shell
	 * @param title Title of the window
	 * @param message Information to be presented
	 * @param icon Icon to be displayed in the dialog
	 */
    public MessageDialog(IShellProvider parentShell, String title, String message, Image icon, int[] buttons) {
        super((parentShell == null) ? new NullShellProvider() : parentShell);
        Assert.isLegal(buttons != null && buttons.length >= 0, "Parameter 'buttons' is invalid!");
        m_Title = title;
        m_Icon = icon;
        m_Message = message;
        m_Buttons = buttons;
        setShellStyle(getShellStyle() | SWT.RESIZE);
    }

    /**
	 * @see Dialog#createDialogArea(Composite)
	 */
    @Override
    protected Control createDialogArea(Composite parent) {
        ImagedComposite content = new ImagedComposite(m_Icon, parent, SWT.NONE);
        createMessageArea(content.getContentComposite());
        return content;
    }

    /**
	 * Creates the message area, displayed as the right column of
	 * the message dialog.
	 * By default a simple label is created as the message area.
	 * Subclasses may reimplement this method to show their
	 * specific contents.
	 */
    protected void createMessageArea(Composite parent) {
        Label message = new Label(parent, SWT.NONE);
        message.setText(m_Message);
        message.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    }

    /**
	 * Configures the window presented to the user.
	 * Actually, the minimum size of the dialog is changed and the title of
	 * the shell.
	 */
    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setMinimumSize(300, 130);
        newShell.setText(m_Title);
    }

    /**
	 * Creates the buttons specified in the constructor.
	 */
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        for (int i = 0; i < m_Buttons.length; i++) {
            createButton(parent, m_Buttons[i], getButtonLabel(m_Buttons[i]), i == 0);
        }
    }

    /**
	 * Returns the label text of the button given by the id.
	 * By default this method gets the localized label from the
	 * resource bundle of the current language using the string
	 * <code>KEY_PREFIX + id</code> as the lookup key. 
	 * @param id given identification number
	 */
    protected String getButtonLabel(int id) {
        ResourceBundle bundle = Resources.getDefaultBundle();
        String key = KEY_PREFIX + id;
        if (bundle.containsKey(key) == true) {
            return bundle.getString(key);
        }
        return BUTTON_NO_LABEL;
    }

    /**
	 * Adds a handling for the serveral standard butttons.
	 * @see base class
	 */
    @Override
    protected void buttonPressed(int buttonId) {
        if (buttonId == YES || buttonId == NO) {
            setReturnCode(buttonId);
            close();
        } else {
            super.buttonPressed(buttonId);
        }
    }
}
