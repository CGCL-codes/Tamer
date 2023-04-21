package org.jmathematics.numeric;

import static org.junit.Assert.assertEquals;
import java.math.BigInteger;
import org.jmathematics.Number;
import org.jmathematics.Operations;
import org.jmathematics.impl.NaturalNumberImpl;
import org.jmathematics.number.Numbers;
import org.jmathematics.operation.Addition;
import org.jmathematics.sets.NumberSet;
import org.junit.Test;

public class NaturalNumberTest {

    private void numericResult(String test, Number result, Class<?> resultClass, NumberSet resultSet, String resultToString) {
        assertEquals(test + " - class", true, resultClass.isAssignableFrom(result.getClass()));
        assertEquals(test + " - numericSet", resultSet, result.getNumberSet());
        assertEquals(test + " - value", resultToString, String.valueOf(result));
    }

    private void numericResult(String test, Number result, String resultToString) {
        assertEquals(test + " - class", NaturalNumberImpl.class, result.getClass());
        assertEquals(test + " - numericSet", NumberSet.NATURAL, result.getNumberSet());
        assertEquals(test + " - value", resultToString, String.valueOf(result));
    }

    @Test
    public void testCache1() {
        assertEquals(true, NaturalNumberImpl.ONE == Numbers.valueOf(BigInteger.ONE));
    }

    @Test
    public void testCache10() {
        assertEquals(true, NaturalNumberImpl.TEN == Numbers.valueOf(BigInteger.TEN));
    }

    @Test
    public void testCache17() {
        assertEquals(true, Numbers.valueOf(new BigInteger("17")) == Numbers.valueOf(BigInteger.valueOf(17L)));
    }
}
