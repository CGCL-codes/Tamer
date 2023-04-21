package ch4;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import javax.swing.JComponent;

public class CircleIntersect {

    private Circle c1;

    private Circle c2;

    private CircleComponent cp;

    public CircleIntersect(double radius1, double radius2) {
        c1 = new Circle(new Point2D.Double(100, 200), radius1);
        c2 = new Circle(new Point2D.Double(200, 100), radius2);
        ArrayList<Circle> circles = new ArrayList<Circle>();
        circles.add(c1);
        circles.add(c2);
        if (c1.intersects(c2)) {
            cp = new CircleComponent(circles, java.awt.Color.green);
        } else {
            cp = new CircleComponent(circles, java.awt.Color.red);
        }
    }

    public JComponent getComponent() {
        return this.cp;
    }
}
