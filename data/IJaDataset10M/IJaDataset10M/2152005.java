package com.hazelcast.core;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import com.hazelcast.nio.Address;

public interface HazelcastInstance {

    public String getName();

    public <E> IQueue<E> getQueue(String name);

    public <E> ITopic<E> getTopic(String name);

    public <E> ISet<E> getSet(String name);

    public <E> IList<E> getList(String name);

    public <K, V> IMap<K, V> getMap(String name);

    public <K, V> MultiMap<K, V> getMultiMap(String name);

    public ILock getLock(Object obj);

    public Cluster getCluster();

    public ExecutorService getExecutorService();

    public Transaction getTransaction();

    public IdGenerator getIdGenerator(String name);

    /**
     * Detaches this member from the cluster.
     * It doesn't shutdown the entire cluster, it shuts down
     * this local member only.
     */
    public void shutdown();

    /**
     * Detaches this member from the cluster first and then restarts it
     * as a new member.
     */
    public void restart();

    /**
     * Returns all queue, map, set, list, topic, lock, multimap
     * instances created by Hazelcast.
     *
     * @return the collection of instances created by Hazelcast.
     */
    public Collection<Instance> getInstances();

    public void addInstanceListener(InstanceListener instanceListener);

    public void removeInstanceListener(InstanceListener instanceListener);
}
