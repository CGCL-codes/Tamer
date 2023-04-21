package pl.edu.amu.wmi.kino.visualjavafx.model.generators;

import pl.edu.amu.wmi.kino.visualjavafx.model.objects.Point;
import pl.edu.amu.wmi.kino.visualjavafx.model.objects.path.LineToPathElement;

/**
 *
 * @author Admin
 */
public class ModelLineToGenerator {

    public static LineToPathElement generateLineTo(Point p) {
        LineToPathElement res = new LineToPathElement();
        res.getPoints().add(p);
        return res;
    }
}
