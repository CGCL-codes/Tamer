package com.google.gdt.eclipse.designer.actions.wizard.model;

import com.google.gdt.eclipse.designer.wizards.model.service.ServiceWizard;
import org.eclipse.wb.internal.core.wizards.AbstractOpenWizardDelegate;
import org.eclipse.jface.wizard.IWizard;

/**
 * Action for adding new GWT RemoteService.
 * 
 * @author scheglov_le
 * @coverage gwt.actions
 */
public class NewServiceAction extends AbstractOpenWizardDelegate {

    @Override
    protected IWizard createWizard() {
        return new ServiceWizard();
    }
}
