package repast.simphony.agents.designer.ui.views;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.part.ISetSelectionTarget;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.navigator.MainActionGroup;
import repast.simphony.agents.designer.core.AgentBuilderPlugin;

/**
 * @author agreif (Adapted by Michael J. North for Use in Repast Simphony from
 *         Alexander Greif�s Flow4J-Eclipse
 *         (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks
 *         to the Original Author)
 * 
 * TODO
 * 
 * 
 * 
 */
public class AgentBuilderResourcesView extends ViewPart implements ISetSelectionTarget, IMenuListener {

    protected TreeViewer treeViewer;

    protected MainActionGroup mainActionGroup;

    /**
	 * TODO
	 * 
	 */
    public AgentBuilderResourcesView() {
        super();
    }

    /**
	 * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
    @Override
    public void createPartControl(Composite parent) {
        treeViewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        treeViewer.addFilter(new AgentBuilderResourcesViewerFilter());
        treeViewer.setContentProvider(new WorkbenchContentProvider());
        treeViewer.setLabelProvider(new WorkbenchLabelProvider());
        treeViewer.setInput(this.getInitialInput());
        treeViewer.addOpenListener(new AgentBuilderResourcesViewOpenListener(this));
        MenuManager menuMgr = new MenuManager("repast.simphony.agents.ui.PopupMenu");
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(this);
        Menu fContextMenu = menuMgr.createContextMenu(treeViewer.getTree());
        treeViewer.getTree().setMenu(fContextMenu);
        IWorkbenchPartSite site = this.getSite();
        site.registerContextMenu(menuMgr, treeViewer);
    }

    /**
	 * @see org.eclipse.ui.IWorkbenchPart#setFocus()
	 */
    @Override
    public void setFocus() {
        treeViewer.getControl().setFocus();
    }

    /**
	 * TODO
	 * 
	 * @return
	 */
    private Object getInitialInput() {
        IAdaptable input = this.getSite().getPage().getInput();
        IResource resource = null;
        if (input instanceof IResource) {
            resource = (IResource) input;
        } else {
            resource = (IResource) input.getAdapter(IResource.class);
        }
        if (resource != null) {
            int type = resource.getType();
            if (type == IResource.FILE) return resource.getParent(); else if (type == IResource.FOLDER || type == IResource.PROJECT || type == IResource.ROOT) return resource;
        }
        return AgentBuilderPlugin.getWorkspace().getRoot();
    }

    /**
	 * @see org.eclipse.ui.part.ISetSelectionTarget#selectReveal(org.eclipse.jface.viewers.ISelection)
	 */
    public void selectReveal(ISelection selection) {
        treeViewer.setSelection(selection, true);
    }

    public void menuAboutToShow(IMenuManager manager) {
    }
}
