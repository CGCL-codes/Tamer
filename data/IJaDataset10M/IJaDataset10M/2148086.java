package eu.medeia.ui.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import eu.medeia.model.ESBKnowledgeRepository;
import eu.medeia.treemodel.TreeObject;

/**
 * The Class NewInstanceModelWizard.
 */
public class NewInstanceModelWizard extends Wizard implements INewWizard {

    /** The details. */
    SpecifyModelDetailsWizardPage details;

    /** The page. */
    SelectMetaModelWizardPage page;

    /**
	 * Instantiates a new new instance model wizard.
	 */
    public NewInstanceModelWizard() {
    }

    @Override
    public void addPages() {
        if (page == null) {
            page = new SelectMetaModelWizardPage("Select MetaModel");
        }
        if (details == null) {
            details = new SpecifyModelDetailsWizardPage("Specify Details");
        }
        addPage(page);
        addPage(details);
    }

    @Override
    public boolean performFinish() {
        TreeObject metaModel = page.getSelectedElement();
        ESBKnowledgeRepository.getInstance().createInstanceModel(metaModel.getId(), details.getInstanceName());
        return true;
    }

    @Override
    public void init(IWorkbench workbench, IStructuredSelection selection) {
    }
}
