package org.gems.designer;

import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.gems.designer.model.Container;

public class GemsPluginGeneratorWizard extends Wizard implements INewWizard {

    private PluginGeneratorTargetPage logicPage = null;

    private VisitorPage visitorPage_ = null;

    private IStructuredSelection selection;

    private IWorkbench workbench;

    private Container root_;

    private String title_;

    private IWizardPage[] pages_;

    private boolean visitor_ = true;

    private boolean showPkg_ = true;

    public GemsPluginGeneratorWizard(Container root) {
        root_ = root;
    }

    public GemsPluginGeneratorWizard(Container root, String title, boolean pkg, boolean visitor) {
        root_ = root;
        title_ = title;
        showPkg_ = pkg;
        visitor_ = visitor;
    }

    public IPackageFragment getTargetPackage() {
        return logicPage.getPackageFragment();
    }

    public IPackageFragmentRoot getRootTarget() {
        return logicPage.getRootTarget();
    }

    public void addPages() {
        logicPage = new PluginGeneratorTargetPage(title_, showPkg_);
        logicPage.setTitle(title_);
        addPage(logicPage);
        if (visitor_) {
            visitorPage_ = new VisitorPage();
            addPage(visitorPage_);
        }
    }

    public String getVisitorClassName() {
        if (visitorPage_.generateVisitor()) {
            return visitorPage_.getVisitorClassName();
        }
        return null;
    }

    public String getVisitorMenuItemName() {
        return visitorPage_.getMenuItemName();
    }

    public void init(IWorkbench aWorkbench, IStructuredSelection currentSelection) {
        workbench = aWorkbench;
        selection = currentSelection;
    }

    public boolean performFinish() {
        return true;
    }
}
