package com.xith3d.sound;

public class SoundState {

    public static final SoundState INITIAL = new SoundState("INITIAL");

    public static final SoundState PLAYING = new SoundState("PLAYING");

    public static final SoundState PAUSED = new SoundState("PAUSED");

    public static final SoundState STOPPED = new SoundState("STOPPED");

    private final String myName;

    private SoundState(String name) {
        myName = name;
    }

    public String toString() {
        return myName;
    }
}
