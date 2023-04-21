package org.activision.model;

public class Graphics {

    private short id, delay;

    public Graphics(short id, short delay) {
        this.id = id;
        this.delay = delay;
    }

    public short getId() {
        return id;
    }

    public short getDelay() {
        return delay;
    }
}
