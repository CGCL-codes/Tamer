package net.sf.jtracer.core;

import net.sf.jtracer.util.Color;
import net.sf.jtracer.util.Vector3D;

public class Pixel {

    private Vector3D point;

    private Color color;

    public Pixel(Vector3D point, Color color) {
        this.point = point;
        this.color = color;
    }

    public Vector3D getPoint() {
        return point;
    }

    public void setPoint(Vector3D point) {
        this.point = point;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
