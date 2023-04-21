package com.tinywebgears.samples.railnetwork;

class Pair<F, S> {

    private final F fist;

    private final S second;

    public Pair(F first, S second) {
        this.fist = first;
        this.second = second;
    }

    public F getFist() {
        return fist;
    }

    public S getSecond() {
        return second;
    }
}
