package jopt.csp.test.choicepoint;

import jopt.csp.CspSolver;
import jopt.csp.spi.AC5;
import jopt.csp.spi.arcalgorithm.variable.GenericIntConstant;
import jopt.csp.spi.arcalgorithm.variable.GenericIntExpr;
import jopt.csp.spi.arcalgorithm.variable.IntVariable;
import jopt.csp.spi.solver.ChoicePointStack;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.spi.util.GenericIndex;
import jopt.csp.util.IntSparseSet;
import jopt.csp.variable.CspConstraint;
import jopt.csp.variable.CspGenericIndex;
import jopt.csp.variable.CspIntExpr;
import jopt.csp.variable.CspIntVariable;
import jopt.csp.variable.CspMath;
import jopt.csp.variable.CspVariableFactory;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;

/**
 * Test pushing and popping along with constraints (and arcs, nodes, expressions, etc.)
 * 
 * @author Chris Johnson
 */
public class ChoicePointWithConstraintsTest extends TestCase {

    IntVariable x1;

    IntVariable x2;

    IntVariable x3;

    IntVariable x11;

    IntVariable w11;

    IntVariable x12;

    IntVariable x13;

    IntVariable x21;

    IntVariable x22;

    IntVariable x23;

    IntVariable x31;

    IntVariable x32;

    IntVariable x33;

    IntVariable x111;

    IntVariable x121;

    IntVariable x131;

    IntVariable x211;

    IntVariable x221;

    IntVariable x231;

    IntVariable x311;

    IntVariable x321;

    IntVariable x331;

    IntVariable x112;

    IntVariable x122;

    IntVariable x132;

    IntVariable x212;

    IntVariable x222;

    IntVariable x232;

    IntVariable x312;

    IntVariable x322;

    IntVariable x332;

    IntVariable x113;

    IntVariable x123;

    IntVariable x133;

    IntVariable x213;

    IntVariable x223;

    IntVariable x233;

    IntVariable x313;

    IntVariable x323;

    IntVariable x333;

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

    IntVariable v11;

    IntVariable v12;

    IntVariable v13;

    IntVariable v21;

    IntVariable v22;

    IntVariable v23;

    IntVariable v31;

    IntVariable v32;

    IntVariable v33;

    IntVariable u1;

    IntVariable u2;

    IntVariable u3;

    IntVariable z1;

    IntVariable z2;

    IntVariable z3;

    IntVariable p1;

    IntVariable p2;

    IntVariable p3;

    IntVariable z11;

    IntVariable z12;

    IntVariable z13;

    IntVariable z21;

    IntVariable z22;

    IntVariable z23;

    IntVariable z31;

    IntVariable z32;

    IntVariable z33;

    GenericIntExpr xiexpr;

    GenericIntExpr xijexpr;

    GenericIntExpr xjiexpr;

    GenericIntExpr xjkexpr;

    GenericIntExpr xjikexpr;

    GenericIntExpr xijkexpr;

    GenericIntExpr yiexpr;

    GenericIntExpr yjexpr;

    GenericIntExpr ykexpr;

    GenericIntExpr yijexpr;

    GenericIntExpr yjkexpr;

    GenericIntExpr vikexpr;

    GenericIntExpr vjkexpr;

    GenericIntExpr uiexpr;

    GenericIntExpr ziexpr;

    GenericIntExpr zjexpr;

    GenericIntExpr pjexpr;

    GenericIntExpr zkexpr;

    GenericIntExpr zijexpr;

    GenericIntExpr zikexpr;

    GenericIntConstant ukconst;

    GenericIntConstant uiconst;

    GenericIntConstant uijconst;

    GenericIntConstant xiconst;

    GenericIntConstant vkconst;

    GenericIntConstant wkconst;

    GenericIntConstant wijconst;

    GenericIntConstant wjiconst;

    GenericIndex idxI;

    GenericIndex idxJ;

    GenericIndex idxK;

    IntVariable x;

    IntVariable y;

    IntVariable z;

    CspSolver solver;

    CspMath varMath;

    CspVariableFactory varFactory;

    public void setUp() {
        solver = CspSolver.createSolver();
        solver.setAutoPropagate(false);
        varMath = solver.getVarFactory().getMath();
        varFactory = solver.getVarFactory();
        idxI = new GenericIndex("i", 3);
        idxJ = new GenericIndex("j", 3);
        idxK = new GenericIndex("k", 3);
        x1 = new IntVariable("x1", 0, 100);
        x2 = new IntVariable("x2", 0, 100);
        x3 = new IntVariable("x3", 0, 100);
        x11 = new IntVariable("x11", 0, 100);
        w11 = new IntVariable("x11", 0, 100);
        x12 = new IntVariable("x12", 0, 100);
        x13 = new IntVariable("x13", 0, 100);
        x21 = new IntVariable("x21", 0, 100);
        x22 = new IntVariable("x22", 0, 100);
        x23 = new IntVariable("x23", 0, 100);
        x31 = new IntVariable("x31", 0, 100);
        x32 = new IntVariable("x32", 0, 100);
        x33 = new IntVariable("x33", 0, 100);
        x111 = new IntVariable("x111", 0, 1);
        x121 = new IntVariable("x121", 0, 1);
        x131 = new IntVariable("x131", 0, 1);
        x211 = new IntVariable("x211", 0, 1);
        x221 = new IntVariable("x222", 0, 1);
        x231 = new IntVariable("x231", 0, 1);
        x311 = new IntVariable("x311", 0, 1);
        x321 = new IntVariable("x321", 0, 1);
        x331 = new IntVariable("x331", 0, 1);
        x112 = new IntVariable("x112", 0, 1);
        x122 = new IntVariable("x122", 0, 1);
        x132 = new IntVariable("x132", 0, 1);
        x212 = new IntVariable("x212", 0, 1);
        x222 = new IntVariable("x222", 0, 1);
        x232 = new IntVariable("x232", 0, 1);
        x312 = new IntVariable("x312", 0, 1);
        x322 = new IntVariable("x322", 0, 1);
        x332 = new IntVariable("x332", 0, 1);
        x113 = new IntVariable("x113", 0, 1);
        x123 = new IntVariable("x123", 0, 1);
        x133 = new IntVariable("x133", 0, 1);
        x213 = new IntVariable("x213", 0, 1);
        x223 = new IntVariable("x223", 0, 1);
        x233 = new IntVariable("x233", 0, 1);
        x313 = new IntVariable("x313", 0, 1);
        x323 = new IntVariable("x323", 0, 1);
        x333 = new IntVariable("x333", 0, 1);
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
        IntSparseSet v1SparseVals = new IntSparseSet();
        v1SparseVals.add(0);
        v1SparseVals.add(2);
        v1SparseVals.add(4);
        v1SparseVals.add(6);
        v1SparseVals.add(8);
        IntSparseSet v2SparseVals = new IntSparseSet();
        v2SparseVals.add(20);
        v2SparseVals.add(22);
        v2SparseVals.add(24);
        v2SparseVals.add(26);
        v2SparseVals.add(28);
        IntSparseSet v3SparseVals = new IntSparseSet();
        v3SparseVals.add(40);
        v3SparseVals.add(42);
        v3SparseVals.add(44);
        v3SparseVals.add(46);
        v3SparseVals.add(48);
        v11 = new IntVariable("v11", v1SparseVals);
        v12 = new IntVariable("v12", v1SparseVals);
        v13 = new IntVariable("v13", v1SparseVals);
        v21 = new IntVariable("v21", v2SparseVals);
        v22 = new IntVariable("v22", v2SparseVals);
        v23 = new IntVariable("v23", v2SparseVals);
        v31 = new IntVariable("v31", v3SparseVals);
        v32 = new IntVariable("v32", v3SparseVals);
        v33 = new IntVariable("v33", v3SparseVals);
        u1 = new IntVariable("u1", 0, 8);
        u2 = new IntVariable("u2", 0, 8);
        u3 = new IntVariable("u3", 0, 8);
        z1 = new IntVariable("z1", 0, 100);
        z2 = new IntVariable("z2", 0, 100);
        z3 = new IntVariable("z3", 0, 100);
        p1 = new IntVariable("p1", 0, 1);
        p2 = new IntVariable("p2", 0, 1);
        p3 = new IntVariable("p3", 0, 1);
        z11 = new IntVariable("z11", 0, 100);
        z12 = new IntVariable("z12", 0, 100);
        z13 = new IntVariable("z13", 0, 100);
        z21 = new IntVariable("z21", 0, 100);
        z22 = new IntVariable("z22", 0, 100);
        z23 = new IntVariable("z23", 0, 100);
        z31 = new IntVariable("z31", 0, 100);
        z32 = new IntVariable("z32", 0, 100);
        z33 = new IntVariable("z33", 0, 100);
        x = new IntVariable("x", 0, 100);
        y = new IntVariable("y", 0, 100);
        z = new IntVariable("z", 0, 100);
        xiexpr = (GenericIntExpr) varFactory.genericInt("xi", idxI, new CspIntVariable[] { x1, x2, x3 });
        xijexpr = (GenericIntExpr) varFactory.genericInt("xij", new GenericIndex[] { idxI, idxJ }, new CspIntVariable[] { x11, x12, x13, x21, x22, x23, x31, x32, x33 });
        xjiexpr = (GenericIntExpr) varFactory.genericInt("xji", new GenericIndex[] { idxJ, idxI }, new CspIntVariable[] { x11, x12, x13, x21, x22, x23, x31, x32, x33 });
        xjkexpr = (GenericIntExpr) varFactory.genericInt("xjk", new GenericIndex[] { idxJ, idxK }, new CspIntVariable[] { x11, x12, x13, x21, x22, x23, x31, x32, x33 });
        xjikexpr = (GenericIntExpr) varFactory.genericInt("xjik", new GenericIndex[] { idxJ, idxI, idxK }, new CspIntVariable[] { x111, x121, x131, x211, x221, x231, x311, x321, x331, x112, x122, x132, x212, x222, x232, x312, x322, x332, x113, x123, x133, x213, x223, x233, x313, x323, x333 });
        xijkexpr = (GenericIntExpr) varFactory.genericInt("xijk", new GenericIndex[] { idxI, idxJ, idxK }, new CspIntVariable[] { x111, x112, x113, x121, x122, x123, x131, x132, x133, x211, x212, x213, x221, x222, x223, x231, x232, x233, x311, x312, x313, x321, x322, x323, x331, x332, x333 });
        yiexpr = (GenericIntExpr) varFactory.genericInt("yi", idxI, new CspIntVariable[] { y1, y2, y3 });
        yjexpr = (GenericIntExpr) varFactory.genericInt("yj", idxJ, new CspIntVariable[] { y1, y2, y3 });
        ykexpr = (GenericIntExpr) varFactory.genericInt("yk", idxK, new CspIntVariable[] { y1, y2, y3 });
        yijexpr = (GenericIntExpr) varFactory.genericInt("yij", new GenericIndex[] { idxI, idxJ }, new CspIntVariable[] { y11, y12, y13, y21, y22, y23, y31, y32, y33 });
        yjkexpr = (GenericIntExpr) varFactory.genericInt("yjk", new GenericIndex[] { idxJ, idxK }, new CspIntVariable[] { y11, y12, y13, y21, y22, y23, y31, y32, y33 });
        vikexpr = (GenericIntExpr) varFactory.genericInt("vik", new GenericIndex[] { idxI, idxK }, new CspIntVariable[] { v11, v12, v13, v21, v22, v23, v31, v32, v33 });
        vjkexpr = (GenericIntExpr) varFactory.genericInt("vjk", new GenericIndex[] { idxJ, idxK }, new CspIntVariable[] { v11, v12, v13, v21, v22, v23, v31, v32, v33 });
        uiexpr = (GenericIntExpr) varFactory.genericInt("ui", idxI, new CspIntVariable[] { u1, u2, u3 });
        ziexpr = (GenericIntExpr) varFactory.genericInt("zi", idxI, new CspIntVariable[] { z1, z2, z3 });
        zjexpr = (GenericIntExpr) varFactory.genericInt("zj", idxJ, new CspIntVariable[] { z1, z2, z3 });
        zkexpr = (GenericIntExpr) varFactory.genericInt("zk", idxK, new CspIntVariable[] { z1, z2, z3 });
        zijexpr = (GenericIntExpr) varFactory.genericInt("zij", new GenericIndex[] { idxI, idxJ }, new CspIntVariable[] { z11, z12, z13, z21, z22, z23, z31, z32, z33 });
        zikexpr = (GenericIntExpr) varFactory.genericInt("zik", new GenericIndex[] { idxI, idxK }, new CspIntVariable[] { z11, z12, z13, z21, z22, z23, z31, z32, z33 });
        pjexpr = (GenericIntExpr) varFactory.genericInt("pj", idxJ, new CspIntVariable[] { p1, p2, p3 });
        ukconst = new GenericIntConstant("ukconst", new GenericIndex[] { idxK }, new int[] { 2, 2, 2 });
        uiconst = new GenericIntConstant("ukconst", new GenericIndex[] { idxI }, new int[] { 1, 1, 1 });
        uijconst = new GenericIntConstant("uijconst", new GenericIndex[] { idxI, idxJ }, new int[] { 1, 1, 1, 1, 1, 1, 1, 1, 1 });
        xiconst = new GenericIntConstant("xiconst", new GenericIndex[] { idxI }, new int[] { 4, 3, 2 });
        vkconst = new GenericIntConstant("vkconst", new GenericIndex[] { idxK }, new int[] { 0, 0, 0 });
        wkconst = new GenericIntConstant("wkconst", new GenericIndex[] { idxK }, new int[] { 1, 1, 1 });
        wijconst = new GenericIntConstant("wijconst", new GenericIndex[] { idxI, idxJ }, new int[] { 2, 0, 0, 0, 0, 0, 0, 0, 0 });
        wjiconst = new GenericIntConstant("wjiconst", new GenericIndex[] { idxJ, idxI }, new int[] { 2, 2, 2, 2, 2, 2, 2, 2, 2 });
    }

    public void tearDown() {
        x1 = null;
        x2 = null;
        x3 = null;
        x11 = null;
        w11 = null;
        x12 = null;
        x13 = null;
        x21 = null;
        x22 = null;
        x23 = null;
        x31 = null;
        x32 = null;
        x33 = null;
        x111 = null;
        x121 = null;
        x131 = null;
        x211 = null;
        x221 = null;
        x231 = null;
        x311 = null;
        x321 = null;
        x331 = null;
        x112 = null;
        x122 = null;
        x132 = null;
        x212 = null;
        x222 = null;
        x232 = null;
        x312 = null;
        x322 = null;
        x332 = null;
        x113 = null;
        x123 = null;
        x133 = null;
        x213 = null;
        x223 = null;
        x233 = null;
        x313 = null;
        x323 = null;
        x333 = null;
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
        v11 = null;
        v12 = null;
        v13 = null;
        v21 = null;
        v22 = null;
        v23 = null;
        v31 = null;
        v32 = null;
        v33 = null;
        u1 = null;
        u2 = null;
        u3 = null;
        z1 = null;
        z2 = null;
        z3 = null;
        p1 = null;
        p2 = null;
        p3 = null;
        z11 = null;
        z12 = null;
        z13 = null;
        z21 = null;
        z22 = null;
        z23 = null;
        z31 = null;
        z32 = null;
        z33 = null;
        xiexpr = null;
        xijexpr = null;
        xjiexpr = null;
        xjkexpr = null;
        xjikexpr = null;
        xijkexpr = null;
        yiexpr = null;
        yjexpr = null;
        ykexpr = null;
        yijexpr = null;
        yjkexpr = null;
        vikexpr = null;
        vjkexpr = null;
        uiexpr = null;
        ziexpr = null;
        zjexpr = null;
        pjexpr = null;
        zkexpr = null;
        zijexpr = null;
        zikexpr = null;
        ukconst = null;
        uiconst = null;
        uijconst = null;
        xiconst = null;
        vkconst = null;
        wkconst = null;
        wijconst = null;
        wjiconst = null;
        idxI = null;
        idxJ = null;
        idxK = null;
        x = null;
        y = null;
        z = null;
        solver = null;
        varMath = null;
        varFactory = null;
    }

    public void testSummationEQVarWithReset() {
        try {
            CspIntExpr sum = (CspIntExpr) varMath.summation(xiexpr, new CspGenericIndex[] { idxI }, null);
            CspConstraint constraint = sum.eq(z);
            solver.addConstraint(constraint);
            assertEquals("min of x1", 0, x1.getMin());
            assertEquals("max of x1", 100, x1.getMax());
            assertEquals("min of x2", 0, x2.getMin());
            assertEquals("max of x2", 100, x2.getMax());
            assertEquals("min of x3", 0, x3.getMin());
            assertEquals("max of x3", 100, x3.getMax());
            assertEquals("min of z", 0, z.getMin());
            assertEquals("max of z", 100, z.getMax());
            assertEquals("min of sum", 0, sum.getMin());
            assertEquals("max of sum", 300, sum.getMax());
            x1.setMin(10);
            solver.propagate();
            assertEquals("min of x1", 10, x1.getMin());
            assertEquals("max of x1", 100, x1.getMax());
            assertEquals("min of x2", 0, x2.getMin());
            assertEquals("max of x2", 90, x2.getMax());
            assertEquals("min of x3", 0, x3.getMin());
            assertEquals("max of x3", 90, x3.getMax());
            assertEquals("min of z", 10, z.getMin());
            assertEquals("max of z", 100, z.getMax());
            assertEquals("min of sum", 10, sum.getMin());
            assertEquals("max of sum", 280, sum.getMax());
            z.setMax(80);
            solver.propagate();
            assertEquals("min of x1", 10, x1.getMin());
            assertEquals("max of x1", 80, x1.getMax());
            assertEquals("min of x2", 0, x2.getMin());
            assertEquals("max of x2", 70, x2.getMax());
            assertEquals("min of x3", 0, x3.getMin());
            assertEquals("max of x3", 70, x3.getMax());
            assertEquals("min of z", 10, z.getMin());
            assertEquals("max of z", 80, z.getMax());
            assertEquals("min of sum", 10, sum.getMin());
            assertEquals("max of sum", 220, sum.getMax());
            solver.reset();
            assertEquals("min of x1", 0, x1.getMin());
            assertEquals("max of x1", 100, x1.getMax());
            assertEquals("min of x2", 0, x2.getMin());
            assertEquals("max of x2", 100, x2.getMax());
            assertEquals("min of x3", 0, x3.getMin());
            assertEquals("max of x3", 100, x3.getMax());
            assertEquals("min of z", 0, z.getMin());
            assertEquals("max of z", 100, z.getMax());
            assertEquals("min of sum", 0, sum.getMin());
            assertEquals("max of sum", 300, sum.getMax());
            x1.setMin(10);
            solver.propagate();
            assertEquals("min of x1", 10, x1.getMin());
            assertEquals("max of x1", 100, x1.getMax());
            assertEquals("min of x2", 0, x2.getMin());
            assertEquals("max of x2", 90, x2.getMax());
            assertEquals("min of x3", 0, x3.getMin());
            assertEquals("max of x3", 90, x3.getMax());
            assertEquals("min of z", 10, z.getMin());
            assertEquals("max of z", 100, z.getMax());
            assertEquals("min of sum", 10, sum.getMin());
            assertEquals("max of sum", 280, sum.getMax());
            z.setMax(80);
            solver.propagate();
            assertEquals("min of x1", 10, x1.getMin());
            assertEquals("max of x1", 80, x1.getMax());
            assertEquals("min of x2", 0, x2.getMin());
            assertEquals("max of x2", 70, x2.getMax());
            assertEquals("min of x3", 0, x3.getMin());
            assertEquals("max of x3", 70, x3.getMax());
            assertEquals("min of z", 10, z.getMin());
            assertEquals("max of z", 80, z.getMax());
            assertEquals("min of sum", 10, sum.getMin());
            assertEquals("max of sum", 220, sum.getMax());
        } catch (PropagationFailureException pfe) {
            fail();
        }
    }

    public void testSummationLTVarWithPop() {
        try {
            ChoicePointStack choicestack = new ChoicePointStack();
            ConstraintStore store = new ConstraintStore(new AC5(), false, choicestack);
            CspIntExpr sum = (CspIntExpr) varMath.summation(xiexpr, new CspGenericIndex[] { idxI }, null);
            CspConstraint constraint = sum.lt(z);
            store.addConstraint(constraint);
            choicestack.push();
            assertEquals("min of x1", 0, x1.getMin());
            assertEquals("max of x1", 100, x1.getMax());
            assertEquals("min of x2", 0, x2.getMin());
            assertEquals("max of x2", 100, x2.getMax());
            assertEquals("min of x3", 0, x3.getMin());
            assertEquals("max of x3", 100, x3.getMax());
            assertEquals("min of z", 0, z.getMin());
            assertEquals("max of z", 100, z.getMax());
            assertEquals("min of sum", 0, sum.getMin());
            assertEquals("max of sum", 300, sum.getMax());
            x1.setMin(10);
            store.propagate();
            choicestack.push();
            assertEquals("min of x1", 10, x1.getMin());
            assertEquals("max of x1", 99, x1.getMax());
            assertEquals("min of x2", 0, x2.getMin());
            assertEquals("max of x2", 89, x2.getMax());
            assertEquals("min of x3", 0, x3.getMin());
            assertEquals("max of x3", 89, x3.getMax());
            assertEquals("min of z", 11, z.getMin());
            assertEquals("max of z", 100, z.getMax());
            assertEquals("min of sum", 10, sum.getMin());
            assertEquals("max of sum", 277, sum.getMax());
            z.setMax(80);
            store.propagate();
            assertEquals("min of x1", 10, x1.getMin());
            assertEquals("max of x1", 79, x1.getMax());
            assertEquals("min of x2", 0, x2.getMin());
            assertEquals("max of x2", 69, x2.getMax());
            assertEquals("min of x3", 0, x3.getMin());
            assertEquals("max of x3", 69, x3.getMax());
            assertEquals("min of z", 11, z.getMin());
            assertEquals("max of z", 80, z.getMax());
            assertEquals("min of sum", 10, sum.getMin());
            assertEquals("max of sum", 217, sum.getMax());
            choicestack.pop();
            assertEquals("min of x1", 10, x1.getMin());
            assertEquals("max of x1", 99, x1.getMax());
            assertEquals("min of x2", 0, x2.getMin());
            assertEquals("max of x2", 89, x2.getMax());
            assertEquals("min of x3", 0, x3.getMin());
            assertEquals("max of x3", 89, x3.getMax());
            assertEquals("min of z", 11, z.getMin());
            assertEquals("max of z", 100, z.getMax());
            assertEquals("min of sum", 10, sum.getMin());
            assertEquals("max of sum", 277, sum.getMax());
            choicestack.pop();
            assertEquals("min of x1", 0, x1.getMin());
            assertEquals("max of x1", 100, x1.getMax());
            assertEquals("min of x2", 0, x2.getMin());
            assertEquals("max of x2", 100, x2.getMax());
            assertEquals("min of x3", 0, x3.getMin());
            assertEquals("max of x3", 100, x3.getMax());
            assertEquals("min of z", 0, z.getMin());
            assertEquals("max of z", 100, z.getMax());
            assertEquals("min of sum", 0, sum.getMin());
            assertEquals("max of sum", 300, sum.getMax());
            x1.setMin(10);
            store.propagate();
            assertEquals("min of x1", 10, x1.getMin());
            assertEquals("max of x1", 99, x1.getMax());
            assertEquals("min of x2", 0, x2.getMin());
            assertEquals("max of x2", 89, x2.getMax());
            assertEquals("min of x3", 0, x3.getMin());
            assertEquals("max of x3", 89, x3.getMax());
            assertEquals("min of z", 11, z.getMin());
            assertEquals("max of z", 100, z.getMax());
            assertEquals("min of sum", 10, sum.getMin());
            assertEquals("max of sum", 277, sum.getMax());
            choicestack.pop();
            assertEquals("min of x1", 0, x1.getMin());
            assertEquals("max of x1", 100, x1.getMax());
            assertEquals("min of x2", 0, x2.getMin());
            assertEquals("max of x2", 100, x2.getMax());
            assertEquals("min of x3", 0, x3.getMin());
            assertEquals("max of x3", 100, x3.getMax());
            assertEquals("min of z", 0, z.getMin());
            assertEquals("max of z", 100, z.getMax());
            assertEquals("min of sum", 0, sum.getMin());
            assertEquals("max of sum", 300, sum.getMax());
            x1.setMin(10);
            store.propagate();
            assertEquals("min of x1", 10, x1.getMin());
            assertEquals("max of x1", 100, x1.getMax());
            assertEquals("min of x2", 0, x2.getMin());
            assertEquals("max of x2", 100, x2.getMax());
            assertEquals("min of x3", 0, x3.getMin());
            assertEquals("max of x3", 100, x3.getMax());
            assertEquals("min of z", 0, z.getMin());
            assertEquals("max of z", 100, z.getMax());
            assertEquals("min of sum", 10, sum.getMin());
            assertEquals("max of sum", 300, sum.getMax());
        } catch (PropagationFailureException pfe) {
            fail();
        }
    }

    public void testSummationGTVarWithPopAndPush() {
        try {
            ChoicePointStack choicestack = new ChoicePointStack();
            ConstraintStore store = new ConstraintStore(new AC5(), false, choicestack);
            CspIntExpr sum = (CspIntExpr) varMath.summation(xiexpr, new CspGenericIndex[] { idxI }, null);
            CspConstraint constraint = sum.gt(z);
            store.addConstraint(constraint);
            choicestack.push();
            assertEquals("min of x1", 0, x1.getMin());
            assertEquals("max of x1", 100, x1.getMax());
            assertEquals("min of x2", 0, x2.getMin());
            assertEquals("max of x2", 100, x2.getMax());
            assertEquals("min of x3", 0, x3.getMin());
            assertEquals("max of x3", 100, x3.getMax());
            assertEquals("min of z", 0, z.getMin());
            assertEquals("max of z", 100, z.getMax());
            assertEquals("min of sum", 0, sum.getMin());
            assertEquals("max of sum", 300, sum.getMax());
            x1.setMin(10);
            store.propagate();
            choicestack.push();
            assertEquals("min of x1", 10, x1.getMin());
            assertEquals("max of x1", 100, x1.getMax());
            assertEquals("min of x2", 0, x2.getMin());
            assertEquals("max of x2", 100, x2.getMax());
            assertEquals("min of x3", 0, x3.getMin());
            assertEquals("max of x3", 100, x3.getMax());
            assertEquals("min of z", 0, z.getMin());
            assertEquals("max of z", 100, z.getMax());
            assertEquals("min of sum", 10, sum.getMin());
            assertEquals("max of sum", 300, sum.getMax());
            z.setMax(80);
            store.propagate();
            assertEquals("min of x1", 10, x1.getMin());
            assertEquals("max of x1", 100, x1.getMax());
            assertEquals("min of x2", 0, x2.getMin());
            assertEquals("max of x2", 100, x2.getMax());
            assertEquals("min of x3", 0, x3.getMin());
            assertEquals("max of x3", 100, x3.getMax());
            assertEquals("min of z", 0, z.getMin());
            assertEquals("max of z", 80, z.getMax());
            assertEquals("min of sum", 10, sum.getMin());
            assertEquals("max of sum", 300, sum.getMax());
            Object data = choicestack.popDelta();
            assertEquals("min of x1", 10, x1.getMin());
            assertEquals("max of x1", 100, x1.getMax());
            assertEquals("min of x2", 0, x2.getMin());
            assertEquals("max of x2", 100, x2.getMax());
            assertEquals("min of x3", 0, x3.getMin());
            assertEquals("max of x3", 100, x3.getMax());
            assertEquals("min of z", 0, z.getMin());
            assertEquals("max of z", 100, z.getMax());
            assertEquals("min of sum", 10, sum.getMin());
            assertEquals("max of sum", 300, sum.getMax());
            choicestack.pushDelta(data);
            assertEquals("min of x1", 10, x1.getMin());
            assertEquals("max of x1", 100, x1.getMax());
            assertEquals("min of x2", 0, x2.getMin());
            assertEquals("max of x2", 100, x2.getMax());
            assertEquals("min of x3", 0, x3.getMin());
            assertEquals("max of x3", 100, x3.getMax());
            assertEquals("min of z", 0, z.getMin());
            assertEquals("max of z", 80, z.getMax());
            assertEquals("min of sum", 10, sum.getMin());
            assertEquals("max of sum", 300, sum.getMax());
            choicestack.pop();
            choicestack.pop();
            assertEquals("min of x1", 0, x1.getMin());
            assertEquals("max of x1", 100, x1.getMax());
            assertEquals("min of x2", 0, x2.getMin());
            assertEquals("max of x2", 100, x2.getMax());
            assertEquals("min of x3", 0, x3.getMin());
            assertEquals("max of x3", 100, x3.getMax());
            assertEquals("min of z", 0, z.getMin());
            assertEquals("max of z", 100, z.getMax());
            assertEquals("min of sum", 0, sum.getMin());
            assertEquals("max of sum", 300, sum.getMax());
            x1.setMin(10);
            store.propagate();
            assertEquals("min of x1", 10, x1.getMin());
            assertEquals("max of x1", 100, x1.getMax());
            assertEquals("min of x2", 0, x2.getMin());
            assertEquals("max of x2", 100, x2.getMax());
            assertEquals("min of x3", 0, x3.getMin());
            assertEquals("max of x3", 100, x3.getMax());
            assertEquals("min of z", 0, z.getMin());
            assertEquals("max of z", 100, z.getMax());
            assertEquals("min of sum", 10, sum.getMin());
            assertEquals("max of sum", 300, sum.getMax());
            data = choicestack.popDelta();
            assertEquals("min of x1", 0, x1.getMin());
            assertEquals("max of x1", 100, x1.getMax());
            assertEquals("min of x2", 0, x2.getMin());
            assertEquals("max of x2", 100, x2.getMax());
            assertEquals("min of x3", 0, x3.getMin());
            assertEquals("max of x3", 100, x3.getMax());
            assertEquals("min of z", 0, z.getMin());
            assertEquals("max of z", 100, z.getMax());
            assertEquals("min of sum", 0, sum.getMin());
            assertEquals("max of sum", 300, sum.getMax());
            x1.setMin(10);
            store.propagate();
            assertEquals("min of x1", 10, x1.getMin());
            assertEquals("max of x1", 100, x1.getMax());
            assertEquals("min of x2", 0, x2.getMin());
            assertEquals("max of x2", 100, x2.getMax());
            assertEquals("min of x3", 0, x3.getMin());
            assertEquals("max of x3", 100, x3.getMax());
            assertEquals("min of z", 0, z.getMin());
            assertEquals("max of z", 100, z.getMax());
            assertEquals("min of sum", 10, sum.getMin());
            assertEquals("max of sum", 300, sum.getMax());
            choicestack.pushDelta(data);
            x1.setMin(10);
            store.propagate();
            assertEquals("min of x1", 10, x1.getMin());
            assertEquals("max of x1", 100, x1.getMax());
            assertEquals("min of x2", 0, x2.getMin());
            assertEquals("max of x2", 100, x2.getMax());
            assertEquals("min of x3", 0, x3.getMin());
            assertEquals("max of x3", 100, x3.getMax());
            assertEquals("min of z", 0, z.getMin());
            assertEquals("max of z", 100, z.getMax());
            assertEquals("min of sum", 10, sum.getMin());
            assertEquals("max of sum", 300, sum.getMax());
        } catch (PropagationFailureException pfe) {
            fail();
        }
    }

    public void testSummationEQVarWithPop() {
        try {
            ChoicePointStack choicestack = new ChoicePointStack();
            ConstraintStore store = new ConstraintStore(new AC5(), false, choicestack);
            CspIntExpr sum = (CspIntExpr) varMath.summation(xiexpr, new CspGenericIndex[] { idxI }, null);
            CspConstraint constraint = sum.eq(z);
            store.addConstraint(constraint);
            choicestack.push();
            assertEquals("min of x1", 0, x1.getMin());
            assertEquals("max of x1", 100, x1.getMax());
            assertEquals("min of x2", 0, x2.getMin());
            assertEquals("max of x2", 100, x2.getMax());
            assertEquals("min of x3", 0, x3.getMin());
            assertEquals("max of x3", 100, x3.getMax());
            assertEquals("min of z", 0, z.getMin());
            assertEquals("max of z", 100, z.getMax());
            assertEquals("min of sum", 0, sum.getMin());
            assertEquals("max of sum", 300, sum.getMax());
            x1.setMin(10);
            store.propagate();
            choicestack.push();
            assertEquals("min of x1", 10, x1.getMin());
            assertEquals("max of x1", 100, x1.getMax());
            assertEquals("min of x2", 0, x2.getMin());
            assertEquals("max of x2", 90, x2.getMax());
            assertEquals("min of x3", 0, x3.getMin());
            assertEquals("max of x3", 90, x3.getMax());
            assertEquals("min of z", 10, z.getMin());
            assertEquals("max of z", 100, z.getMax());
            assertEquals("min of sum", 10, sum.getMin());
            assertEquals("max of sum", 280, sum.getMax());
            z.setMax(80);
            store.propagate();
            assertEquals("min of x1", 10, x1.getMin());
            assertEquals("max of x1", 80, x1.getMax());
            assertEquals("min of x2", 0, x2.getMin());
            assertEquals("max of x2", 70, x2.getMax());
            assertEquals("min of x3", 0, x3.getMin());
            assertEquals("max of x3", 70, x3.getMax());
            assertEquals("min of z", 10, z.getMin());
            assertEquals("max of z", 80, z.getMax());
            assertEquals("min of sum", 10, sum.getMin());
            assertEquals("max of sum", 220, sum.getMax());
            choicestack.pop();
            assertEquals("min of x1", 10, x1.getMin());
            assertEquals("max of x1", 100, x1.getMax());
            assertEquals("min of x2", 0, x2.getMin());
            assertEquals("max of x2", 90, x2.getMax());
            assertEquals("min of x3", 0, x3.getMin());
            assertEquals("max of x3", 90, x3.getMax());
            assertEquals("min of z", 10, z.getMin());
            assertEquals("max of z", 100, z.getMax());
            assertEquals("min of sum", 10, sum.getMin());
            assertEquals("max of sum", 280, sum.getMax());
            choicestack.pop();
            assertEquals("min of x1", 0, x1.getMin());
            assertEquals("max of x1", 100, x1.getMax());
            assertEquals("min of x2", 0, x2.getMin());
            assertEquals("max of x2", 100, x2.getMax());
            assertEquals("min of x3", 0, x3.getMin());
            assertEquals("max of x3", 100, x3.getMax());
            assertEquals("min of z", 0, z.getMin());
            assertEquals("max of z", 100, z.getMax());
            assertEquals("min of sum", 0, sum.getMin());
            assertEquals("max of sum", 300, sum.getMax());
            x1.setMin(10);
            store.propagate();
            assertEquals("min of x1", 10, x1.getMin());
            assertEquals("max of x1", 100, x1.getMax());
            assertEquals("min of x2", 0, x2.getMin());
            assertEquals("max of x2", 90, x2.getMax());
            assertEquals("min of x3", 0, x3.getMin());
            assertEquals("max of x3", 90, x3.getMax());
            assertEquals("min of z", 10, z.getMin());
            assertEquals("max of z", 100, z.getMax());
            assertEquals("min of sum", 10, sum.getMin());
            assertEquals("max of sum", 280, sum.getMax());
            choicestack.pop();
            assertEquals("min of x1", 0, x1.getMin());
            assertEquals("max of x1", 100, x1.getMax());
            assertEquals("min of x2", 0, x2.getMin());
            assertEquals("max of x2", 100, x2.getMax());
            assertEquals("min of x3", 0, x3.getMin());
            assertEquals("max of x3", 100, x3.getMax());
            assertEquals("min of z", 0, z.getMin());
            assertEquals("max of z", 100, z.getMax());
            assertEquals("min of sum", 0, sum.getMin());
            assertEquals("max of sum", 300, sum.getMax());
            x1.setMin(10);
            store.propagate();
            assertEquals("min of x1", 10, x1.getMin());
            assertEquals("max of x1", 100, x1.getMax());
            assertEquals("min of x2", 0, x2.getMin());
            assertEquals("max of x2", 100, x2.getMax());
            assertEquals("min of x3", 0, x3.getMin());
            assertEquals("max of x3", 100, x3.getMax());
            assertEquals("min of z", 0, z.getMin());
            assertEquals("max of z", 100, z.getMax());
            assertEquals("min of sum", 10, sum.getMin());
            assertEquals("max of sum", 300, sum.getMax());
        } catch (PropagationFailureException pfe) {
            fail();
        }
    }

    public void testSummationEQVarWithPopAndPush() {
        try {
            ChoicePointStack choicestack = new ChoicePointStack();
            ConstraintStore store = new ConstraintStore(new AC5(), false, choicestack);
            CspIntExpr sum = (CspIntExpr) varMath.summation(xiexpr, new CspGenericIndex[] { idxI }, null);
            CspConstraint constraint = sum.eq(z);
            store.addConstraint(constraint);
            choicestack.push();
            assertEquals("min of x1", 0, x1.getMin());
            assertEquals("max of x1", 100, x1.getMax());
            assertEquals("min of x2", 0, x2.getMin());
            assertEquals("max of x2", 100, x2.getMax());
            assertEquals("min of x3", 0, x3.getMin());
            assertEquals("max of x3", 100, x3.getMax());
            assertEquals("min of z", 0, z.getMin());
            assertEquals("max of z", 100, z.getMax());
            assertEquals("min of sum", 0, sum.getMin());
            assertEquals("max of sum", 300, sum.getMax());
            x1.setMin(10);
            store.propagate();
            choicestack.push();
            assertEquals("min of x1", 10, x1.getMin());
            assertEquals("max of x1", 100, x1.getMax());
            assertEquals("min of x2", 0, x2.getMin());
            assertEquals("max of x2", 90, x2.getMax());
            assertEquals("min of x3", 0, x3.getMin());
            assertEquals("max of x3", 90, x3.getMax());
            assertEquals("min of z", 10, z.getMin());
            assertEquals("max of z", 100, z.getMax());
            assertEquals("min of sum", 10, sum.getMin());
            assertEquals("max of sum", 280, sum.getMax());
            z.setMax(80);
            store.propagate();
            assertEquals("min of x1", 10, x1.getMin());
            assertEquals("max of x1", 80, x1.getMax());
            assertEquals("min of x2", 0, x2.getMin());
            assertEquals("max of x2", 70, x2.getMax());
            assertEquals("min of x3", 0, x3.getMin());
            assertEquals("max of x3", 70, x3.getMax());
            assertEquals("min of z", 10, z.getMin());
            assertEquals("max of z", 80, z.getMax());
            assertEquals("min of sum", 10, sum.getMin());
            assertEquals("max of sum", 220, sum.getMax());
            Object data = choicestack.popDelta();
            assertEquals("min of x1", 10, x1.getMin());
            assertEquals("max of x1", 100, x1.getMax());
            assertEquals("min of x2", 0, x2.getMin());
            assertEquals("max of x2", 90, x2.getMax());
            assertEquals("min of x3", 0, x3.getMin());
            assertEquals("max of x3", 90, x3.getMax());
            assertEquals("min of z", 10, z.getMin());
            assertEquals("max of z", 100, z.getMax());
            assertEquals("min of sum", 10, sum.getMin());
            assertEquals("max of sum", 280, sum.getMax());
            choicestack.pushDelta(data);
            assertEquals("min of x1", 10, x1.getMin());
            assertEquals("max of x1", 80, x1.getMax());
            assertEquals("min of x2", 0, x2.getMin());
            assertEquals("max of x2", 70, x2.getMax());
            assertEquals("min of x3", 0, x3.getMin());
            assertEquals("max of x3", 70, x3.getMax());
            assertEquals("min of z", 10, z.getMin());
            assertEquals("max of z", 80, z.getMax());
            assertEquals("min of sum", 10, sum.getMin());
            assertEquals("max of sum", 220, sum.getMax());
            choicestack.pop();
            choicestack.pop();
            assertEquals("min of x1", 0, x1.getMin());
            assertEquals("max of x1", 100, x1.getMax());
            assertEquals("min of x2", 0, x2.getMin());
            assertEquals("max of x2", 100, x2.getMax());
            assertEquals("min of x3", 0, x3.getMin());
            assertEquals("max of x3", 100, x3.getMax());
            assertEquals("min of z", 0, z.getMin());
            assertEquals("max of z", 100, z.getMax());
            assertEquals("min of sum", 0, sum.getMin());
            assertEquals("max of sum", 300, sum.getMax());
            x1.setMin(10);
            store.propagate();
            assertEquals("min of x1", 10, x1.getMin());
            assertEquals("max of x1", 100, x1.getMax());
            assertEquals("min of x2", 0, x2.getMin());
            assertEquals("max of x2", 90, x2.getMax());
            assertEquals("min of x3", 0, x3.getMin());
            assertEquals("max of x3", 90, x3.getMax());
            assertEquals("min of z", 10, z.getMin());
            assertEquals("max of z", 100, z.getMax());
            assertEquals("min of sum", 10, sum.getMin());
            assertEquals("max of sum", 280, sum.getMax());
            data = choicestack.popDelta();
            assertEquals("min of x1", 0, x1.getMin());
            assertEquals("max of x1", 100, x1.getMax());
            assertEquals("min of x2", 0, x2.getMin());
            assertEquals("max of x2", 100, x2.getMax());
            assertEquals("min of x3", 0, x3.getMin());
            assertEquals("max of x3", 100, x3.getMax());
            assertEquals("min of z", 0, z.getMin());
            assertEquals("max of z", 100, z.getMax());
            assertEquals("min of sum", 0, sum.getMin());
            assertEquals("max of sum", 300, sum.getMax());
            x1.setMin(10);
            store.propagate();
            assertEquals("min of x1", 10, x1.getMin());
            assertEquals("max of x1", 100, x1.getMax());
            assertEquals("min of x2", 0, x2.getMin());
            assertEquals("max of x2", 100, x2.getMax());
            assertEquals("min of x3", 0, x3.getMin());
            assertEquals("max of x3", 100, x3.getMax());
            assertEquals("min of z", 0, z.getMin());
            assertEquals("max of z", 100, z.getMax());
            assertEquals("min of sum", 10, sum.getMin());
            assertEquals("max of sum", 300, sum.getMax());
            choicestack.pushDelta(data);
            x1.setMin(10);
            store.propagate();
            assertEquals("min of x1", 10, x1.getMin());
            assertEquals("max of x1", 100, x1.getMax());
            assertEquals("min of x2", 0, x2.getMin());
            assertEquals("max of x2", 90, x2.getMax());
            assertEquals("min of x3", 0, x3.getMin());
            assertEquals("max of x3", 90, x3.getMax());
            assertEquals("min of z", 10, z.getMin());
            assertEquals("max of z", 100, z.getMax());
            assertEquals("min of sum", 10, sum.getMin());
            assertEquals("max of sum", 280, sum.getMax());
        } catch (PropagationFailureException pfe) {
            fail();
        }
    }
}
