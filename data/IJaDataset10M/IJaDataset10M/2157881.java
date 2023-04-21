package com.hvilela.common;

public class Position extends Point {

    private static final long serialVersionUID = -6099991550262030397L;

    protected float direction;

    public Position() {
        super();
    }

    public Position(float x, float y, float z, float rotation) {
        super(x, y, z);
        this.direction = rotation;
    }

    public float getDirection() {
        return direction;
    }

    public void setDirection(float rotation) {
        this.direction = rotation;
    }
}
