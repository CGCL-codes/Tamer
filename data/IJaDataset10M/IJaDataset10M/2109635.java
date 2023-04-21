package edu.ksu.cis.bnj.ver3.core.values;

import edu.ksu.cis.bnj.ver3.core.Value;

public class Field {

    public static Value add(Value a, Value b) {
        if ((a instanceof ValueDouble) && (b instanceof ValueDouble)) {
            return new ValueDouble(((ValueDouble) a).getValue() + ((ValueDouble) b).getValue());
        }
        if ((a instanceof ValueDouble) && (b instanceof ValueUnity)) {
            return new ValueDouble(((ValueDouble) a).getValue() + 1);
        }
        if ((b instanceof ValueDouble) && (a instanceof ValueUnity)) {
            return new ValueDouble(((ValueDouble) b).getValue() + 1);
        }
        if ((a instanceof ValueFloat) && (b instanceof ValueFloat)) {
            return new ValueFloat(((ValueFloat) a).getValue() + ((ValueFloat) b).getValue());
        }
        if ((a instanceof ValueFloat) && (b instanceof ValueUnity)) {
            return new ValueFloat(((ValueFloat) a).getValue() + 1);
        }
        if ((b instanceof ValueFloat) && (a instanceof ValueUnity)) {
            return new ValueFloat(((ValueFloat) b).getValue() + 1);
        }
        if ((a instanceof ValueZero)) {
            return b;
        }
        if ((b instanceof ValueZero)) {
            return a;
        }
        if ((a instanceof ValueRational) && (b instanceof ValueRational)) {
            return ValueRational.add((ValueRational) a, (ValueRational) b);
        }
        if ((a instanceof ValueRational) && (b instanceof ValueUnity)) {
            return ValueRational.addunity((ValueRational) a);
        }
        if ((b instanceof ValueRational) && (a instanceof ValueUnity)) {
            return ValueRational.addunity((ValueRational) b);
        }
        return new ExprAdd(a, b);
    }

    public static Value subtract(Value a, Value b) {
        if ((a instanceof ValueDouble) && (b instanceof ValueDouble)) {
            return new ValueDouble(((ValueDouble) a).getValue() - ((ValueDouble) b).getValue());
        }
        if ((a instanceof ValueDouble) && (b instanceof ValueUnity)) {
            return new ValueDouble(((ValueDouble) a).getValue() - 1);
        }
        if ((b instanceof ValueDouble) && (a instanceof ValueUnity)) {
            return new ValueDouble(-((ValueDouble) b).getValue() + 1);
        }
        if ((a instanceof ValueFloat) && (b instanceof ValueFloat)) {
            return new ValueFloat(((ValueFloat) a).getValue() - ((ValueFloat) b).getValue());
        }
        if ((a instanceof ValueFloat) && (b instanceof ValueFloat)) {
            return new ValueFloat(((ValueFloat) a).getValue() - 1);
        }
        if ((b instanceof ValueFloat) && (a instanceof ValueUnity)) {
            return new ValueFloat(-((ValueFloat) b).getValue() + 1);
        }
        if ((b instanceof ValueZero)) {
            return a;
        }
        if ((a instanceof ValueRational) && (b instanceof ValueRational)) {
            return ValueRational.subtract((ValueRational) a, (ValueRational) b);
        }
        return Field.add(a, new ExprNegate(b));
    }

    public static Value mult(Value a, Value b) {
        if ((a instanceof ValueDouble) && (b instanceof ValueDouble)) {
            return new ValueDouble(((ValueDouble) a).getValue() * ((ValueDouble) b).getValue());
        }
        if ((a instanceof ValueFloat) && (b instanceof ValueFloat)) {
            return new ValueFloat(((ValueFloat) a).getValue() * ((ValueFloat) b).getValue());
        }
        if ((b instanceof ValueUnity)) {
            return a;
        }
        if ((a instanceof ValueUnity)) {
            return b;
        }
        if ((a instanceof ValueZero)) {
            return a;
        }
        if ((b instanceof ValueZero)) {
            return b;
        }
        if ((a instanceof ValueRational) && (b instanceof ValueRational)) {
            return ValueRational.mult((ValueRational) a, (ValueRational) b);
        }
        return new ExprMultiply(a, b);
    }

    public static Value divide(Value a, Value b) {
        if ((a instanceof ValueDouble) && (b instanceof ValueDouble)) {
            if (((ValueDouble) b).getValue() != 0.0) {
                return new ValueDouble(((ValueDouble) a).getValue() / ((ValueDouble) b).getValue());
            } else {
                if (((ValueDouble) a).getValue() == 0.0) return a;
            }
        }
        if ((a instanceof ValueFloat) && (b instanceof ValueFloat)) {
            if (((ValueFloat) b).getValue() != 0.0) {
                return new ValueFloat(((ValueFloat) a).getValue() / ((ValueFloat) b).getValue());
            } else {
                if (((ValueFloat) a).getValue() == 0.0) return a;
            }
        }
        if ((b instanceof ValueUnity)) {
            return a;
        }
        if ((b instanceof ValueDouble) && (a instanceof ValueUnity)) {
            if (((ValueDouble) b).getValue() != 0.0) {
                return new ValueDouble(1.0 / ((ValueDouble) b).getValue());
            } else {
                if (((ValueDouble) a).getValue() == 0.0) return a;
            }
        }
        if ((b instanceof ValueFloat) && (a instanceof ValueUnity)) {
            if (((ValueFloat) b).getValue() != 0.0) {
                return new ValueFloat(1.0f / ((ValueFloat) b).getValue());
            } else {
                if (((ValueFloat) a).getValue() == 0.0) return a;
            }
        }
        if ((a instanceof ValueRational) && (b instanceof ValueRational)) {
            return ValueRational.divide((ValueRational) a, (ValueRational) b);
        }
        if ((a instanceof ValueZero)) {
            return a;
        }
        return new ExprDivide(a, b);
    }
}
