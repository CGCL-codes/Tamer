package com.volantis.mcs.eclipse.builder.editors.themes;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import java.util.List;

/**
 * This class is the {@link org.eclipse.jface.viewers.ITreeContentProvider} for the style category tree.
 * The tree only contains StyleCategory objects, therefore, this content
 * providers {@link #getChildren} and {@link #getElements} methods will
 * return arrays of StyleCategory objects. StyleCategory objects contain
 * two different types of child element - child StyleCategory and child style
 * properties. This content provider should ignore the syle property elements
 * as only the tree will only be displaying StyleCategory nodes.
 */
public class StyleCategoriesContentProvider implements ITreeContentProvider {

    private static final StyleCategory[] EMPTY_STYLE_CATEGORY_ARRAY = new StyleCategory[0];

    public Object[] getChildren(Object element) {
        final List childList = ((StyleCategory) element).getSubCategories();
        return (childList == null ? EMPTY_STYLE_CATEGORY_ARRAY : childList.toArray());
    }

    public Object getParent(Object element) {
        return ((StyleCategory) element).getParent();
    }

    public boolean hasChildren(Object element) {
        return ((StyleCategory) element).hasSubCategories();
    }

    public Object[] getElements(Object element) {
        return (Object[]) element;
    }

    public void dispose() {
    }

    public void inputChanged(Viewer viewer, Object oldInpt, Object newInput) {
    }
}
