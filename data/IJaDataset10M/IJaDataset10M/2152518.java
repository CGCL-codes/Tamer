package astcentric.structure.basic.property;

import java.math.BigInteger;

/**
 * Factory for {@link NumberProperty NumberProperties}. Based on the
 * concrete type (i.e. subclass of <code>Number</code>) one of the
 * concrete {@link Property} class implementing {@link NumberProperty} is 
 * returned:
 * <table border="1" cellspacing="0" cellpadding="5">
 * <tr><th>Property</th><th>Type of input</th></tr>
 * <tr><td>{@link IntegerProperty}</td><td>Integer, Short, Byte</td></tr>
 * <tr><td>{@link LongProperty}</td><td>Long, BigInteger</td></tr>
 * <tr><td>{@link FloatProperty}</td><td>Float</td></tr>
 * <tr><td>{@link DoubleProperty}</td><td>Double, BigDecimal, and all other
 * subclasses of Number</td></tr>
 * </table>
 *
 */
public class NumberPropertyFactory {

    /**
   * Creates a new instance from the specified number.
   */
    public static NumberProperty create(Number number) {
        if (number instanceof Integer || number instanceof Short || number instanceof Byte) {
            return new IntegerProperty(number.intValue());
        }
        if (number instanceof Long || number instanceof BigInteger) {
            return new LongProperty(number.longValue());
        }
        if (number instanceof Float) {
            return new FloatProperty(number.floatValue());
        }
        return new DoubleProperty(number.doubleValue());
    }
}
