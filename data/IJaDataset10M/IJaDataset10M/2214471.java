package ch.sahits.codegen.java.internal.wizards;

import java.sql.SQLException;
import java.util.List;
import java.util.Vector;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import ch.sahits.codegen.help.ISahitsHelpSystem;
import ch.sahits.codegen.i18n.JavaMessages;
import ch.sahits.codegen.java.extensions.InputDBConnection;
import ch.sahits.codegen.java.extensions.InputDBParser;
import ch.sahits.codegen.java.input.IDBInputParser;
import ch.sahits.codegen.java.model.IDBConnectionModelGenerator;
import ch.sahits.codegen.java.wizards.BasicCodeGenWizard;
import ch.sahits.codegen.java.wizards.BasicDBDefinitionPage;
import ch.sahits.codegen.java.wizards.JavaCodeGenerator;

/**
 * This WizardPage let's you supply the database specific data. 
 * This class watches  {@link JavaCodeGenerator}  for any change 
 * of the dbProduct.
 * @author  Andi Hotz
 * @since 0.9.2
 */
public class DBDefinitionPage extends BasicDBDefinitionPage {

    /** Input field for the host */
    private Text txtHost;

    /** Input field of the port number */
    private Text txtPort;

    /** Input field for the user name */
    private Text txtUserName;

    /** Input field for the password */
    private Text txtPassword;

    /** Input field for the database name */
    private Text txtName;

    /** Input field for the schema/owner name */
    private Text txtSchema;

    /** Input field for table name */
    private Text txtTable;

    /**
	 * Constructor for creating this page. This constructor is used through reflection
	 * @param pageTitle title of the page
	 * @param caller calling wizard
	 */
    public DBDefinitionPage(String pageTitle, BasicCodeGenWizard caller) {
        super(pageTitle, caller);
        setTitle(JavaMessages.DBDefinitionPage_2);
        setDescription(JavaMessages.DBDefinitionPage_3);
        setImage();
    }

    /**
	 * Retrieve the model generator for a definition page. Basically any implementation
	 * of this class should also be implemented as static method since when calling this
	 * method the calling wizard may be unknown in the calling class 
	 * @param dbProduct Vendor name of the database product.
	 * @return Model generator for a connection
	 */
    public IDBConnectionModelGenerator getModelGenerator(String dbProduct) {
        return InputDBConnection.getParser(dbProduct);
    }

    /**
	 * Retrieve a list of all database product supported by
	 * this implementation of the extension point ch.sahits.codegen.java.connectionpage
	 * @return List of all database product
	 */
    @Override
    public List<String> getDataBaseProducts() {
        String[] products = InputDBConnection.getDataBaseProducts();
        Vector<String> v = new Vector<String>();
        for (int i = 0; i < products.length; i++) {
            v.add(products[i]);
        }
        return v;
    }

    /**
	 * Create the input field for the host
	 * @param container parent container
	 */
    protected void createHostInput(Composite container) {
        txtHost = new Text(container, SWT.BORDER | SWT.SINGLE);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        txtHost.setLayoutData(gd);
        txtHost.setToolTipText(JavaMessages.DBDefinitionPage_12);
        txtHost.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                setHost(txtHost.getText());
                setConnectionTestedFalse();
                dialogChanged();
            }
        });
    }

    /**
	 * Create the input part for the port number
	 * @param container parent container
	 */
    protected void creatInputPort(Composite container) {
        txtPort = new Text(container, SWT.BORDER | SWT.SINGLE);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        txtPort.setLayoutData(gd);
        txtPort.setToolTipText(JavaMessages.DBDefinitionPage_14);
        txtPort.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                setPort(txtPort.getText());
                setConnectionTestedFalse();
                dialogChanged();
            }
        });
    }

    /**
	 * Create the input field for the user name
	 * @param container parent container
	 */
    protected void createInputUserName(Composite container) {
        txtUserName = new Text(container, SWT.BORDER | SWT.SINGLE);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        txtUserName.setLayoutData(gd);
        txtUserName.setToolTipText(JavaMessages.DBDefinitionPage_16);
        txtUserName.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                setUser(txtUserName.getText());
                setConnectionTestedFalse();
                dialogChanged();
            }
        });
    }

    /**
	 * Create the input field for the password
	 * @param container parent container
	 */
    protected void createInputPassword(Composite container) {
        txtPassword = new Text(container, SWT.BORDER | SWT.PASSWORD);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        txtPassword.setLayoutData(gd);
        txtPassword.setToolTipText(JavaMessages.DBDefinitionPage_18);
        txtPassword.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                setPasswd(txtPassword.getText());
                setConnectionTestedFalse();
                dialogChanged();
            }
        });
    }

    /**
	 * Create the input field for the database name
	 * @param container parent container
	 */
    protected void createInputDatabaseName(Composite container) {
        txtName = new Text(container, SWT.BORDER | SWT.SINGLE);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        txtName.setLayoutData(gd);
        txtName.setToolTipText(JavaMessages.DBDefinitionPage_20);
        txtName.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                setDbName(txtName.getText());
                setConnectionTestedFalse();
                dialogChanged();
            }
        });
    }

    /**
	 * Create the input field for the schema/owner
	 * @param container parent container
	 */
    protected void createInputSchema(Composite container) {
        txtSchema = new Text(container, SWT.BORDER | SWT.SINGLE);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        txtSchema.setLayoutData(gd);
        txtSchema.setToolTipText(JavaMessages.DBDefinitionPage_10);
        txtSchema.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                setSchema(txtSchema.getText());
                setConnectionTestedFalse();
                dialogChanged();
            }
        });
        txtSchema.setEnabled(false);
    }

    /**
	 * Create the input field for the table name
	 * @param container parent container
	 */
    protected void createInputTableName(Composite container) {
        txtTable = new Text(container, SWT.BORDER | SWT.SINGLE);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        txtTable.setLayoutData(gd);
        txtTable.setToolTipText(JavaMessages.DBDefinitionPage_8);
        txtTable.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                setTableName(txtTable.getText());
                setConnectionTestedFalse();
                dialogChanged();
            }
        });
    }

    /**
	 * @param _host the host to set
	 */
    public void setHost(String _host) {
        this.host = _host;
    }

    /**
	 * @param _port the port to set
	 */
    public void setPort(String _port) {
        this.port = _port;
    }

    /**
	 * @param _user the user to set
	 */
    public void setUser(String _user) {
        this.user = _user;
    }

    /**
	 * @param _passwd the passwd to set
	 */
    public void setPasswd(String _passwd) {
        this.passwd = _passwd;
    }

    /**
	 * @param _dbName  the dbName to set
	 */
    public void setDbName(String _dbName) {
        this.dbName = _dbName;
    }

    /**
	 * @param _schema the schema to set
	 */
    public void setSchema(String _schema) {
        this.schema = _schema;
        if (_schema != null && !_schema.trim().equals("")) {
            showSchema(true);
        }
    }

    /**
	 * Set the name of the table
	 * @param _tableName table name
	 */
    public void setTableName(String _tableName) {
        this.tableName = _tableName;
    }

    /**
	 * check whether the table name input field is enabled
	 * @return true if the input field is enabled
	 */
    protected boolean isTableNameEnabled() {
        return txtTable.isEnabled();
    }

    /**
	 * Enable the input field for the table name
	 * @param enable or disable the field
	 */
    protected void enableTableName(boolean enable) {
        txtTable.setEnabled(enable);
    }

    /**
	 * Hide or show the Schema input
	 * @param show flag indication to show (true) or hide the input field
	 */
    public void showSchema(boolean show) {
        lblSchema.setVisible(show);
        txtSchema.setVisible(show);
        if (show) {
            txtSchema.setEnabled(true);
        }
    }

    /**
	 * Check if the schema must be specified
	 * @return true if a schema is needed
	 */
    protected boolean needsSchemaDefinition() {
        return txtSchema.isVisible();
    }

    /**
	 * Initialize the fields with guessed defaults
	 * for the specific database. So far the following
	 * databases are supported:
	 * <ul>
	 * <li>ORACLE</li>
	 * </ul>
	 * @param _dbProduct name of the database product.
	 * @param testNeeded flag indicating that a test is needed
	 */
    public void initData(String _dbProduct, boolean testNeeded) {
        initConnectionTestNeeded(testNeeded);
        if (_dbProduct == null) {
            initEmpty();
        } else if (withConnection) {
            IDBConnectionModelGenerator parser = InputDBConnection.getParser(_dbProduct);
            txtHost.setText(parser.getDefaultHost());
            txtPort.setText(parser.getDefaultPort());
            txtUserName.setText(parser.getDefaultUserName());
            txtPassword.setText("");
            txtName.setText("");
            showSchema(parser.hasSchema());
            setDbProduct(_dbProduct);
        } else {
            IDBInputParser parser = InputDBParser.getParser(_dbProduct);
            if (parser != null) {
                txtHost.setText(parser.getDefaultHost());
                txtPort.setText(String.valueOf(parser.getDefaultPort()));
                txtUserName.setText(parser.getDefaultUser());
                txtPassword.setText(parser.getPassword());
                txtName.setText(parser.getDatabaseName());
                showSchema(parser.hasSchema());
                setDbProduct(_dbProduct);
            } else {
                initEmpty();
            }
        }
    }

    /**
	 * Fill empty strings into all fields
	 */
    protected void initEmpty() {
        txtHost.setText("");
        txtPort.setText("");
        txtUserName.setText("");
        txtPassword.setText("");
        txtName.setText("");
    }

    /**
	   * Compute the context help string for the current selection
	   * @return Context-ID
	   * @since 1.1.0
	   */
    protected String getHelpContext() {
        String prefix = ISahitsHelpSystem.PLUGIN_ID + ".";
        String postfix = "DBDefinitionPage";
        return prefix + postfix;
    }

    /**
		 * Test the connection
		 * @since 1.1.1
		 */
    protected void testConnection() {
        IDBConnectionModelGenerator generator = InputDBConnection.getParser(getDbProduct());
        try {
            String db = getDbName();
            generator.init(getHost(), getUserName(), getPassword(), db, getTableName(), "", "", Integer.parseInt(getPort()), getSchema());
        } catch (SQLException e) {
            updateStatus(e.getMessage());
        }
        String errorCode = generator.testConnection();
        if (errorCode == null) {
            setConnectionTestedTrue();
            dialogChanged();
        } else {
            updateStatus(errorCode);
        }
    }

    /**
		 * Initialize the values and the text fields (from unserializing)
		 * @param dbName Database name
		 * @param tableName Table name
		 * @param host host name
		 * @param port port number
		 * @param user database user
		 * @param password user's password
		 * @param schema schema or null if no shema is needed for the product
		 */
    public void init(String dbName, String tableName, String host, String port, String user, String password, String schema) {
        setDbName(dbName);
        txtName.setText(dbName);
        setTableName(tableName);
        txtTable.setText(tableName);
        enableTableName(true);
        setHost(host);
        txtHost.setText(host);
        setPort(port);
        txtPort.setText(port);
        setUser(user);
        txtUserName.setText(user);
        setPasswd(password);
        txtPassword.setText(password);
        setSchema(schema);
        if (schema != null) {
            txtSchema.setText(schema);
        }
    }
}
