package net.sourceforge.keytool.wizards;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import net.sourceforge.keytool.certificate.KeystoreFile;
import net.sourceforge.keytool.helpers.TreeParent;
import net.sourceforge.keytool.views.KeyStoreView;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public abstract class AbstractWizardPage extends WizardPage {

    public static final String CREATE_A_NEW_KEYSTORE_TEXT = "<Create a new keystore>";

    public static final String LABEL_KEYSTORE = "Keystore";

    private static final int LENGTH_OF_DATE = 10;

    private DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    private ModifyListener modifyListener = null;

    private boolean editable = true;

    private int colspan = 0;

    protected abstract void dialogChanged();

    protected AbstractWizardPage(String pageName) {
        super(pageName);
    }

    protected final void checkDate(Text dateText, String fieldName) {
        try {
            if (dateText == null || dateText.getText().length() != LENGTH_OF_DATE) {
                updateStatus("'" + fieldName + "' is not a valid date");
            } else {
                dateFormat.parse(dateText.getText());
            }
        } catch (ParseException e) {
            updateStatus("'" + fieldName + "' is not a valid date");
        }
    }

    protected Date parseDate(Text text) {
        try {
            return getDateFormat().parse(text.getText());
        } catch (ParseException e) {
            System.out.println("Could not parse date");
            e.printStackTrace();
            return null;
        }
    }

    protected final void checkStatus(Combo combo, String message) {
        checkStatus(combo == null ? null : combo.getText(), message);
    }

    protected final void checkStatus(Text text, String message) {
        if (text != null) {
            checkStatus(text.getText(), message);
        } else {
            updateStatus(message);
        }
    }

    private void checkStatus(String txt, String message) {
        if (txt == null || txt.length() == 0) {
            updateStatus(message);
        }
    }

    protected final void resetStatus() {
        setErrorMessage(null);
        setPageComplete(false);
    }

    protected final void updateStatus(String message) {
        setErrorMessage(message);
        setPageComplete(message == null);
    }

    protected final ModifyListener getModifyListener() {
        if (modifyListener == null) {
            modifyListener = new ModifyListener() {

                public void modifyText(ModifyEvent e) {
                    dialogChanged();
                }
            };
        }
        return modifyListener;
    }

    public final DateFormat getDateFormat() {
        return dateFormat;
    }

    protected final Text makeLine(Composite composite, String labelText, String value) {
        return makeLine(composite, labelText, value, SWT.BORDER);
    }

    protected final Text makeLine(Composite composite, String labelText, String value, int style) {
        Label label = new Label(composite, SWT.NONE);
        label.setText(labelText);
        GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, false);
        if (colspan > 0) {
            gridData.horizontalSpan = colspan;
        }
        Text text = new Text(composite, style);
        text.setLayoutData(gridData);
        text.setText(value == null ? "" : value);
        text.setEditable(editable);
        text.addModifyListener(getModifyListener());
        return text;
    }

    public final boolean isEditable() {
        return editable;
    }

    public final void setEditable(boolean editable) {
        this.editable = editable;
    }

    public int getColspan() {
        return colspan;
    }

    public void setColspan(int colspan) {
        this.colspan = colspan;
    }

    protected String[] getExtensions() {
        return new String[] { "*.cer", "*.pfx" };
    }

    protected void setFilenameText(Text filenameText, String text) {
        filenameText.setText(text);
    }

    protected void addBrowseButton(Composite composite, final Text filenameText) {
        Button button = new Button(composite, SWT.PUSH);
        button.setText("Browse...");
        button.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                handleBrowse(e, filenameText);
            }
        });
    }

    protected void handleBrowse(SelectionEvent e, Text filenameText) {
        if (e.widget instanceof Button) {
            FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
            dialog.setFilterExtensions(getExtensions());
            String file = dialog.open();
            if (file != null) {
                file = file.trim();
                if (file.length() > 0) {
                    setFilenameText(filenameText, file);
                }
            }
            dialogChanged();
        }
    }

    protected Combo addOpenKeystores(Composite container, TreeParent selection) {
        Label label = new Label(container, SWT.NONE);
        label.setText(LABEL_KEYSTORE);
        GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, false, 1, 1);
        Combo keystoreCombo = new Combo(container, SWT.DROP_DOWN | SWT.READ_ONLY);
        keystoreCombo.setLayoutData(gridData);
        keystoreCombo.addModifyListener(getModifyListener());
        boolean hasSelection = false;
        String selectedFilename = "";
        keystoreCombo.add(CREATE_A_NEW_KEYSTORE_TEXT);
        if (selection != null) {
            hasSelection = true;
            selectedFilename = selection.getKeystoreFilename();
        } else {
            selectedFilename = CREATE_A_NEW_KEYSTORE_TEXT;
            keystoreCombo.select(0);
        }
        ArrayList<KeystoreFile> keystores = KeyStoreView.getKeystores();
        if (keystores != null) {
            for (int i = 0; i < keystores.size(); i++) {
                String keystorefilename = keystores.get(i).getKeystorefilename();
                keystoreCombo.add(keystorefilename);
                if (hasSelection && selectedFilename.equals(keystorefilename)) {
                    keystoreCombo.select(i + 1);
                }
            }
        }
        return keystoreCombo;
    }
}
