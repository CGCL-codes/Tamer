package br.biofoco.p2p.services;

public class ServiceResult<V> {

    private long id;

    private final V value;

    public ServiceResult(long taskID, V value) {
        this.id = taskID;
        this.value = value;
    }

    public long getID() {
        return id;
    }

    public V value() {
        return value;
    }

    @Override
    public String toString() {
        return "" + value;
    }

    public void setID(long id) {
        this.id = id;
    }
}
