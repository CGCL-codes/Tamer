package org.ufacekit.ui.qt.jface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.ufacekit.ui.viewers.IContentProvider;
import org.ufacekit.ui.viewers.IElementComparer;
import org.ufacekit.ui.viewers.ISelection;
import org.ufacekit.ui.viewers.ISelectionChangedListener;
import org.ufacekit.ui.viewers.ISelectionProvider;
import org.ufacekit.ui.viewers.IStructuredViewer;
import org.ufacekit.ui.viewers.SelectionChangedEvent;
import org.ufacekit.ui.viewers.StructuredSelection;
import org.ufacekit.ui.viewers.ViewerComparator;
import org.ufacekit.ui.viewers.ViewerFilter;

public abstract class Viewer<ModelElement, Input> implements IStructuredViewer<ModelElement, Input>, ISelectionProvider<ModelElement> {

    private IContentProvider<ModelElement, Input> contentProvider;

    private Input input;

    @SuppressWarnings("unchecked")
    private IElementComparer elementComparer;

    private ViewerComparator<ModelElement> comparator;

    private Collection<ViewerFilter<ModelElement>> filters;

    private List<ISelectionChangedListener<ModelElement>> selectionChangeListener = new ArrayList<ISelectionChangedListener<ModelElement>>();

    public IContentProvider<ModelElement, Input> getContentProvider() {
        return contentProvider;
    }

    public Input getInput() {
        return input;
    }

    public void setContentProvider(IContentProvider<ModelElement, Input> contentProvider) {
        this.contentProvider = contentProvider;
    }

    public void setInput(Input input) {
        Input oldInput = getInput();
        this.input = input;
        getContentProvider().inputChanged(this, oldInput, input);
        doRefreshAll();
    }

    public ViewerComparator<ModelElement> getComparator() {
        return comparator;
    }

    @SuppressWarnings("unchecked")
    public <T> IElementComparer<T> getComparer() {
        return elementComparer;
    }

    public Collection<ViewerFilter<ModelElement>> getFilters() {
        return filters;
    }

    public void setComparator(ViewerComparator<ModelElement> comparator) {
        this.comparator = comparator;
    }

    public <T> void setComparer(IElementComparer<T> elementComparer) {
        this.elementComparer = elementComparer;
    }

    public void setFilters(Collection<ViewerFilter<ModelElement>> filters) {
        this.filters = filters;
    }

    public void addSelectionChangedListener(ISelectionChangedListener<ModelElement> listener) {
        selectionChangeListener.add(listener);
    }

    void fireSelectionChangeListener() {
        SelectionChangedEvent<ModelElement> evt = new SelectionChangedEvent<ModelElement>(this, getSelection());
        for (ISelectionChangedListener<ModelElement> l : selectionChangeListener) {
            l.selectionChanged(evt);
        }
    }

    public ISelection<ModelElement> getSelection() {
        return new StructuredSelection<ModelElement>(doGetSelectedElements(), getComparer());
    }

    public void removeSelectionChangedListener(ISelectionChangedListener<ModelElement> listener) {
        selectionChangeListener.remove(listener);
    }

    public void setSelection(ISelection<ModelElement> selection) {
        doSetSelectedElements(selection.getElements());
    }

    abstract void doRefreshAll();

    abstract List<ModelElement> doGetSelectedElements();

    abstract void doSetSelectedElements(List<ModelElement> elements);

    List<ModelElement> getFilteredAndSortedElements() {
        Collection<ModelElement> rv = this.contentProvider.getElements(input);
        if (filters != null && filters.size() > 0) {
            rv = filter(rv);
        }
        return new ArrayList<ModelElement>(getSortChildren(rv));
    }

    @SuppressWarnings("unchecked")
    private Collection<ModelElement> getSortChildren(Collection<ModelElement> elements) {
        if (comparator != null) {
            ModelElement[] els = (ModelElement[]) elements.toArray();
            comparator.sort(this, els);
            elements = Arrays.asList(els);
        }
        return elements;
    }

    private Collection<ModelElement> filter(Collection<ModelElement> elements) {
        if (filters != null) {
            ArrayList<ModelElement> filtered = new ArrayList<ModelElement>(elements.size());
            for (ModelElement m : elements) {
                boolean add = true;
                for (ViewerFilter<ModelElement> f : filters) {
                    add = f.select(this, input, m);
                    if (!add) {
                        break;
                    }
                }
                if (add) {
                    filtered.add((ModelElement) m);
                }
            }
            return filtered;
        }
        return elements;
    }
}
