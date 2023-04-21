package basic;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.LinkedList;
import java.util.List;

public class Metaballs {

    private int width, height;

    private double xrange, yrange;

    private List<CPoint2D> controlPoints;

    private final double a = 5, b = 1;

    public Metaballs(int width, int height, double xrange, double yrange) {
        this.width = width;
        this.height = height;
        this.xrange = xrange;
        this.yrange = yrange;
        controlPoints = new LinkedList<CPoint2D>();
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setXRange(double xrange) {
        this.xrange = xrange;
    }

    public void setYRange(double yrange) {
        this.yrange = yrange;
    }

    public void addControlPoint(CPoint2D point) {
        controlPoints.add(point);
    }

    public void addControlPoints(List<CPoint2D> points) {
        controlPoints.addAll(points);
    }

    public void removeControlPoint(CPoint2D point) {
        controlPoints.remove(point);
    }

    public void clear() {
        controlPoints.clear();
    }

    public BufferedImage render() {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int[] buff = ((DataBufferInt) (image.getRaster().getDataBuffer())).getData();
        double x = -(xrange / 2);
        double y;
        double dx = xrange / width;
        double dy = yrange / height;
        double _dx, _dy;
        double val, r2;
        double max = 0;
        for (int xi = 0; xi < width; xi++) {
            y = -(yrange / 2);
            for (int yi = 0; yi < height; yi++) {
                val = 0;
                for (CPoint2D point : controlPoints) {
                    _dx = x - point.x;
                    _dy = y - point.y;
                    r2 = _dx * _dx + _dy * _dy;
                    if (r2 <= b * b) {
                        r2 = a * (1 - ((4.0 * r2 * r2 * r2) / (9.0 * b * b * b * b * b * b)) + ((17.0 * r2 * r2) / (9.0 * b * b * b * b)) - ((22.0 * r2) / (9.0 * b * b)));
                    } else {
                        r2 = 0;
                    }
                    val += r2;
                }
                if (val > xrange) val = xrange;
                val = (val / xrange) * 255;
                buff[xi + (yi * width)] = ((int) val << 16) | ((int) val << 8) | ((int) val);
                y += dy;
            }
            x += dx;
        }
        return image;
    }
}
