package de.fraunhofer.isst.axbench.views.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import de.fraunhofer.isst.axbench.axlang.api.IAXLangElement;
import de.fraunhofer.isst.axbench.axlang.elements.architecturemodel.Port;
import de.fraunhofer.isst.axbench.utilities.IAXLangElementNode;
import de.fraunhofer.isst.axbench.views.instanceview.InstanceView;

/**
 * @brief Show menu for connecting ports and copies the connection string to the clipboard.
 * @author ekleinod
 * @author skaegebein
 * @version 0.8.0
 * @since 0.7.2
 */
public class ConnectPortAction implements IObjectActionDelegate {

    private IWorkbenchPart thePart = null;

    private IAXLangElement thePort = null;

    private IAXLangElement theSubComponent = null;

    /**
     * @brief Constructor.
     */
    public ConnectPortAction() {
    }

    /**
     * @brief Sets the active part.
     * @param action action proxy
     * @param targetPart new active part
     */
    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
        thePart = targetPart;
    }

    /**
     * @brief Executes the action.
     * @param action action proxy
     */
    public void run(IAction action) {
        if (thePort != null && theSubComponent != null) {
            ConnectPortDialog theDialog = new ConnectPortDialog(thePort, theSubComponent, thePart.getSite().getShell());
            theDialog.open();
        }
    }

    /**
     * @brief Reacts on selection changes.
     * @param action action proxy
     * @param selection changes selection
     */
    public void selectionChanged(IAction action, ISelection selection) {
        thePort = null;
        theSubComponent = null;
        if (selection instanceof IStructuredSelection) {
            IStructuredSelection theSelection = (IStructuredSelection) selection;
            if (theSelection.getFirstElement() instanceof IAXLangElementNode) {
                if (((IAXLangElementNode) theSelection.getFirstElement()).getIAXLangElement() instanceof Port) {
                    thePort = (IAXLangElement) theSelection.getFirstElement();
                }
            }
            if (theSelection.getFirstElement() instanceof Port) {
                thePort = (IAXLangElement) theSelection.getFirstElement();
            }
        }
        if (thePort != null) {
            TreeViewer treeViewer = ((InstanceView) thePart).getTreeViewer();
            TreeItem selectedItem = null;
            if (treeViewer.getTree().getSelection()[0] != null) {
                selectedItem = treeViewer.getTree().getSelection()[0];
                if (selectedItem.getParentItem().getParentItem().getParentItem().getData() instanceof IAXLangElementNode) {
                    theSubComponent = ((IAXLangElementNode) selectedItem.getParentItem().getParentItem().getParentItem().getData()).getIAXLangElement();
                }
                if (selectedItem.getParentItem().getParentItem().getParentItem().getData() instanceof IAXLangElement) {
                    theSubComponent = (IAXLangElement) selectedItem.getParentItem().getParentItem().getParentItem().getData();
                }
            }
        }
    }
}
