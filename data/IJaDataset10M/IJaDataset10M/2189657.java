package org.peaseplate.queryengine.command;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import org.peaseplate.utils.command.Command;
import org.testng.annotations.Test;

@Test(groups = "query-engine-unit-test")
public class DivideCommandTest extends AbstractNumericCommandTest {

    @Override
    protected String getSign() {
        return "/";
    }

    @Override
    protected Command createCommand(final Object left, final Object right) {
        return new DivideCommand(getTypeConversionService(), 1, 1, new MirrorCommand(left), new MirrorCommand(right));
    }

    @SuppressWarnings("boxing")
    @Override
    protected Number[][] createOperands() {
        return new Number[][] { { 42, -17 }, { -12345678, 78 } };
    }

    @Override
    @SuppressWarnings("boxing")
    protected void createData(final List<Object[]> data, final Number left, final Number right) {
        data.add(new Object[] { null, right.byteValue(), (byte) 0 });
        data.add(new Object[] { null, right.shortValue(), (short) 0 });
        data.add(new Object[] { null, right.intValue(), 0 });
        data.add(new Object[] { null, right.longValue(), (long) 0 });
        data.add(new Object[] { null, right.floatValue(), 0 / right.floatValue() });
        data.add(new Object[] { null, right.doubleValue(), 0 / right.doubleValue() });
        data.add(new Object[] { null, BigInteger.valueOf(right.longValue()), BigInteger.ZERO });
        data.add(new Object[] { null, BigDecimal.valueOf(right.doubleValue()), BigDecimal.ZERO });
        data.add(new Object[] { null, (char) right.intValue(), (char) 0 });
        data.add(new Object[] { left.byteValue(), right.byteValue(), (byte) (left.byteValue() / right.byteValue()) });
        data.add(new Object[] { left.byteValue(), right.shortValue(), (short) (left.byteValue() / right.shortValue()) });
        data.add(new Object[] { left.byteValue(), right.intValue(), left.byteValue() / right.intValue() });
        data.add(new Object[] { left.byteValue(), right.longValue(), left.byteValue() / right.longValue() });
        data.add(new Object[] { left.byteValue(), right.floatValue(), left.byteValue() / right.floatValue() });
        data.add(new Object[] { left.byteValue(), right.doubleValue(), left.byteValue() / right.doubleValue() });
        data.add(new Object[] { left.byteValue(), BigInteger.valueOf(right.longValue()), BigInteger.valueOf(left.byteValue()).divide(BigInteger.valueOf(right.longValue())) });
        data.add(new Object[] { left.byteValue(), (char) right.intValue(), (char) ((char) left.byteValue() / (char) right.intValue()) });
        data.add(new Object[] { left.shortValue(), right.byteValue(), (short) (left.shortValue() / right.byteValue()) });
        data.add(new Object[] { left.shortValue(), right.shortValue(), (short) (left.shortValue() / right.shortValue()) });
        data.add(new Object[] { left.shortValue(), right.intValue(), left.shortValue() / right.intValue() });
        data.add(new Object[] { left.shortValue(), right.longValue(), left.shortValue() / right.longValue() });
        data.add(new Object[] { left.shortValue(), right.floatValue(), left.shortValue() / right.floatValue() });
        data.add(new Object[] { left.shortValue(), right.doubleValue(), left.shortValue() / right.doubleValue() });
        data.add(new Object[] { left.shortValue(), BigInteger.valueOf(right.longValue()), BigInteger.valueOf(left.shortValue()).divide(BigInteger.valueOf(right.longValue())) });
        data.add(new Object[] { left.shortValue(), (char) right.intValue(), (char) ((char) left.shortValue() / (char) right.intValue()) });
        data.add(new Object[] { left.intValue(), right.byteValue(), left.intValue() / right.byteValue() });
        data.add(new Object[] { left.intValue(), right.shortValue(), left.intValue() / right.shortValue() });
        data.add(new Object[] { left.intValue(), right.intValue(), left.intValue() / right.intValue() });
        data.add(new Object[] { left.intValue(), right.longValue(), left.intValue() / right.longValue() });
        data.add(new Object[] { left.intValue(), right.floatValue(), left.intValue() / right.floatValue() });
        data.add(new Object[] { left.intValue(), right.doubleValue(), left.intValue() / right.doubleValue() });
        data.add(new Object[] { left.intValue(), BigInteger.valueOf(right.longValue()), BigInteger.valueOf(left.intValue()).divide(BigInteger.valueOf(right.longValue())) });
        data.add(new Object[] { left.intValue(), (char) right.intValue(), left.intValue() / (char) right.intValue() });
        data.add(new Object[] { left.longValue(), right.byteValue(), (long) (left.longValue() / right.byteValue()) });
        data.add(new Object[] { left.longValue(), right.shortValue(), (long) (left.longValue() / right.shortValue()) });
        data.add(new Object[] { left.longValue(), right.intValue(), (long) (left.longValue() / right.intValue()) });
        data.add(new Object[] { left.longValue(), right.longValue(), (long) (left.longValue() / right.longValue()) });
        data.add(new Object[] { left.longValue(), right.floatValue(), left.longValue() / right.floatValue() });
        data.add(new Object[] { left.longValue(), right.doubleValue(), left.longValue() / right.doubleValue() });
        data.add(new Object[] { left.longValue(), BigInteger.valueOf(right.longValue()), BigInteger.valueOf(left.longValue()).divide(BigInteger.valueOf(right.longValue())) });
        data.add(new Object[] { left.longValue(), (char) right.intValue(), (long) (left.longValue() / (char) right.intValue()) });
        data.add(new Object[] { left.floatValue(), right.byteValue(), (float) (left.floatValue() / right.byteValue()) });
        data.add(new Object[] { left.floatValue(), right.shortValue(), (float) (left.floatValue() / right.shortValue()) });
        data.add(new Object[] { left.floatValue(), right.intValue(), (float) (left.floatValue() / right.intValue()) });
        data.add(new Object[] { left.floatValue(), right.longValue(), (float) (left.floatValue() / right.longValue()) });
        data.add(new Object[] { left.floatValue(), right.floatValue(), (float) (left.floatValue() / right.floatValue()) });
        data.add(new Object[] { left.floatValue(), right.doubleValue(), left.floatValue() / right.doubleValue() });
        data.add(new Object[] { left.floatValue(), BigInteger.valueOf(right.longValue()), BigInteger.valueOf(left.longValue()).divide(BigInteger.valueOf(right.longValue())) });
        data.add(new Object[] { left.floatValue(), (char) right.intValue(), (float) (left.floatValue() / (char) right.intValue()) });
        data.add(new Object[] { left.doubleValue(), right.byteValue(), (double) (left.doubleValue() / right.byteValue()) });
        data.add(new Object[] { left.doubleValue(), right.shortValue(), (double) (left.doubleValue() / right.shortValue()) });
        data.add(new Object[] { left.doubleValue(), right.intValue(), (double) (left.doubleValue() / right.intValue()) });
        data.add(new Object[] { left.doubleValue(), right.longValue(), (double) (left.doubleValue() / right.longValue()) });
        data.add(new Object[] { left.doubleValue(), right.floatValue(), (double) (left.doubleValue() / right.floatValue()) });
        data.add(new Object[] { left.doubleValue(), right.doubleValue(), left.doubleValue() / right.doubleValue() });
        data.add(new Object[] { left.doubleValue(), BigInteger.valueOf(right.longValue()), BigInteger.valueOf(left.longValue()).divide(BigInteger.valueOf(right.longValue())) });
        data.add(new Object[] { left.doubleValue(), (char) right.intValue(), (double) (left.doubleValue() / (char) right.intValue()) });
        data.add(new Object[] { BigInteger.valueOf(left.longValue()), right.byteValue(), BigInteger.valueOf(left.longValue()).divide(BigInteger.valueOf(right.byteValue())) });
        data.add(new Object[] { BigInteger.valueOf(left.longValue()), right.shortValue(), BigInteger.valueOf(left.longValue()).divide(BigInteger.valueOf(right.shortValue())) });
        data.add(new Object[] { BigInteger.valueOf(left.longValue()), right.intValue(), BigInteger.valueOf(left.longValue()).divide(BigInteger.valueOf(right.intValue())) });
        data.add(new Object[] { BigInteger.valueOf(left.longValue()), right.longValue(), BigInteger.valueOf(left.longValue()).divide(BigInteger.valueOf(right.longValue())) });
        data.add(new Object[] { BigInteger.valueOf(left.longValue()), right.floatValue(), BigInteger.valueOf(left.longValue()).divide(BigInteger.valueOf((long) right.floatValue())) });
        data.add(new Object[] { BigInteger.valueOf(left.longValue()), right.doubleValue(), BigInteger.valueOf(left.longValue()).divide(BigInteger.valueOf((long) right.doubleValue())) });
        data.add(new Object[] { BigInteger.valueOf(left.longValue()), BigInteger.valueOf(right.longValue()), BigInteger.valueOf(left.longValue()).divide(BigInteger.valueOf(right.longValue())) });
        data.add(new Object[] { BigInteger.valueOf(left.longValue()), (char) right.intValue(), BigInteger.valueOf(left.longValue()).divide(BigInteger.valueOf((char) right.intValue())) });
        data.add(new Object[] { (char) left.intValue(), right.byteValue(), (char) ((char) left.intValue() / (char) right.byteValue()) });
        data.add(new Object[] { (char) left.intValue(), right.shortValue(), (char) ((char) left.intValue() / (char) right.shortValue()) });
        data.add(new Object[] { (char) left.intValue(), right.intValue(), (char) left.intValue() / right.intValue() });
        data.add(new Object[] { (char) left.intValue(), right.longValue(), (char) left.intValue() / right.longValue() });
        data.add(new Object[] { (char) left.intValue(), right.floatValue(), (char) left.intValue() / right.floatValue() });
        data.add(new Object[] { (char) left.intValue(), right.doubleValue(), (char) left.intValue() / right.doubleValue() });
        data.add(new Object[] { (char) left.intValue(), BigInteger.valueOf(right.longValue()), BigInteger.valueOf((char) left.intValue()).divide(BigInteger.valueOf(right.longValue())) });
        data.add(new Object[] { (char) left.intValue(), (char) right.intValue(), (char) ((char) left.intValue() / (char) right.intValue()) });
    }
}
