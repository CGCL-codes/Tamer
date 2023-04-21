package edu.ucla.cs.typecast.rmi;

/**
 *
 * @date Sep 20, 2007
 */
public interface FutureListener<V> {

    public void newValueArrived(Future<V> future, V value);

    public void freezed(Future<V> future);
}
