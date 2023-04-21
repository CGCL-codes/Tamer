package project2.operations;

import project2.operations.util.*;
import project2.scene.SuperSceneGraph;
import project2.scene.geom.*;
import project2.scene.util.*;

public class Operation {

    protected Point2D popLowestPoint(Point2DCollection points) {
        Point2D point = getLowestPoint(points);
        points.remove(point);
        return point;
    }

    protected Point2D getLowestPoint(Point2DCollection points) {
        Point2D temp, point = points.get(0);
        for (int i = 1; i < points.size(); i++) {
            temp = (Point2D) points.get(i);
            if (point.getY() > temp.getY()) point = temp; else if (point.getY() == temp.getY()) if (point.getX() > temp.getX()) point = temp;
        }
        return point;
    }

    protected ScanLine[] createArrayOfScanLines(Point2D p, Point2DCollection points) {
        ScanLine lines[] = new ScanLine[points.size()];
        for (int i = 0; i < points.size(); i++) lines[i] = new ScanLine(p, points.get(i));
        return lines;
    }

    protected PointX[] createArrayOfPointX(Point2DCollection points) {
        PointX px[] = new PointX[points.size()];
        for (int i = 0; i < points.size(); i++) px[i] = new PointX(points.get(i));
        return px;
    }

    protected PointY[] createArrayOfPointY(Point2DCollection points) {
        PointY py[] = new PointY[points.size()];
        for (int i = 0; i < points.size(); i++) py[i] = new PointY(points.get(i));
        return py;
    }
}
