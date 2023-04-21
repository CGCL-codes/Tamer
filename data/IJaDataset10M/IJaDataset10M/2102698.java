package org.nakedobjects.ide.ui.wizards.proj.appl;

import org.apache.log4j.Logger;
import org.eclipse.pde.internal.ui.wizards.IProjectProvider;
import org.eclipse.pde.internal.ui.wizards.plugin.PluginFieldData;
import org.nakedobjects.ide.ui.wizards.proj.common.AbstractProjectCreationOperation;
import org.nakedobjects.ide.ui.wizards.proj.common.AbstractProjectWizard;
import org.nakedobjects.ide.ui.wizards.proj.common.IExportedPackageProvider;
import org.nakedobjects.ide.ui.wizards.proj.common.ProjectWizardsMessages;

@SuppressWarnings("restriction")
public class ApplicationProjectWizard extends AbstractProjectWizard {

    private static final Logger LOGGER = Logger.getLogger(ApplicationProjectWizard.class);

    public Logger getLOGGER() {
        return LOGGER;
    }

    public ApplicationProjectWizard() {
        super(ProjectWizardsMessages.NewApplicationProject_windowTitle);
    }

    public ApplicationProjectWizard(String osgiFramework) {
        super(ProjectWizardsMessages.NewApplicationProject_windowTitle, osgiFramework);
    }

    @Override
    protected String getCreationPageTitle() {
        return ProjectWizardsMessages.NewApplicationProjectWizard_MainPage_title;
    }

    @Override
    protected String getCreationPageDescription() {
        return ProjectWizardsMessages.NewApplicationProjectWizard_MainPage_desc;
    }

    @Override
    protected String getGenerateSampleCodePageTitle() {
        return ProjectWizardsMessages.NewApplicationProjectWizard_GenerateSampleCodePage_title;
    }

    @Override
    protected String getGenerateSampleCodePageDescription() {
        return ProjectWizardsMessages.NewApplicationProjectWizard_GenerateSampleCodePage_desc;
    }

    /**
     * Note that this has a hard-coded dependency on the templates.
     */
    @Override
    protected AbstractProjectCreationOperation createProjectCreationOperation(PluginFieldData pluginData, IProjectProvider projectProvider) {
        ApplicationProjectCreationOperation projectCreationOperation = new ApplicationProjectCreationOperation(pluginData, projectProvider, getGenerateExampleCodeProvider());
        projectCreationOperation.setBundleIdProvider(null);
        projectCreationOperation.setExportedPackageProvider(new IExportedPackageProvider() {

            public String[] getExportedPackages() {
                return new String[] { getProjectName() + ".domain", getProjectName() + ".services" };
            }
        });
        return projectCreationOperation;
    }

    @Override
    protected boolean shouldFireListener() {
        return true;
    }
}
