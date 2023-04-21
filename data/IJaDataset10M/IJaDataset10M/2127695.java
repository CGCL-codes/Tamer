package uk.ac.lkl.common.util.collections.event;

import uk.ac.lkl.common.util.collections.AbstractNotifyingList;

public interface ListListener<T> extends CollectionListener<AbstractNotifyingList<T>, T, ListEvent<T>> {

    public void elementMoved(ListEvent<T> e);
}
