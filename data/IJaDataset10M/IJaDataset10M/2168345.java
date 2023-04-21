package de.sonivis.tool.core.datamodel.extractwizard;

import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;

/**
 * Every implementation of {@link Transformer} must return an implementation of this class if
 * {@link Transformer#getWizardPage()} is called.
 * 
 * @author Benedikt
 */
public abstract class TransformWizardPage extends WizardPage {

    public TransformWizardPage(String name) {
        super(name);
    }

    /**
	 * @see org.eclipse.jface.wizard.WizardPage#canFlipToNextPage()
	 */
    @Override
    public final boolean canFlipToNextPage() {
        if (((DataModelWizard) getWizard()).getModel().getEnabledExtractorPages().hasNext()) {
            ((DataModelWizard) getWizard()).getModel().getEnabledExtractorPages().next();
        }
        return !((DataModelWizard) getWizard()).getModel().getEnabledTransformerPages().hasNext();
    }

    /**
	 * @see org.eclipse.jface.wizard.WizardPage#getNextPage()
	 */
    @Override
    public final IWizardPage getNextPage() {
        saveData();
        return ((DataModelWizard) getWizard()).getModel().getEnabledTransformerPages().next();
    }

    /**
	 * @see org.eclipse.jface.wizard.WizardPage#getPreviousPage()
	 */
    @Override
    public final IWizardPage getPreviousPage() {
        if (((DataModelWizard) getWizard()).getModel().getEnabledTransformerPages().hasPrevious()) {
            ((DataModelWizard) getWizard()).getModel().getEnabledTransformerPages().previous();
        }
        return super.getPreviousPage();
    }

    /**
	 * @see org.eclipse.jface.wizard.WizardPage#isPageComplete()
	 */
    @Override
    public abstract boolean isPageComplete();

    /**
	 * This method must save all preference data, given by the user via the {@link WizardPage},
	 * adequately. E.g. in the {@link PreferenceStore}.
	 */
    public abstract void saveData();
}
