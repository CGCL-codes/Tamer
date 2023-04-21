package com.android.ide.eclipse.adt.internal.wizards.actions;

import com.android.ide.eclipse.adt.internal.wizards.newproject.NewProjectWizard;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IWorkbenchWizard;

/**
 * Delegate for the toolbar action "Android Project".
 * It displays the Android New Project wizard to create a new Android Project (not a test project).
 *
 * @see NewTestProjectAction
 */
public class NewProjectAction extends OpenWizardAction {

    @Override
    protected IWorkbenchWizard instanciateWizard(IAction action) {
        return new NewProjectWizard();
    }
}
