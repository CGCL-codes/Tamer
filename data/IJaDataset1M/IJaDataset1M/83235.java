package org.thechiselgroup.choosel.protovis.client;

public class Link {

    private int source;

    private int target;

    private double value;

    public Link(int source, int target, double value) {
        this.source = source;
        this.target = target;
        this.value = value;
    }

    public int getSource() {
        return source;
    }

    public int getTarget() {
        return target;
    }

    public double getValue() {
        return value;
    }
}
