package org.isi.monet.modelling.views.utils;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.isi.monet.modelling.views.model.TagManager;

public class TagContentProvider implements IStructuredContentProvider {

    @Override
    public Object[] getElements(Object inputElement) {
        if (TagManager.class.isInstance(inputElement)) {
            return ((TagManager) inputElement).getTags();
        }
        return null;
    }

    @Override
    public void dispose() {
    }

    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    }
}
