package eu.pisolutions.ocelot.document.page;

import java.util.Iterator;
import java.util.NoSuchElementException;
import eu.pisolutions.lang.Validations;

public final class FilteredPageTreeNodeIterator<E extends PageTreeNode> extends Object implements Iterator<E> {

    public static FilteredPageTreeNodeIterator<PageTree> createPageTreeIterator(Iterator<? extends PageTreeNode> iterator) {
        return new FilteredPageTreeNodeIterator<PageTree>(iterator, PageTree.class);
    }

    public static FilteredPageTreeNodeIterator<Page> createPageIterator(Iterator<? extends PageTreeNode> iterator) {
        return new FilteredPageTreeNodeIterator<Page>(iterator, Page.class);
    }

    private final Iterator<? extends PageTreeNode> iterator;

    private final Class<E> elementClass;

    private E nextElement;

    private FilteredPageTreeNodeIterator(Iterator<? extends PageTreeNode> iterator, Class<E> elementClass) {
        super();
        Validations.notNull(iterator, "iterator");
        assert elementClass != null;
        this.iterator = iterator;
        this.elementClass = elementClass;
        this.seekNextChild();
    }

    public Class<E> getElementClass() {
        return this.elementClass;
    }

    public boolean hasNext() {
        return this.nextElement != null;
    }

    public E next() {
        if (this.nextElement == null) {
            throw new NoSuchElementException();
        }
        final E nextChild = this.nextElement;
        this.seekNextChild();
        return nextChild;
    }

    public void remove() {
        this.iterator.remove();
    }

    private void seekNextChild() {
        this.nextElement = null;
        while (this.iterator.hasNext()) {
            final PageTreeNode child = this.iterator.next();
            if (this.elementClass.isAssignableFrom(child.getClass())) {
                this.nextElement = this.elementClass.cast(child);
                break;
            }
        }
    }
}
