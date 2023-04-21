package dynamic.shapes;

import math.geom2d.*;
import math.geom2d.conic.*;
import math.geom2d.point.PointArray2D;
import dynamic.*;

/**
 * @author dlegland
 */
public class ConicFocii2D extends DynamicShape2D {

    DynamicShape2D parent1;

    PointArray2D set = new PointArray2D();

    public ConicFocii2D(DynamicShape2D conic) {
        this.parent1 = conic;
        parents.add(conic);
        parents.trimToSize();
        update();
    }

    @Override
    public Shape2D getShape() {
        return set;
    }

    @Override
    public void update() {
        this.defined = false;
        if (!parent1.isDefined()) return;
        Shape2D shape = parent1.getShape();
        if (!(shape instanceof Conic2D)) return;
        Conic2D conic = (Conic2D) shape;
        set = new PointArray2D(2);
        switch(conic.getConicType()) {
            case CIRCLE:
                set.addPoint(((Circle2D) conic).getCenter());
                break;
            case ELLIPSE:
                set.addPoint(((Ellipse2D) conic).getFocus1());
                set.addPoint(((Ellipse2D) conic).getFocus2());
                break;
            case PARABOLA:
                set.addPoint(((Parabola2D) conic).getFocus());
                break;
            case HYPERBOLA:
                set.addPoint(((Hyperbola2D) conic).getFocus1());
                set.addPoint(((Hyperbola2D) conic).getFocus2());
                break;
            default:
                System.err.println("ConicFocii2D: unknown type of conic !");
        }
        this.defined = true;
    }
}
