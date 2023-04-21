package net.sourceforge.combean.mathprog.linalg;

import net.sourceforge.combean.interfaces.mathprog.linalg.SparseVector;
import net.sourceforge.combean.interfaces.mathprog.lp.model.NoLabel;

public class DoubleVector extends DoubleVec<NoLabel> implements SparseVector {

    /**
     * @param doubleArr
     */
    public DoubleVector(double[] doubleArr) {
        super(doubleArr);
    }
}
