package org.mxeclipse.object.tree;

import java.util.ArrayList;
import java.util.List;
import matrix.util.MatrixException;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.mxeclipse.exception.MxEclipseException;
import org.mxeclipse.model.MxFilter;
import org.mxeclipse.model.MxTreeDomainObject;

/**
 * <p>Title: MxTreeContentProvider</p>
 * <p>Description: </p>
 * <p>Company: ABB Switzerland</p>
 * @author Tihomir Ilic
 * @version 1.0
 *
 */
public class MxTreeContentProvider implements ITreeContentProvider {

    private MxFilter filter;

    public Object[] getChildren(Object parentElement) {
        if (parentElement != null && parentElement instanceof MxTreeDomainObject) {
            MxTreeDomainObject parentDomain = (MxTreeDomainObject) parentElement;
            try {
                return parentDomain.getChildren(false);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public Object getParent(Object element) {
        MxTreeDomainObject treeDomainObject = (MxTreeDomainObject) element;
        return treeDomainObject.getParent();
    }

    public boolean hasChildren(Object element) {
        MxTreeDomainObject treeDomainObject = (MxTreeDomainObject) element;
        return treeDomainObject.hasChildren();
    }

    public Object[] getElements(Object inputElement) {
        return getChildren(inputElement);
    }

    public void dispose() {
    }

    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    }

    public void setFilter(MxFilter filter) {
        this.filter = filter;
    }

    public MxFilter getFilter() {
        return this.filter;
    }
}
