package deesel.lang.ext;

/**
 * Created by IntelliJ IDEA. User: neilellis Date: Mar 11, 2005 Time: 6:05:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class NumberExtender {

    private static NumberExtender instance = new NumberExtender();

    public static NumberExtender getInstance() {
        return instance;
    }

    public int operator_plus(Number number, int value) {
        return number.intValue() + value;
    }

    public int operator_plus(Number number, short value) {
        return number.shortValue() + value;
    }

    public int operator_plus(Number number, byte value) {
        return number.byteValue() + value;
    }

    public long operator_plus(Number number, long value) {
        return number.longValue() + value;
    }

    public float operator_plus(Number number, float value) {
        return number.floatValue() + value;
    }

    public double operator_plus(Number number, double value) {
        return number.intValue() + value;
    }

    public int operator_minus(Number number, int value) {
        return number.intValue() - value;
    }

    public int operator_minus(Number number, short value) {
        return number.shortValue() - value;
    }

    public int operator_minus(Number number, byte value) {
        return number.byteValue() - value;
    }

    public long operator_minus(Number number, long value) {
        return number.longValue() - value;
    }

    public float operator_minus(Number number, float value) {
        return number.floatValue() - value;
    }

    public double operator_minus(Number number, double value) {
        return number.intValue() - value;
    }

    public int operator_star(Number number, int value) {
        return number.intValue() * value;
    }

    public int operator_star(Number number, short value) {
        return number.shortValue() * value;
    }

    public int operator_star(Number number, byte value) {
        return number.byteValue() * value;
    }

    public long operator_star(Number number, long value) {
        return number.longValue() * value;
    }

    public float operator_star(Number number, float value) {
        return number.floatValue() * value;
    }

    public double operator_star(Number number, double value) {
        return number.intValue() * value;
    }

    public int operator_percent(Number number, int value) {
        return number.intValue() % value;
    }

    public int operator_percent(Number number, short value) {
        return number.shortValue() % value;
    }

    public int operator_percent(Number number, byte value) {
        return number.byteValue() % value;
    }

    public long operator_percent(Number number, long value) {
        return number.longValue() % value;
    }

    public float operator_percent(Number number, float value) {
        return number.floatValue() % value;
    }

    public double operator_percent(Number number, double value) {
        return number.intValue() % value;
    }

    public int operator_slash(Number number, int value) {
        return number.intValue() / value;
    }

    public int operator_slash(Number number, short value) {
        return number.shortValue() / value;
    }

    public int operator_slash(Number number, byte value) {
        return number.byteValue() / value;
    }

    public long operator_slash(Number number, long value) {
        return number.longValue() / value;
    }

    public float operator_slash(Number number, float value) {
        return number.floatValue() / value;
    }

    public double operator_slash(Number number, double value) {
        return number.intValue() / value;
    }

    public boolean operator_lessThan(Number number, int value) {
        return number.intValue() < value;
    }

    public boolean operator_lessThan(Number number, short value) {
        return number.shortValue() < value;
    }

    public boolean operator_lessThan(Number number, byte value) {
        return number.byteValue() < value;
    }

    public boolean operator_lessThan(Number number, long value) {
        return number.longValue() < value;
    }

    public boolean operator_lessThan(Number number, float value) {
        return number.floatValue() < value;
    }

    public boolean operator_lessThan(Number number, double value) {
        return number.intValue() < value;
    }

    public boolean operator_greaterThan(Number number, int value) {
        return number.intValue() > value;
    }

    public boolean operator_greaterThan(Number number, short value) {
        return number.shortValue() > value;
    }

    public boolean operator_greaterThan(Number number, byte value) {
        return number.byteValue() > value;
    }

    public boolean operator_greaterThan(Number number, long value) {
        return number.longValue() > value;
    }

    public boolean operator_greaterThan(Number number, float value) {
        return number.floatValue() > value;
    }

    public boolean operator_greaterThan(Number number, double value) {
        return number.intValue() > value;
    }

    public boolean operator_lessThan_equal(Number number, int value) {
        return number.intValue() <= value;
    }

    public boolean operator_lessThan_equal(Number number, short value) {
        return number.shortValue() <= value;
    }

    public boolean operator_lessThan_equal(Number number, byte value) {
        return number.byteValue() <= value;
    }

    public boolean operator_lessThan_equal(Number number, long value) {
        return number.longValue() <= value;
    }

    public boolean operator_lessThan_equal(Number number, float value) {
        return number.floatValue() <= value;
    }

    public boolean operator_lessThan_equal(Number number, double value) {
        return number.intValue() <= value;
    }

    public boolean operator_greaterThan_equal(Number number, int value) {
        return number.intValue() >= value;
    }

    public boolean operator_greaterThan_equal(Number number, short value) {
        return number.shortValue() >= value;
    }

    public boolean operator_greaterThan_equal(Number number, byte value) {
        return number.byteValue() >= value;
    }

    public boolean operator_greaterThan_equal(Number number, long value) {
        return number.longValue() >= value;
    }

    public boolean operator_greaterThan_equal(Number number, float value) {
        return number.floatValue() >= value;
    }

    public boolean operator_greaterThan_equal(Number number, double value) {
        return number.intValue() >= value;
    }
}
