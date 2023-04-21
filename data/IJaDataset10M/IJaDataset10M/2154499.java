package saf.evaluator;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Behaviour implements INodeVisitable, List<Rule> {

    @Override
    public void accept(INodeVisitor v) {
        v.visit(this);
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<Rule> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return null;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public boolean add(Rule e) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends Rule> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends Rule> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {
    }

    @Override
    public Rule get(int index) {
        return null;
    }

    @Override
    public Rule set(int index, Rule element) {
        return null;
    }

    @Override
    public void add(int index, Rule element) {
    }

    @Override
    public Rule remove(int index) {
        return null;
    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public ListIterator<Rule> listIterator() {
        return null;
    }

    @Override
    public ListIterator<Rule> listIterator(int index) {
        return null;
    }

    @Override
    public List<Rule> subList(int fromIndex, int toIndex) {
        return null;
    }
}
