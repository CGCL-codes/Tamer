package net.wrburns.joctree;

import net.wrburns.joctree.Vector3;
import net.wrburns.joctree.Plane;
import net.wrburns.joctree.Sphere;

public class Frustum {

    public Plane a, b, c, d;

    public Frustum() {
    }

    public void set(Vector3 center, Vector3 direction, double fovy, double fovx) {
        Vector3 a1, a2, b1, b2, c1, c2, d1, d2;
    }
}
