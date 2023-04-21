package de.beas.explicanto.client.rcp.editor;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.actions.ActionFactory;
import de.beas.explicanto.client.I18N;
import de.beas.explicanto.client.rcp.editor.actions.AddChildAction;
import de.beas.explicanto.client.rcp.editor.actions.CopyIntoAction;
import de.beas.explicanto.client.rcp.editor.actions.ExpandAction;
import de.beas.explicanto.client.rcp.editor.actions.ShowContentAction;
import de.beas.explicanto.client.rcp.editor.parts.NodeEditPart;
import de.beas.explicanto.client.rcp.editor.parts.NodeTreeEditPart;
import de.beas.explicanto.client.rcp.editor.parts.ParentNodeEditPart;
import de.beas.explicanto.client.rcp.editor.parts.ParentNodeTreeEditPart;
import de.beas.explicanto.client.rcp.editor.parts.RootNodeEditPart;
import de.beas.explicanto.client.rcp.editor.parts.RootNodeTreeEditPart;
import de.beas.explicanto.client.template.Document;
import de.beas.explicanto.client.template.Element;

/**
 * CsdeContextMenuProvider
 * This constructs the right click context menu inside the editor,
 * adding entries for cut/copy/paste etc., but also for each allowable
 * child and sibling, dynamically.
 * @author Lucian Brancovean
 * @version 1.0
 *  
 */
public class CsdeContextMenuProvider extends ContextMenuProvider {

    private final ActionRegistry actionRegistry;

    /**
     * @param viewer
     */
    public CsdeContextMenuProvider(EditPartViewer viewer, ActionRegistry registry) {
        super(viewer);
        actionRegistry = registry;
    }

    /**
     * Called when the context menu is about to show. Actions will appear in the
     * context menu.
     * 
     * @see org.eclipse.gef.ContextMenuProvider#buildContextMenu(org.eclipse.jface.action.IMenuManager)
     */
    public void buildContextMenu(IMenuManager menu) {
        GEFActionConstants.addStandardActionGroups(menu);
        menu.appendToGroup(GEFActionConstants.GROUP_EDIT, getAction(ActionFactory.CUT.getId()));
        menu.appendToGroup(GEFActionConstants.GROUP_EDIT, getAction(ActionFactory.COPY.getId()));
        menu.appendToGroup(GEFActionConstants.GROUP_EDIT, getAction(ActionFactory.PASTE.getId()));
        menu.appendToGroup(GEFActionConstants.GROUP_EDIT, getAction(ActionFactory.DELETE.getId()));
        menu.appendToGroup(GEFActionConstants.GROUP_EDIT, getAction(ExpandAction.ID));
        menu.appendToGroup(GEFActionConstants.GROUP_ADD, getAction(CopyIntoAction.ID));
        addChildSubmenu(menu);
    }

    /**
     * 
     * @param menu
     */
    private void addChildSubmenu(IMenuManager menu) {
        IStructuredSelection sel = (IStructuredSelection) getViewer().getSelection();
        int level = 0;
        if (level > 0) if (sel.getFirstElement() instanceof RootNodeEditPart || sel.getFirstElement() instanceof RootNodeTreeEditPart) return;
        if (sel.getFirstElement() instanceof NodeEditPart && !(sel.getFirstElement() instanceof RootNodeEditPart)) {
            MenuManager newSubmenu = new MenuManager(I18N.translate("Actions.add_sibling"));
            NodeEditPart part = (NodeEditPart) sel.getFirstElement();
            ParentNodeEditPart parentPart = (ParentNodeEditPart) part.getRoot().getViewer().getEditPartRegistry().get(part.getNode().getParent());
            de.beas.explicanto.client.template.Element element = parentPart.getParentNode().getElement();
            Iterator iter = getPossibleChildren(element).iterator();
            while (iter.hasNext()) {
                Element kid = (Element) iter.next();
                newSubmenu.add(new AddChildAction(parentPart.getParentNode(), kid, getViewer().getEditDomain().getCommandStack()));
            }
            menu.add(newSubmenu);
        }
        if (sel.getFirstElement() instanceof NodeTreeEditPart && !(sel.getFirstElement() instanceof RootNodeTreeEditPart)) {
            MenuManager newSubmenu = new MenuManager(I18N.translate("Actions.add_sibling"));
            NodeTreeEditPart part = (NodeTreeEditPart) sel.getFirstElement();
            ParentNodeTreeEditPart parentPart = (ParentNodeTreeEditPart) part.getRoot().getViewer().getEditPartRegistry().get(part.getNode().getParent());
            Element element = parentPart.getParentNode().getElement();
            Iterator iter = getPossibleChildren(element).iterator();
            while (iter.hasNext()) {
                Element kid = (Element) iter.next();
                newSubmenu.add(new AddChildAction(parentPart.getParentNode(), kid, getViewer().getEditDomain().getCommandStack()));
            }
            menu.add(newSubmenu);
        }
        if (sel.getFirstElement() instanceof ParentNodeEditPart) {
            MenuManager newSubmenu = new MenuManager(I18N.translate("Actions.add_child"));
            ParentNodeEditPart part = (ParentNodeEditPart) sel.getFirstElement();
            Element element = part.getParentNode().getElement();
            Iterator iter = getPossibleChildren(element).iterator();
            while (iter.hasNext()) {
                Element kid = (Element) iter.next();
                newSubmenu.add(new AddChildAction(part.getParentNode(), kid, getViewer().getEditDomain().getCommandStack()));
            }
            menu.add(newSubmenu);
        }
        if (sel.getFirstElement() instanceof ParentNodeTreeEditPart) {
            MenuManager newSubmenu = new MenuManager(I18N.translate("Actions.add_child"));
            ParentNodeTreeEditPart part = (ParentNodeTreeEditPart) sel.getFirstElement();
            Element element = part.getParentNode().getElement();
            Iterator iter = getPossibleChildren(element).iterator();
            while (iter.hasNext()) {
                Element kid = (Element) iter.next();
                newSubmenu.add(new AddChildAction(part.getParentNode(), kid, getViewer().getEditDomain().getCommandStack()));
            }
            menu.add(newSubmenu);
        }
    }

    private IAction getAction(String actionId) {
        return actionRegistry.getAction(actionId);
    }

    private List getPossibleChildren(Element element) {
        List list = new LinkedList();
        Document template = element.getDocument();
        Iterator iterator = template.getElementsIterator();
        while (iterator.hasNext()) {
            Element child = (Element) iterator.next();
            if (child.getParents().contains(element.getId())) list.add(child);
        }
        return list;
    }
}
