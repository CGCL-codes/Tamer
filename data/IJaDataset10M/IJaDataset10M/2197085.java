package com.amazonaws.eclipse.sdk.ui;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import com.amazonaws.eclipse.core.AwsUrls;
import com.amazonaws.eclipse.core.ui.overview.OverviewSection;
import com.amazonaws.eclipse.sdk.ui.wizard.NewAwsJavaProjectWizard;

/**
 * AWS SDK for Java specific section on the AWS Toolkit for Eclipse overview page.
 */
public class SdkOverviewSection extends OverviewSection implements OverviewSection.V2 {

    private static final String SDK_FOR_JAVA_GETTING_STARTED_GUIDE_URL = "http://developer.amazonwebservices.com/connect/entry.jspa?externalID=3586" + "&" + AwsUrls.TRACKING_PARAMS;

    /**
     * @see com.amazonaws.eclipse.core.ui.overview.OverviewSection#createControls(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createControls(Composite parent) {
        Composite tasksSection = toolkit.newSubSection(parent, "Tasks");
        toolkit.newListItem(tasksSection, "Create a new AWS Java project", null, openNewAwsJavaProjectAction);
        Composite resourcesSection = toolkit.newSubSection(parent, "Additional Resources");
        toolkit.newListItem(resourcesSection, "Getting started with the AWS SDK for Java", SDK_FOR_JAVA_GETTING_STARTED_GUIDE_URL, null);
    }

    /** Action to open the New AWS Java Project wizard in a dialog */
    private static final IAction openNewAwsJavaProjectAction = new Action() {

        @Override
        public void run() {
            NewAwsJavaProjectWizard newWizard = new NewAwsJavaProjectWizard();
            newWizard.init(PlatformUI.getWorkbench(), null);
            WizardDialog dialog = new WizardDialog(Display.getCurrent().getActiveShell(), newWizard);
            dialog.open();
        }
    };
}
