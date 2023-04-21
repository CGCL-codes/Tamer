package net.sourceforge.hatbox.wk;

public class WKBEnvelope {

    private double minX = Double.POSITIVE_INFINITY;

    private double maxX = Double.NEGATIVE_INFINITY;

    private double minY = Double.POSITIVE_INFINITY;

    private double maxY = Double.NEGATIVE_INFINITY;

    public final void expandToFit(final double x, final double y) {
        if (x < minX) {
            minX = x;
        }
        if (x > maxX) {
            maxX = x;
        }
        if (y < minY) {
            minY = y;
        }
        if (y > maxY) {
            maxY = y;
        }
    }

    public double getMinX() {
        return minX;
    }

    public double getMaxX() {
        return maxX;
    }

    public double getMinY() {
        return minY;
    }

    public double getMaxY() {
        return maxY;
    }
}
