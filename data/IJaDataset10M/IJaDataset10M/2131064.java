package com.alexmcchesney.poster.plugins.metaweblogapi.gui;

import java.util.Properties;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.events.*;
import com.alexmcchesney.poster.account.Credentials;
import com.alexmcchesney.poster.plugins.metaweblogapi.*;
import com.alexmcchesney.poster.plugins.IAccountPropertiesForm;

/**
 * Wrapper to composite form for gathering properties of
 * a Blogger account.
 * @author amcchesney
 *
 */
public class GenericServicePropertiesForm implements IAccountPropertiesForm {

    /** The composite making up the form */
    private Composite m_composite = null;

    /** Username box */
    private Text m_userName = null;

    /** Password box */
    private Text m_password = null;

    /** Endpoint box */
    private Text m_endpoint = null;

    /** Account to be updated */
    private MetaWeblogAPIAccount m_account = null;

    /**
	 * Constructor.  Initialises the form composite and attaches it to the parent.
	 * @param parent
	 * @param account	Accounts whose properties are being updated
	 */
    public GenericServicePropertiesForm(Composite parent, MetaWeblogAPIAccount account, Credentials loginInfo) {
        m_composite = new Composite(parent, SWT.NONE);
        m_account = account;
        initForm(loginInfo);
    }

    /**
	 * Initialises the form
	 * @param folder	Tab folder to which the new tab should belong
	 */
    private void initForm(Credentials loginInfo) {
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        m_composite.setLayout(gridLayout);
        Label userLabel = new Label(m_composite, SWT.NONE);
        userLabel.setText(GUIStringResources.gui.getString("USER_NAME"));
        m_userName = new Text(m_composite, SWT.BORDER);
        m_userName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        m_userName.setToolTipText(GUIStringResources.tooltip.getString("USER_NAME_ENTRY"));
        Label passwordLabel = new Label(m_composite, SWT.NONE);
        passwordLabel.setText(GUIStringResources.gui.getString("PASSWORD") + ":");
        m_password = new Text(m_composite, SWT.BORDER);
        m_password.setEchoChar('*');
        m_password.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        m_password.setToolTipText(GUIStringResources.tooltip.getString("PASSWORD_ENTRY"));
        if (loginInfo != null) {
            m_userName.setText(loginInfo.getUserName());
            m_password.setText(loginInfo.getPassword());
        } else if (m_account != null) {
            String sUserName = m_account.getUserName();
            if (sUserName == null) {
                sUserName = "";
            }
            String sPassword = m_account.getPassword();
            if (sPassword == null) {
                sPassword = "";
            }
            m_userName.setText(sUserName);
            m_password.setText(sPassword);
        }
        Label endpointLabel = new Label(m_composite, SWT.NONE);
        endpointLabel.setText(GUIStringResources.gui.getString("SERVICE_ENDPOINT"));
        m_endpoint = new Text(m_composite, SWT.BORDER);
        m_endpoint.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        m_endpoint.setToolTipText(GUIStringResources.tooltip.getString("ENDPOINT_ENTRY"));
        if (m_account != null) {
            m_endpoint.setText(m_account.getEndpoint());
        } else {
            m_endpoint.setText("");
        }
        if (m_account.getCachedTargets() != null) {
            Button fetchButton = new Button(m_composite, SWT.PUSH);
            fetchButton.setText(GUIStringResources.gui.getString("RESET_BLOG_INFO"));
            fetchButton.setToolTipText(GUIStringResources.tooltip.getString("RESET_BLOGS_BUTTON"));
            fetchButton.addSelectionListener(new SelectionListener() {

                public void widgetDefaultSelected(SelectionEvent e) {
                    onResetBlogs();
                }

                public void widgetSelected(SelectionEvent e) {
                    widgetDefaultSelected(e);
                }
            });
        }
    }

    /**
	 * Fetches blogs for the current account
	 *
	 */
    private void onResetBlogs() {
        m_account.clearTargetCache();
        MessageBox msg = new MessageBox(m_composite.getShell(), SWT.ICON_INFORMATION | SWT.OK);
        msg.setText(GUIStringResources.gui.getString("SUCCESS"));
        msg.setMessage(GUIStringResources.gui.getString("BLOG_LIST_RESET"));
        msg.open();
    }

    /**
	 * Gets values from form as a properties bag
	 * @return Properties object containing the propertiesof the account.
	 */
    public Properties getProperties() {
        Properties props = new Properties();
        props.setProperty(MetaWeblogAPIPlugin.NAME_PROPERTY, m_userName.getText());
        props.setProperty(MetaWeblogAPIPlugin.PASSWORD_PROPERTY, m_password.getText());
        String sEndpoint = m_endpoint.getText();
        if (sEndpoint.endsWith("/")) {
            sEndpoint = sEndpoint.substring(0, sEndpoint.length() - 1);
        }
        props.setProperty(MetaWeblogAPIPlugin.ENDPOINT_PROPERTY, m_password.getText());
        return props;
    }

    /**
	 * Disposes of the form composite
	 */
    public void dispose() {
        m_composite.dispose();
    }

    public boolean isDirty() {
        return false;
    }

    public Composite getComposite() {
        return m_composite;
    }

    /**
	 * Validates that the user has entered valid data
	 * @return String containing an error message if the user has not entered
	 * valid data.  Null if it's ok.
	 */
    public String validate() {
        if (m_userName.getText().length() == 0) {
            return GUIStringResources.errors.getString("MUST_ENTER_USER_NAME");
        }
        if (m_password.getText().length() == 0) {
            return GUIStringResources.errors.getString("MUST_ENTER_PASSWORD");
        }
        return null;
    }

    /**
	 * Commits changes to the form, applying it to the account
	 *
	 */
    public void commitChanges() {
        if (m_account != null) {
            m_account.setUserName(m_userName.getText());
            m_account.setPassword(m_password.getText());
            String sEndpoint = m_endpoint.getText();
            if (sEndpoint.endsWith("/")) {
                sEndpoint = sEndpoint.substring(0, sEndpoint.length() - 1);
            }
            m_account.setEndpoint(sEndpoint);
        }
    }

    /**
	 * If the form contains username/password fields, it should
	 * return them in a credentials object.
	 * @return	Credentials provided in form.  May be null if the
	 * form contains no credentials.
	 */
    public Credentials getLoginDetails() {
        return new Credentials(m_userName.getText(), m_password.getText());
    }
}
