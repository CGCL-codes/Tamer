package physic;

import java.util.Vector;

public class Rays {

    protected static int MaxRayCount = 20;

    protected Vector<java.lang.Double> x;

    protected Vector<java.lang.Double> y;

    public int GetSize() {
        return x.size();
    }

    public double GetX(int index) {
        return this.x.get(index);
    }

    public double GetY(int index) {
        return this.y.get(index);
    }

    public Rays() {
        this.x = new Vector();
        this.y = new Vector();
    }

    public void AddX(double x) {
        this.x.add(x);
    }

    public void AddY(double y) {
        this.y.add(y);
    }

    public void AddRay(double x, double y) {
        this.x.add(x);
        this.y.add(y);
    }

    public void Clear() {
        this.x.clear();
        this.y.clear();
    }
}
