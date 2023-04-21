package org.tagbox.engine.function;

/**
 */
public class MinFunction extends MaxFunction {

    protected boolean closer(double lhs, double rhs) {
        return lhs < rhs;
    }
}
