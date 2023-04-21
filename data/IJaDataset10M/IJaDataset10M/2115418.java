package org.liris.schemerger.core.event;

import org.liris.schemerger.core.pattern.URIDisplayer;

/**
 * An itemset.
 * 
 * @author Damien Cram
 * 
 * @param <T>
 *            the type of item contained in the itemset
 */
public interface IItemset<T extends Comparable<? super T>> extends Comparable<IItemset<T>>, Iterable<T>, URIDisplayer {

    public void addItem(T element);

    public IItemset<T> block();

    public boolean containsItem(T item);

    public boolean containsItemset(IItemset<T> itemset);

    public T first();

    public T get(int i);

    public T last();

    public IItemset<T> queue();

    public int size();
}
