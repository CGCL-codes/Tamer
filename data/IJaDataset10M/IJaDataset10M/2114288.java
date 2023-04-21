package de.helwich.math.symbolic.node;

/**
 * @author Hendrik Helwich
 */
public class ScalarOperations {

    static final Scalar mergeNumbers(ScalarFactory sf, Scalar s) {
        if (s.isAtomic()) return s;
        Function f = (Function) s;
        final int inputsLength = f.getInputCount();
        Scalar[] inputs = f.getInputs();
        Scalar[] newInputs = null;
        for (int i = 0; i < inputsLength; i++) {
            Scalar in = inputs[i];
            Scalar _in = mergeNumbers(sf, in);
            if (newInputs != null) newInputs[i] = _in; else if (in != _in) {
                newInputs = new Scalar[inputsLength];
                for (int j = 0; j < i; j++) newInputs[j] = inputs[j];
                newInputs[i] = _in;
            }
        }
        if (newInputs != null) inputs = newInputs;
        final Number _0 = ScalarFactory.ZERO;
        final Number _1 = ScalarFactory.ONE;
        final Number _1_ = ScalarFactory.ONE_NEG;
        final FunctionType type = f.getFunctionType();
        switch(type) {
            case ADDITION:
                if (inputs[0] instanceof Number) {
                    if (_0 == inputs[0]) return inputs[1];
                    if (inputs[1] instanceof Number) return sf.add(add((Number) inputs[0], (Number) inputs[1]));
                }
                if (_0 == inputs[1]) return inputs[0];
                break;
            case SUBTRACTION:
                if (inputs[0] instanceof Number) {
                    if (inputs[1] instanceof Number) return sf.add(subtract((Number) inputs[0], (Number) inputs[1]));
                    if (_0 == inputs[0]) return sf.add(negate((Number) inputs[1]));
                }
                if (_0 == inputs[1]) return inputs[0];
                break;
            case MULTIPLICATION:
                if (inputs[0] instanceof Number) {
                    if (_1 == inputs[0]) return inputs[1];
                    if (_0 == inputs[0]) return _0;
                    if (inputs[1] instanceof Number) return sf.add(multiply((Number) inputs[0], (Number) inputs[1]));
                    if (_1_ == inputs[0]) return sf.getFunction(FunctionType.NEGATE, inputs[1]);
                }
                if (_1 == inputs[1]) return inputs[0];
                if (_0 == inputs[1]) return _0;
                if (_1_ == inputs[1]) return sf.getFunction(FunctionType.NEGATE, inputs[0]);
                break;
            case DIVISION:
                if (inputs[1] instanceof Number) {
                    if (_0 == inputs[1]) throw new DivisionByZeroException();
                    if (_1 == inputs[1]) return inputs[0];
                    if (inputs[0] instanceof Number) return sf.add(divide((Number) inputs[0], (Number) inputs[1]));
                    if (_1_ == inputs[1]) return sf.getFunction(FunctionType.NEGATE, inputs[0]);
                }
                if (inputs[0] instanceof Number) {
                    if (_1 == inputs[0]) return sf.getFunction(FunctionType.INVERT, inputs[1]);
                    if (_0 == inputs[0]) return _0;
                    if (_1_ == inputs[0]) return sf.getFunction(FunctionType.NEGATE, sf.getFunction(FunctionType.INVERT, inputs[1]));
                }
                break;
            case NEGATE:
                if (inputs[0] instanceof Number) return sf.add(negate((Number) inputs[0]));
                break;
            case INVERT:
                if (inputs[0] instanceof Number) return sf.add(invert((Number) inputs[0]));
                break;
            case LOGICAL_EQUAL:
                if (inputs[0] instanceof Number && inputs[1] instanceof Number) return inputs[0] == inputs[1] ? _1 : _0;
                if (inputs[0] == inputs[1]) return _1;
                break;
            case CONDITIONAL:
                if (_0 == inputs[0]) return inputs[2];
                if (_1 == inputs[0]) return inputs[1];
                break;
            default:
                break;
        }
        return newInputs != null ? sf.getFunction(type, inputs) : f;
    }

    private static Number negate(Number number) {
        return new Number(number.getNominator().negate(), number.getDenominator());
    }

    /**
	 * Returns a new number which is the multiplicative inverse.
	 * 
	 * @return A new number which is the multiplicative inverse
	 * @throws DivisionByZeroException
	 */
    private static Number invert(Number number) throws DivisionByZeroException {
        return new Number(number.getDenominator(), number.getNominator());
    }

    private static Number add(Number n1, Number n2) {
        if (n1.getDenominator().equals(n2.getDenominator())) return new Number(n1.getNominator().add(n2.getNominator()), n1.getDenominator()); else return new Number(n1.getNominator().multiply(n2.getDenominator()).add(n2.getNominator().multiply(n1.getDenominator())), n1.getDenominator().multiply(n2.getDenominator()));
    }

    private static Number multiply(Number n1, Number n2) {
        return new Number(n1.getNominator().multiply(n2.getNominator()), n1.getDenominator().multiply(n2.getDenominator()));
    }

    private static Number subtract(Number n1, Number n2) {
        return add(n1, negate(n2));
    }

    private static Number divide(Number n1, Number n2) throws DivisionByZeroException {
        return multiply(n1, invert(n2));
    }

    /**
	 * Replaces the scalar with the specified index with the specified scalar.
	 * If both nodes are equal, no new instances are generated. In the other
	 * case, all {@link Function}s above the replaced node must be cloned.
	 * 
	 * @param  idx
	 * @param  scalar must be created with the specified {@link ScalarFactory}
	 * @return
	 */
    public static final Scalar replaceScalar(ScalarFactory sf, Scalar scalar, int idx, Scalar replace) {
        if (idx < 0) throw new IllegalArgumentException();
        if (idx == 0) return replace;
        if (scalar.isAtomic()) return scalar;
        idx--;
        Function f = (Function) scalar;
        final int inputsLength = f.getInputCount();
        Scalar[] inputs = f.getInputs();
        for (int i = 0; i < inputsLength; i++) {
            int is = inputs[i].getSize();
            if (idx < is) {
                Scalar replaced = replaceScalar(sf, inputs[i], idx, replace);
                if (replaced != inputs[i]) {
                    Scalar[] _inputs = new Scalar[inputsLength];
                    for (int j = 0; j < inputsLength; j++) _inputs[j] = i == j ? replaced : inputs[j];
                    return sf.getFunction(f.getFunctionType(), _inputs);
                }
                return scalar;
            }
            idx -= is;
        }
        throw new RuntimeException("implementation fault");
    }

    public static final int findScalar(Scalar scalar, Scalar find) {
        return -1;
    }
}
