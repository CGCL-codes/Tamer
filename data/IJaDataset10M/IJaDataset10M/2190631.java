package com.ivis.xprocess.ui.licensing.wizard;

import java.io.IOException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import com.ivis.xprocess.ui.licensing.UILicenseUtil;
import com.ivis.xprocess.ui.properties.LicenseMessages;
import com.ivis.xprocess.ui.util.DialogUtil;
import com.ivis.xprocess.ui.util.TestHarness;
import com.ivis.xprocess.ui.wizards.XProcessWizardPage;

public class InstallLicenceWizardViewLicencePage extends XProcessWizardPage {

    protected boolean canFlip = false;

    private InstallLicenseWizard installWizard;

    private boolean canFinish = false;

    private boolean firstTime = true;

    private Button agreeButton;

    protected InstallLicenceWizardViewLicencePage(InstallLicenseWizard wizard) {
        super(UILicenseUtil.VIEW_LICENCE_PAGE_ID);
        this.setTitle(LicenseMessages.install_license_wizard_title);
        this.setDescription(LicenseMessages.install_license_wizard_read_license);
        this.installWizard = wizard;
        this.setPageComplete(false);
    }

    @Override
    protected void setupData() {
    }

    @Override
    public void checkData() {
        if (firstTime) {
            firstTime = false;
            installWizard.notifyOnSecondPage();
        }
        canFinish = this.agreeButton.getSelection();
        setPageComplete(canFinish);
        if (canFinish) {
            installWizard.notifyCanFinish();
        }
        getWizard().getContainer().updateButtons();
    }

    @Override
    public boolean save() {
        return true;
    }

    public void createControl(Composite parent) {
        container = new Composite(parent, SWT.NONE);
        container.setLayout(new GridLayout());
        Group browserComposite = new Group(container, SWT.NONE);
        browserComposite.setLayout(new FillLayout());
        GridData data = new GridData(GridData.FILL_BOTH);
        browserComposite.setLayoutData(data);
        Text text = new Text(browserComposite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.WRAP | SWT.READ_ONLY);
        try {
            text.setText(UILicenseUtil.getLicencePage());
        } catch (IOException e) {
            DialogUtil.openErrorDialog("Cannot find the licence agreement", "Error: cannot find the licence agreement");
            System.exit(-1);
        }
        new Label(container, SWT.NONE);
        agreeButton = new Button(container, SWT.CHECK);
        agreeButton.setText("I have read, understood and agreed with the above licensing terms");
        agreeButton.setSelection(false);
        agreeButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                checkData();
            }
        });
        setControl(container);
        setupTestHarness();
    }

    @Override
    public boolean canFlipToNextPage() {
        return canFlip;
    }

    public void uncheck() {
        agreeButton.setSelection(false);
        checkData();
    }

    private void setupTestHarness() {
        TestHarness.name(agreeButton, TestHarness.INSTALL_LICENSE_AGREEBUTTON);
    }
}
