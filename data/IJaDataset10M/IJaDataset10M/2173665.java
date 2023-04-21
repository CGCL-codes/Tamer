package tm.clc.ast;

import tm.utilities.Assert;

public class Arithmetic {

    public static final int NEGATE = 0, DONOTHING = 1, BOOLEAN_NOT = 2, BITWISE_NOT = 3, MULTIPLY = 4, DIVIDE = 5, REMAINDER = 6, ADD = 7, SUBTRACT = 8, SHIFT_LEFT = 9, SHIFT_RIGHT_ZERO_FILL = 10, SHIFT_RIGHT_SIGN_FILL = 11, LESS = 12, GREATER = 13, LESS_OR_EQUAL = 14, GREATER_OR_EQUAL = 15, EQUAL = 16, NOT_EQUAL = 17, BITWISE_AND = 18, BITWISE_XOR = 19, BITWISE_OR = 20, ADD_POINTER_INT = 21, SUBTRACT_POINTER_INT = 22, SUBTRACT_POINTER_POINTER = 23, BOOLEAN_AND = 24, BOOLEAN_OR = 25, ENTRIES = BOOLEAN_OR + 1;

    public static long do_arith(int numberOfBytes, int operator, long x, long y) {
        long r;
        switch(operator) {
            case MULTIPLY:
                r = x * y;
                break;
            case DIVIDE:
                if (y == 0) r = -99; else r = x / y;
                break;
            case REMAINDER:
                if (y == 0) r = -99; else r = x % y;
                break;
            case ADD:
                r = x + y;
                break;
            case SUBTRACT:
                r = x - y;
                break;
            case SHIFT_LEFT:
                r = x << y;
                break;
            case SHIFT_RIGHT_ZERO_FILL:
                long mask = ~(-1L << (8 * numberOfBytes));
                x = x & mask;
                r = x >>> y;
                break;
            case SHIFT_RIGHT_SIGN_FILL:
                r = x >> y;
                break;
            case LESS:
                r = (x < y) ? 1 : 0;
                break;
            case GREATER:
                r = (x > y) ? 1 : 0;
                break;
            case LESS_OR_EQUAL:
                r = (x <= y) ? 1 : 0;
                break;
            case GREATER_OR_EQUAL:
                r = (x >= y) ? 1 : 0;
                break;
            case EQUAL:
                r = (x == y) ? 1 : 0;
                break;
            case NOT_EQUAL:
                r = (x != y) ? 1 : 0;
                break;
            case BITWISE_AND:
                r = x & y;
                break;
            case BITWISE_XOR:
                r = x ^ y;
                break;
            case BITWISE_OR:
                r = x | y;
                break;
            default:
                Assert.check(false);
                r = 0;
        }
        return r;
    }

    public static long do_arith(int numberOfBytes, int operator, long x) {
        long r;
        switch(operator) {
            case NEGATE:
                r = -x;
                break;
            case DONOTHING:
                r = x;
                break;
            case BOOLEAN_NOT:
                if (x == 0) r = 1; else r = 0;
                break;
            case BITWISE_NOT:
                r = ~x;
                break;
            default:
                Assert.check(false);
                r = 0;
        }
        return r;
    }

    public static double do_arith(int operator, double x, double y) {
        double r;
        switch(operator) {
            case MULTIPLY:
                r = x * y;
                break;
            case DIVIDE:
                if (y == 0) r = -99.99; else r = x / y;
                break;
            case ADD:
                r = x + y;
                break;
            case SUBTRACT:
                r = x - y;
                break;
            default:
                Assert.check(false);
                r = 0;
        }
        return r;
    }

    public static long do_comparison(int operator, double x, double y) {
        long r;
        switch(operator) {
            case LESS:
                r = (x < y) ? 1 : 0;
                break;
            case GREATER:
                r = (x > y) ? 1 : 0;
                break;
            case LESS_OR_EQUAL:
                r = (x <= y) ? 1 : 0;
                break;
            case GREATER_OR_EQUAL:
                r = (x >= y) ? 1 : 0;
                break;
            case EQUAL:
                r = (x == y) ? 1 : 0;
                break;
            case NOT_EQUAL:
                r = (x != y) ? 1 : 0;
                break;
            default:
                Assert.check(false);
                r = 0;
        }
        return r;
    }

    public static double do_arith(int operator, double x) {
        double r;
        switch(operator) {
            case NEGATE:
                r = -x;
                break;
            case DONOTHING:
                r = x;
                break;
            default:
                Assert.check(false);
                r = 0;
        }
        return r;
    }

    public static int do_ptr_arith(int operator, int x, int y, int item_size) {
        int r;
        switch(operator) {
            case ADD_POINTER_INT:
                r = x + y * item_size;
                break;
            case SUBTRACT_POINTER_INT:
                r = x - y * item_size;
                break;
            case SUBTRACT_POINTER_POINTER:
                r = (x - y) / item_size;
                break;
            default:
                Assert.check(false);
                r = 0;
        }
        return r;
    }
}
