package org.monet.modelling.ide.wizards;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.monet.modelling.ide.Activator;
import org.monet.modelling.ide.providers.ProjectProvider;
import org.monet.modelling.ide.providers.ProjectsProviderFactory;
import org.monet.modelling.ide.providers.impl.v1.constants.ProjectProviderConstants;
import org.monet.modelling.ide.providers.impl.v1.utils.ProviderParameters;
import org.monet.modelling.ide.wizards.pages.CreationPageProject;

public class NewProjectWizard extends Wizard implements INewWizard {

    public static final String CREATION_PROJECT = "Create new Monet project";

    private CreationPageProject _pageCreation;

    private final String[] metaModelVersions;

    private final String[] languages = new String[] { "es", "en" };

    public NewProjectWizard() {
        this.metaModelVersions = Activator.getDefault().getMetaModelService().getMetaModelVersions();
    }

    @Override
    public boolean performFinish() {
        final ProviderParameters parameters = extractParameters();
        String version = (String) parameters.getValue(ProjectProviderConstants.PROJECT_VERSION);
        try {
            ProjectsProviderFactory factory = ProjectsProviderFactory.getProjectsBuilderFactory();
            final ProjectProvider builder = factory.getProjectBuilder(version);
            Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
            ProgressMonitorDialog dialog = new ProgressMonitorDialog(shell);
            dialog.run(true, true, new IRunnableWithProgress() {

                @Override
                public void run(IProgressMonitor monitor) {
                    builder.createProject(parameters, monitor);
                }
            });
        } catch (Exception e) {
            MessageBox errorMessage = new MessageBox(getShell(), SWT.ICON_ERROR);
            errorMessage.setText("Project creation failed");
            errorMessage.setMessage(e.getMessage());
            errorMessage.open();
            e.printStackTrace();
        }
        return true;
    }

    private ProviderParameters extractParameters() {
        ProviderParameters parameters = new ProviderParameters();
        parameters.addParameter(ProjectProviderConstants.PROJECT_NAME, _pageCreation.getProjectName());
        parameters.addParameter(ProjectProviderConstants.PROJECT_VERSION, _pageCreation.getVersion());
        parameters.addParameter(ProjectProviderConstants.PROJECT_PARENT, _pageCreation.getProjectParent());
        parameters.addParameter(ProjectProviderConstants.PROJECT_LANGUAGES, _pageCreation.getLanguagesSelected());
        parameters.addParameter(ProjectProviderConstants.PROJECT_ICON, _pageCreation.getIconName());
        parameters.addParameter(ProjectProviderConstants.PROJECT_INITIAL_IMAGE, _pageCreation.getInitialImage());
        parameters.addParameter(ProjectProviderConstants.PROJECT_TOP_IMAGE, _pageCreation.getTopImage());
        parameters.addParameter(ProjectProviderConstants.PROJECT_DOMAIN, _pageCreation.getDomain());
        return parameters;
    }

    @Override
    public void init(IWorkbench workbench, IStructuredSelection selection) {
    }

    @Override
    public void addPages() {
        super.addPages();
        setWindowTitle("New Monet Project");
        _pageCreation = new CreationPageProject(CREATION_PROJECT);
        _pageCreation.setTitle("Monet Project Wizard");
        _pageCreation.setVersions(this.metaModelVersions);
        _pageCreation.setLanguages(this.languages);
        addPage(_pageCreation);
    }
}
