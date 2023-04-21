package org.xulbooster.eclipse.xb.ui.export;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.internal.wizards.datatransfer.DataTransferMessages;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.xulbooster.eclipse.xb.utils.XulProjectProperties;

/**
 * Standard workbench wizard for exporting resources from the workspace to a zip
 * file.
 * <p>
 * This class may be instantiated and used without further configuration; this
 * class is not intended to be subclassed.
 * </p>
 * <p>
 * Example:
 * 
 * <pre>
 * IWizard wizard = new ZipFileExportWizard();
 * wizard.init(workbench, selection);
 * WizardDialog dialog = new WizardDialog(shell, wizard);
 * dialog.open();
 * </pre>
 * 
 * During the call to <code>open</code>, the wizard dialog is presented to
 * the user. When the user hits Finish, the user-selected workspace resources
 * are exported to the user-specified zip file, the dialog closes, and the call
 * to <code>open</code> returns.
 * </p>
 */
public class XpiExportWizard extends Wizard implements IExportWizard {

    private IStructuredSelection selection;

    private XpiExportWizardPage mainPage;

    private IProject defaultSelection;

    /**
	 * Creates a wizard for exporting workspace resources to a zip file.
	 */
    public XpiExportWizard() {
        super();
        AbstractUIPlugin plugin = (AbstractUIPlugin) Platform.getPlugin(PlatformUI.PLUGIN_ID);
        IDialogSettings workbenchSettings = plugin.getDialogSettings();
        IDialogSettings section = workbenchSettings.getSection("XpiExportWizard");
        if (section == null) {
            section = workbenchSettings.addNewSection("XpiExportWizard");
        }
        setDialogSettings(section);
    }

    public IProject getDefaultSelection() {
        return defaultSelection;
    }

    public void setDefaultSelection(IProject defaultSelection) {
        this.defaultSelection = defaultSelection;
    }

    public void addPages() {
        super.addPages();
        mainPage = new XpiExportWizardPage(selection);
        addPage(mainPage);
    }

    public static List computeSelectedResources(IStructuredSelection originalSelection) {
        List resources = null;
        try {
            for (Iterator e = originalSelection.iterator(); e.hasNext(); ) {
                Object next = e.next();
                Object resource = null;
                if (next instanceof IResource) {
                    if ((next instanceof IProject) == false) {
                        IResource res = (IResource) next;
                        next = res.getProject();
                    }
                    if (next instanceof IProject) {
                        IProject pro = (IProject) next;
                        IFolder src = pro.getFolder(XulProjectProperties.getSourceFolderName(pro));
                        if (src != null) {
                            IResource[] res = src.members();
                            for (int x = 0; x < res.length; x++) {
                                if (resources == null) {
                                    resources = new ArrayList(originalSelection.size());
                                }
                                resources.add(res[x]);
                            }
                        }
                    }
                } else if (next instanceof IAdaptable) {
                    resource = ((IAdaptable) next).getAdapter(IResource.class);
                }
                if (resource != null) {
                    if (resources == null) {
                        resources = new ArrayList(originalSelection.size());
                    }
                    resources.add(resource);
                }
            }
            if (resources == null) {
                return Collections.EMPTY_LIST;
            }
        } catch (CoreException e1) {
            e1.printStackTrace();
        }
        return resources;
    }

    private IStructuredSelection getSelection(IStructuredSelection currentSelection) {
        if (((currentSelection == null) || currentSelection.isEmpty()) && (this.defaultSelection != null)) {
            List selectedResources = new ArrayList(1);
            selectedResources.add(this.defaultSelection);
            currentSelection = new StructuredSelection(selectedResources);
        }
        return currentSelection;
    }

    public void init(IWorkbench workbench, IStructuredSelection currentSelection) {
        currentSelection = getSelection(currentSelection);
        this.selection = currentSelection;
        List selectedResources = computeSelectedResources(currentSelection);
        if (!selectedResources.isEmpty()) {
            this.selection = new StructuredSelection(selectedResources);
        }
        setWindowTitle("xpi export");
        ImageDescriptor imgDesc = Activator.getImageDescriptor("icons/xpinstallItemGeneric.png");
        setDefaultPageImageDescriptor(imgDesc);
        setNeedsProgressMonitor(true);
    }

    public boolean performFinish() {
        return mainPage.finish();
    }
}
