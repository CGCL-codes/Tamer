package com.alexmcchesney.poster.gui.tags;

import com.alexmcchesney.poster.PosterApp;
import com.alexmcchesney.poster.gui.*;
import com.alexmcchesney.poster.os.OperatingSystem;
import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;

/**
 * Dialog requesting the name of new tags.
 * @author AMCCHESNEY
 *
 */
public class NewTagDialog extends com.alexmcchesney.poster.gui.Dialog {

    /** Textbox for entering tag names */
    private Text m_textBox = null;

    /** Shell for the dialog */
    private Shell m_shell = null;

    /** Ok button */
    private Button m_okButton = null;

    /** Cancel button */
    private Button m_cancelButton;

    /** String containing the final value.  Null if the dialog was cancelled */
    private String m_sValue = null;

    /**
	 * Constructor.
	 * @param parent	The parent shell to attach the dialog to
	 */
    public NewTagDialog(Shell parent) {
        m_shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.PRIMARY_MODAL);
        m_shell.setText(GUIStringResources.gui.getString("NEW_TAGS"));
        FillLayout layout = new FillLayout(SWT.VERTICAL);
        layout.marginWidth = 5;
        layout.marginHeight = 5;
        layout.spacing = 5;
        m_shell.setLayout(layout);
        Label label = new Label(m_shell, SWT.NONE);
        label.setText(GUIStringResources.gui.getString("ENTER_NEW_TAGS"));
        m_textBox = new Text(m_shell, SWT.BORDER);
        Composite buttonComposite = new Composite(m_shell, SWT.NONE);
        FormLayout buttonLayout = new FormLayout();
        buttonLayout.spacing = 3;
        buttonComposite.setLayout(buttonLayout);
        m_cancelButton = new Button(buttonComposite, SWT.PUSH);
        m_cancelButton.setText(GUIStringResources.gui.getString("CANCEL"));
        m_cancelButton.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {
                m_shell.dispose();
            }

            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }
        });
        m_okButton = new Button(buttonComposite, SWT.PUSH);
        m_okButton.setText(GUIStringResources.gui.getString("OK"));
        m_okButton.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {
                m_sValue = m_textBox.getText();
                m_shell.dispose();
            }

            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }
        });
        OperatingSystem os = PosterApp.getOperatingSystem();
        FormData cancelData = new FormData();
        if (os.cancelFirst()) {
            cancelData.right = new FormAttachment(m_okButton, 0);
        } else {
            cancelData.right = new FormAttachment(100, 0);
        }
        cancelData.bottom = new FormAttachment(100, 0);
        m_cancelButton.setLayoutData(cancelData);
        FormData okData = new FormData();
        if (os.cancelFirst()) {
            okData.right = new FormAttachment(100, 0);
        } else {
            okData.right = new FormAttachment(m_cancelButton, 0);
        }
        okData.bottom = new FormAttachment(100, 0);
        m_okButton.setLayoutData(okData);
        m_shell.setDefaultButton(m_okButton);
        m_shell.pack();
        center(parent, m_shell);
        m_shell.layout();
    }

    public boolean isDisposed() {
        return m_shell.isDisposed();
    }

    /**
	 * Displays the dialog
	 *
	 */
    public void show() {
        m_shell.setVisible(true);
        m_textBox.setFocus();
    }

    /**
	 * Returns the final value of the dialog.
	 * @return	String containing the content of the text box, or null if the dialog was cancelled.
	 */
    public String getValue() {
        return m_sValue;
    }
}
