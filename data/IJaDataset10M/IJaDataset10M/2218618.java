package org.armedbear.lisp;

import static org.armedbear.lisp.Nil.NIL;
import static org.armedbear.lisp.Lisp.*;
import java.math.BigInteger;

public final class FloatFunctions extends LispFile {

    private static final Primitive SET_FLOATING_POINT_MODES = new Primitive("set-floating-point-modes", PACKAGE_EXT, true, "&key traps") {

        @Override
        public LispObject execute(LispObject[] args) throws ConditionThrowable {
            if (args.length % 2 != 0) error(new ProgramError("Odd number of keyword arguments."));
            for (int i = 0; i < args.length; i += 2) {
                LispObject key = checkSymbol(args[i]);
                LispObject value = args[i + 1];
                if (key == Keyword.TRAPS) {
                    boolean trap_overflow = false;
                    boolean trap_underflow = false;
                    while (value != NIL) {
                        LispObject car = value.CAR();
                        if (car == Keyword.OVERFLOW) trap_overflow = true; else if (car == Keyword.UNDERFLOW) trap_underflow = true; else error(new LispError("Unsupported floating point trap: " + car.writeToString()));
                        value = value.CDR();
                    }
                    TRAP_OVERFLOW = trap_overflow;
                    TRAP_UNDERFLOW = trap_underflow;
                } else error(new LispError("Unrecognized keyword: " + key.writeToString()));
            }
            return LispThread.currentThread().nothing();
        }
    };

    private static final Primitive GET_FLOATING_POINT_MODES = new Primitive("get-floating-point-modes", PACKAGE_EXT, true, "") {

        @Override
        public LispObject execute() throws ConditionThrowable {
            LispObject traps = NIL;
            if (TRAP_UNDERFLOW) traps = traps.push(Keyword.UNDERFLOW);
            if (TRAP_OVERFLOW) traps = traps.push(Keyword.OVERFLOW);
            return list(Keyword.TRAPS, traps);
        }
    };

    private static final Primitive INTEGER_DECODE_FLOAT = new Primitive("integer-decode-float", "float") {

        @Override
        public LispObject execute(LispObject arg) throws ConditionThrowable {
            if (arg instanceof SingleFloat) {
                int bits = Float.floatToRawIntBits(arg.floatValue());
                int s = ((bits >> 31) == 0) ? 1 : -1;
                int e = (int) ((bits >> 23) & 0xffL);
                int m;
                if (e == 0) m = (bits & 0x7fffff) << 1; else m = (bits & 0x7fffff) | 0x800000;
                LispObject significand = number(m);
                Fixnum exponent = Fixnum.makeFixnum(e - 150);
                Fixnum sign = Fixnum.makeFixnum(s);
                return LispThread.currentThread().setValues(significand, exponent, sign);
            }
            if (arg instanceof DoubleFloat) {
                long bits = Double.doubleToRawLongBits((double) arg.doubleValue());
                int s = ((bits >> 63) == 0) ? 1 : -1;
                int e = (int) ((bits >> 52) & 0x7ffL);
                long m;
                if (e == 0) m = (bits & 0xfffffffffffffL) << 1; else m = (bits & 0xfffffffffffffL) | 0x10000000000000L;
                LispObject significand = number(m);
                Fixnum exponent = Fixnum.makeFixnum(e - 1075);
                Fixnum sign = Fixnum.makeFixnum(s);
                return LispThread.currentThread().setValues(significand, exponent, sign);
            }
            return type_error(arg, SymbolConstants.FLOAT);
        }
    };

    private static final Primitive _FLOAT_BITS = new Primitive("%float-bits", PACKAGE_SYS, true, "integer") {

        @Override
        public LispObject execute(LispObject arg) throws ConditionThrowable {
            if (arg instanceof SingleFloat) {
                int bits = Float.floatToIntBits(arg.floatValue());
                BigInteger big = BigInteger.valueOf(bits >> 1);
                return Bignum.getInteger(big.shiftLeft(1).add(((bits & 1) == 1) ? BigInteger.ONE : BigInteger.ZERO));
            }
            if (arg instanceof DoubleFloat) {
                long bits = Double.doubleToLongBits(arg.doubleValue());
                BigInteger big = BigInteger.valueOf(bits >> 1);
                return Bignum.getInteger(big.shiftLeft(1).add(((bits & 1) == 1) ? BigInteger.ONE : BigInteger.ZERO));
            }
            return type_error(arg, SymbolConstants.FLOAT);
        }
    };

    private static final Primitive RATIONAL = new Primitive("rational", "number") {

        @Override
        public LispObject execute(LispObject arg) throws ConditionThrowable {
            if (arg.floatp()) return arg.rational();
            if (arg.rationalp()) return arg;
            return type_error(arg, SymbolConstants.REAL);
        }
    };

    private static final Primitive FLOAT_RADIX = new Primitive("float-radix", "float") {

        @Override
        public LispObject execute(LispObject arg) throws ConditionThrowable {
            if (arg instanceof SingleFloat || arg instanceof DoubleFloat) return Fixnum.TWO;
            return type_error(arg, SymbolConstants.FLOAT);
        }
    };

    static final Fixnum FIXNUM_24 = Fixnum.makeFixnum(24);

    static final Fixnum FIXNUM_53 = Fixnum.makeFixnum(53);

    private static final Primitive FLOAT_DIGITS = new Primitive("float-digits", "float") {

        @Override
        public LispObject execute(LispObject arg) throws ConditionThrowable {
            if (arg instanceof SingleFloat) return FIXNUM_24;
            if (arg instanceof DoubleFloat) return FIXNUM_53;
            return type_error(arg, SymbolConstants.FLOAT);
        }
    };

    private static final Primitive SCALE_FLOAT = new Primitive("scale-float", "float integer") {

        @Override
        public LispObject execute(LispObject first, LispObject second) throws ConditionThrowable {
            if (first instanceof SingleFloat) {
                float f = first.floatValue();
                int n = second.intValue();
                return NumericLispObject.createSingleFloat(f * (float) Math.pow(2, n));
            }
            if (first instanceof DoubleFloat) {
                double d = first.doubleValue();
                int n = second.intValue();
                return NumericLispObject.createDoubleFloat(d * Math.pow(2, n));
            }
            return type_error(first, SymbolConstants.FLOAT);
        }
    };

    private static final Primitive COERCE_TO_SINGLE_FLOAT = new Primitive("coerce-to-single-float", PACKAGE_SYS, false) {

        @Override
        public LispObject execute(LispObject arg) throws ConditionThrowable {
            return NumericLispObject.coerceToSingleFloat(arg);
        }
    };

    private static final Primitive COERCE_TO_DOUBLE_FLOAT = new Primitive("coerce-to-double-float", PACKAGE_SYS, false) {

        @Override
        public LispObject execute(LispObject arg) throws ConditionThrowable {
            return NumericLispObject.coerceToDoubleFloat(arg);
        }
    };

    private static final Primitive FLOAT = new Primitive("float", "number &optional prototype") {

        @Override
        public LispObject execute(LispObject arg) throws ConditionThrowable {
            if (arg instanceof SingleFloat || arg instanceof DoubleFloat) return arg;
            return NumericLispObject.coerceToSingleFloat(arg);
        }

        @Override
        public LispObject execute(LispObject first, LispObject second) throws ConditionThrowable {
            if (second instanceof SingleFloat) return NumericLispObject.coerceToSingleFloat(first);
            if (second instanceof DoubleFloat) return NumericLispObject.coerceToDoubleFloat(first);
            return type_error(second, SymbolConstants.FLOAT);
        }
    };

    private static final Primitive FLOATP = new Primitive("floatp", "object") {

        @Override
        public LispObject execute(LispObject arg) throws ConditionThrowable {
            if (arg instanceof SingleFloat) return T;
            if (arg instanceof DoubleFloat) return T;
            return NIL;
        }
    };

    private static final Primitive SINGLE_FLOAT_BITS = new Primitive("single-float-bits", PACKAGE_SYS, true, "float") {

        @Override
        public LispObject execute(LispObject arg) throws ConditionThrowable {
            if (arg instanceof SingleFloat) {
                return Fixnum.makeFixnum(Float.floatToIntBits(arg.floatValue()));
            }
            return type_error(arg, SymbolConstants.FLOAT);
        }
    };

    private static final Primitive DOUBLE_FLOAT_HIGH_BITS = new Primitive("double-float-high-bits", PACKAGE_SYS, true, "float") {

        @Override
        public LispObject execute(LispObject arg) throws ConditionThrowable {
            if (arg instanceof DoubleFloat) {
                return number(Double.doubleToLongBits(arg.doubleValue()) >>> 32);
            }
            return type_error(arg, SymbolConstants.DOUBLE_FLOAT);
        }
    };

    private static final Primitive DOUBLE_FLOAT_LOW_BITS = new Primitive("double-float-low-bits", PACKAGE_SYS, true, "float") {

        @Override
        public LispObject execute(LispObject arg) throws ConditionThrowable {
            if (arg instanceof DoubleFloat) {
                return number(Double.doubleToLongBits(arg.doubleValue()) & 0xffffffffL);
            }
            return type_error(arg, SymbolConstants.DOUBLE_FLOAT);
        }
    };

    private static final Primitive MAKE_SINGLE_FLOAT = new Primitive("make-single-float", PACKAGE_SYS, true, "bits") {

        @Override
        public LispObject execute(LispObject arg) throws ConditionThrowable {
            if (arg instanceof Fixnum) {
                int bits = arg.intValue();
                return NumericLispObject.createSingleFloat(Float.intBitsToFloat(bits));
            }
            if (arg instanceof Bignum) {
                long bits = arg.bigIntegerValue().longValue();
                return NumericLispObject.createSingleFloat(Float.intBitsToFloat((int) bits));
            }
            return type_error(arg, SymbolConstants.INTEGER);
        }
    };

    private static final Primitive MAKE_DOUBLE_FLOAT = new Primitive("make-double-float", PACKAGE_SYS, true, "bits") {

        @Override
        public LispObject execute(LispObject arg) throws ConditionThrowable {
            if (arg instanceof Fixnum) {
                long bits = (long) arg.intValue();
                return NumericLispObject.createDoubleFloat(Double.longBitsToDouble(bits));
            }
            if (arg instanceof Bignum) {
                long bits = arg.bigIntegerValue().longValue();
                return NumericLispObject.createDoubleFloat(Double.longBitsToDouble(bits));
            }
            return type_error(arg, SymbolConstants.INTEGER);
        }
    };

    private static final Primitive FLOAT_INFINITY_P = new Primitive("float-infinity-p", PACKAGE_SYS, true) {

        @Override
        public LispObject execute(LispObject arg) throws ConditionThrowable {
            if (arg instanceof SingleFloat) return Float.isInfinite(arg.floatValue()) ? T : NIL;
            if (arg instanceof DoubleFloat) return Double.isInfinite(arg.doubleValue()) ? T : NIL;
            return type_error(arg, SymbolConstants.FLOAT);
        }
    };

    private static final Primitive FLOAT_NAN_P = new Primitive("float-nan-p", PACKAGE_SYS, true) {

        @Override
        public LispObject execute(LispObject arg) throws ConditionThrowable {
            if (arg instanceof SingleFloat) return Float.isNaN(arg.floatValue()) ? T : NIL;
            if (arg instanceof DoubleFloat) return Double.isNaN(arg.doubleValue()) ? T : NIL;
            return type_error(arg, SymbolConstants.FLOAT);
        }
    };

    private static final Primitive FLOAT_STRING = new Primitive("float-string", PACKAGE_SYS, true) {

        @Override
        public LispObject execute(LispObject arg) throws ConditionThrowable {
            final String s1;
            if (arg instanceof SingleFloat) s1 = String.valueOf(arg.floatValue()); else if (arg instanceof DoubleFloat) s1 = String.valueOf(arg.doubleValue()); else return type_error(arg, SymbolConstants.FLOAT);
            int i = s1.indexOf('E');
            if (i < 0) return new SimpleString(s1);
            String s2 = s1.substring(0, i);
            int exponent = Integer.parseInt(s1.substring(i + 1));
            if (exponent == 0) return new SimpleString(s2);
            int index = s2.indexOf('.');
            if (index < 0) return new SimpleString(s2);
            StringBuffer sb = new StringBuffer(s2);
            if (index >= 0) sb.deleteCharAt(index);
            if (exponent > 0) {
                int newIndex = index + exponent;
                if (newIndex < sb.length()) sb.insert(newIndex, '.'); else if (newIndex == sb.length()) sb.append('.'); else {
                    while (newIndex > sb.length()) sb.append('0');
                    sb.append('.');
                }
            } else {
                Debug.assertTrue(exponent < 0);
                int newIndex = index + exponent;
                while (newIndex < 0) {
                    sb.insert(0, '0');
                    ++newIndex;
                }
                sb.insert(0, '.');
            }
            return new SimpleString(sb.toString());
        }
    };
}
