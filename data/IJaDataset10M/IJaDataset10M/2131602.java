package org.databene.benclipse.wizards;

import java.util.Observable;
import java.util.Observer;
import org.databene.benclipse.BenclipsePlugin;
import org.databene.benclipse.archetype.Archetype;
import org.databene.benclipse.core.Messages;
import org.databene.benclipse.internal.EclipseUtil;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * Wizard for creating a benerator project.<br/>
 * <br/>
 * Created at 06.02.2009 08:53:13
 * @since 0.5.9
 * @author Volker Bergmann
 */
public class BeneratorProjectWizard extends Wizard implements INewWizard, Observer {

    private static final String ICON_BENERATOR = "icons/wizban/benerator_project_wiz.gif";

    private static final ImageDescriptor DEFAULT_PAGE_IMAGE = BenclipsePlugin.getImageDescriptor(ICON_BENERATOR);

    protected ProjectLocationPage locationPage;

    protected ArchetypePage archetypePage;

    protected TargetDatabasePage targetDatabasePage;

    protected GenerationConfigPage generationSettingsPage;

    public BeneratorProjectWizard() {
        setWindowTitle(Messages.getString("wizard.project.title"));
        setDefaultPageImageDescriptor(DEFAULT_PAGE_IMAGE);
        setNeedsProgressMonitor(true);
    }

    public void init(IWorkbench workbench, IStructuredSelection selection) {
    }

    @Override
    public void addPages() {
        addPage(locationPage = new ProjectLocationPage());
        addPage(archetypePage = new ArchetypePage());
        addPage(targetDatabasePage = new TargetDatabasePage());
        addPage(generationSettingsPage = new GenerationConfigPage());
    }

    @Override
    public void addPage(IWizardPage page) {
        super.addPage(page);
        if (page instanceof BeneratorWizardPage) ((BeneratorWizardPage) page).addObserver(this);
    }

    public void update(Observable observable, Object obj) {
        updateControls();
    }

    @Override
    public boolean canFinish() {
        if (isUsingDB() && !targetDatabasePage.isPageComplete()) return false;
        return locationPage.isPageComplete();
    }

    @Override
    public boolean performFinish() {
        ProjectNameGroup nameGroup = getProjectNameGroup();
        JDBCConnectionInfo targetJdbcInfo = null;
        targetJdbcInfo = (isUsingDB() ? getDatabaseGroup().getConnectionInfo() : null);
        GenerationConfig generationConfig = generationSettingsPage.getGenerationConfig();
        boolean startHsql = isUsingDB() && getDatabaseGroup().isUsingBuiltInDriver();
        boolean result;
        DBProjectInfo dbInfo = new DBProjectInfo(targetJdbcInfo, targetDatabasePage.getSchemaInitializationInfo(), startHsql);
        try {
            BenclipseProjectBuilder.runProjectBuilder(nameGroup.getProjectName(), archetypePage.getSelectedArchetype(), getJREGroup().getSelectedJRE(), dbInfo, generationConfig, EclipseUtil.getActiveWorkbenchWindow());
            result = true;
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    public boolean isSimpleProject() {
        return getProjectNameGroup().isSimple();
    }

    void updateControls() {
        boolean simple = isSimpleProject();
        getJREGroup().setEnabled(!simple);
        boolean db = isUsingDB();
        archetypePage.setNextPage(simple ? null : (db ? targetDatabasePage : generationSettingsPage));
        boolean repro = archetypePage.isRepro();
        targetDatabasePage.setInitializing(repro);
        getContainer().updateButtons();
    }

    public boolean isUsingDB() {
        Archetype selectedArchetype = archetypePage.getSelectedArchetype();
        boolean db = (selectedArchetype != null && selectedArchetype.getDirectory().getName().endsWith("db"));
        return db;
    }

    ProjectNameGroup getProjectNameGroup() {
        return locationPage.getProjectNameGroup();
    }

    JREGroup getJREGroup() {
        return locationPage.getJREGroup();
    }

    DatabaseConnectionGroup getDatabaseGroup() {
        return targetDatabasePage.getDatabaseGroup();
    }
}
