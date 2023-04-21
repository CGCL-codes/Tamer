package au.edu.archer.metadata.mde.validator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;

public class BooleanValue extends AbstractValue {

    private final boolean value;

    public static final BooleanValue FALSE = new BooleanValue(false);

    public static final BooleanValue TRUE = new BooleanValue(true);

    private BooleanValue(boolean b) {
        this.value = b;
    }

    @Override
    public boolean isTrue() {
        return value;
    }

    @Override
    public DecimalValue asDecimalValue() {
        return new DecimalValue(value ? BigDecimal.ONE : BigDecimal.ZERO);
    }

    @Override
    public IntegerValue asIntegerValue() {
        return new IntegerValue(value ? BigInteger.ONE : BigInteger.ZERO);
    }

    @Override
    public DateValue asDateValue() throws ValueConversionException {
        throw new ValueConversionException("Cannot convert a 'boolean' to a 'date'");
    }

    @Override
    public StringValue asStringValue() {
        return new StringValue(value ? "true" : "");
    }

    public static BooleanValue toBooleanValue(boolean b) {
        return b ? TRUE : FALSE;
    }

    @Override
    public int getKind() {
        return BOOLEAN_KIND;
    }

    public boolean valueOf() {
        return value;
    }

    @Override
    public String toString() {
        return "BooleanValue{" + value + "}";
    }

    @Override
    public CollectionValue asCollectionValue() throws ValueConversionException {
        return new CollectionValue(Collections.singletonList((AbstractValue) this));
    }
}
