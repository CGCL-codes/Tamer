package net.sf.mmm.util.collection.api;

import java.util.Deque;

/**
 * This is the interface for a {@link CollectionFactory} that {@link #create()
 * creates} instances of {@link Deque}.
 * 
 * @see net.sf.mmm.util.collection.base.LinkedListDequeFactory#INSTANCE
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.1
 */
@SuppressWarnings("rawtypes")
public interface DequeFactory extends CollectionFactory<Deque> {

    /**
   * {@inheritDoc}
   */
    <E> Deque<E> create();

    /**
   * {@inheritDoc}
   */
    <E> Deque<E> create(int capacity);
}
