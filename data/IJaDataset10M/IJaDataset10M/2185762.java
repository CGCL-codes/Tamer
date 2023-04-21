package repast.simphony.agents.designer.ui.wizards;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import repast.simphony.agents.designer.core.AgentBuilderPlugin;
import repast.simphony.agents.designer.ui.AgentBuilderPluginImages;

/**
 * @author agreif (Adapted by Michael J. North for Use in Repast Simphony from
 *         Alexander Greif�s Flow4J-Eclipse
 *         (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks
 *         to the Original Author)
 * 
 * TODO
 * 
 * 
 * 
 */
public class NewAgentCreationWizard extends Wizard implements INewWizard {

    private IStructuredSelection selection;

    private NewAgentCreationWizardPage wizardPage;

    private String title;

    private String description;

    private String fileName;

    private ImageDescriptor imageDescriptor;

    private String templateSourceFile = null;

    private String parentClassName = "";

    private String interfaces = "";

    private String imports = "";

    /**
	 * Constructor
	 * 
	 */
    public NewAgentCreationWizard() {
        super();
        this.setTitle(AgentBuilderPlugin.getResourceString("WizardPage_NewAgent.pageName"));
        this.setFileName(AgentBuilderPlugin.getResourceString("WizardPage_NewAgent.fileName"));
        this.setDescription(AgentBuilderPlugin.getResourceString("WizardPage_NewAgent.pageDescription"));
        this.setImageDescriptor(AgentBuilderPluginImages.DESC_WIZBAN_NewAgent);
    }

    /**
	 * Initializes the Wizard.
	 * 
	 * @param workbench
	 * @param selection
	 */
    public void init(IWorkbench workbench, IStructuredSelection selection) {
        this.selection = selection;
    }

    /**
	 * @see org.eclipse.jface.wizard.IWizard#addPages()
	 */
    @Override
    public void addPages() {
        wizardPage = new NewAgentCreationWizardPage(this.getTitle(), selection);
        wizardPage.setTitle(this.getTitle());
        wizardPage.setDescription(this.getDescription());
        wizardPage.setFileName(this.getFileName());
        wizardPage.setImageDescriptor(this.getImageDescriptor());
        wizardPage.setTemplateSourceFile(templateSourceFile);
        wizardPage.setParentClassName(parentClassName);
        wizardPage.setInterfaces(interfaces);
        wizardPage.setImports(imports);
        this.addPage(wizardPage);
    }

    /**
	 * @see org.eclipse.jface.wizard.IWizard#performFinish()
	 */
    @Override
    public boolean performFinish() {
        return wizardPage.finish();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ImageDescriptor getImageDescriptor() {
        return imageDescriptor;
    }

    public void setImageDescriptor(ImageDescriptor imageDescriptor) {
        this.imageDescriptor = imageDescriptor;
    }

    public String getParentClassName() {
        return parentClassName;
    }

    public void setParentClassName(String parentClassName) {
        this.parentClassName = parentClassName;
    }

    public String getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(String interfaces) {
        this.interfaces = interfaces;
    }

    public String getImports() {
        return imports;
    }

    public void setImports(String imports) {
        this.imports = imports;
    }

    public String getTemplateSourceFile() {
        return templateSourceFile;
    }

    public void setTemplateSourceFile(String templateSourceFile) {
        this.templateSourceFile = templateSourceFile;
    }
}
