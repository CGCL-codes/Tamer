package com.google.devtools.depan.eclipse.trees;

import com.google.devtools.depan.eclipse.trees.NodeTreeView.NodeWrapper;
import com.google.devtools.depan.eclipse.trees.NodeTreeView.NodeWrapperRoot;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.model.IWorkbenchAdapter;

/**
 * @author ycoppel@google.com (Yohann Coppel)
 *
 * @param <E> Type of data associated to each Node<Element>.
 */
public class NodeViewAdapterFactory<E> implements IAdapterFactory {

    NodeTreeViewAdapter<E> treeAdapter = new NodeTreeViewAdapter<E>();

    @SuppressWarnings("unchecked")
    private static NodeViewAdapterFactory instance = null;

    @SuppressWarnings("unchecked")
    public Object getAdapter(Object adaptableObject, Class adapterType) {
        if (adapterType != IWorkbenchAdapter.class) {
            return null;
        }
        if (adaptableObject instanceof NodeWrapper || adaptableObject instanceof NodeWrapperRoot) {
            return treeAdapter;
        }
        return null;
    }

    public Class<?>[] getAdapterList() {
        return new Class[] { IWorkbenchAdapter.class };
    }

    @SuppressWarnings("unchecked")
    protected static void register() {
        if (null == instance) {
            instance = new NodeViewAdapterFactory();
        }
        Platform.getAdapterManager().registerAdapters(instance, NodeWrapperRoot.class);
        Platform.getAdapterManager().registerAdapters(instance, NodeWrapper.class);
    }
}
