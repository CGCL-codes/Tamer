package com.mockturtlesolutions.snifflib.functions.transcendental;

import com.mockturtlesolutions.snifflib.datatypes.DblMatrix;
import com.mockturtlesolutions.snifflib.functions.UniUniFunction;

public class Exp extends UniUniFunction {

    /**
	Default constructor.
	*/
    public Exp() {
        super();
    }

    /**
	Get value at given X.
	*/
    public DblMatrix getValueAt(DblMatrix X) {
        DblMatrix out;
        out = DblMatrix.exp(X);
        return (out);
    }
}
