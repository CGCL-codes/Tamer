package br.usp.iterador;

import org.jmock.MockObjectTestCase;
import br.usp.iterador.gui.MockedIteratorFrame;
import br.usp.iterador.plugin.bacia.model.MyPolygon;

public abstract class AbstractTestCase extends MockObjectTestCase {

    protected MockedIteratorFrame frame;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        frame = new MockedIteratorFrame();
    }

    protected MyPolygon createPolygon(double a, double b, double c, double d, double e, double f, double g, double h) {
        MyPolygon polygon = new MyPolygon();
        polygon.addPoint(a, b);
        polygon.addPoint(c, d);
        polygon.addPoint(e, f);
        polygon.addPoint(g, h);
        return polygon;
    }
}
