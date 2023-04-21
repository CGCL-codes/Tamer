package com.mockturtlesolutions.snifflib.linalg;

import com.mockturtlesolutions.snifflib.datatypes.DblMatrix;
import com.mockturtlesolutions.snifflib.datatypes.Subscript;

public class PVectorNormAlgorithm extends AbstractVectorNormAlgorithm {

    private double P;

    public PVectorNormAlgorithm() {
        super();
        this.P = 1.0;
    }

    public PVectorNormAlgorithm(int P) {
        super();
        Double DP = new Double(P);
        this.P = DP.doubleValue();
    }

    public PVectorNormAlgorithm(double P) {
        super();
        this.P = P;
    }

    /**
	Get current setting of the norm's P.
	*/
    public double getP() {
        return (this.P);
    }

    /**
	Return the norm.
	*/
    public DblMatrix getNorm(DblMatrix X) {
        return (DblMatrix.Sum(DblMatrix.abs(X).pow(this.P)).pow(1 / this.P));
    }
}
