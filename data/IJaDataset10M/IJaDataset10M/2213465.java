package net.sourceforge.gosp.hieorword.util;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class MarkingIterator<E> implements ListIterator<E> {

    protected List<E> list;

    protected ListIterator<E> iterator;

    public MarkingIterator(List<E> list) {
        this.list = list;
        this.iterator = list.listIterator();
    }

    public void setIndex(int i) {
        this.iterator = list.listIterator(i);
    }

    public int markNext() {
        return iterator.nextIndex();
    }

    public int markPrevious() {
        return iterator.previousIndex();
    }

    public void returnToMark(int mark) {
        iterator = list.listIterator(mark);
    }

    public void removeToMark(int mark) {
        int nextIndex = iterator.nextIndex();
        if (mark >= nextIndex) {
            int count = mark - nextIndex + 1;
            for (int i = 0; i < count; i++) list.remove(nextIndex);
            setIndex(nextIndex);
        } else {
            int count = iterator.previousIndex() - mark + 1;
            for (int i = 0; i < count; i++) list.remove(mark);
            setIndex(mark);
        }
    }

    public void replaceToMark(int mark, E item) {
        removeToMark(mark);
        add(item);
    }

    public void reset() {
        setIndex(0);
    }

    public void add(E e) {
        iterator.add(e);
    }

    public boolean hasNext() {
        return iterator.hasNext();
    }

    public boolean hasPrevious() {
        return iterator.hasPrevious();
    }

    public E next() {
        return iterator.next();
    }

    public int nextIndex() {
        return iterator.nextIndex();
    }

    public E previous() {
        return iterator.previous();
    }

    public int previousIndex() {
        return iterator.previousIndex();
    }

    public void remove() {
        iterator.remove();
    }

    public void set(E e) {
        iterator.set(e);
    }

    public static void main(String[] args) {
        LinkedList<Integer> list = new LinkedList<Integer>();
        for (int i = 0; i < 10; i++) list.add(i);
        MarkingIterator<Integer> iter = new MarkingIterator<Integer>(list);
        int start = iter.markNext();
        System.out.println(iter.next());
        System.out.println(iter.next());
        int end = iter.markPrevious();
        iter.returnToMark(start);
        System.out.println(list);
        iter.replaceToMark(end, 22);
        System.out.println(list);
        System.out.println(iter.next());
    }
}
