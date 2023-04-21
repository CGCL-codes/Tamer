package com.bonkey.wizards.registration;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * Wizard page for welcoming the user
 * 
 * @author marcel
 */
public class RegPageWelcome extends WizardPage {

    public RegPageWelcome(String pageName) {
        super(pageName);
    }

    public RegPageWelcome(String pageName, String title, ImageDescriptor titleImage) {
        super(pageName, title, titleImage);
    }

    public void createControl(Composite parent) {
        setTitle(Messages.getString("RegPageWelcome.WelcomeTitle"));
        setMessage(Messages.getString("RegPageWelcome.WelcomeMessage"));
        Composite base = new Composite(parent, SWT.NULL);
        setControl(base);
        GridLayout layout = new GridLayout();
        layout.horizontalSpacing = 20;
        layout.verticalSpacing = 10;
        Label label = new Label(base, SWT.WRAP);
        label.setText(Messages.getString("RegPageWelcome.SetupRemoteStorage"));
        label.setLayoutData(new GridData(SWT.FILL, SWT.NULL, true, false));
        base.setLayout(layout);
        setPageComplete(true);
    }
}
