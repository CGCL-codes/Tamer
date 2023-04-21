package nl.uva.saf.simulation;

public class Vector2d {

    public static Vector2d add(Vector2d vec1, Vector2d vec2) {
        Vector2d resultVector = new Vector2d(vec1);
        resultVector.add(vec2);
        return resultVector;
    }

    public static Vector2d multiply(Vector2d vec, double scalar) {
        Vector2d resultVector = new Vector2d(vec);
        resultVector.x *= scalar;
        resultVector.y *= scalar;
        return resultVector;
    }

    public static Vector2d substract(Vector2d vec1, Vector2d vec2) {
        Vector2d resultVector = new Vector2d(vec1);
        resultVector.substract(vec2);
        return resultVector;
    }

    public double x, y;

    public Vector2d() {
        x = 0;
        y = 0;
    }

    public Vector2d(double components) {
        x = components;
        y = components;
    }

    public Vector2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2d(Vector2d source) {
        x = source.x;
        y = source.y;
    }

    public void add(Vector2d vector) {
        x += vector.x;
        y += vector.y;
    }

    public boolean equals(Vector2d vec) {
        return vec.x == x && vec.y == y;
    }

    public double length() {
        return Math.abs(Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)));
    }

    public void normalize() {
        double length = this.length();
        if (length != 0) {
            x /= length;
            y /= length;
        }
    }

    public void substract(Vector2d vector) {
        x -= vector.x;
        y -= vector.y;
    }
}
