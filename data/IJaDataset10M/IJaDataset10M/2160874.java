package net.entropysoft.transmorph.converters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.math.BigDecimal;
import java.math.BigInteger;
import net.entropysoft.transmorph.ConverterException;
import net.entropysoft.transmorph.DefaultConverters;
import net.entropysoft.transmorph.Transmorph;
import org.junit.Test;

public class NumberToNumberTest {

    @Test
    public void testNumberPrimitiveToNumberPrimitive() throws Exception {
        DefaultConverters defaultConverters = new DefaultConverters();
        Transmorph converter = new Transmorph(defaultConverters);
        int myInt = 55;
        long myLong = converter.convert(myInt, Long.TYPE);
        assertEquals(55, myLong);
        try {
            Long myLong2 = converter.convert(null, Long.TYPE);
            fail("Should not have been able to convert");
        } catch (ConverterException e) {
        }
        defaultConverters.getNumberToNumber().setCheckOutOfRange(false);
        assertEquals((byte) -126, (byte) converter.convert(130, Byte.TYPE));
        defaultConverters.getNumberToNumber().setCheckOutOfRange(true);
        try {
            converter.convert(130, Byte.TYPE);
            fail("Should not have been able to convert");
        } catch (ConverterException e) {
        }
    }

    @Test
    public void testNumberWrapperToNumberWrapper() throws Exception {
        Transmorph converter = new Transmorph(new DefaultConverters());
        Long myLongWrapper = converter.convert(new Integer(44), Long.class);
        assertEquals(Long.valueOf(44), myLongWrapper);
        myLongWrapper = converter.convert(null, Long.class);
        assertEquals(null, myLongWrapper);
    }

    @Test
    public void testNumberPrimitiveToNumberWrapper() throws Exception {
        Transmorph converter = new Transmorph(new DefaultConverters());
        assertEquals(new Long(55), converter.convert(55, Long.class));
        assertEquals(BigInteger.valueOf(55), converter.convert(55, BigInteger.class));
    }

    @Test
    public void testNullReplacementForPrimitive() throws Exception {
        DefaultConverters defaultConverters = new DefaultConverters();
        NumberToNumber numberToNumber = defaultConverters.getNumberToNumber();
        numberToNumber.setNullReplacementForPrimitive(0);
        Transmorph converter = new Transmorph(defaultConverters);
        long[] longsArray = converter.convert(new Integer[] { 1, 2, null, 4 }, long[].class);
        assertEquals(1, longsArray[0]);
        assertEquals(2, longsArray[1]);
        assertEquals(0, longsArray[2]);
        assertEquals(4, longsArray[3]);
    }

    @Test
    public void testToBigDecimal() throws Exception {
        DefaultConverters defaultConverters = new DefaultConverters();
        Transmorph converter = new Transmorph(defaultConverters);
        BigDecimal number = converter.convert(150, BigDecimal.class);
        assertEquals(BigDecimal.valueOf(150), number);
        number = converter.convert(1.0 / 3.0, BigDecimal.class);
        assertEquals(BigDecimal.valueOf(1.0 / 3.0), number);
    }
}
