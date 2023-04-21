package edu.gatech.lbs.core.vector;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import edu.gatech.lbs.core.logging.Stat;
import edu.gatech.lbs.core.world.roadnet.RoadMap;

public class CartesianVector implements IVector {

    public static final byte typeCode = 'f';

    private double x;

    private double y;

    public CartesianVector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public CartesianVector(DataInputStream in) throws IOException {
        x = in.readDouble();
        y = in.readDouble();
    }

    public void setDimension(int d, double v) {
        switch(d) {
            case 0:
                x = v;
                break;
            case 1:
                y = v;
                break;
            default:
                System.out.println("Invalid dimension number '" + d + "'.");
                System.exit(-1);
        }
    }

    public double getDimension(int d) {
        switch(d) {
            case 0:
                return x;
            case 1:
                return y;
            default:
                System.out.println("Invalid dimension number '" + d + "'.");
                System.exit(-1);
                return -1;
        }
    }

    public void setLength(double length) {
        double phi = Math.atan2(y, x);
        y = Math.sin(phi) * length;
        x = Math.cos(phi) * length;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getLongitude() {
        double lat = getLatitude();
        double a = 6378137;
        double b = 6356752.3;
        double piOver180 = 3.14159265 / 180;
        double aa = Math.pow(a * Math.cos(lat * piOver180), 2);
        double bb = Math.pow(b * Math.sin(lat * piOver180), 2);
        return x / (piOver180 * Math.cos(lat * piOver180) * Math.sqrt((a * a * aa + b * b * bb) / (aa + bb)));
    }

    public double getLatitude() {
        return y / 110574.2727;
    }

    public IVector times(double d) {
        x *= d;
        y *= d;
        return this;
    }

    public IVector add(IVector v) {
        CartesianVector vv = v.toCartesianVector();
        x += vv.getX();
        y += vv.getY();
        return this;
    }

    public IVector vectorTo(IVector v) {
        assert (v instanceof CartesianVector);
        CartesianVector vv = (CartesianVector) v;
        return new CartesianVector(vv.getX() - x, vv.getY() - y);
    }

    public String toString() {
        return "(" + Stat.round(x, 1) + "m, " + Stat.round(y, 1) + "m)";
    }

    public boolean equals(Object o) {
        if (o instanceof CartesianVector) {
            CartesianVector v = (CartesianVector) o;
            return (v.getX() == x && v.getY() == y);
        }
        return false;
    }

    public int hashCode() {
        Double xx = new Double(x);
        Double yy = new Double(y);
        return xx.hashCode() ^ yy.hashCode();
    }

    public double getLength() {
        return Math.sqrt(x * x + y * y);
    }

    public IVector clone() {
        return new CartesianVector(x, y);
    }

    public CartesianVector toCartesianVector() {
        return this;
    }

    public RoadnetVector toRoadnetVector() {
        return null;
    }

    public RoadnetVector toRoadnetVector(RoadMap roadmap) {
        return roadmap.getRoadnetLocation(this);
    }

    public void saveTo(DataOutputStream out) throws IOException {
        out.writeByte(typeCode);
        out.writeDouble(x);
        out.writeDouble(y);
    }

    public byte getTypeCode() {
        return typeCode;
    }
}
