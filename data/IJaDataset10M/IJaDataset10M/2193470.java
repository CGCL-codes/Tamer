package org.lcelb.accounts.manager.ui.extensions.wizards;

import java.util.List;
import org.lcelb.accounts.manager.data.AbstractOwner;
import org.lcelb.accounts.manager.data.ModelElement;
import org.lcelb.accounts.manager.data.extensions.transaction.validity.DefaultValidity;
import org.lcelb.accounts.manager.data.extensions.transaction.validity.ValidityFactory;
import org.lcelb.accounts.manager.data.transaction.validity.AbstractValidity;
import org.lcelb.accounts.manager.ui.extensions.data.UIContainer;
import org.lcelb.accounts.manager.ui.extensions.message.Messages;
import org.lcelb.accounts.manager.ui.extensions.wizards.pages.DefaultElementPage;
import org.lcelb.accounts.manager.ui.extensions.wizards.pages.DefaultValidityPage;

/**
 * Wizard to create a new validity.<br>
 * 
 * @author fournier <br>
 * 
 * 1 dec. 06
 */
public class NewDefaultValidityWizard extends NewDefaultElementWizard {

    /**
   * Constructor
   * 
   * @param container_p
   */
    public NewDefaultValidityWizard(UIContainer container_p) {
        super(Messages.NewDefaultValidityWizard_Title, container_p);
    }

    /**
   * Create the default wizard page used by this wizard
   */
    @Override
    protected DefaultElementPage createWizardPage() {
        DefaultValidityPage validityPage = new DefaultValidityPage(getPageId(), getPageTitle(), getPageDescription(), getElement());
        validityPage.setElementNameLabel(getElementLabelInPage());
        return validityPage;
    }

    /**
   * @see org.lcelb.accounts.manager.ui.extensions.wizards.NewDefaultElementWizard#addModelElementInParent(org.lcelb.accounts.manager.data.ModelElement,
   *      java.lang.Object)
   */
    @Override
    protected void addModelElementInParent(ModelElement createdElement_p, Object parent_p) {
        AbstractOwner owner = (AbstractOwner) parent_p;
        owner.getValidities().add((AbstractValidity) createdElement_p);
        handleValidatedAttributeValue((DefaultValidity) createdElement_p);
    }

    /**
   * @see org.lcelb.accounts.manager.ui.extensions.wizards.NewDefaultElementWizard#createModelElement()
   */
    @Override
    protected ModelElement createModelElement() {
        DefaultValidity validity = ValidityFactory.eINSTANCE.createDefaultValidity();
        setName(validity);
        setValidatedAttribute(validity);
        return validity;
    }

    /**
   * @see org.lcelb.accounts.manager.ui.extensions.wizards.NewDefaultElementWizard#getPageDescription()
   */
    @Override
    protected String getPageDescription() {
        return Messages.DefaultValidityPage_Description_Create;
    }

    /**
   * @see org.lcelb.accounts.manager.ui.extensions.wizards.NewDefaultElementWizard#getPageId()
   */
    @Override
    protected String getPageId() {
        return "DefaultValidityPage";
    }

    /**
   * @see org.lcelb.accounts.manager.ui.extensions.wizards.NewDefaultElementWizard#getPageTitle()
   */
    @Override
    protected String getPageTitle() {
        return Messages.DefaultValidityPage_Title;
    }

    @Override
    protected String getElementLabelInPage() {
        return Messages.DefaultValidityPage_Validity_Name;
    }

    /**
   * @see org.lcelb.accounts.manager.ui.extensions.wizards.NewDefaultElementWizard#getElement()
   */
    @Override
    protected ModelElement getElement() {
        return null;
    }

    /**
   * @see org.lcelb.accounts.manager.ui.extensions.wizards.NewDefaultElementWizard#isCreation()
   */
    @Override
    protected boolean isCreation() {
        return true;
    }

    /**
   * @see org.lcelb.accounts.manager.ui.extensions.wizards.NewDefaultElementWizard#modifyModelElement()
   */
    @Override
    protected void modifyModelElement() {
        DefaultValidity validity = (DefaultValidity) getElement();
        setName(validity);
        setValidatedAttribute(validity);
        handleValidatedAttributeValue(validity);
    }

    /**
   * Set the entered (by end-user) final validation value.
   * 
   * @param validity_p
   */
    private void setValidatedAttribute(DefaultValidity validity_p) {
        boolean validated = ((DefaultValidityPage) getDefaultElementPage()).isFinalValidation();
        validity_p.setValidated(validated);
    }

    /**
   * Set the entered (by end-user) name
   * 
   * @param validity_p
   */
    private void setName(DefaultValidity validity_p) {
        String name = getDefaultElementPage().getElementName();
        validity_p.setName(name);
    }

    /**
   * Only one default validity has the validated attribute set to true.
   * 
   * @param validity_p
   */
    @SuppressWarnings("unchecked")
    private void handleValidatedAttributeValue(DefaultValidity validity_p) {
        if (validity_p.isValidated()) {
            AbstractOwner owner = (AbstractOwner) validity_p.eContainer();
            List validities = owner.getValidities();
            DefaultValidity[] validityArray = (DefaultValidity[]) validities.toArray(new DefaultValidity[validities.size()]);
            for (DefaultValidity validity : validityArray) {
                if (!validity.equals(validity_p)) {
                    validity.setValidated(false);
                }
            }
        }
    }
}
