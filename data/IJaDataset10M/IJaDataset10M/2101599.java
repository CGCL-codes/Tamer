package com.ivis.xprocess.ui.wizards.schema;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import com.ivis.xprocess.ui.properties.WizardMessages;

public class ManageUserDefinedSchemaWizard extends Wizard implements INewWizard {

    private ManageUserDefinedSchemaWizardPage manageUserDefinedSchemaWizardPage;

    private Object selectedObject;

    public ManageUserDefinedSchemaWizard() {
    }

    public ManageUserDefinedSchemaWizard(Object selectedObject) {
        this.selectedObject = selectedObject;
    }

    @Override
    public boolean performFinish() {
        manageUserDefinedSchemaWizardPage.save();
        return true;
    }

    @Override
    public boolean performCancel() {
        manageUserDefinedSchemaWizardPage.cancel();
        return true;
    }

    @Override
    public void addPages() {
        this.setWindowTitle(WizardMessages.edituserschema_wizard_title);
        if (selectedObject != null) {
            manageUserDefinedSchemaWizardPage = new ManageUserDefinedSchemaWizardPage(WizardMessages.edituserschema_wizard_title, selectedObject);
        } else {
            manageUserDefinedSchemaWizardPage = new ManageUserDefinedSchemaWizardPage(WizardMessages.edituserschema_wizard_title);
        }
        addPage(manageUserDefinedSchemaWizardPage);
    }

    public void init(IWorkbench workbench, IStructuredSelection selection) {
    }
}
