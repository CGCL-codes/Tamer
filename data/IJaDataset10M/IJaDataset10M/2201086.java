package com.ochafik.util.listenable;

import java.util.Set;

/**
 * Default implementation of the ListenableSet interface.<br/>
 * This class follows both the decorator and proxy patterns : it wraps an existing java.util.Set and adds the listenable feature to it.<br/>
 * @author Olivier Chafik
 * @param <T> Type of the elements of the set
 */
class DefaultListenableSet<T> extends DefaultListenableCollection<T> implements ListenableSet<T> {

    public DefaultListenableSet(Set<T> set, ListenableSupport<T> collectionSupport) {
        super(set, collectionSupport);
    }

    public DefaultListenableSet(Set<T> set) {
        super(set);
    }
}
