package org.openscience.jmol.render;

import org.openscience.jmol.DisplayControl;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import javax.vecmath.Point3d;

public class BoundingBox {

    DisplayControl control;

    Point3d pointOrigin;

    Point3d pointCorner;

    BboxShape[] bboxShapes;

    Point3d[] unitBboxPoints = { new Point3d(1, 1, 1), new Point3d(1, 1, -1), new Point3d(1, -1, 1), new Point3d(1, -1, -1), new Point3d(-1, 1, 1), new Point3d(-1, 1, -1), new Point3d(-1, -1, 1), new Point3d(-1, -1, -1) };

    public BoundingBox(DisplayControl control) {
        this.control = control;
        bboxShapes = new BboxShape[8];
        for (int i = 0; i < 8; ++i) bboxShapes[i] = new BboxShape(new Point3d(unitBboxPoints[i]), i);
    }

    public Shape[] getBboxShapes() {
        return bboxShapes;
    }

    public void recalc() {
        Point3d pointOrigin = control.getBoundingBoxCenter();
        Point3d pointCorner = control.getBoundingBoxCorner();
        for (int i = 0; i < 8; ++i) {
            Point3d pointBbox = bboxShapes[i].getPoint();
            pointBbox.set(unitBboxPoints[i]);
            pointBbox.x *= pointCorner.x;
            pointBbox.y *= pointCorner.y;
            pointBbox.z *= pointCorner.z;
            pointBbox.add(pointOrigin);
        }
    }

    class BboxShape extends Shape {

        Point3d point;

        int myIndex;

        BboxShape(Point3d point, int myIndex) {
            this.point = point;
            this.myIndex = myIndex;
        }

        Point3d getPoint() {
            return point;
        }

        public void transform(DisplayControl control) {
            Point3d screen = control.transformPoint(point);
            x = (int) screen.x;
            y = (int) screen.y;
            z = (int) screen.z;
        }

        public void render(Graphics g, DisplayControl control) {
            boolean colorSet = false;
            for (int i = 1; i <= 4; i <<= 1) {
                int indexOther = myIndex ^ i;
                BboxShape bboxOther = bboxShapes[indexOther];
                if (z > bboxOther.z || (z == bboxOther.z && myIndex > indexOther)) {
                    if (!colorSet) {
                        g.setColor(control.getColorAxes());
                        colorSet = true;
                    }
                    g.drawLine(x, y, bboxOther.x, bboxOther.y);
                }
            }
        }
    }
}
