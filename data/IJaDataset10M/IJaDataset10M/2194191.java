package jopt.csp.test.constraint;

import jopt.csp.spi.SolverImpl;
import jopt.csp.spi.arcalgorithm.constraint.num.NumConstraint;
import jopt.csp.spi.arcalgorithm.variable.GenericIntExpr;
import jopt.csp.spi.arcalgorithm.variable.IntVariable;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.spi.util.GenericIndex;
import jopt.csp.variable.CspIntExpr;
import jopt.csp.variable.CspIntVariable;
import jopt.csp.variable.CspMath;
import jopt.csp.variable.CspVariableFactory;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;

/**
 * Test for SummationConstraint violation and propagation
 * 
 * @author jboerkoel
 * @author Chris Johnson
 */
public class SummationConstraintTest extends TestCase {

    IntVariable w1;

    IntVariable w2;

    IntVariable w3;

    IntVariable w4;

    IntVariable x1;

    IntVariable x2;

    IntVariable x3;

    IntVariable x11;

    IntVariable x12;

    IntVariable x13;

    IntVariable x21;

    IntVariable x22;

    IntVariable x23;

    IntVariable x31;

    IntVariable x32;

    IntVariable x33;

    IntVariable y1;

    IntVariable y2;

    IntVariable y3;

    IntVariable y11;

    IntVariable y12;

    IntVariable y13;

    IntVariable y21;

    IntVariable y22;

    IntVariable y23;

    IntVariable y31;

    IntVariable y32;

    IntVariable y33;

    IntVariable z1;

    IntVariable z2;

    IntVariable z3;

    GenericIntExpr whexpr;

    GenericIntExpr xiexpr;

    GenericIntExpr xijexpr;

    GenericIntExpr xjiexpr;

    GenericIntExpr yiexpr;

    GenericIntExpr yjexpr;

    GenericIntExpr yijexpr;

    GenericIntExpr yjkexpr;

    GenericIntExpr ziexpr;

    GenericIntExpr zjexpr;

    GenericIntExpr zkexpr;

    ConstraintStore store;

    CspVariableFactory varFactory;

    GenericIndex idxH;

    GenericIndex idxI;

    GenericIndex idxJ;

    GenericIndex idxK;

    IntVariable y;

    IntVariable z;

    NumConstraint constraint;

    CspMath math;

    public void setUp() {
        store = new ConstraintStore(SolverImpl.createDefaultAlgorithm());
        store.setAutoPropagate(false);
        varFactory = store.getConstraintAlg().getVarFactory();
        idxH = new GenericIndex("h", 4);
        idxI = new GenericIndex("i", 3);
        idxJ = new GenericIndex("j", 3);
        idxK = new GenericIndex("k", 3);
        w1 = new IntVariable("w1", 0, 100);
        w2 = new IntVariable("w2", 0, 100);
        w3 = new IntVariable("w3", 0, 100);
        w4 = new IntVariable("w4", 0, 100);
        x1 = new IntVariable("x1", 0, 100);
        x2 = new IntVariable("x2", 0, 100);
        x3 = new IntVariable("x3", 0, 100);
        x11 = new IntVariable("x11", 1, 10);
        x12 = new IntVariable("x12", 1, 10);
        x13 = new IntVariable("x13", 1, 10);
        x21 = new IntVariable("x21", 3, 20);
        x22 = new IntVariable("x22", 3, 20);
        x23 = new IntVariable("x23", 3, 20);
        x31 = new IntVariable("x31", 7, 30);
        x32 = new IntVariable("x32", 7, 30);
        x33 = new IntVariable("x33", 7, 30);
        y1 = new IntVariable("y1", 0, 100);
        y2 = new IntVariable("y2", 0, 100);
        y3 = new IntVariable("y3", 0, 100);
        y11 = new IntVariable("y11", 0, 100);
        y12 = new IntVariable("y12", 0, 100);
        y13 = new IntVariable("y13", 0, 100);
        y21 = new IntVariable("y21", 0, 100);
        y22 = new IntVariable("y22", 0, 100);
        y23 = new IntVariable("y23", 0, 100);
        y31 = new IntVariable("y31", 0, 100);
        y32 = new IntVariable("y32", 0, 100);
        y33 = new IntVariable("y33", 0, 100);
        z1 = new IntVariable("z1", 0, 100);
        z2 = new IntVariable("z2", 0, 100);
        z3 = new IntVariable("z3", 0, 100);
        y = new IntVariable("y", 0, 100);
        z = new IntVariable("z", 0, 200);
        whexpr = (GenericIntExpr) varFactory.genericInt("wh", idxH, new CspIntVariable[] { w1, w2, w3, w4 });
        xiexpr = (GenericIntExpr) varFactory.genericInt("xi", idxI, new CspIntVariable[] { x1, x2, x3 });
        xijexpr = (GenericIntExpr) varFactory.genericInt("xij", new GenericIndex[] { idxI, idxJ }, new CspIntVariable[] { x11, x12, x13, x21, x22, x23, x31, x32, x33 });
        xjiexpr = (GenericIntExpr) varFactory.genericInt("xji", new GenericIndex[] { idxJ, idxI }, new CspIntVariable[] { x11, x21, x31, x12, x22, x32, x13, x23, x33 });
        yiexpr = (GenericIntExpr) varFactory.genericInt("yi", idxI, new CspIntVariable[] { y1, y2, y3 });
        yjexpr = (GenericIntExpr) varFactory.genericInt("yj", idxJ, new CspIntVariable[] { y1, y2, y3 });
        yijexpr = (GenericIntExpr) varFactory.genericInt("yij", new GenericIndex[] { idxI, idxJ }, new CspIntVariable[] { y11, y12, y13, y21, y22, y23, y31, y32, y33 });
        yjkexpr = (GenericIntExpr) varFactory.genericInt("yjk", new GenericIndex[] { idxJ, idxK }, new CspIntVariable[] { y11, y12, y13, y21, y22, y23, y31, y32, y33 });
        ziexpr = (GenericIntExpr) varFactory.genericInt("zi", idxI, new CspIntVariable[] { z1, z2, z3 });
        zjexpr = (GenericIntExpr) varFactory.genericInt("zj", idxJ, new CspIntVariable[] { z1, z2, z3 });
        zkexpr = (GenericIntExpr) varFactory.genericInt("zk", idxK, new CspIntVariable[] { z1, z2, z3 });
        math = store.getConstraintAlg().getVarFactory().getMath();
    }

    public void tearDown() {
        w1 = null;
        w2 = null;
        w3 = null;
        w4 = null;
        x1 = null;
        x2 = null;
        x3 = null;
        x11 = null;
        x12 = null;
        x13 = null;
        x21 = null;
        x22 = null;
        x23 = null;
        x31 = null;
        x32 = null;
        x33 = null;
        y1 = null;
        y2 = null;
        y3 = null;
        y11 = null;
        y12 = null;
        y13 = null;
        y21 = null;
        y22 = null;
        y23 = null;
        y31 = null;
        y32 = null;
        y33 = null;
        z1 = null;
        z2 = null;
        z3 = null;
        whexpr = null;
        xiexpr = null;
        xijexpr = null;
        xjiexpr = null;
        yiexpr = null;
        yjexpr = null;
        yijexpr = null;
        yjkexpr = null;
        ziexpr = null;
        zjexpr = null;
        zkexpr = null;
        store = null;
        varFactory = null;
        idxH = null;
        idxI = null;
        idxJ = null;
        idxK = null;
        y = null;
        z = null;
        constraint = null;
        math = null;
    }

    public void testSummationLTNoViolate() {
        try {
            CspIntExpr iExpr = math.summation(xiexpr, new GenericIndex[] { idxI }, null);
            constraint = (NumConstraint) iExpr.lt(z);
            store.addConstraint(constraint);
            assertFalse("constraint is not violated yet", constraint.isViolated(false));
            x1.setDomainMin(new Integer(5));
            store.propagate();
            x2.setDomainMin(new Integer(2));
            x3.setDomainMin(new Integer(1));
            z.setDomainMax(new Integer(9));
            store.propagate();
        } catch (PropagationFailureException pfe) {
            fail();
        }
        assertFalse("constraint is not violated", constraint.isViolated(false));
    }
}
