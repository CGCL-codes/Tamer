package com.google.clearsilver.jsilver.functions.operators;

import com.google.clearsilver.jsilver.autoescape.EscapeMode;
import com.google.clearsilver.jsilver.functions.NonEscapingFunction;
import com.google.clearsilver.jsilver.values.Value;
import static com.google.clearsilver.jsilver.values.Value.literalValue;

/**
 * X + Y (string).
 */
public class AddFunction extends NonEscapingFunction {

    public Value execute(Value... args) {
        Value left = args[0];
        Value right = args[1];
        EscapeMode mode = EscapeMode.combineModes(left.getEscapeMode(), right.getEscapeMode());
        return literalValue(left.asString() + right.asString(), mode, left.isPartiallyEscaped() || right.isPartiallyEscaped());
    }
}
