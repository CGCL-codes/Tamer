package tests.polygon;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import org.junit.Test;
import pl.edu.amu.wmi.kino.visualjavafx.javafxcodegenerators.api.JavaFXCodeGenerator;
import pl.edu.amu.wmi.kino.visualjavafx.model.generators.ModelPolygonGenerator;
import pl.edu.amu.wmi.kino.visualjavafx.model.objects.Application;
import pl.edu.amu.wmi.kino.visualjavafx.model.objects.Point;
import pl.edu.amu.wmi.kino.visualjavafx.model.objects.shapes.Polygon;
import tests.helpers.Test2Provider;

/**
 *PASSED
 * @author Admin
 */
public class PolygonTest2 {

    @Test
    public void main() throws IOException {
        Test2Provider m = new Test2Provider();
        Polygon c = this.generatePolygon();
        Application app = m.getModelForObject(c);
        String res = JavaFXCodeGenerator.generateCode(app);
        System.out.println(res);
    }

    private Polygon generatePolygon() {
        Polygon res = new Polygon();
        Point p1 = new Point();
        p1.setHPos(300);
        p1.setVPos(200);
        Point p2 = new Point();
        p2.setHPos(500);
        p2.setVPos(200);
        Point p3 = new Point();
        p3.setHPos(500);
        p3.setVPos(400);
        Point p4 = new Point();
        p4.setHPos(400);
        p4.setVPos(300);
        Point p5 = new Point();
        p5.setHPos(100);
        p5.setVPos(100);
        ArrayList<Point> points = new ArrayList<Point>();
        points.add(p1);
        points.add(p2);
        points.add(p3);
        points.add(p4);
        points.add(p5);
        res = ModelPolygonGenerator.generatePolygon(0, 0, 0, 1, 5, Color.GREEN, Color.BLUE, points);
        return res;
    }
}
