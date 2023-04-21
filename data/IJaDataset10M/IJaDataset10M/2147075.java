package org.jjflyboy.slee.descriptors.editors;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.jdt.ui.actions.OpenNewClassWizardAction;
import org.eclipse.jdt.ui.wizards.NewClassWizardPage;
import org.eclipse.jdt.ui.wizards.NewTypeWizardPage;

public class ClassInputComponent extends TypeInputComponent {

    public ClassInputComponent(EStructuralFeature feature) {
        super(feature);
    }

    @Override
    protected NewTypeWizardPage runTypeCreateWizardAction() {
        NewClassWizardPage page = new NewClassWizardPage();
        page.setSuperClass("java.lang.Object", true);
        OpenNewClassWizardAction action = new OpenNewClassWizardAction();
        action.setConfiguredWizardPage(page);
        action.run();
        return page;
    }

    @Override
    protected int getBrowserSearchFilter() {
        return IJavaElementSearchConstants.CONSIDER_CLASSES;
    }
}
