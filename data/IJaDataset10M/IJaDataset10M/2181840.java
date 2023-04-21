package vectormap;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Point2D;

/**
 * A projection that projects latitude and longitude on a rectangular map. 
 * @author Aaron Hamid (aaron at users dot sf dot net)
 */
public final class RectangularProjection implements Projection {

    private double centerx;

    private double centery;

    private double pixPerHorDeg;

    private double pixPerVerDeg;

    public void setSize(Dimension d) {
        System.out.println("RectangularProjection setSize: " + d);
        centerx = d.width / 2d;
        centery = d.height / 2d;
        pixPerHorDeg = d.width / 360d;
        pixPerVerDeg = d.height / 180d;
    }

    public int projectLongitude(double longitude) {
        return (int) (centerx + (longitude * pixPerHorDeg));
    }

    public int projectLatitude(double latitude) {
        return (int) (centery - (latitude * pixPerVerDeg));
    }

    public void project(double longitude, double latitude, Point point) {
        point.x = (int) (centerx + (longitude * pixPerHorDeg));
        point.y = (int) (centery - (latitude * pixPerVerDeg));
    }

    public void unproject(int x, int y, Point2D.Double point) {
        point.x = (x - centerx) / pixPerHorDeg;
        point.y = (y + centery) / pixPerVerDeg;
    }
}
