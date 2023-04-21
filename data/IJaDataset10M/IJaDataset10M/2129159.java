package org.truxingame;

public class Vector2 {

    float X;

    float Y;

    public Vector2() {
        X = 0;
        Y = 0;
    }

    public Vector2(float l, float r) {
        X = l;
        Y = r;
    }

    public float getX() {
        return X;
    }

    public float getY() {
        return Y;
    }

    public void setX(float value) {
        X = value;
    }

    public void setY(float value) {
        Y = value;
    }
}
