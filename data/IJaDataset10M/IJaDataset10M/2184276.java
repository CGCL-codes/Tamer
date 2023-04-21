package net.sourceforge.fraglets.zeig.eclipse.views;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * @author marion@users.souceforge.net
 * @version $Revision: 1.4 $
 */
class ZeigContentProvider implements IStructuredContentProvider, ITreeContentProvider {

    private ZeigRootNode invisibleRoot;

    private Viewer viewer;

    public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        this.viewer = v;
    }

    public void dispose() {
    }

    public Object[] getElements(Object parent) {
        if (parent.equals(ResourcesPlugin.getWorkspace())) {
            if (invisibleRoot == null) initialize();
            return getChildren(invisibleRoot);
        }
        return getChildren(parent);
    }

    public Object getParent(Object child) {
        if (child instanceof ZeigNode) {
            return ((ZeigNode) child).getParent();
        }
        return null;
    }

    public Object[] getChildren(Object parent) {
        if (parent instanceof ZeigContainerNode) {
            return ((ZeigContainerNode) parent).getChildren();
        }
        return new Object[0];
    }

    public boolean hasChildren(Object parent) {
        if (parent instanceof ZeigContainerNode) return ((ZeigContainerNode) parent).hasChildren();
        return false;
    }

    private void initialize() {
        invisibleRoot = new ZeigRootNode();
        invisibleRoot.setViewContentProvider(this);
    }

    /**
     * @param root
     */
    protected void addRoot(String url) {
        invisibleRoot.getProject(url);
    }

    /**
     * @return
     */
    public Viewer getViewer() {
        return viewer;
    }
}
