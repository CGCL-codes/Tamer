package de.bitsetter.roomak.data;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Vector;
import de.bitsetter.roomak.RooMak;
import de.bitsetter.roomak.gui.Viewport;

public class RasterEditHelper extends EditHelper {

    int raster;

    private RasterEditHelper(int r) {
        raster = r;
    }

    public static RasterEditHelper createRasterEditHelper(int raster) {
        return new RasterEditHelper(raster);
    }

    @Override
    public Vector<Point> getIntersections(EditHelper e) {
        return new Vector<Point>();
    }

    @Override
    public Point help(Point h) {
        double X = h.getX();
        double Y = h.getY();
        int x = ((int) (X + (X < 0 ? -0.5 : 0.5) * raster)) / raster;
        int y = ((int) (Y + (Y < 0 ? -0.5 : 0.5) * raster)) / raster;
        return Point.virtualPoint(x * raster, y * raster);
    }

    @Override
    public boolean hitTest(Point p) {
        return false;
    }

    public void paint(Graphics2D g2d, double scale, Point offset) {
        Viewport v = RooMak.getInstance().getMainWindow().view;
        Point a = help(v.getRealPos(0, 0));
        Point b = help(v.getRealPos(v.getWidth(), v.getHeight()));
        g2d.setColor(Color.MAGENTA);
        for (int ix = (int) a.getX(); ix <= (int) b.getX(); ix += raster) {
            for (int iy = (int) b.getY(); iy <= (int) a.getY(); iy += raster) {
                int x = (int) ((ix * scale) + offset.getX());
                int y = (int) ((iy * scale) + offset.getY());
                g2d.drawLine(x - 1, -y, x + 1, -y);
                g2d.drawLine(x, -y + 1, x, -y - 1);
            }
        }
    }

    public void paintHighlighted(Graphics2D g2d, double scale, Point offset) {
        this.paint(g2d, scale, offset);
    }

    public void paintSelected(Graphics2D g2d, double scale, Point offset) {
        this.paint(g2d, scale, offset);
    }
}
