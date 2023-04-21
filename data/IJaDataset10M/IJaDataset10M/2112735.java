package it.battlehorse.rcp.tools.dbbase.preferences;

import it.battlehorse.rcp.sl.annot.Injected;
import it.battlehorse.rcp.sl.annot.Serviceable;
import it.battlehorse.rcp.tools.dbbase.DatasourceConfig;
import it.battlehorse.rcp.tools.dbbase.DbBaseActivator;
import it.battlehorse.rcp.tools.dbbase.DbConfig;
import it.battlehorse.rcp.tools.dbbase.IDbManager;
import java.util.List;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Defines the dialog window used to add/edit a datasource configuration in 
 * the datasource preferences pane.
 * 
 * @author battlehorse
 * @since Nov 22, 2005
 */
@Serviceable
public class DatasourceEditDialog extends TitleAreaDialog implements FocusListener, KeyListener {

    private boolean addMode;

    private Image dialogImage;

    /**
	 * Contains the datasource configuration which is returned once editing is finished
	 */
    private DatasourceConfig datasourceConfig;

    /**
	 * Contains the datasource which is being edited (if we're not adding a new one)
	 */
    private DatasourceConfig initialDatasourceConfig;

    /**
	 * The list of the current datasource names
	 */
    private List<String> currentNames;

    private Combo dataSourceCombo;

    private Text dsNameText, urlText, userText, passwordText;

    /**
	 * A reference to the button which confirms the dialog
	 */
    private Button okButton;

    private Image lockImage;

    private IDbManager dbManager;

    /**
	 * Injects a db manager into this object
	 * 
	 * @param manager a db manager
	 */
    @Injected
    public void setDbManager(IDbManager manager) {
        this.dbManager = manager;
    }

    /**
	 * Creates a new instance of the class
	 * 
	 * @param parentShell the parent shell
	 * @param addMode defines if the dialog is used to Add or to Edit a datasource
	 * 		configuration
	 * @param currentNames the list of the current datasource names
	 */
    public DatasourceEditDialog(Shell parentShell, boolean addMode, List<String> currentNames) {
        this(parentShell, addMode, null, currentNames);
    }

    /**
	 * Creates a new instance of the class
	 * 
	 * @param parentShell the parent shell
	 * @param addMode defines if the dialog is used to Add or to Edit a datasource
	 * 		configuration
	 * @param dsConfig the datasource config which has to be edited.
	 * @param currentNames the list of the current datasource names 
	 */
    public DatasourceEditDialog(Shell parentShell, boolean addMode, DatasourceConfig dsConfig, List<String> currentNames) {
        super(parentShell);
        this.addMode = addMode;
        dialogImage = DbBaseActivator.getImageDescriptor("resources/datasource_dialog.png").createImage();
        datasourceConfig = null;
        initialDatasourceConfig = dsConfig;
        this.currentNames = currentNames;
        lockImage = DbBaseActivator.getImageDescriptor("resources/lock.png").createImage();
    }

    @Override
    protected Control createContents(Composite parent) {
        Control contents = super.createContents(parent);
        super.setTitleImage(dialogImage);
        if (addMode) {
            super.setTitle("Add new Datasource");
            super.setMessage("Add a new datasource to the platform");
        } else {
            super.setTitle("Edit Datasource");
            super.setMessage("Edit the selected datasource");
        }
        return contents;
    }

    @Override
    public boolean close() {
        if (lockImage != null) lockImage.dispose();
        dialogImage.dispose();
        return super.close();
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite c = (Composite) super.createDialogArea(parent);
        Composite area = new Composite(c, SWT.NONE);
        area.setLayout(new GridLayout(2, false));
        GridData gd;
        Label l;
        l = new Label(area, SWT.NONE);
        l.setText("Database: ");
        l.setToolTipText("Select the database among the available ones");
        dataSourceCombo = new Combo(area, SWT.DROP_DOWN | SWT.READ_ONLY);
        List<DbConfig> configs = dbManager.getDbConfigList();
        if (configs != null) {
            for (DbConfig config : configs) {
                dataSourceCombo.add(config.getName());
            }
        }
        gd = new GridData();
        gd.grabExcessHorizontalSpace = true;
        dataSourceCombo.setLayoutData(gd);
        dataSourceCombo.addFocusListener(this);
        if (initialDatasourceConfig != null) dataSourceCombo.setText(initialDatasourceConfig.getDatabase().getName());
        dsNameText = createTextRow(area, "Datasource Name:", "Select the datasource name. Actors and monitors will use this name " + "to retrieve a database connection.", false, initialDatasourceConfig != null ? initialDatasourceConfig.getName() : null, !addMode);
        urlText = createTextRow(area, "Connection URL:", "The JDBC database connection URL", false, initialDatasourceConfig != null ? initialDatasourceConfig.getConnectionUrl() : null, false);
        userText = createTextRow(area, "User:", "The username to be used to connect to the database", false, initialDatasourceConfig != null ? initialDatasourceConfig.getUser() : null, false);
        passwordText = createTextRow(area, "Password:", "The password to be used to connect to the database", true, initialDatasourceConfig != null ? initialDatasourceConfig.getPassword() : null, false);
        return c;
    }

    /**
	 * Creates a row composed of a label and a text box.
	 * 
	 * @param parent the SWT parent
	 * @param labelText the label text
	 * @param labelTooltip the label tooltip
	 * @param isPassword defines if the text box is a password one or not
	 * @param initialValue the optional initial value (otherwise {@code null}).
	 * @param lock decide if the row has to be locked
	 * @return the text box reference
	 */
    private Text createTextRow(Composite parent, String labelText, String labelTooltip, boolean isPassword, String initialValue, boolean lock) {
        CLabel l;
        l = new CLabel(parent, SWT.NONE);
        l.setText(labelText);
        l.setToolTipText(labelTooltip);
        if (lock) l.setImage(lockImage);
        Text t;
        if (!isPassword) t = new Text(parent, SWT.BORDER); else t = new Text(parent, SWT.BORDER | SWT.PASSWORD);
        GridData gd = new GridData();
        gd.grabExcessHorizontalSpace = true;
        gd.horizontalAlignment = SWT.FILL;
        gd.widthHint = 300;
        t.setLayoutData(gd);
        t.addKeyListener(this);
        if (initialValue != null) t.setText(initialValue);
        if (lock) t.setEnabled(false);
        return t;
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        okButton = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
    }

    @Override
    protected void buttonPressed(int buttonId) {
        setReturnCode(buttonId);
        if (buttonId == IDialogConstants.OK_ID) {
            DbConfig dbase = dbManager.findDbConfig(dataSourceCombo.getText());
            datasourceConfig = new DatasourceConfig(dbase, urlText.getText(), userText.getText(), passwordText.getText(), dsNameText.getText());
        }
        close();
    }

    /**
	 * Returns the datasource configured using this dialog, or {@code null} if the
	 * dialog has been canceled.
	 * 
	 * @return the datasource configured using this dialog, or {@code null} if the
	 * 	dialog has been canceled.
	 */
    public DatasourceConfig getDatasourceConfig() {
        return datasourceConfig;
    }

    /**
	 * Validates the dialog fields
	 *
	 */
    private void validate() {
        boolean valid = true;
        String message = null;
        if (dbManager.findDbConfig(dataSourceCombo.getText()) == null) {
            valid = false;
            message = "You must select a valid database";
        }
        if (valid && dsNameText.getText().trim().length() == 0) {
            valid = false;
            message = "You must define the datasource name";
        }
        if (valid && addMode && currentNames != null && currentNames.contains(dsNameText.getText())) {
            valid = false;
            message = "The specified datasource name already exists";
        }
        if (valid && urlText.getText().trim().length() == 0) {
            valid = false;
            message = "You must define the connection URL";
        }
        if (!valid) {
            okButton.setEnabled(false);
            setMessage(message, IMessageProvider.ERROR);
        } else {
            okButton.setEnabled(true);
            if (addMode) {
                DatasourceEditDialog.this.setMessage("Add a new datasource to the platform");
            } else {
                DatasourceEditDialog.this.setMessage("Edit the selected datasource");
            }
        }
    }

    public void focusLost(FocusEvent e) {
        validate();
    }

    public void focusGained(FocusEvent e) {
        validate();
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
        validate();
    }
}
