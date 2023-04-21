package com.ivis.xprocess.csv.ximport.wizard;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import com.ivis.xprocess.csv.ximport.properties.ImportPersonsMessages;
import com.ivis.xprocess.ui.licensing.wizard.UnsupportedFeatureWizardPage;
import com.ivis.xprocess.ui.properties.WizardMessages;
import com.ivis.xprocess.ui.util.ProgressMonitorManager;
import com.ivis.xprocess.ui.util.ViewUtil;
import com.ivis.xprocess.util.FeatureUtil;
import com.ivis.xprocess.util.LicensingEnums.Feature;

public class ImportXProcessPersonsWizard extends Wizard implements IImportWizard {

    private ImportXProcessPersonsWizardStartPage importXProcessPersonsWizardStartPage;

    private ImportXProcessPersonsWizardMappingPage importXProcessPersonsWizardMappingPage;

    private UnsupportedFeatureWizardPage unsupportedFeatureWizardPage;

    private ImportXProcessPersonsWizardConfirmationPage importXProcessPersonsWizardConfirmationPage;

    @Override
    public boolean performFinish() {
        ProgressMonitorManager progressMonitorManager = new ProgressMonitorManager();
        ProgressMonitorDialog progressMonitorDialog = progressMonitorManager.create();
        progressMonitorDialog.setCancelable(true);
        progressMonitorDialog.open();
        try {
            if (unsupportedFeatureWizardPage != null) {
                return true;
            }
            if (importXProcessPersonsWizardConfirmationPage != null) {
                progressMonitorDialog.getProgressMonitor().beginTask(ImportPersonsMessages.import_people_jobtitle, IProgressMonitor.UNKNOWN);
                progressMonitorDialog.getProgressMonitor().setTaskName(ImportPersonsMessages.import_people_jobtitle);
                importXProcessPersonsWizardConfirmationPage.save();
            }
        } finally {
            progressMonitorDialog.getProgressMonitor().done();
            progressMonitorDialog.close();
        }
        return true;
    }

    public void init(IWorkbench workbench, IStructuredSelection selection) {
        setWindowTitle(ImportPersonsMessages.import_csv_persons_wizard_window_title);
    }

    @Override
    public void addPages() {
        if (!FeatureUtil.can(Feature.IMPORT_ALL)) {
            String feature = WizardMessages.importMSProject;
            unsupportedFeatureWizardPage = new UnsupportedFeatureWizardPage(feature, feature, feature);
            addPage(unsupportedFeatureWizardPage);
            return;
        }
        if (PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getDirtyEditors().length > 0) {
            boolean userToContinue = MessageDialog.openQuestion(ViewUtil.getCurrentShell(), ImportPersonsMessages.unsaved_editors_dialog_title, ImportPersonsMessages.unsaved_editors_dialog_message);
            if (!userToContinue) {
                return;
            }
        }
        boolean allSaved = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().saveAllEditors(true);
        if (!allSaved) {
            return;
        }
        importXProcessPersonsWizardStartPage = new ImportXProcessPersonsWizardStartPage(ImportPersonsMessages.import_csv_persons_wizard_window_pagename, null);
        addPage(importXProcessPersonsWizardStartPage);
        importXProcessPersonsWizardMappingPage = new ImportXProcessPersonsWizardMappingPage(ImportPersonsMessages.import_csv_persons_wizard_window_pagename, null);
        addPage(importXProcessPersonsWizardMappingPage);
        importXProcessPersonsWizardConfirmationPage = new ImportXProcessPersonsWizardConfirmationPage(ImportPersonsMessages.import_csv_persons_wizard_window_pagename, null);
        addPage(importXProcessPersonsWizardConfirmationPage);
    }
}
