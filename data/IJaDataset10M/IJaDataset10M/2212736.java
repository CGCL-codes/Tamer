package com.ilog.translator.java2cs.plugin.wizard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;
import com.ilog.translator.java2cs.configuration.GlobalOptions;
import com.ilog.translator.java2cs.configuration.TranslatorProjectOptions;
import com.ilog.translator.java2cs.plugin.Messages;
import com.ilog.translator.java2cs.plugin.TranslationPlugin;

/**
 * 
 */
@SuppressWarnings("unchecked")
public class ProjectTranslateWizard extends Wizard implements IExportWizard {

    private final WizardProjectTranslateSelectPage selectProjectPage;

    private final WizardProjectTranslateOptionsPage optionsPage;

    private final WizardProjectTranslateAdvancedOptionsPage advancedOptionsPage;

    private final TranslatorProjectOptions options = new TranslatorProjectOptions(new GlobalOptions());

    private boolean optionsRead = false;

    public ProjectTranslateWizard() {
        selectProjectPage = new WizardProjectTranslateSelectPage();
        optionsPage = new WizardProjectTranslateOptionsPage(options);
        advancedOptionsPage = new WizardProjectTranslateAdvancedOptionsPage(options);
    }

    /**
	 * @see org.eclipse.jface.wizard.IWizard#addPages()
	 */
    @Override
    public void addPages() {
        super.addPages();
        addPage(selectProjectPage);
        addPage(optionsPage);
        addPage(advancedOptionsPage);
    }

    /**
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
	 *      org.eclipse.jface.viewers.IStructuredSelection)
	 */
    public void init(IWorkbench workbench, IStructuredSelection selection) {
        setWindowTitle(Messages.getString("ProjectTranslateWizard.title"));
        setDefaultPageImageDescriptor(TranslationPlugin.getImageDescriptor("icons/java2cs_project_translation_wizard.png"));
        final Collection selectedProjects = new ArrayList();
        for (final Iterator iter = selection.iterator(); iter.hasNext(); ) {
            final Object element = iter.next();
            if (element instanceof IProject) {
                selectedProjects.add(element);
            } else if (element instanceof IJavaProject) {
                selectedProjects.add(((IJavaProject) element).getProject());
            } else if (element instanceof IAdaptable) {
                final IResource res = (IResource) ((IAdaptable) element).getAdapter(IResource.class);
                if (res instanceof IProject) selectedProjects.add(res);
            }
        }
        selectProjectPage.getSelectedProjects().addAll(selectedProjects);
        selectProjectPage.getReferenceCountProjects().addAll(selectedProjects);
    }

    /**
	 * @see org.eclipse.jface.wizard.IWizard#canFinish()
	 */
    @Override
    public boolean canFinish() {
        return selectProjectPage.isPageComplete();
    }

    /**
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
    @Override
    public boolean performFinish() {
        final IProject project = getSelectedProject();
        try {
            optionsPage.saveOptionsSettings();
            advancedOptionsPage.saveOptionsSettings();
            if (options.getProjectConfigurationFileName() == null) {
                final IJavaProject jproject = JavaCore.create(project);
                options.setProjectConfigurationFileName(TranslatorProjectOptions.searchTranslatorDir(jproject, options.getGlobalOptions(), true, true, "rve"));
            }
            options.save();
        } catch (final Exception e) {
            options.getGlobalOptions().getLogger().logException("", e);
        }
        TranslationPlugin.getTranslationHandler().translateProjects(project, options, true);
        return true;
    }

    IProject getSelectedProject() {
        return getSelectedProjects()[0];
    }

    IProject[] getSelectedProjects() {
        return (IProject[]) selectProjectPage.getSelectedProjects().toArray(new IProject[selectProjectPage.getSelectedProjects().size()]);
    }

    @Override
    public IWizardPage getNextPage(IWizardPage page) {
        final IWizardPage nextPage = super.getNextPage(page);
        if (nextPage == optionsPage) {
            final IJavaProject jProject = JavaCore.create(getSelectedProject());
            try {
                if (!optionsRead) {
                    final boolean read = true;
                    final String translatorDir = TranslatorProjectOptions.searchTranslatorDir(jProject, options.getGlobalOptions(), true, true, options.getProfile());
                    final String confFileName = TranslatorProjectOptions.getOrCreateTranslatorConfigurationFile(jProject, options.getGlobalOptions(), true, translatorDir, true);
                    options.setProjectConfigurationFileName(confFileName);
                    options.read(jProject, false);
                    optionsPage.initOptionsValue();
                    advancedOptionsPage.initOptionsValue();
                }
            } catch (final Exception e) {
                options.getGlobalOptions().getLogger().logException("Loading project options failed ", e);
            }
        }
        return nextPage;
    }
}
