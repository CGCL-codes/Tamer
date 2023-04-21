package com.dukesoftware.utils.math.function;

import com.dukesoftware.utils.solve.DoubleValueHolder;

public interface IMinimizer {

    double solve(IFunction f, DoubleValueHolder value);
}
