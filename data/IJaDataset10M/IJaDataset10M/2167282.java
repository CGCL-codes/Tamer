package org.plazmaforge.studio.appmanager.model;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.plazmaforge.studio.core.model.nodes.INode;

/** 
 * @author Oleh Hapon
 * $Id: ConfigurationTreeContentProvider.java,v 1.3 2010/04/28 06:37:26 ohapon Exp $
 */
public class ConfigurationTreeContentProvider implements ITreeContentProvider {

    public Object[] getElements(Object inputElement) {
        return getChildren(inputElement);
    }

    public Object getParent(Object element) {
        return getNode(element).getParent();
    }

    public Object[] getChildren(Object parentElement) {
        return getNode(parentElement).getChildNodes();
    }

    public boolean hasChildren(Object element) {
        return getNode(element).hasChildren();
    }

    protected INode getNode(Object element) {
        return (INode) element;
    }

    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    }

    public void dispose() {
    }
}
