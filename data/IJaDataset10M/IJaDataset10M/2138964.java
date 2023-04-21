package ac.hiu.j314.elmve;

import java.io.*;
import javax.vecmath.*;

public class Place implements Serializable, Cloneable {

    double x;

    double y;

    double z;

    public Place() {
    }

    public Place(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Place(Place p) {
        this.x = p.x;
        this.y = p.y;
        this.z = p.z;
    }

    public synchronized void set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public synchronized void set(double p[]) {
        x = p[0];
        y = p[1];
        z = p[2];
    }

    public synchronized void set(Place p) {
        x = p.x;
        y = p.y;
        z = p.z;
    }

    public synchronized void get(double p[]) {
        p[0] = x;
        p[1] = y;
        p[2] = z;
    }

    public synchronized void add(Place p) {
        x += p.x;
        y += p.y;
        z += p.z;
    }

    public synchronized void add(Place p1, Place p2) {
        x = p1.x + p2.x;
        y = p1.y + p2.y;
        z = p1.z + p2.z;
    }

    public synchronized void sub(Place p) {
        x -= p.x;
        y -= p.y;
        z -= p.z;
    }

    public synchronized void sub(Place p1, Place p2) {
        x = p1.x - p2.x;
        y = p1.y - p2.y;
        z = p1.z - p2.z;
    }

    public synchronized double distanceSquared(Place p) {
        return (x - p.x) * (x - p.x) + (y - p.y) * (y - p.y) + (z - p.z) * (z - p.z);
    }

    public synchronized double distance(Place p) {
        return Math.sqrt(distanceSquared(p));
    }

    public synchronized void scale(double d) {
        x = d * x;
        y = d * y;
        z = d * z;
    }

    public synchronized void scale(double d, Place p) {
        x = d * p.x;
        y = d * p.y;
        z = d * p.z;
    }

    public double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public void normalize() {
        double d = Math.sqrt(x * x + y * y + z * z);
        if (d == 0.0) return;
        x /= d;
        y /= d;
        z /= d;
    }

    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }

    public Vector3d toVector3d() {
        return new Vector3d(x, y, z);
    }

    public Object clone() {
        return new Place(x, y, z);
    }
}
