package net.sourceforge.eclipseopengl.wizard;

import java.lang.reflect.InvocationTargetException;
import net.sourceforge.eclipseopengl.OpenGLMessages;
import net.sourceforge.eclipseopengl.project.IProjectProvider;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.*;

public class NewOpenGLProjectWizard extends Wizard implements INewWizard {

    private IWorkbench workbench;

    private IStructuredSelection selection;

    private NewProjectPage newProjectPage;

    private BindingPage bindingPage;

    private ExamplePage examplePage;

    private PluginFieldData fieldData;

    private IProjectProvider projectProvider;

    public NewOpenGLProjectWizard() {
        fieldData = new PluginFieldData();
        setWindowTitle(OpenGLMessages.NewOpenGLProjectWizard_NewProject);
    }

    public boolean performFinish() {
        IProject ip = newProjectPage.getProjectHandle();
        for (int i = 0; i < newProjectPage.getSelectedWorkingSets().length; i++) {
            IWorkingSet workingset = newProjectPage.getSelectedWorkingSets()[i];
            System.out.println(workingset.getName());
        }
        try {
            getContainer().run(false, true, new NewProjectCreationOperation(fieldData, projectProvider));
            return true;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void init(IWorkbench workbench, IStructuredSelection selection) {
        this.workbench = workbench;
        this.selection = selection;
    }

    public void addPages() {
        newProjectPage = new NewProjectPage(selection, fieldData);
        bindingPage = new BindingPage(fieldData);
        examplePage = new ExamplePage(fieldData);
        projectProvider = new ProjectProviderNewProjectPage(newProjectPage);
        addPage(newProjectPage);
        addPage(bindingPage);
        addPage(examplePage);
        fieldData.addBindingChangeListener(examplePage);
        fieldData.addPlatformChangeListener(examplePage);
    }

    public boolean canFinish() {
        IWizardPage page = getContainer().getCurrentPage();
        return super.canFinish() && page != newProjectPage;
    }
}
