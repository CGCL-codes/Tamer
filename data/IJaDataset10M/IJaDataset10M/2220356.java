package com.google.gdt.eclipse.designer.wizards.model.service;

import com.google.gdt.eclipse.designer.wizards.Activator;
import org.eclipse.wb.internal.core.DesignerPlugin;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

/**
 * Wizard for new GWT RemoteModule.
 * 
 * @author scheglov_ke
 * @coverage gwt.wizard.ui
 */
public class ServiceWizard extends Wizard implements INewWizard {

    private IPackageFragment m_selectedPackage;

    private ServiceWizardPage m_servicePage;

    public ServiceWizard() {
        setDefaultPageImageDescriptor(Activator.getImageDescriptor("wizards/service/banner.gif"));
        setWindowTitle("New GWT RemoteService");
    }

    @Override
    public void addPages() {
        m_servicePage = new ServiceWizardPage(m_selectedPackage);
        addPage(m_servicePage);
    }

    public void init(IWorkbench workbench, IStructuredSelection selection) {
        if (selection != null) {
            Object selectedObject = selection.getFirstElement();
            if (selectedObject instanceof IResource) {
                IResource resource = (IResource) selectedObject;
                selectedObject = JavaCore.create(resource);
            }
            if (selectedObject instanceof IJavaElement) {
                IJavaElement selectedJavaElement = (IJavaElement) selectedObject;
                m_selectedPackage = (IPackageFragment) selectedJavaElement.getAncestor(IJavaElement.PACKAGE_FRAGMENT);
            } else {
                m_selectedPackage = null;
            }
        }
    }

    @Override
    public boolean performFinish() {
        try {
            m_servicePage.createService();
            return true;
        } catch (Throwable e) {
            DesignerPlugin.log(e);
        }
        return false;
    }
}
