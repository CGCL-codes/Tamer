package com.delambo.drool.interfaces;

public interface PoolNode<R extends Resource, T> {

    interface Factory<R> {

        PoolNode create(R r);
    }

    /**
     * Gets the pooling resource from this node.
     * @return nodes resource.
     */
    public T getResource();

    /**
     * Validates the nodes resource.
     */
    public boolean resourceValidates();

    /**
     * Kills this nodes resource.
     */
    public void killResource();

    /**
     * Expires the lease for this node.
     */
    public void expireLease();

    /**
     * Gets the amount of time this node has been leased.
     * @return lease time.
     */
    public long getLeaseTime();

    /**
     * Gets the amount of time since the last use of this node.
     * @return time last used.
     */
    public long getLastUse();

    /**
     * Leases this node.
     */
    public void lease();

    /**
     * Returns the result for wether or not this node is leased.
     * @return true if node is leased; otherwise, false.
     */
    public boolean inUse();

    /**
     * Cleans the leasing status of this node, resetting it to unleased.
     */
    public void cleanUp();

    /**
     * Initializes this node, setting it to unleased.
     */
    public void init();
}
