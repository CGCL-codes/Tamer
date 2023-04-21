package org.jlib.core.collections.list;

/**
 * Iterator over an EditableIndexList using a random access data store.
 *
 * @param <Element>
 *        type of elements held in the List
 * @author Igor Akkerman
 */
public class DefaultEditableIndexListIterator<Element> extends DefaultIndexListIterator<Element> implements EditableIndexListIterator<Element> {

    /** EditableIndexList traversed by this Iterator */
    private EditableIndexList<Element> list;

    /**
     * Creates a new DefaultEditableIndexListIterator over the Elements of the specified
     * EditableIndexList.
     *
     * @param list
     *        EditableIndexList to traverse
     */
    protected DefaultEditableIndexListIterator(EditableIndexList<Element> list) {
        this(list, list.minIndex());
    }

    /**
     * Creates a new DefaultEditableIndexListIterator over the Elements of the specified IndexList
     * starting the traversal at the specified index.
     *
     * @param list
     *        EditableIndexList to traverse
     * @param startIndex
     *        integer specifying the start index of the traversal
     * @throws IndexOutOfBoundsException
     *         if {@code startIndex < matrix.minIndex() || matrix.maxIndex > startIndex}
     */
    protected DefaultEditableIndexListIterator(EditableIndexList<Element> list, int startIndex) throws IndexOutOfBoundsException {
        super(list, startIndex);
        this.list = list;
    }

    @Override
    public void set(Element element) {
        list.set(lastRetreivedElementIndex(), element);
    }
}
