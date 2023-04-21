package com.tresys.slide.plugin.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import com.tresys.slide.plugin.editors.ModuleEditor;
import com.tresys.slide.plugin.wizards.DomainWizard;

public class AddDomainAction extends Action {

    private ModuleEditor m_modEditor = null;

    public AddDomainAction(ModuleEditor i_editor) {
        super();
        m_modEditor = i_editor;
    }

    public void run() {
        Shell shell = getShell();
        if (shell == null || m_modEditor == null) return;
        DomainWizard wizard = new DomainWizard(m_modEditor);
        WizardDialog mydialog = new WizardDialog(shell, wizard);
        mydialog.setPageSize(200, 400);
        mydialog.setBlockOnOpen(true);
        mydialog.open();
    }

    private Shell getShell() {
        if (m_modEditor != null) return m_modEditor.getEditorSite().getShell();
        return null;
    }
}
