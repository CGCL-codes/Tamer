package org.deved.antlride.ui.viewers;

import org.deved.antlride.core.model.ElementKind;
import org.deved.antlride.core.model.IGrammar;
import org.deved.antlride.core.model.IModelElement;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * TODO: fix this????
 * @author edgar
 *
 */
public class AntlrModelElementContentProvider implements IStructuredContentProvider {

    private static final Object[] NO_CHILDREN = new Object[0];

    public AntlrModelElementContentProvider() {
    }

    public Object[] getElements(Object element) {
        IModelElement mElement = (IModelElement) element;
        ElementKind elementKind = mElement.getElementKind();
        switch(elementKind) {
            case GRAMMAR:
                return mElement.getAdapter(IGrammar.class).getRules();
        }
        return NO_CHILDREN;
    }

    public void dispose() {
    }

    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    }
}
