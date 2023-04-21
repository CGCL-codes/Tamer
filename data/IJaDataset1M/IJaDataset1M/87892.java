package brainbots.util;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

public class Rotation {

    public double sin, cos;

    public Rotation(double angle) {
        sin = Math.sin(angle);
        cos = Math.cos(angle);
    }

    public double calcAngle() {
        return Math.atan2(sin, cos);
    }

    public Rotation(Rotation r) {
        sin = r.sin;
        cos = r.cos;
    }

    public Rotation(double sin, double cos) {
        this.sin = sin;
        this.cos = cos;
        normalize();
    }

    public Rotation() {
        sin = 0;
        cos = 1;
    }

    private void add(double s, double c) {
        double s1 = sin * c + cos * s;
        double c1 = cos * c - sin * s;
        sin = s1;
        cos = c1;
    }

    public Rotation add(Rotation r) {
        add(r.sin, r.cos);
        return this;
    }

    public Rotation inv() {
        sin = -sin;
        return this;
    }

    public final double getRotatedX(double x, double y) {
        return x * cos - y * sin;
    }

    public final double getRotatedY(double x, double y) {
        return x * sin + y * cos;
    }

    public Rotation sub(Rotation r) {
        add(-r.sin, r.cos);
        return this;
    }

    public void normalize() {
        double n = 1 / Math.sqrt(sin * sin + cos * cos);
        sin *= n;
        cos *= n;
    }

    public Rotation add(double angle) {
        add(Math.sin(angle), Math.cos(angle));
        return this;
    }

    public Rotation sub(double angle) {
        add(-Math.sin(angle), Math.cos(angle));
        return this;
    }

    public Rotation copy() {
        return new Rotation(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Rotation) {
            Rotation r = (Rotation) obj;
            return r.sin == sin && r.cos == cos;
        } else return false;
    }

    @Override
    public String toString() {
        return String.format("rot(%d,%d)", cos, sin);
    }

    public static void main(String[] args) {
        Rotation r1 = new Rotation(0.5);
        System.out.println(r1.add(r1));
    }

    public void set(Rotation phi) {
        sin = phi.sin;
        cos = phi.cos;
    }

    public double getUnRotatedX(double x, double y) {
        return x * cos + y * sin;
    }

    public double getUnRotatedY(double x, double y) {
        return -x * sin + y * cos;
    }
}
