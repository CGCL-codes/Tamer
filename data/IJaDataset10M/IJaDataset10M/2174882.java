package org.regilo.modules.wizards;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import org.eclipse.jface.wizard.Wizard;
import org.regilo.core.model.DrupalSite;
import org.regilo.modules.jobs.InstallJob;
import org.regilo.modules.model.Module;
import org.regilo.modules.model.Project;

public class InstallNewModuleWizard extends Wizard {

    private DrupalSite drupalSite;

    private ChooseProjectVersionPage chooseProjectVersionPage;

    private ChooseModulesPage chooseModulesPage;

    private ReviewDependencyPage reviewDependencyPage;

    private ChooseProjectPage chooseProjectPage;

    public InstallNewModuleWizard(DrupalSite drupalSite) {
        this.drupalSite = drupalSite;
        setNeedsProgressMonitor(true);
    }

    @Override
    public boolean performFinish() {
        Module[] modules = reviewDependencyPage.getModules();
        File tempDir = chooseModulesPage.getTempDir();
        Project project = chooseProjectVersionPage.getProject();
        InstallJob installJob = new InstallJob(drupalSite, project, modules, tempDir);
        try {
            getContainer().run(true, false, installJob);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void addPages() {
        setWindowTitle("Install and enable a new module");
        chooseProjectPage = new ChooseProjectPage("chooseProject");
        addPage(chooseProjectPage);
        chooseProjectVersionPage = new ChooseProjectVersionPage("chooseProjectVersion");
        addPage(chooseProjectVersionPage);
        chooseModulesPage = new ChooseModulesPage("chooseModules");
        addPage(chooseModulesPage);
        reviewDependencyPage = new ReviewDependencyPage("reviewDependency");
        addPage(reviewDependencyPage);
    }

    public DrupalSite getDrupalSite() {
        return drupalSite;
    }

    public boolean canFinish() {
        if (getContainer().getCurrentPage() == reviewDependencyPage) {
            if (getContainer().getCurrentPage().isPageComplete()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
