package com.google.gdt.eclipse.designer.gxt.wizards;

import org.eclipse.wb.internal.core.wizards.AbstractDesignWizardPage;

/**
 * Wizard for <code>com.extjs.gxt.ui.client.widget.Dialog</code>.
 * 
 * @author scheglov_ke
 * @coverage ExtGWT.wizard
 */
public final class DialogWizard extends GxtWizard {

    public DialogWizard() {
        setWindowTitle("New Ext-GWT Dialog");
    }

    @Override
    protected AbstractDesignWizardPage createMainPage() {
        return new DialogWizardPage();
    }
}
