package net.sourceforge.jrcom.test.chaos.rossler;

import by.bsu.rfe.numerical.function.VectorFunction;
import by.bsu.rfe.numerical.matrix.RealVector;
import by.bsu.rfe.numerical.matrix.RealVectorImpl;

public class RosslerFunction implements VectorFunction {

    private static final int DIMENSION = 3;

    private final double a;

    private final double b;

    private final double r;

    public RosslerFunction(double a, double b, double r) {
        this.a = a;
        this.b = b;
        this.r = r;
    }

    @Override
    public int getRows() {
        return DIMENSION;
    }

    @Override
    public int getCols() {
        return 1;
    }

    @Override
    public RealVector valueAt(RealVector point) {
        RealVector result = new RealVectorImpl(DIMENSION);
        final double x = point.getElement(0);
        final double y = point.getElement(1);
        final double z = point.getElement(2);
        result.setElement(0, -y - z);
        result.setElement(1, x + a * y);
        result.setElement(2, b + (x - r) * z);
        return result;
    }
}
