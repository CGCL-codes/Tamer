package net.confex.app.views;

import net.confex.app.Confex;
import net.confex.app.core.INode;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.BaseNewWizardMenu;
import org.eclipse.ui.part.ISetSelectionTarget;
import org.eclipse.ui.part.ViewPart;

public class View extends ViewPart implements ISetSelectionTarget {

    public static final String ID = Confex.PLUGIN_ID + ".view";

    private TreeViewer viewer;

    private BaseNewWizardMenu newWizardMenu;

    /**
	 * 
	 */
    public void createPartControl(Composite parent) {
        viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        initListeners();
        initContextMenu();
        viewer.setContentProvider(new ViewContentProvider());
        viewer.setLabelProvider(new ViewLabelProvider());
        viewer.setInput(Confex.getModel());
        getSite().setSelectionProvider(viewer);
    }

    private void initListeners() {
        viewer.addDoubleClickListener(new IDoubleClickListener() {

            @Override
            public void doubleClick(DoubleClickEvent event) {
                IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                Object element = selection.getFirstElement();
                boolean isNode = element instanceof INode;
                if (viewer.isExpandable(element)) {
                    boolean expanded = viewer.getExpandedState(element);
                    if ((isNode && !expanded) || !isNode) viewer.setExpandedState(element, !expanded);
                }
                if (selection.size() == 1 && isNode) {
                    OpenNodeAction ona = new OpenNodeAction(getSite().getPage());
                    ona.selectionChanged((IStructuredSelection) viewer.getSelection());
                    if (ona.isEnabled()) ona.run();
                }
            }
        });
    }

    private void initContextMenu() {
        MenuManager menuMgr = new MenuManager("popup:View");
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() {

            public void menuAboutToShow(IMenuManager manager) {
                View.this.fillContextMenu(manager);
            }
        });
        Menu menu = menuMgr.createContextMenu(viewer.getTree());
        viewer.getTree().setMenu(menu);
        getSite().registerContextMenu(menuMgr, viewer);
    }

    protected void fillContextMenu(IMenuManager manager) {
        String newText = "&New";
        String newId = ActionFactory.NEW.getId();
        MenuManager newMenu = new MenuManager(newText, newId);
        newMenu.setActionDefinitionId("org.eclipse.ui.file.newQuickMenu");
        newMenu.add(new Separator(newId));
        this.newWizardMenu = new BaseNewWizardMenu(getSite().getWorkbenchWindow(), null);
        newMenu.add(this.newWizardMenu);
        newMenu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
        manager.add(newMenu);
        manager.add(new GroupMarker(IWorkbenchActionConstants.NEW_EXT));
        manager.add(new Separator());
    }

    /**
	 * Passing the focus request to the viewer's control.
	 */
    public void setFocus() {
        viewer.getControl().setFocus();
    }

    @Override
    public void selectReveal(ISelection selection) {
        if (!(selection instanceof IStructuredSelection) || selection.isEmpty()) return;
        getViewer().getControl().setRedraw(false);
        getViewer().setSelection(selection, true);
        getViewer().getControl().setRedraw(true);
    }

    public TreeViewer getViewer() {
        return viewer;
    }
}
