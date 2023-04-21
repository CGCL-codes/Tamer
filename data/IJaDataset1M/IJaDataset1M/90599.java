package bitWave.examples.linAlg.ode.problems;

import bitWave.dataStructures.Record;
import bitWave.dataStructures.VariableSet;
import bitWave.dataStructures.util.DataException;
import bitWave.linAlg.LinAlgFactory;
import bitWave.linAlg.Vec4;
import bitWave.linAlg.impl.util.ODEProblemImpl;

/**
 * Implements a two body problem consisting of a central body M with large mass
 * orbited by a second body m with negligable mass. Masses are included in
 * the problem so feel free to mess with them.
 * 
 * The ODE for the problem is (I warmly recommend this <a href="http://www.math.umass.edu/~gardner/1.B.pdf">introduction</a> to the problem):
 * <pre>                     
 *                            dj - di
 * di''(x) = -G * mi * mj * -------------
 *                          | dj - di |^3
 * </pre> where i, j are indizes for the first and second body, d = displacement, m = mass, G = gravitational constant.
 * 
 * @author fw
 *
 */
public class Problem_2Body extends ODEProblemImpl {

    /**
     * Creates the 2-body problem.
     * @param linAlgFactory The linear algebra factory to use with the problem.
     * @throws DataException if the dependent variables could not be properly initialized.
     */
    public Problem_2Body(final LinAlgFactory linAlgFactory) throws DataException {
        super(linAlgFactory);
        String[] r = { "M", "Mvx", "Mvy", "Mvz", "Mdx", "Mdy", "Mdz", "m", "mvx", "mvy", "mvz", "mdx", "mdy", "mdz" };
        VariableSet vs = new VariableSet(r);
        this.m_dependentVariables = vs;
    }

    /**
     * Calculates the acceleration acting on b1 due to gravitational attraction
     * between b1 and b2, based on the given record and the start indizes for the 
     * position vector.
     * @param y The record from which the variable values are read.
     * @param b1x The start index of the position vector of b1 within the record.
     * @param b1m The index of the mass variable for b1.
     * @param b2x The start index of the position vector of b2 within the record.
     * @param b2m The index of the mass variable for b2.
     * @return The force vector acting on b1.
     */
    protected Vec4 calcGravity(final Record y, final int b1x, final int b1m, final int b2x, final int b2m) {
        final double G = 6.6742E-11;
        double[] yv = y.getValues();
        Vec4 d = this.m_laf.subtractVectors(this.m_laf.createVector(yv[b2x], yv[b2x + 1], yv[b2x + 2]), this.m_laf.createVector(yv[b1x], yv[b1x + 1], yv[b1x + 2]));
        double dn = d.getLengthSquared();
        double fa = G * yv[b1m] * yv[b2m] / dn;
        d = this.m_laf.normalizeVector(d);
        return this.m_laf.scaleVector(d, fa / yv[b1m]);
    }

    public Record getDerivatives(double x, Record y) {
        double[] yv = y.getValues();
        double[] rv = new double[yv.length];
        rv[0] = 0;
        Vec4 a = calcGravity(y, 4, 0, 11, 7);
        rv[1] = a.x();
        rv[2] = a.y();
        rv[3] = a.z();
        rv[4] = yv[1];
        rv[5] = yv[2];
        rv[6] = yv[3];
        rv[7] = 0;
        a = calcGravity(y, 11, 7, 4, 0);
        rv[8] = a.x();
        rv[9] = a.y();
        rv[10] = a.z();
        rv[11] = yv[8];
        rv[12] = yv[9];
        rv[13] = yv[10];
        return new Record(this.m_dependentVariables, rv);
    }
}
