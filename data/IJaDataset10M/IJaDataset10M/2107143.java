package com.volantis.mcs.layouts.common;

public class FragmentLinkOrder {

    public static final FragmentLinkOrder PEERS_FIRST = new FragmentLinkOrder("PeersFirst");

    public static final FragmentLinkOrder PARENT_FIRST = new FragmentLinkOrder("ParentFirst");

    private final String name;

    private FragmentLinkOrder(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
}
