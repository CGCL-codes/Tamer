package net.sf.javagimmicks.collections.event;

public interface EventSortedMapListener<K, V> {

    public void eventOccured(SortedMapEvent<K, V> event);
}
