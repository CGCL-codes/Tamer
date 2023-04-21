package com.mockturtlesolutions.snifflib.linalg;

import com.mockturtlesolutions.snifflib.datatypes.DblMatrix;
import com.mockturtlesolutions.snifflib.datatypes.Subscript;

public class OneVectorNormAlgorithm extends AbstractVectorNormAlgorithm {

    public OneVectorNormAlgorithm() {
        super();
    }

    /**
	Return the norm.
	*/
    public DblMatrix getNorm(DblMatrix X) {
        return (DblMatrix.Sum(DblMatrix.abs(X)));
    }
}
