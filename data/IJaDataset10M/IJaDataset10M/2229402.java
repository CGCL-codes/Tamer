package jopt.csp.test.trig;

import java.util.Random;
import jopt.csp.CspSolver;
import jopt.csp.spi.arcalgorithm.constraint.num.NatLogConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.NumExpr;
import jopt.csp.variable.CspDoubleVariable;
import jopt.csp.variable.CspVariableFactory;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;

public class NatLogConstraintTest extends TestCase {

    private CspSolver solver;

    private CspVariableFactory varFactory;

    private CspDoubleVariable a;

    private CspDoubleVariable z;

    private NumExpr aexpr;

    private NumExpr zexpr;

    public void setUp() {
        solver = CspSolver.createSolver();
        varFactory = solver.getVarFactory();
        a = varFactory.doubleVar("a", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        z = varFactory.doubleVar("z", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        aexpr = (NumExpr) a;
        zexpr = (NumExpr) z;
    }

    public void tearDown() {
        solver = null;
        varFactory = null;
        a = null;
        z = null;
        aexpr = null;
        zexpr = null;
    }

    public void testExpModZ() {
        try {
            Random rand = new Random();
            for (int i = 1; i <= 2; i++) {
                for (int j = 1; j <= 5; j++) {
                    int base = j * ((i == 1) ? -1 : 1);
                    for (int k = 0; k < 100; k++) {
                        solver.clear();
                        solver.addVariable(z);
                        double n1 = base * rand.nextDouble();
                        double n2 = base * rand.nextDouble();
                        double minZ = Math.min(n1, n2);
                        z.setMin(minZ);
                        double maxZ = Double.NEGATIVE_INFINITY;
                        while (maxZ < minZ) {
                            n1 = minZ + rand.nextDouble();
                            n2 = minZ + rand.nextDouble();
                            maxZ = Math.max(n1, n2);
                        }
                        z.setMax(maxZ);
                        solver.addConstraint(new NatLogConstraint(aexpr, zexpr));
                        double minA = Math.exp(z.getMin());
                        double maxA = Math.exp(z.getMax());
                        assertEquals("a min", minA, a.getMin(), 0.0001);
                        assertEquals("a max", maxA, a.getMax(), 0.0001);
                        assertEquals("z min", minZ, z.getMin(), 0.0001);
                        assertEquals("z max", maxZ, z.getMax(), 0.0001);
                    }
                }
            }
        } catch (PropagationFailureException propx) {
            propx.printStackTrace(System.out);
            fail();
        }
    }

    public void testExpModA() {
        try {
            Random rand = new Random();
            for (int j = 1; j <= 5; j++) {
                for (int k = 0; k < 100; k++) {
                    solver.clear();
                    solver.addVariable(a);
                    double n1 = j * rand.nextDouble();
                    double n2 = j * rand.nextDouble();
                    double minA = Math.min(n1, n2);
                    a.setMin(minA);
                    double maxA = Double.NEGATIVE_INFINITY;
                    while (maxA < minA) {
                        n1 = minA + rand.nextDouble();
                        n2 = minA + rand.nextDouble();
                        maxA = Math.max(n1, n2);
                    }
                    a.setMax(maxA);
                    solver.addConstraint(new NatLogConstraint(aexpr, zexpr));
                    double minZ = Math.log(a.getMin());
                    double maxZ = Math.log(a.getMax());
                    assertEquals("a min", minA, a.getMin(), 0.0001);
                    assertEquals("a max", maxA, a.getMax(), 0.0001);
                    assertEquals("z min", minZ, z.getMin(), 0.0001);
                    assertEquals("z max", maxZ, z.getMax(), 0.0001);
                }
            }
        } catch (PropagationFailureException propx) {
            propx.printStackTrace(System.out);
            fail();
        }
    }
}
