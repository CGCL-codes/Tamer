package org.deri.wsml.eclipse.visualizer.dialogs;

import java.util.Map;
import net.sourceforge.powerswing.localization.PBundle;
import net.sourceforge.powerswing.util.multireturn.Pair;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class NamespaceDialog extends Dialog {

    private PBundle messages;

    private Button okButton;

    private Text prefixText;

    private Text iriText;

    private Text errorMessageText;

    private Pair<String, String> result = null;

    private Map<String, String> currentValues;

    public NamespaceDialog(Shell parentShell, PBundle theMessages, String thePrefix, String theIRI, Map<String, String> theCurrentValues) {
        super(parentShell);
        this.messages = theMessages;
        if (thePrefix != null && theIRI != null) {
            result = new Pair<String, String>(thePrefix, theIRI);
        }
        currentValues = theCurrentValues;
        if (thePrefix != null) {
            currentValues.remove(thePrefix);
        }
    }

    protected void buttonPressed(int buttonId) {
        if (buttonId == IDialogConstants.OK_ID) {
            result = new Pair<String, String>(prefixText.getText().trim(), iriText.getText().trim());
        } else {
            result = null;
        }
        super.buttonPressed(buttonId);
    }

    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        if (result == null) {
            shell.setText(messages.getString("NamespaceDialog.AddTitle"));
        } else {
            shell.setText(messages.getString("NamespaceDialog.EditTitle"));
        }
    }

    protected void createButtonsForButtonBar(Composite parent) {
        okButton = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
        prefixText.setFocus();
        if (result != null) {
            prefixText.setText(result.getFirst());
            iriText.setText(result.getSecond());
        }
        validateInput();
    }

    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        gridLayout.marginLeft = 5;
        gridLayout.marginRight = 5;
        gridLayout.marginTop = 5;
        composite.setLayout(gridLayout);
        new Label(composite, SWT.NONE).setText(messages.getString("NamespaceDialog.Prefix") + ":");
        prefixText = new Text(composite, SWT.BORDER);
        prefixText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        prefixText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                validateInput();
            }
        });
        new Label(composite, SWT.NONE).setText(messages.getString("NamespaceDialog.IRI") + ":");
        iriText = new Text(composite, SWT.BORDER);
        iriText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        iriText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                validateInput();
            }
        });
        errorMessageText = new Text(composite, SWT.READ_ONLY);
        GridData data4 = new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL);
        data4.horizontalSpan = 2;
        data4.widthHint = 300;
        errorMessageText.setLayoutData(data4);
        errorMessageText.setBackground(errorMessageText.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
        errorMessageText.setForeground(errorMessageText.getDisplay().getSystemColor(SWT.COLOR_RED));
        applyDialogFont(composite);
        return composite;
    }

    protected Label getErrorMessageLabel() {
        return null;
    }

    protected Button getOkButton() {
        return okButton;
    }

    public String getPrefix() {
        return result.getFirst();
    }

    public String getIRI() {
        return result.getSecond();
    }

    protected void validateInput() {
        String errorMessage = null;
        if (prefixText.getText().trim().length() == 0) {
            errorMessage = messages.getString("NamespaceDialog.NoPrefixSpecified");
        } else if (isPrefixInUse(prefixText.getText().trim())) {
            errorMessage = messages.getString("NamespaceDialog.PrefixIsInUse");
        } else if (iriText.getText().trim().length() == 0) {
            errorMessage = messages.getString("NamespaceDialog.NoIRISpecified");
        } else if (isIRIInUse(iriText.getText().trim())) {
            errorMessage = messages.getString("NamespaceDialog.IRIIsInUse");
        }
        setErrorMessage(errorMessage);
    }

    public boolean isPrefixInUse(String thePrefix) {
        return currentValues.containsKey(thePrefix);
    }

    public boolean isIRIInUse(String theIRI) {
        for (String prefix : currentValues.keySet()) {
            String iri = currentValues.get(prefix);
            if (iri.equals(theIRI)) {
                return true;
            }
        }
        return false;
    }

    public void setErrorMessage(String errorMessage) {
        errorMessageText.setText(errorMessage == null ? "" : errorMessage);
        okButton.setEnabled(errorMessage == null);
        errorMessageText.getParent().update();
    }
}
