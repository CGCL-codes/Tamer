package org.chessworks.common.javatools;

/**
 * Common interface for a Proxy that redirect requests to various underlying
 * implementations.
 * 
 * @author "Doug Bateman" ("DuckStorm")
 */
public interface RetargetableProxy<T> {

    /**
	 * Returns the underlying object that handles delegated requests.
	 * 
	 * @return the underlying object that handles delegated requests.
	 */
    public T getProxyTarget();

    /**
	 * Connects this proxy to the target. Future calls to this proxy will then
	 * be delegated to this target.
	 * 
	 * @param target The underlying map which will process delegated requests.
	 */
    public void setProxyTarget(T target);
}
