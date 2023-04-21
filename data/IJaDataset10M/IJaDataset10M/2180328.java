package com.griddynamics.openspaces.convergence.computegrid.gridgain;

import com.griddynamics.openspaces.convergence.monitor.AffinityKey;
import org.gridgain.grid.GridJobAdapter;
import java.io.Serializable;

public abstract class DataAwareJobAdapter<K extends AffinityKey, G extends Serializable> extends GridJobAdapter<G> implements DataAwareJob<K> {

    private K key;

    protected DataAwareJobAdapter(K key) {
        this.key = key;
    }

    protected DataAwareJobAdapter(K key, G... args) {
        super(args);
        this.key = key;
    }

    public K getAffinityKey() {
        return key;
    }
}
