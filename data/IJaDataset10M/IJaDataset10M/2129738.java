package com.guanda.swidgex.component;

import java.awt.Point;

public class WaterWave {

    private int depth;

    private int volumn;

    private int wave;

    private int time;

    private double dim;

    private double reflection;

    private Point center;

    private double pointsize;

    public WaterWave(int depth, int volumn, int wave, int point_size, double dim, double ref) {
        this.depth = depth;
        this.volumn = volumn;
        this.wave = wave;
        this.reflection = ref;
        this.dim = dim;
        this.pointsize = point_size;
    }

    private double delta(double radium) {
        double omiga = 2 * Math.PI / wave;
        double r = Math.sqrt(dim * dim + omiga * omiga);
        double gama = Math.asin(dim / r);
        double c = r * volumn * Math.sin(omiga * (radium + time) + gama) * Math.exp(-dim * radium);
        double theta = Math.atan(c);
        double alpha = Math.asin(reflection * Math.sin(theta));
        double beta = theta - alpha;
        return (depth + volumn * Math.cos(omiga * radium)) * Math.tan(beta);
    }

    public void setCenter(Point p) {
        center = p;
    }

    public void setTime(int offset) {
        this.time = offset;
    }

    public int getTime() {
        return time;
    }

    public Point getPixel(Point p) {
        if (p.x == center.x && p.y == center.y) return p;
        double radium = p.distance(center);
        if (radium < pointsize) return p;
        double newr = radium - delta(radium - pointsize);
        double ratio = newr / radium;
        double newx = center.x + (p.x - center.x) * ratio;
        double newy = center.y + (p.y - center.y) * ratio;
        return new Point((int) (Math.round(newx)), (int) (Math.round(newy)));
    }
}
