package org.netxilia.api.operation;

import org.netxilia.api.concurrent.IListenableFuture;

/**
 * This is the interface to be implemented by any operation.
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 * 
 * @param <V>
 */
public interface IOperation<V> {

    IListenableFuture<V> execute();
}
