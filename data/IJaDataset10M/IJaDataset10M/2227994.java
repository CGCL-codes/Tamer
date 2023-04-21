package org.lcelb.accounts.manager.ui.wizards;

import org.eclipse.jface.wizard.Wizard;
import org.lcelb.accounts.manager.data.DataActivator;

/**
 * Provides services to create model elements wizards<br>
 * 
 * @author fournier <br>
 * 
 * 24 nov. 06
 */
public abstract class ModelElementWizard extends Wizard {

    /**
   * Mark data as dirty
   * 
   */
    protected void markDirty() {
        DataActivator.getDefault().getDataHolder().markDirty();
    }
}
