package org.retro.neural;

class BPDelay {

    public double w;

    public BPLayer f;

    BPDelay(BPLayer from, double weight) {
        f = from;
        w = weight;
    }
}
