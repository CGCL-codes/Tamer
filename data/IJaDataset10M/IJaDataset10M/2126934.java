package com.fluendo.player;

public interface StatusListener {

    public void newState(int newState);

    public void newSeek(double position);
}
