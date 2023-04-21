package net.sourceforge.freejava.collection.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;
import net.sourceforge.freejava.collection.list.LinkedStack;
import net.sourceforge.freejava.collection.list.Stack;

/**
 * @see NestedIterator
 */
public class StackedIterator<T> implements Iterator<T> {

    private Stack<Iterator<T>> stack;

    private Iterator<T> currentIterator;

    private Iterator<T> lastIterator;

    public StackedIterator() {
        stack = new LinkedStack<Iterator<T>>();
    }

    public StackedIterator(Iterable<Iterator<T>> iterators) {
        this();
        for (Iterator<T> iter : iterators) push(iter);
    }

    public StackedIterator(Iterator<T> start) {
        this();
        push(start);
    }

    public int size() {
        return stack.size();
    }

    public void push(Iterator<T> iterator) {
        if (currentIterator != null) stack.push(currentIterator);
        currentIterator = iterator;
        join();
    }

    void join() {
        if (currentIterator.hasNext()) {
        } else {
            while (!stack.isEmpty()) {
                Iterator<T> top_ = stack.top();
                if (top_.hasNext()) {
                    currentIterator = stack.pop();
                    return;
                } else stack.pop();
            }
            currentIterator = null;
        }
    }

    @Override
    public boolean hasNext() {
        if (currentIterator == null) return false;
        return true;
    }

    static boolean looseMode = true;

    @Override
    public T next() {
        if (currentIterator == null) throw new NoSuchElementException();
        lastIterator = currentIterator;
        T x = currentIterator.next();
        join();
        return x;
    }

    @Override
    public void remove() {
        if (lastIterator == null) throw new IllegalStateException();
        lastIterator.remove();
    }

    @Override
    public String toString() {
        if (currentIterator == null) return "empty stack";
        StringBuffer buf = new StringBuffer((1 + stack.size()) * 100);
        buf.append("Iterators in stack: \n");
        buf.append("  * " + currentIterator.getClass().getSimpleName() + ": " + currentIterator + "\n");
        for (Iterator<T> iter : stack) {
            buf.append("    ");
            buf.append(iter.getClass().getSimpleName());
            buf.append(": ");
            buf.append(iter);
            buf.append('\n');
        }
        return buf.toString();
    }
}
