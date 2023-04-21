package ru.jnano.math.calc.optimal;

import ru.jnano.math.IFunctionOne;

public interface IOptimalMethod {

    public OptimalSolverStatus call(double a, double b, double e, double value, double dv, IFunctionOne fun);
}
