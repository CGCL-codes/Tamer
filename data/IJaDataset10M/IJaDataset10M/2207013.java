package net.sf.gef.core.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbench;

public abstract class AbstractWizard extends Wizard {

    public abstract void init(IWorkbench workbench, IStructuredSelection selection);
}
