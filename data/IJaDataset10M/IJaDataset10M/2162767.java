package ch.olsen.routes.data;

import ch.olsen.routes.data.DoubleDataElementImpl.DoubleDataType;
import ch.olsen.routes.data.IntegerDataElementImpl.IntegerDataType;
import ch.olsen.routes.data.LongDataElementImpl.LongDataType;
import ch.olsen.routes.data.VoidDataElement.VoidDataType;

/**
 * Integer Data Element
 * Automatic cast to Integer, Long, Double
 * @author vito
 *
 */
public class BooleanDataElementImpl extends DataElementAbstr implements BooleanDataElement, IntegerDataElement, LongDataElement, DoubleDataElement, StringDataElement {

    private static final long serialVersionUID = 1L;

    public static final String BOOLEANID = "boolean";

    boolean b;

    public BooleanDataElementImpl(boolean b) {
        this.b = b;
    }

    public DataType getType() {
        return BooleanDataElement.booleanDataType;
    }

    public boolean booleanValue() {
        return b;
    }

    public int intValue() {
        return b ? 1 : 0;
    }

    public final void update(int value) {
        b = value != 0;
    }

    public final void update(long value) {
        b = value != 0;
    }

    public final void update(double value) {
        b = value != 0.0;
    }

    public final void update(boolean value) {
        b = value;
    }

    public void update(String newvalue) {
        this.b = newvalue.equals("true") || newvalue.equals("TRUE") ? true : false;
    }

    public long longValue() {
        return b ? 1 : 0;
    }

    public double doubleValue() {
        return b ? 1.0 : 0.0;
    }

    public String stringValue() {
        return Boolean.toString(b);
    }

    /**
	 * Automatic cast
	 */
    @Override
    public BooleanDataElement toBooleanDE() throws ClassCastException {
        return this;
    }

    /**
	 * Automatic cast
	 */
    @Override
    public IntegerDataElement toIntegerDE() throws ClassCastException {
        return this;
    }

    /**
	 * Automatic cast
	 */
    @Override
    public LongDataElement toLongDE() throws ClassCastException {
        return this;
    }

    /**
	 * Automatic cast
	 */
    @Override
    public DoubleDataElement toDoubleDE() throws ClassCastException {
        return this;
    }

    /**
	 * Automatic cast
	 */
    @Override
    public StringDataElement toStringDE() throws ClassCastException {
        return this;
    }

    public static class BooleanDataType implements DataType {

        private static final long serialVersionUID = 1L;

        public boolean isCompatible(DataType other) {
            if (other instanceof VoidDataType) return true;
            return other instanceof DoubleDataType || other instanceof LongDataType || other instanceof IntegerDataType || other instanceof BooleanDataType;
        }

        @Override
        public String toString() {
            return "Boolean";
        }
    }

    @Override
    public String toString() {
        return BOOLEANID + ":" + (b ? "true" : "false");
    }

    public final PrimitiveType getPrimiteType() {
        return PrimitiveType.BOOLEAN;
    }
}
