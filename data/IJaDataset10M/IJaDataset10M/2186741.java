package com.ivis.xprocess.ui.wizards.workflow;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import com.ivis.xprocess.ui.datawrappers.IElementWrapper;
import com.ivis.xprocess.ui.properties.WizardMessages;

public class NewExternalEventTypeWizard extends Wizard implements INewWizard {

    private NewExternalEventTypeWizardPage newExternalEventTypeWizardPage;

    private Object selectedObject;

    public NewExternalEventTypeWizard() {
    }

    public NewExternalEventTypeWizard(Object selectedObject) {
        this.selectedObject = selectedObject;
    }

    @Override
    public boolean performFinish() {
        newExternalEventTypeWizardPage.save();
        return true;
    }

    @Override
    public boolean performCancel() {
        newExternalEventTypeWizardPage.cancel();
        return true;
    }

    @Override
    public void addPages() {
        this.setWindowTitle(WizardMessages.externaleventtype_wizard_title);
        if (selectedObject != null) {
            newExternalEventTypeWizardPage = new NewExternalEventTypeWizardPage(WizardMessages.externaleventtype_pagename, selectedObject);
            addPage(newExternalEventTypeWizardPage);
        }
    }

    public void init(IWorkbench workbench, IStructuredSelection selection) {
    }

    public IElementWrapper getCreatedElementWrapper() {
        return newExternalEventTypeWizardPage.getCreatedWrapper();
    }
}
