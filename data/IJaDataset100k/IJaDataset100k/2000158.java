package ac.hiu.j314.elmve.comp;

import ac.hiu.j314.elmve.*;
import ac.hiu.j314.elmve.ui.*;
import java.io.*;
import java.awt.*;

public class EContainer2DBG extends Elm2DBGBase {

    protected double width = 3.0;

    protected double height = 3.0;

    public void init(Serializable data) {
        Serializable d[] = (Serializable[]) data;
        width = ((Double) d[0]).doubleValue();
        height = ((Double) d[1]).doubleValue();
    }

    public void update(Serializable data) {
        Serializable d[] = (Serializable[]) data;
        width = ((Double) d[0]).doubleValue();
        height = ((Double) d[1]).doubleValue();
        repaint();
    }

    public void paintComponent(Graphics g) {
        Point p1 = placeToPoint(-width / 2.0, height / 2.0);
        Point p2 = placeToPoint(width / 2.0, -height / 2.0);
        g.drawRect(p1.x, p1.y, p2.x - p1.x, p2.y - p1.y);
    }

    public void setCenX(double x) {
        ;
    }

    public void setCenY(double y) {
        ;
    }

    public void setPPM(double ppm) {
        ;
    }

    public double getCenX() {
        return 0.0;
    }

    public double getCenY() {
        return 0.0;
    }

    public double getPPM() {
        return 0.0;
    }
}
