package org.pentaho.PentahoAdmin;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.pentaho.PentahoAdmin.Exception.PentahoAdminXMLException;
import org.pentaho.PentahoAdmin.panels.Email;
import org.pentaho.PentahoAdmin.panels.HostConfiguration;
import org.pentaho.PentahoAdmin.panels.Passwords;
import org.pentaho.PentahoAdmin.panels.Security;

public class TabPnl extends Composite {

    private HostConfiguration hostsComposite;

    private Passwords passwordsComposite;

    private Security securityComposite;

    private Email emailComposite;

    /**
	 * @param parent
	 * @param style
	 * @throws PentahoAdminXMLException 
	 */
    public TabPnl(Composite parent, int style) throws PentahoAdminXMLException {
        super(parent, style);
        setLayout(new FillLayout());
        final TabFolder tabFolder = new TabFolder(this, SWT.NONE);
        final TabItem hostConfigurationTabItem = new TabItem(tabFolder, SWT.NONE);
        hostConfigurationTabItem.setText("Host Configuration");
        final TabItem passwordConfigurationTabItem = new TabItem(tabFolder, SWT.NONE);
        passwordConfigurationTabItem.setText("Password Configuration");
        final TabItem securityConfigurationTabItem = new TabItem(tabFolder, SWT.NONE);
        securityConfigurationTabItem.setText("Security Configuration");
        final TabItem emailConfigurationTabItem = new TabItem(tabFolder, SWT.NONE);
        emailConfigurationTabItem.setText("Email Configuration");
        hostsComposite = new HostConfiguration(tabFolder, SWT.NONE);
        hostConfigurationTabItem.setControl(hostsComposite);
        passwordsComposite = new Passwords(tabFolder, SWT.NONE);
        passwordConfigurationTabItem.setControl(passwordsComposite);
        securityComposite = new Security(tabFolder, SWT.NONE);
        securityConfigurationTabItem.setControl(securityComposite);
        emailComposite = new Email(tabFolder, SWT.NONE);
        emailConfigurationTabItem.setControl(emailComposite);
    }

    /**
	 * 
	 */
    public void Saveme() {
    }
}
