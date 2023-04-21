package Utility;

public class TripplePoint {

    float x, y, z;

    public TripplePoint(float a, float b, float c) {
        x = a;
        y = b;
        z = c;
    }

    public TripplePoint(int a, int b, int c) {
        x = a;
        y = b;
        z = c;
    }

    public void setX(float newX) {
        x = newX;
    }

    public void setY(float newY) {
        y = newY;
    }

    public void setZ(float newZ) {
        z = newZ;
    }

    public void setAll(float newX, float newY, float newZ) {
        x = newX;
        y = newY;
        z = newZ;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }
}
