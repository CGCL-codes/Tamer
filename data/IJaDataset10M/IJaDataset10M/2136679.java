package com.safi.workshop.license;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import com.safi.server.saflet.mbean.LicenseException;

public class LicenseMessageDialog extends Dialog {

    private Text safletLic1DoesText;

    protected Object result;

    protected Shell shell;

    Link safisystemsLink;

    private String licenseText = "Proper SafiServer licensing for the Saflet LIC1 is missing.\r\nThe SafiServer requires proper licensing in order to accept\r\nand run Saflets\r\n\r\n--> Missing: LIC2";

    private String url = "<a href=\"url\">SafiServer License Help</a>";

    private String httpurl = "http://www.safisystems.com/index.cfm?pageMode=licensehelp";

    private String httpurl2 = "http://www.safisystems.com/index.cfm?pageMode=licensehelp";

    /**
   * Create the dialog
   * 
   * @param parent
   * @param style
   */
    public LicenseMessageDialog(Shell parent, int style) {
        super(parent, style);
    }

    /**
   * Create the dialog
   * 
   * @param parent
   */
    public LicenseMessageDialog(Shell parent) {
        this(parent, SWT.NONE);
    }

    /**
   * Open the dialog
   * 
   * @return the result
   */
    public Object open() {
        createContents();
        shell.open();
        shell.layout();
        Display display = getParent().getDisplay();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        return result;
    }

    /**
   * Create contents of the dialog
   */
    protected void createContents() {
        shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        shell.setSize(350, 220);
        shell.setText("SafiServer Licensing Error!");
        safletLic1DoesText = new Text(shell, SWT.READ_ONLY | SWT.MULTI);
        safletLic1DoesText.setText(licenseText);
        safletLic1DoesText.setBounds(10, 10, 325, 68);
        final Label inOrderToLabel = new Label(shell, SWT.NONE);
        inOrderToLabel.setText("For more information on SafiServer licensing, please visit:");
        inOrderToLabel.setBounds(10, 100, 325, 20);
        safisystemsLink = new Link(shell, SWT.NONE);
        safisystemsLink.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                BareBonesBrowserLaunch.openURL(httpurl2);
            }
        });
        safisystemsLink.setText(url);
        safisystemsLink.setBounds(10, 120, 325, 20);
        final Button okButton = new Button(shell, SWT.NONE);
        okButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                shell.close();
            }
        });
        okButton.setText("OK");
        okButton.setBounds(153, 160, 44, 23);
    }

    public void setLicenseException(String handlerName, LicenseException ex) {
        String desc = ex.getDesc();
        if (desc != null) {
            this.licenseText = this.licenseText.replace("LIC1", handlerName);
            this.licenseText = this.licenseText.replace("LIC2", desc);
            this.url = this.url.replace("url", ex.getUrl());
            this.httpurl = ex.getUrl();
        } else {
            desc = ex.getMessage();
            this.licenseText = this.licenseText.replace("LIC1", handlerName);
            this.licenseText = this.licenseText.replace("LIC2", desc == null ? "" : desc);
            String urlTemp = ex.getUrl();
            if (urlTemp != null) {
                this.url = this.url.replace("url", ex.getUrl());
                this.httpurl = ex.getUrl();
            }
        }
    }
}
