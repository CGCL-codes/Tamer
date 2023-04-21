package net.sourceforge.akrogen.eclipse.wizard.generation;

import java.util.Iterator;
import java.util.List;
import net.sourceforge.akrogen.core.component.AkrogenComponent;
import net.sourceforge.akrogen.eclipse.util.AkrogenUtil;
import net.sourceforge.akrogen.core.wizard.AkrogenWizard;
import net.sourceforge.akrogen.core.wizard.AkrogenWizardPage;
import net.sourceforge.akrogen.eclipse.AkrogenMessages;
import net.sourceforge.akrogen.eclipse.project.AkrogenProject;
import net.sourceforge.akrogen.eclipse.wizard.dynamic.IDynamicWizardPage;
import org.eclipse.core.resources.IFile;

/**
 * 
 * @version 1.0.0 
 * @author <a href="mailto:angelo.zerr@gmail.com">Angelo ZERR</a>
 *
 */
public class NewAkrogenWizardGenerationWizard extends AbstractAkrogenGenerationWizard {

    private AkrogenWizard akrogenWizard;

    public NewAkrogenWizardGenerationWizard(AkrogenProject akrogenProject) {
        this(akrogenProject, null, null);
    }

    public NewAkrogenWizardGenerationWizard(AkrogenProject akrogenProject, AkrogenWizard akrogenWizard, IFile selectedFile) {
        super(akrogenProject);
        String title = akrogenWizard.getTitle();
        if (title == null) {
            title = AkrogenMessages.getString("NewAkrogenWizardGenerationWizard.wizard.no_title", akrogenWizard.getName());
        }
        setWindowTitle(title);
        setNeedsProgressMonitor(true);
        this.selectedFile = selectedFile;
        this.akrogenWizard = akrogenWizard;
    }

    /**
	 * Add Pages swith AkrogenWizard configuration.
	 */
    public void addPages() {
        if (akrogenWizard != null) {
            List akrogenWizardPages = akrogenWizard.getAkrogenWizardPages();
            int i = 0;
            for (Iterator iter = akrogenWizardPages.iterator(); iter.hasNext(); i++) {
                AkrogenWizardPage akrogenWizardPage = (AkrogenWizardPage) iter.next();
                String componentNameWithNamespace = akrogenWizardPage.getComponentRef();
                String componentName = AkrogenUtil.getComponenName(componentNameWithNamespace);
                String namespace = AkrogenUtil.getNamespace(componentNameWithNamespace);
                addComponentPage(i, componentName, namespace, akrogenWizardPage);
            }
        }
    }

    private NewAkrogenWizardGenerationWizardPage addComponentPage(int i, String componentName, String namespace, AkrogenWizardPage akrogenWizardPage) {
        AkrogenComponent component = getComponentManager().getAkrogenComponent(componentName, namespace);
        NewAkrogenWizardGenerationWizardPage wizardPage = null;
        if (component != null) {
            wizardPage = new NewAkrogenWizardGenerationWizardPage(getAkrogenProject(), component, akrogenWizardPage);
            wizardPage.setSelectedXmlFileSource(selectedFile);
            if (i == 0) addPage(wizardPage); else {
                wizardPage.setPageComplete(false);
                addDynamicWizardPage(wizardPage);
            }
        } else {
            IDynamicWizardPage wizardPageDontExist = new NewComponentGenerationDontExistWizardPage(getAkrogenProject(), componentName);
            if (i == 0) addPage(wizardPageDontExist); else addDynamicWizardPage(wizardPageDontExist);
        }
        return wizardPage;
    }
}
