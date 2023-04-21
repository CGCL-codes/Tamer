package net.sf.jdsc;

import static java.lang.String.format;

/**
 * @author <a href="mailto:twyss@users.sourceforge.net">twyss</a>
 * @version 1.0
 */
public class ListSet<E> extends AbstractSet<E> implements IList<E> {

    protected IList<E> list;

    public ListSet(IList<E> list) {
        super();
        if (list == null) list = new SingleLinkedList<E>();
        this.list = list;
    }

    public ListSet() {
        this(null);
    }

    @Override
    public ListSet<E> create() {
        return new ListSet<E>(list.create());
    }

    @Override
    public boolean add(E element) throws FullDataStructureException {
        boolean contains = list.contains(element);
        if (!contains) list.insert(element);
        return !contains;
    }

    @Override
    public Item<E> insert(E element) throws FullDataStructureException {
        return list.insert(element);
    }

    @Override
    public boolean add(Item<E> item) throws FullDataStructureException {
        return list.add(item);
    }

    @Override
    public Item<E> insertFirst(E element) throws FullDataStructureException {
        return list.insertFirst(element);
    }

    @Override
    public boolean addFirst(Item<E> item) throws PositionNotFoundException, FullDataStructureException {
        return list.addFirst(item);
    }

    @Override
    public Item<E> insertLast(E element) throws FullDataStructureException {
        return list.insertLast(element);
    }

    @Override
    public boolean addLast(Item<E> item) throws PositionNotFoundException, FullDataStructureException {
        return list.addLast(item);
    }

    @Override
    public Item<E> insertBefore(Item<E> next, E element) throws PositionNotFoundException, FullDataStructureException {
        return list.insertBefore(next, element);
    }

    @Override
    public boolean addBefore(Item<E> next, Item<E> item) throws PositionNotFoundException, PositionNotFoundException, FullDataStructureException {
        return list.addBefore(next, item);
    }

    @Override
    public Item<E> insertAfter(Item<E> previous, E element) throws PositionNotFoundException, FullDataStructureException {
        return list.insertAfter(previous, element);
    }

    @Override
    public boolean addAfter(Item<E> previous, Item<E> item) throws PositionNotFoundException, PositionNotFoundException, FullDataStructureException {
        return list.addAfter(previous, item);
    }

    @Override
    public E get(E element) throws PositionNotFoundException {
        return getExistingItem(element).getElement();
    }

    private Item<E> getExistingItem(E element) throws PositionNotFoundException {
        Item<E> item = list.positionOf(element);
        if (item == null) throw new PositionNotFoundException(format("No element '%s' exists in this set.", element));
        return item;
    }

    @Override
    public Item<E> first() {
        return list.first();
    }

    @Override
    public Item<E> last() {
        return list.last();
    }

    @Override
    public Item<E> positionOf(E element) {
        return list.positionOf(element);
    }

    @Override
    public IEnumerator<E> elements() {
        return list.elements();
    }

    @Override
    public IEnumerator<? extends Item<E>> items() {
        return list.items();
    }

    @Override
    public E replace(E element) throws PositionNotFoundException {
        Item<E> item = getExistingItem(element);
        E oldElement = item.getElement();
        item.setElement(element);
        return oldElement;
    }

    @Override
    public E replace(Item<E> item, E element) throws PositionNotFoundException {
        return list.replace(item, element);
    }

    @Override
    public boolean contains(E element) {
        return list.contains(element);
    }

    @Override
    public boolean containsItem(Item<E> item) {
        return list.containsItem(item);
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean remove(E element) {
        return list.delete(element) != null;
    }

    @Override
    public Item<E> delete(E element) {
        return list.delete(element);
    }

    @Override
    public boolean remove(Item<E> item) throws PositionNotFoundException {
        return list.remove(item);
    }

    @Override
    public int clear() {
        return list.clear();
    }

    @Override
    public void merge(IList<E> list) {
        if (list != null) {
            for (E element : list) insert(element);
        }
    }

    @Override
    public IList<E> sublist(Item<E> first, Item<E> last) {
        return list.sublist(first, last);
    }

    @Override
    public ListSet<E> clone() {
        return clone(false);
    }

    @Override
    public ListSet<E> clone(boolean deepclone) {
        ListSet<E> clone = (ListSet<E>) super.clone(deepclone);
        clone.list = this.list.clone(deepclone);
        return clone;
    }
}
