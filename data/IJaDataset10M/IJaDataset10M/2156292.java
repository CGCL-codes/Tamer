package br.usp.ime.origami.model;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import br.usp.ime.origami.primitives.AnglePrimitives;

public class SimplePolygon {

    private static final Logger logger = Logger.getLogger(SimplePolygon.class);

    private List<SimplePoint> points = new ArrayList<SimplePoint>();

    public void add(SimplePoint p) {
        this.points.add(p);
    }

    public List<SimplePoint> getPoints() {
        return points;
    }

    public List<MovingPoint> toMovingPoints() {
        List<MovingPoint> movingPoints = new ArrayList<MovingPoint>();
        List<SimplePoint> temp = new ArrayList<SimplePoint>(points);
        temp.add(0, points.get(points.size() - 1));
        temp.add(points.get(0));
        for (int i = 1; i < temp.size() - 1; i++) {
            SimplePoint p = temp.get(i);
            SimplePoint before = temp.get(i - 1);
            SimplePoint after = temp.get(i + 1);
            double angleBetween = AnglePrimitives.angleBetween(before, p, after);
            logger.info("anglulo " + Math.toDegrees(angleBetween));
            double direction = AnglePrimitives.baseAngle(p, after) - (angleBetween / 2);
            direction = direction < 0 ? direction + 2 * Math.PI : direction;
            logger.info("base " + Math.toDegrees(AnglePrimitives.baseAngle(p, after)));
            logger.info("bissetriz " + Math.toDegrees(direction));
            MovingPoint movingPoint = new MovingPoint(p, direction, 1 / Math.sin(angleBetween / 2));
            movingPoints.add(movingPoint);
        }
        return movingPoints;
    }

    @Override
    public String toString() {
        return points.toString();
    }
}
