package com.ogprover.test.junit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.ogprover.main.OpenGeoProver;
import com.ogprover.polynomials.Power;
import com.ogprover.polynomials.UFraction;
import com.ogprover.polynomials.UPolynomial;
import com.ogprover.polynomials.UTerm;
import com.ogprover.polynomials.UXVariable;
import com.ogprover.polynomials.Variable;
import com.ogprover.polynomials.XPolynomial;
import com.ogprover.polynomials.XTerm;
import com.ogprover.prover_protocol.cp.OGPCP;
import com.ogprover.prover_protocol.cp.geoconstruction.FreePoint;
import com.ogprover.prover_protocol.cp.geoconstruction.MidPoint;
import com.ogprover.prover_protocol.cp.geoconstruction.Point;
import junit.framework.TestCase;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for unit testing of Point</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class JUTestPoint extends TestCase {

    private Point point;

    private OGPCP consProtocol = null;

    public JUTestPoint(String name) {
        super(name);
    }

    @Before
    public void setUp() throws Exception {
        consProtocol = new OGPCP();
        consProtocol.setNumZeroIndices(3);
        point = new FreePoint("A");
        consProtocol.addGeoConstruction(point);
        OpenGeoProver.settings.getLogger().info("Starting new point test");
    }

    @After
    public void tearDown() {
        point = null;
        consProtocol = null;
        OpenGeoProver.settings.getLogger().info("Finished point test");
    }

    @Test
    public void testPointInstantiation() {
        OpenGeoProver.settings.getLogger().info("Testing point instantiation");
        Point pointB = new FreePoint("B");
        consProtocol.addGeoConstruction(pointB);
        Point pointM = new MidPoint("M", point, pointB);
        consProtocol.addGeoConstruction(pointM);
        point.transformToAlgebraicForm();
        Variable u0 = new UXVariable(Variable.VAR_TYPE_UX_U, 0);
        assertEquals(u0, point.getX());
        assertEquals(u0, point.getY());
        pointB.transformToAlgebraicForm();
        Variable u1 = new UXVariable(Variable.VAR_TYPE_UX_U, 1);
        assertEquals(u0, pointB.getX());
        assertEquals(u1, pointB.getY());
        pointM.transformToAlgebraicForm();
        Variable x1 = new UXVariable(Variable.VAR_TYPE_UX_X, 1);
        assertEquals(u0, pointM.getX());
        assertEquals(x1, pointM.getY());
        XPolynomial xpoly = new XPolynomial();
        UPolynomial up = new UPolynomial();
        UTerm ut = new UTerm(2);
        up.addTerm(ut);
        UFraction uf = new UFraction(up);
        XTerm xt = new XTerm(uf);
        xt.addPower(new Power(x1, 1));
        xpoly.addTerm(xt);
        up = new UPolynomial();
        ut = new UTerm(-1);
        ut.addPower(new Power(u1, 1));
        up.addTerm(ut);
        uf = new UFraction(up);
        xt = new XTerm(uf);
        xpoly.addTerm(xt);
        xpoly.reduceByUTermDivision();
        assertEquals(xpoly, consProtocol.getAlgebraicGeoTheorem().getHypotheses().getXPoly(0));
    }
}
