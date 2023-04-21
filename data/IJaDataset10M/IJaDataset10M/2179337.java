package net.sf.beatrix.module.ui.viewers;

import net.sf.beatrix.core.container.DataCollection;
import net.sf.beatrix.ui.viewers.AbstractTreeContentProviderExtension;
import net.sf.beatrix.ui.viewers.outline.ContentOutlineNode;
import org.eclipse.jface.viewers.Viewer;

public class DataCollectionContentProvider extends AbstractTreeContentProviderExtension {

    @Override
    public Object[] getElements(Object inputElement) {
        return getChildren(inputElement);
    }

    @Override
    public boolean hasChildren(Object element) {
        Object parent = null;
        if (element instanceof ContentOutlineNode) {
            ContentOutlineNode node = (ContentOutlineNode) element;
            element = node.getContent();
            parent = node.getParent();
        }
        return (element instanceof DataCollection && !((DataCollection) element).isEmpty()) || (element instanceof String && parent instanceof DataCollection && ((DataCollection) parent).contains((String) element));
    }

    @Override
    public Object[] getChildren(Object parentElement) {
        Object parent = null;
        ContentOutlineNode coNode = null;
        if (parentElement instanceof ContentOutlineNode) {
            coNode = (ContentOutlineNode) parentElement;
            parent = coNode.getParent();
            parentElement = coNode.getContent();
        }
        if (parentElement instanceof DataCollection) {
            DataCollection dc = (DataCollection) parentElement;
            return ContentOutlineNode.fromList(dc.providedAttributes(), dc).toArray();
        }
        if (parentElement instanceof String && parent instanceof DataCollection) {
            DataCollection dc = (DataCollection) parent;
            if (dc.contains((String) parentElement)) {
                return ContentOutlineNode.fromList(dc.get((String) parentElement), coNode == null ? parentElement : coNode).toArray();
            }
        }
        if (parentElement instanceof Object[]) {
            return (Object[]) parentElement;
        }
        return null;
    }

    @Override
    public Object getParent(Object element) {
        if (element instanceof ContentOutlineNode) {
            return ((ContentOutlineNode) element).getParent();
        }
        return null;
    }

    @Override
    public DataCollection getRoot(Object element) {
        Object root = element;
        while (root != null && !(root instanceof DataCollection)) {
            root = getParent(root);
        }
        return (DataCollection) root;
    }

    @Override
    public void inputChanged(Viewer newViewer, Object oldInput, Object newInput) {
    }

    @Override
    public void dispose() {
    }
}
