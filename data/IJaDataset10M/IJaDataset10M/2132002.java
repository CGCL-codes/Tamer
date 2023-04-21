package org.skuebeck.ooc.examples.transactions;

import org.skuebeck.ooc.annotations.Concurrent;

@Concurrent
public interface RepositoryClient<K, V> extends RepositoryAccess<K, V> {

    void commit();

    void rollback();
}
