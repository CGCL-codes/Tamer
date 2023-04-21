package com.jiexplorer.rcp.actions;

import java.util.List;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import com.jiexplorer.rcp.model.CategoryNode;
import com.jiexplorer.rcp.srv.PersistentThumbnailService;
import com.jiexplorer.rcp.ui.category.CategoryViewer;
import com.jiexplorer.rcp.util.Messages;
import com.jiexplorer.rcp.util.UID;

public class NewCategoryAction extends RenameCategoryAction {

    /**
	 * The constructor.
	 */
    public NewCategoryAction(final String txt) {
        super(txt);
    }

    public NewCategoryAction(final CategoryViewer viewer, final String txt) {
        super(viewer, txt);
    }

    @Override
    public void run() {
        final ISelection sel = viewer.getSelection();
        if (!sel.isEmpty() && sel instanceof ISelection) {
            final IStructuredSelection asel = (IStructuredSelection) sel;
            final Object obj = asel.getFirstElement();
            if (obj instanceof CategoryNode) {
                final CategoryNode catNode = (CategoryNode) obj;
                String newNodeName = Messages.new_category_name;
                if (catNode.hasChildren()) {
                    final List children = catNode.getChildren();
                    boolean found = false;
                    int cnt = 0;
                    do {
                        newNodeName = found ? newNodeName + cnt : newNodeName;
                        found = false;
                        ++cnt;
                        if (catNode.hasChildren()) {
                            for (final Object childObj : children) {
                                if (childObj instanceof CategoryNode) {
                                    if (((CategoryNode) childObj).getName().equals(newNodeName)) {
                                        found = true;
                                    }
                                }
                            }
                        }
                    } while (found);
                }
                final UID uid = new UID();
                final CategoryNode newNode = new CategoryNode(catNode, newNodeName, uid.toString(), null);
                PersistentThumbnailService.getInstance().addCategories(catNode, new CategoryNode[] { newNode });
                catNode.addChild(newNode);
                final Widget widget = viewer.getItem(catNode);
                ((TreeItem) widget).setExpanded(true);
                final TreeSelection tsel = new TreeSelection(new TreePath[] { newNode.getTreePath() });
                final IStructuredSelection ssel = new StructuredSelection(tsel);
                viewer.setSelection(ssel, true);
                Widget newwidget = viewer.getItem(newNode);
                final TreeItem[] treeItems = ((TreeItem) widget).getItems();
                for (final TreeItem ti : treeItems) {
                    if (ti.getText().equals(newNodeName)) {
                        newwidget = ti;
                        break;
                    }
                }
                viewer.getTree().setSelection((TreeItem) newwidget);
                ((TreeItem) newwidget).setExpanded(true);
                originalText = newNodeName;
                if (editor == null) {
                    createEditor();
                }
                showEditor(originalText);
            }
        }
    }
}
