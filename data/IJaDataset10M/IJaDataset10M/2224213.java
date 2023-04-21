package net.sf.molae.pipe.tree;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;
import net.sf.molae.pipe.basic.ObjectProxy;

/**
 * Provides an Iterator that runs depth first through a tree.
 * For each tree node a method that returns its children as a 
 * collection is required.
 * This can be done by <ol>
 * <li>Direct composition: Each member of a collection is again a
       collection.</li>
 * <li>the method {@link Composition#getComponents() getComponents()} in the
 *     interface {@link Composition}.</li>
 * <li>Defining a {@link CompositionView} that
 *     contains the method calls for each class, that return the children.</li>
 * </ol>
 * @version 2.0
 * @author Ralph Wagner
 */
public final class DepthFirstIterator<E> implements Iterator<E> {

    /** Contains the active iterators, one from each tree level */
    final Stack<Iterator<? extends E>> mStack = new Stack<Iterator<? extends E>>();

    /** The iterator from which the last element was taken */
    Iterator<? extends E> mIterator;

    /** The composition view that is used */
    final CompositionView<E> mCompositionView;

    private DepthFirstIterator() {
        mCompositionView = null;
    }

    /**
     * Constructs a <code>DepthFirstIterator</code>.
     * @param iterator an iterator defining the topmost level of the tree
     * @param pCompositionView underlying composition view,
     *  must not be <code>null</code>.
     */
    public DepthFirstIterator(Iterator<E> iterator, CompositionView<E> pCompositionView) {
        mStack.push(iterator);
        mCompositionView = pCompositionView;
        ObjectProxy.assertNotNull(mCompositionView);
    }

    /**
     * Constructs a <code>DepthFirstIterator</code>.
     * @param pObject The root of the tree to be traversed
     * @param pCompositionView underlying composition view,
     *  must not be <code>null</code>.
     */
    public DepthFirstIterator(E pObject, CompositionView<E> pCompositionView) {
        this(Collections.singleton(pObject).iterator(), pCompositionView);
    }

    /**
     * Constructs a <code>DepthFirstIterator</code>.
     * @param pObject The root of the tree to be traversed
     */
    public static DepthFirstIterator<Object> newInstance(Object pObject) {
        return new DepthFirstIterator<Object>(pObject, CompositionView.DEFAULT_COMPOSITION_VIEW);
    }

    public boolean hasNext() {
        while (!mStack.isEmpty() && !mStack.peek().hasNext()) {
            mStack.pop();
        }
        return !mStack.isEmpty();
    }

    /**
     * @throws NoSuchElementException iteration has no more elements.
     */
    public E next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        mIterator = mStack.peek();
        E mNext = mIterator.next();
        Iterable<? extends E> children = mCompositionView.getChildren(mNext);
        if (children != null) {
            mStack.push(children.iterator());
        }
        return mNext;
    }

    /**
     * @throws UnsupportedOperationException depending on the collections of
     *  the base tree
     * @throws IllegalStateException if the <tt>next</tt> method has not
     *   yet been called, or the <tt>remove</tt> method has already
     *   been called after the last call to the <tt>next</tt> method.
     */
    public void remove() {
        if (mIterator == null) {
            throw new IllegalStateException("Next must be called before remove!");
        }
        mIterator.remove();
    }
}
