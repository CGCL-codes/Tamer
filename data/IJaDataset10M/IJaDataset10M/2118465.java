package org.jquantlib.testsuite.math.optimization;

import static org.junit.Assert.fail;
import org.jquantlib.math.Array;
import org.jquantlib.math.Closeness;
import org.jquantlib.math.optimization.NoConstraint;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoConstraintTest {

    private static final Logger logger = LoggerFactory.getLogger(NoConstraintTest.class);

    private final NoConstraint nc;

    public NoConstraintTest() {
        logger.info("\n\n::::: " + this.getClass().getSimpleName() + " :::::");
        this.nc = new NoConstraint();
    }

    @Ignore
    @Test
    public void testTest() {
        if (!nc.test(new Array())) fail("NoConstraint test method failed");
    }

    @Ignore
    @Test
    public void testUpdate() {
        Array params = new Array(new double[] { 1.0d, 1.1d, 2.3d });
        Array direction = new Array(new double[] { 0.1d, 0.3d, 1.1d });
        double beta = 2.0;
        nc.update(params, direction, beta);
        logger.info("params after co.update=" + "{" + params.getData()[0] + "," + params.getData()[1] + "," + params.getData()[2] + "}");
        logger.info("Test 1.1 + 2.0 * 0.3 = " + (1.1 + 2.0 * 0.3));
        if (!isArrayEqual(params, new Array(new double[] { 1.2d, 1.7d, 4.5d }), 0.000001)) fail("Constraint update method failed");
    }

    @Ignore
    @Test
    public void testEmpty() {
        if (nc.empty()) fail("Constraint empty method failed");
    }

    private boolean isArrayEqual(Array one, Array two, double precision) {
        Array diffArray = one.operatorSubtractCopy(two);
        logger.info("diffArray =" + "{" + diffArray.getData()[0] + "," + diffArray.getData()[1] + "," + diffArray.getData()[2] + "}");
        return Closeness.isCloseEnough(diffArray.dotProduct(diffArray, diffArray), precision * precision);
    }
}
