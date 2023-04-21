package org.eclipse.babel.runtime.actions;

import org.eclipse.babel.runtime.external.FormattedTranslatableText;
import org.eclipse.babel.runtime.external.ITranslatableText;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

class TextInputContentProvider implements IStructuredContentProvider, ITreeContentProvider {

    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    }

    public void dispose() {
    }

    public Object[] getElements(Object parent) {
        return getChildren(parent);
    }

    public Object getParent(Object child) {
        return null;
    }

    public Object[] getChildren(Object parent) {
        if (parent instanceof ITranslatableText[]) {
            return (Object[]) parent;
        } else if (parent instanceof FormattedTranslatableText) {
            return ((FormattedTranslatableText) parent).getDependentTexts();
        }
        return new Object[0];
    }

    public boolean hasChildren(Object parent) {
        return (parent instanceof FormattedTranslatableText);
    }
}
