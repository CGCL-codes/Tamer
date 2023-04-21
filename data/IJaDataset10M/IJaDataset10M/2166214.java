package edu.asu.vogon.ontology.ui.wizard;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.graphics.Image;
import edu.asu.vogon.digitalHPS.IVocabularyEntry;
import edu.asu.vogon.ontology.ui.Activator;
import edu.asu.vogon.ontology.ui.Constants;
import edu.asu.vogon.util.files.FileHandler;
import edu.asu.vogon.util.images.ImageRegistry;
import edu.asu.vogon.util.properties.PropertyHandler;
import edu.asu.vogon.util.properties.PropertyHandlerRegistry;

public class AddTextResultWizard extends Wizard {

    private String selectedTerm;

    private SelectTermWizardPage selectPage;

    private NewTermWizardPage newPage;

    private IVocabularyEntry term;

    public AddTextResultWizard(String selectedTerm) {
        this.selectedTerm = selectedTerm;
        {
            String path = FileHandler.getAbsolutePath(Activator.PLUGIN_ID, "icons/thunderbird56.png");
            if (path != null) {
                Image image = ImageRegistry.REGISTRY.getImage(path);
                if (image != null) {
                    ImageDescriptor desc = ImageDescriptor.createFromImage(image);
                    setDefaultPageImageDescriptor(desc);
                }
            }
        }
    }

    @Override
    public void addPages() {
        super.addPages();
        PropertyHandler handler = PropertyHandlerRegistry.REGISTRY.getPropertyHandler(Activator.PLUGIN_ID, Constants.PROPERTIES_FILE);
        selectPage = new SelectTermWizardPage(handler.getProperty("_select_term_wizard_selection"), selectedTerm);
        newPage = new NewTermWizardPage(handler.getProperty("_new_term_wizard_title"), selectedTerm);
        addPage(selectPage);
        addPage(newPage);
    }

    @Override
    public boolean performFinish() {
        if (selectPage.termExistsButtonSelected()) {
            term = selectPage.getSelectedTerm();
        } else {
        }
        return true;
    }

    @Override
    public boolean canFinish() {
        if (selectPage.termExistsButtonSelected() && selectPage.isPageComplete()) return true;
        return super.canFinish();
    }

    public IVocabularyEntry getTerm() {
        return term;
    }
}
