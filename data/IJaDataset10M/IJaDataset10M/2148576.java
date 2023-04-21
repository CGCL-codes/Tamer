package org.eclipse.datatools.enablement.msft.sqlserver.internal.ui.connection.drivers;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.eclipse.datatools.connectivity.drivers.jdbc.IJDBCConnectionProfileConstants;
import org.eclipse.datatools.connectivity.drivers.jdbc.IJDBCDriverDefinitionConstants;
import org.eclipse.datatools.connectivity.ui.wizards.IDriverUIContributor;
import org.eclipse.datatools.connectivity.ui.wizards.IDriverUIContributorInformation;
import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import com.ibm.icu.text.MessageFormat;

public class SQLServer2000DriverUIContributor implements IDriverUIContributor, Listener {

    private static final String CUI_NEWCW_DATABASE_LBL_UI_ = Messages.getString("CUI_NEWCW_DATABASE_LBL_UI_");

    private static final String CUI_NEWCW_HOST_LBL_UI_ = Messages.getString("CUI_NEWCW_HOST_LBL_UI_");

    private static final String CUI_NEWCW_PORT_LBL_UI_ = Messages.getString("CUI_NEWCW_PORT_LBL_UI_");

    private static final String CUI_NEWCW_USERNAME_LBL_UI_ = Messages.getString("CUI_NEWCW_USERNAME_LBL_UI_");

    private static final String CUI_NEWCW_PASSWORD_LBL_UI_ = Messages.getString("CUI_NEWCW_PASSWORD_LBL_UI_");

    private static final String CUI_NEWCW_SAVE_PASSWORD_LBL_UI_ = Messages.getString("CUI_NEWCW_SAVE_PASSWORD_LBL_UI_");

    private static final String CUI_NEWCW_CONNECTIONURL_LBL_UI_ = Messages.getString("CUI_NEWCW_CONNECTIONURL_LBL_UI_");

    private static final String CUI_NEWCW_DEFDBNAME_VAL_UI_ = Messages.getString("CUI_NEWCW_DEFDBNAME_VAL_UI_");

    private static final String CUI_NEWCW_DATABASE_SUMMARY_DATA_TEXT_ = Messages.getString("CUI_NEWCW_DATABASE_SUMMARY_DATA_TEXT_");

    private static final String CUI_NEWCW_HOST_SUMMARY_DATA_TEXT_ = Messages.getString("CUI_NEWCW_HOST_SUMMARY_DATA_TEXT_");

    private static final String CUI_NEWCW_PORT_SUMMARY_DATA_TEXT_ = Messages.getString("CUI_NEWCW_PORT_SUMMARY_DATA_TEXT_");

    private static final String CUI_NEWCW_USERNAME_SUMMARY_DATA_TEXT_ = Messages.getString("CUI_NEWCW_USERNAME_SUMMARY_DATA_TEXT_");

    private static final String CUI_NEWCW_SAVE_PASSWORD_SUMMARY_DATA_TEXT_ = Messages.getString("CUI_NEWCW_SAVE_PASSWORD_SUMMARY_DATA_TEXT_");

    private static final String CUI_NEWCW_TRUE_SUMMARY_DATA_TEXT_ = Messages.getString("CUI_NEWCW_TRUE_SUMMARY_DATA_TEXT_");

    private static final String CUI_NEWCW_FALSE_SUMMARY_DATA_TEXT_ = Messages.getString("CUI_NEWCW_FALSE_SUMMARY_DATA_TEXT_");

    private static final String CUI_NEWCW_URL_SUMMARY_DATA_TEXT_ = Messages.getString("CUI_NEWCW_URL_SUMMARY_DATA_TEXT_");

    protected IDriverUIContributorInformation contributorInformation;

    private Label databaseLabel;

    protected Text databaseText;

    private Label hostLabel;

    protected Text hostText;

    private Label portLabel;

    protected Text portText;

    private Label usernameLabel;

    private Text usernameText;

    private Label passwordLabel;

    private Text passwordText;

    private Button savePasswordButton;

    private Label urlLabel;

    protected Text urlText;

    private DialogPage parentPage;

    private ScrolledComposite parentComposite;

    private Properties properties;

    private boolean isReadOnly = false;

    public boolean determineContributorCompletion() {
        boolean isComplete = true;
        if (databaseText.getText().trim().length() < 1) {
            parentPage.setErrorMessage(MessageFormat.format(Messages.getString("CUI_NEWCW_VALIDATE_DATABASE_REQ_UI_"), new String[] { CUI_NEWCW_DEFDBNAME_VAL_UI_ }));
            isComplete = false;
        } else if (hostText.getText().length() < 1) {
            parentPage.setErrorMessage(Messages.getString("CUI_NEWCW_VALIDATE_HOST_REQ_UI_"));
            isComplete = false;
        } else if (portText.getText().length() < 1) {
            parentPage.setErrorMessage(Messages.getString("CUI_NEWCW_VALIDATE_PORT_REQ_UI_"));
            isComplete = false;
        } else if (usernameText.getText().trim().length() < 1) {
            parentPage.setErrorMessage(Messages.getString("CUI_NEWCW_VALIDATE_USERID_REQ_UI_"));
            isComplete = false;
        } else if (passwordText.getText().trim().length() < 1) {
            parentPage.setErrorMessage(Messages.getString("CUI_NEWCW_VALIDATE_PASSWORD_REQ_UI_"));
            isComplete = false;
        } else if (urlText.getText().trim().length() < 1) {
            parentPage.setErrorMessage(Messages.getString("CUI_NEWCW_VALIDATE_URL_REQ_UI_"));
            isComplete = false;
        }
        return isComplete;
    }

    public Composite getContributedDriverUI(Composite parent, boolean isReadOnly) {
        if ((parentComposite == null) || parentComposite.isDisposed() || (this.isReadOnly != isReadOnly)) {
            GridData gd;
            this.isReadOnly = isReadOnly;
            int additionalStyles = SWT.NONE;
            if (isReadOnly) {
                additionalStyles = SWT.READ_ONLY;
            }
            parentComposite = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
            parentComposite.setExpandHorizontal(true);
            parentComposite.setExpandVertical(true);
            parentComposite.setLayout(new GridLayout());
            Composite baseComposite = new Composite(parentComposite, SWT.NULL);
            GridLayout layout = new GridLayout();
            layout.numColumns = 3;
            baseComposite.setLayout(layout);
            databaseLabel = new Label(baseComposite, SWT.NONE);
            databaseLabel.setText(CUI_NEWCW_DATABASE_LBL_UI_);
            gd = new GridData();
            gd.verticalAlignment = GridData.BEGINNING;
            databaseLabel.setLayoutData(gd);
            databaseText = new Text(baseComposite, SWT.SINGLE | SWT.BORDER | additionalStyles);
            gd = new GridData();
            gd.verticalAlignment = GridData.BEGINNING;
            gd.horizontalAlignment = GridData.FILL;
            gd.horizontalSpan = 2;
            databaseText.setLayoutData(gd);
            hostLabel = new Label(baseComposite, SWT.NONE);
            hostLabel.setText(CUI_NEWCW_HOST_LBL_UI_);
            gd = new GridData();
            gd.verticalAlignment = GridData.BEGINNING;
            gd.horizontalSpan = 1;
            hostLabel.setLayoutData(gd);
            hostText = new Text(baseComposite, SWT.SINGLE | SWT.BORDER | additionalStyles);
            gd = new GridData();
            gd.horizontalAlignment = GridData.FILL;
            gd.verticalAlignment = GridData.BEGINNING;
            gd.horizontalSpan = 2;
            gd.grabExcessHorizontalSpace = true;
            hostText.setLayoutData(gd);
            portLabel = new Label(baseComposite, SWT.NONE);
            portLabel.setText(CUI_NEWCW_PORT_LBL_UI_);
            gd = new GridData();
            gd.verticalAlignment = GridData.BEGINNING;
            gd.horizontalSpan = 1;
            portLabel.setLayoutData(gd);
            portText = new Text(baseComposite, SWT.SINGLE | SWT.BORDER | additionalStyles);
            gd = new GridData();
            gd.horizontalAlignment = GridData.FILL;
            gd.verticalAlignment = GridData.BEGINNING;
            gd.grabExcessHorizontalSpace = true;
            gd.horizontalSpan = 2;
            portText.setLayoutData(gd);
            usernameLabel = new Label(baseComposite, SWT.NONE);
            usernameLabel.setText(CUI_NEWCW_USERNAME_LBL_UI_);
            gd = new GridData();
            gd.verticalAlignment = GridData.BEGINNING;
            usernameLabel.setLayoutData(gd);
            usernameText = new Text(baseComposite, SWT.SINGLE | SWT.BORDER | additionalStyles);
            gd = new GridData();
            gd.horizontalAlignment = GridData.FILL;
            gd.verticalAlignment = GridData.BEGINNING;
            gd.grabExcessHorizontalSpace = true;
            gd.horizontalSpan = 2;
            usernameText.setLayoutData(gd);
            passwordLabel = new Label(baseComposite, SWT.NONE);
            passwordLabel.setText(CUI_NEWCW_PASSWORD_LBL_UI_);
            gd = new GridData();
            gd.verticalAlignment = GridData.BEGINNING;
            passwordLabel.setLayoutData(gd);
            passwordText = new Text(baseComposite, SWT.SINGLE | SWT.BORDER | SWT.PASSWORD | additionalStyles);
            gd = new GridData();
            gd.horizontalAlignment = GridData.FILL;
            gd.verticalAlignment = GridData.BEGINNING;
            gd.grabExcessHorizontalSpace = true;
            gd.horizontalSpan = 2;
            passwordText.setLayoutData(gd);
            this.savePasswordButton = new Button(baseComposite, SWT.CHECK);
            this.savePasswordButton.setText(CUI_NEWCW_SAVE_PASSWORD_LBL_UI_);
            gd = new GridData();
            gd.horizontalAlignment = GridData.FILL;
            gd.verticalAlignment = GridData.BEGINNING;
            gd.horizontalSpan = 3;
            gd.grabExcessHorizontalSpace = true;
            savePasswordButton.setLayoutData(gd);
            urlLabel = new Label(baseComposite, SWT.NONE);
            urlLabel.setText(CUI_NEWCW_CONNECTIONURL_LBL_UI_);
            gd = new GridData();
            gd.verticalAlignment = GridData.BEGINNING;
            gd.horizontalSpan = 1;
            urlLabel.setLayoutData(gd);
            urlText = new Text(baseComposite, SWT.MULTI | SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL);
            gd = new GridData();
            gd.horizontalAlignment = GridData.FILL;
            gd.verticalAlignment = GridData.BEGINNING;
            gd.grabExcessHorizontalSpace = true;
            gd.horizontalSpan = 2;
            gd.widthHint = 190;
            gd.heightHint = 50;
            urlText.setLayoutData(gd);
            parentComposite.setContent(baseComposite);
            parentComposite.setMinSize(baseComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
            initialize();
        }
        return parentComposite;
    }

    public List getSummaryData() {
        List summaryData = new ArrayList();
        summaryData.add(new String[] { CUI_NEWCW_DATABASE_SUMMARY_DATA_TEXT_, this.databaseText.getText().trim() });
        summaryData.add(new String[] { CUI_NEWCW_HOST_SUMMARY_DATA_TEXT_, this.hostText.getText().trim() });
        summaryData.add(new String[] { CUI_NEWCW_PORT_SUMMARY_DATA_TEXT_, this.portText.getText().trim() });
        summaryData.add(new String[] { CUI_NEWCW_USERNAME_SUMMARY_DATA_TEXT_, this.usernameText.getText().trim() });
        summaryData.add(new String[] { CUI_NEWCW_SAVE_PASSWORD_SUMMARY_DATA_TEXT_, savePasswordButton.getSelection() ? CUI_NEWCW_TRUE_SUMMARY_DATA_TEXT_ : CUI_NEWCW_FALSE_SUMMARY_DATA_TEXT_ });
        summaryData.add(new String[] { CUI_NEWCW_URL_SUMMARY_DATA_TEXT_, this.urlText.getText().trim() });
        return summaryData;
    }

    public void loadProperties() {
        removeListeners();
        SqlServerJDBCURL url = new SqlServerJDBCURL(this.properties.getProperty(IJDBCDriverDefinitionConstants.URL_PROP_ID));
        hostText.setText(url.getNode());
        portText.setText(url.getPort());
        databaseText.setText(url.getDatabaseName());
        String username = this.properties.getProperty(IJDBCDriverDefinitionConstants.USERNAME_PROP_ID);
        if (username != null) {
            usernameText.setText(username);
        }
        String password = this.properties.getProperty(IJDBCDriverDefinitionConstants.PASSWORD_PROP_ID);
        if (password != null) {
            passwordText.setText(password);
        }
        String savePassword = this.properties.getProperty(IJDBCConnectionProfileConstants.SAVE_PASSWORD_PROP_ID);
        if ((savePassword != null) && Boolean.valueOf(savePassword) == Boolean.TRUE) {
            savePasswordButton.setSelection(true);
        }
        updateURL();
        addListeners();
        setConnectionInformation();
    }

    public void setDialogPage(DialogPage parentPage) {
        this.parentPage = parentPage;
    }

    public void setDriverUIContributorInformation(IDriverUIContributorInformation contributorInformation) {
        this.contributorInformation = contributorInformation;
        this.properties = contributorInformation.getProperties();
    }

    public void handleEvent(Event event) {
        if (isReadOnly) {
            if (event.widget == savePasswordButton) {
                savePasswordButton.setSelection(!savePasswordButton.getSelection());
            }
        } else {
            updateURL();
            setConnectionInformation();
        }
    }

    private void setConnectionInformation() {
        properties.setProperty(IJDBCDriverDefinitionConstants.DATABASE_NAME_PROP_ID, this.databaseText.getText().trim());
        properties.setProperty(IJDBCDriverDefinitionConstants.PASSWORD_PROP_ID, this.passwordText.getText());
        properties.setProperty(IJDBCConnectionProfileConstants.SAVE_PASSWORD_PROP_ID, String.valueOf(savePasswordButton.getSelection()));
        properties.setProperty(IJDBCDriverDefinitionConstants.USERNAME_PROP_ID, this.usernameText.getText());
        properties.setProperty(IJDBCDriverDefinitionConstants.URL_PROP_ID, this.urlText.getText().trim());
        this.contributorInformation.setProperties(properties);
    }

    private void initialize() {
        updateURL();
        addListeners();
    }

    private void addListeners() {
        databaseText.addListener(SWT.Modify, this);
        hostText.addListener(SWT.Modify, this);
        portText.addListener(SWT.Modify, this);
        usernameText.addListener(SWT.Modify, this);
        passwordText.addListener(SWT.Modify, this);
        savePasswordButton.addListener(SWT.Selection, this);
    }

    private void removeListeners() {
        databaseText.removeListener(SWT.Modify, this);
        hostText.removeListener(SWT.Modify, this);
        portText.removeListener(SWT.Modify, this);
        usernameText.removeListener(SWT.Modify, this);
        passwordText.removeListener(SWT.Modify, this);
        savePasswordButton.removeListener(SWT.Selection, this);
    }

    protected void updateURL() {
        String url = "jdbc:microsoft:sqlserver://" + hostText.getText() + ":" + portText.getText() + ";databaseName=" + databaseText.getText();
        urlText.setText(url);
    }

    private class SqlServerJDBCURL {

        private String subprotocol = "";

        private String node = "";

        private String port = "";

        private String databaseName = "";

        private String properties = "";

        /**
		 * @param url
		 */
        public SqlServerJDBCURL(String url) {
            parseURL(url);
        }

        /**
		 * @return Returns the databaseName.
		 */
        public String getDatabaseName() {
            return databaseName;
        }

        /**
		 * @return Returns the node.
		 */
        public String getNode() {
            return node;
        }

        /**
		 * @return Returns the subprotocol.
		 */
        public String getSubprotocol() {
            return subprotocol;
        }

        private void parseURL(String url) {
            try {
                String remainingURL = url.substring(url.indexOf(':') + 1);
                this.subprotocol = remainingURL.substring(0, remainingURL.indexOf(':'));
                remainingURL = remainingURL.substring(remainingURL.indexOf(':') + 1);
                this.subprotocol = this.subprotocol + remainingURL.substring(0, remainingURL.indexOf(':'));
                remainingURL = remainingURL.substring(remainingURL.indexOf(':') + 3);
                this.node = remainingURL.substring(0, remainingURL.indexOf(';'));
                if (this.node.indexOf(':') > -1) {
                    this.port = this.node.substring(this.node.indexOf(':') + 1);
                    this.node = this.node.substring(0, this.node.indexOf(':'));
                }
                remainingURL = remainingURL.substring(remainingURL.indexOf(';') + 1);
                if (remainingURL.indexOf(';') > -1) {
                    this.databaseName = remainingURL.substring(0, remainingURL.indexOf(';'));
                    this.databaseName = this.databaseName.substring(new String("databaseName=").length());
                    remainingURL = remainingURL.substring(remainingURL.indexOf(';') + 1);
                    this.properties = remainingURL;
                } else {
                    this.databaseName = remainingURL.substring(new String("databaseName=").length());
                }
            } catch (Exception e) {
            }
        }

        /**
		 * @return Returns the port.
		 */
        public String getPort() {
            return port;
        }

        /**
		 * @return Returns the properties.
		 */
        public String getProperties() {
            return properties;
        }
    }
}
