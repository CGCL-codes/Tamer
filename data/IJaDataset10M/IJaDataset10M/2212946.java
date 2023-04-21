package passreminder.ui;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import passreminder.DBManager;
import passreminder.Messages;
import passreminder.PreferenceConstants;
import passreminder.PreferenceManager;

/**
 * @author Eyecanseeyou
 * 
 */
public class SearchDialog extends Dialog {

    protected String[] cols = null;

    protected Combo searchCombo;

    protected Text expirationStartField;

    protected Label expirationLabel = null;

    protected Text expirationEndField;

    private Date expirationStartDate;

    private Date expirationEndDate;

    protected Button expirationCheckbox;

    protected Text lastModificationStartField;

    protected Label lastModificationLabel = null;

    protected Text lastModificationEndField;

    private Date lastModificationStartDate;

    private Date lastModificationEndDate;

    protected Button lastModificationCheckbox;

    protected String search = null;

    protected Image keyLockImage;

    protected String message = null;

    protected Label warningText = null;

    protected Label warningLabel = null;

    private SimpleDateFormat sdf = null;

    public SearchDialog(Shell parentShell, String message) {
        super(parentShell);
        this.message = message;
        sdf = new SimpleDateFormat(PreferenceManager.getInstance().getPreferenceStore().getString(PreferenceConstants.DEFAULT_DATE_FORMAT));
        sdf.setLenient(false);
    }

    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(Messages.getString("dialog.search.title"));
    }

    public void create() {
        super.create();
        searchCombo.setFocus();
        dialogChanged();
        stateChanged();
    }

    protected Control createDialogArea(Composite parent) {
        Composite top = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        top.setLayout(layout);
        top.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        Composite imageComposite = new Composite(top, SWT.NONE);
        layout = new GridLayout();
        imageComposite.setLayout(layout);
        imageComposite.setLayoutData(new GridData(GridData.FILL_VERTICAL));
        Composite main = new Composite(top, SWT.NONE);
        layout = new GridLayout();
        layout.numColumns = 4;
        main.setLayout(layout);
        main.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        Label imageLabel = new Label(imageComposite, SWT.NONE);
        keyLockImage = ImageDescriptor.createFromURL(getClass().getResource("/passreminder/action/search.gif")).createImage();
        imageLabel.setImage(keyLockImage);
        GridData data = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
        imageLabel.setLayoutData(data);
        if (message != null) {
            Label messageLabel = new Label(main, SWT.WRAP);
            messageLabel.setText(message + "\n ");
            data = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
            data.horizontalSpan = 4;
            data.widthHint = 300;
            messageLabel.setLayoutData(data);
        }
        createSearchField(main);
        Composite warningComposite = new Composite(main, SWT.NONE);
        layout = new GridLayout();
        layout.numColumns = 2;
        layout.marginHeight = 0;
        layout.marginHeight = 10;
        warningComposite.setLayout(layout);
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 3;
        warningComposite.setLayoutData(data);
        warningLabel = new Label(warningComposite, SWT.NONE);
        warningLabel.setImage(getImage(DLG_IMG_MESSAGE_WARNING));
        warningLabel.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.HORIZONTAL_ALIGN_BEGINNING));
        warningText = new Label(warningComposite, SWT.WRAP);
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.widthHint = 300;
        warningText.setLayoutData(data);
        Dialog.applyDialogFont(parent);
        return main;
    }

    /**
	 * Create a spacer.
	 */
    protected void createSpacer(Composite top, int columnSpan, int vertSpan) {
        Label l = new Label(top, SWT.NONE);
        GridData data = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
        data.horizontalSpan = columnSpan;
        data.verticalSpan = vertSpan;
        l.setLayoutData(data);
    }

    /**
	 * Creates the three widgets that represent the password entry area.
	 * 
	 * @param parent
	 *            the parent of the widgets
	 */
    protected void createDateCriteriaArea(Composite parent) {
        expirationCheckbox = new Button(parent, SWT.CHECK);
        expirationCheckbox.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event evt) {
                stateChanged();
            }
        });
        expirationStartField = new Text(parent, SWT.BORDER);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        expirationLabel = new Label(parent, SWT.NONE | SWT.CENTER);
        expirationLabel.setText(Messages.getString("dialog.search.range.expiration_date", PreferenceManager.getInstance().getPreferenceStore().getString(PreferenceConstants.DEFAULT_DATE_FORMAT)));
        expirationEndField = new Text(parent, SWT.BORDER);
        data = new GridData(GridData.FILL_HORIZONTAL);
        expirationEndField.setLayoutData(data);
        expirationStartField.addListener(SWT.Modify, new Listener() {

            public void handleEvent(Event event) {
                dialogChanged();
            }
        });
        expirationEndField.addListener(SWT.Modify, new Listener() {

            public void handleEvent(Event event) {
                dialogChanged();
            }
        });
        lastModificationCheckbox = new Button(parent, SWT.CHECK);
        lastModificationCheckbox.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event evt) {
                stateChanged();
            }
        });
        lastModificationStartField = new Text(parent, SWT.BORDER);
        data = new GridData(GridData.FILL_HORIZONTAL);
        lastModificationLabel = new Label(parent, SWT.NONE | SWT.CENTER);
        lastModificationLabel.setText(Messages.getString("dialog.search.range.expiration_date", PreferenceManager.getInstance().getPreferenceStore().getString(PreferenceConstants.DEFAULT_DATE_FORMAT)));
        lastModificationEndField = new Text(parent, SWT.BORDER);
        data = new GridData(GridData.FILL_HORIZONTAL);
        lastModificationEndField.setLayoutData(data);
        lastModificationStartField.addListener(SWT.Modify, new Listener() {

            public void handleEvent(Event event) {
                dialogChanged();
            }
        });
        lastModificationEndField.addListener(SWT.Modify, new Listener() {

            public void handleEvent(Event event) {
                dialogChanged();
            }
        });
    }

    private void stateChanged() {
    }

    protected void createSearchField(Composite parent) {
        new Label(parent, SWT.NONE).setText(Messages.getString("dialog.search.search_for"));
        searchCombo = new Combo(parent, SWT.BORDER);
        for (int i = DBManager.getInstance().lastSearches.size() - 1; i >= 0; i--) {
            System.out.println((String) DBManager.getInstance().lastSearches.get(i));
            searchCombo.add((String) DBManager.getInstance().lastSearches.get(i));
        }
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 3;
        data.widthHint = convertHorizontalDLUsToPixels(IDialogConstants.ENTRY_FIELD_WIDTH);
        searchCombo.setLayoutData(data);
        searchCombo.addListener(SWT.Modify, new Listener() {

            public void handleEvent(Event event) {
                dialogChanged();
            }
        });
    }

    private void dialogChanged() {
        if (searchCombo.getText().length() == 0) {
            warningText.setVisible(true);
            warningLabel.setVisible(true);
            warningText.setText(Messages.getString("dialog.search.search.required"));
            getButton(IDialogConstants.OK_ID).setEnabled(false);
            return;
        }
        getButton(IDialogConstants.OK_ID).setEnabled(true);
        warningText.setVisible(false);
        warningLabel.setVisible(false);
    }

    private void computeDate() {
        try {
            if (expirationStartField.getText().length() == PreferenceManager.getInstance().getPreferenceStore().getString(PreferenceConstants.DEFAULT_DATE_FORMAT).length()) expirationStartDate = sdf.parse(expirationStartField.getText()); else expirationStartDate = null;
        } catch (Exception e) {
            expirationStartDate = null;
        }
        try {
            if (expirationEndField.getText().length() == PreferenceManager.getInstance().getPreferenceStore().getString(PreferenceConstants.DEFAULT_DATE_FORMAT).length()) expirationEndDate = sdf.parse(expirationEndField.getText()); else expirationEndDate = null;
        } catch (Exception e) {
            expirationEndDate = null;
        }
    }

    public String getSearch() {
        return search;
    }

    protected void okPressed() {
        computeDate();
        search = searchCombo.getText();
        DBManager.getInstance().lastSearches.remove(search);
        DBManager.getInstance().lastSearches.add(search);
        DBManager.getInstance().isMetadataSaved = false;
        super.okPressed();
    }

    public boolean close() {
        if (keyLockImage != null) {
            keyLockImage.dispose();
        }
        return super.close();
    }
}
