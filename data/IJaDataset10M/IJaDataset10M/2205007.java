package glass.processable;

import java.awt.geom.Point2D;
import darwin.population.Node;
import darwin.problemObject.Processable;

public class Point extends Processable<Point2D.Double> {

    @Override
    public Point2D.Double getValue(Object parameters, Node[] childNodes) {
        Double x = (Double) (childNodes[0].getValue(parameters));
        Double y = (Double) (childNodes[1].getValue(parameters));
        return new Point2D.Double(x, y);
    }

    @Override
    public Processable<Point2D.Double> clone() {
        return new Point();
    }
}
