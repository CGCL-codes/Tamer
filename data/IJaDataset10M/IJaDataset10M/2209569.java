package edu.iastate.cs.ja_panc.runtime;

public class Entry {

    public int hash;

    public Class c;

    public long f;

    public int eventOffset;

    public boolean thisInstance;

    public Entry next;

    protected Entry(int hash, Class c, long f, int eventOffset, boolean thisInstance, Entry next) {
        this.hash = hash;
        this.c = c;
        this.f = f;
        this.next = next;
        this.thisInstance = thisInstance;
        this.eventOffset = eventOffset;
    }
}
