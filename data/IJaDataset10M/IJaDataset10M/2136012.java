package com.google.clearsilver.jsilver.functions.operators;

import com.google.clearsilver.jsilver.functions.NonEscapingFunction;
import com.google.clearsilver.jsilver.values.Value;
import static com.google.clearsilver.jsilver.values.Value.literalConstant;

/**
 * X &gt;= Y.
 */
public class GreaterOrEqualFunction extends NonEscapingFunction {

    public Value execute(Value... args) {
        Value left = args[0];
        Value right = args[1];
        return literalConstant(left.asNumber() >= right.asNumber(), left, right);
    }
}
