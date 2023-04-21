package de.ufinke.cubaja.config.basic;

import org.junit.*;
import de.ufinke.cubaja.config.Configurator;
import de.ufinke.cubaja.config.FileResourceLoader;
import static org.junit.Assert.*;
import java.util.*;
import java.math.*;

public class ConfiguratorTest {

    @Test
    public void basicTest() throws Exception {
        Configurator configurator = new Configurator();
        configurator.setResourceLoader(new FileResourceLoader("test/de/ufinke/cubaja/config/basic"));
        configurator.setName("basic_config");
        TestConfig config = configurator.configure(new TestConfig());
        assertEquals(TestEnum.A, config.getEnumValue());
        assertEquals(101, config.getByteValue());
        assertEquals(102, config.getShortValue());
        assertEquals(103, config.getIntValue());
        assertEquals(104, config.getLongValue());
        assertEquals(1.23, config.getFloatValue(), 0.001);
        assertEquals(4.56, config.getDoubleValue(), 0.001);
        assertEquals('x', config.getCharValue());
        assertEquals(true, config.isBooleanValue());
        assertEquals(Byte.valueOf((byte) -101), config.getByteObjectValue());
        assertEquals(Short.valueOf((short) -102), config.getShortObjectValue());
        assertEquals(Integer.valueOf(-103), config.getIntObjectValue());
        assertEquals(Long.valueOf(-104), config.getLongObjectValue());
        assertEquals(Float.valueOf((float) -1.23), config.getFloatObjectValue(), 0.001);
        assertEquals(Double.valueOf(-4.56), config.getDoubleObjectValue(), 0.001);
        assertEquals(Character.valueOf('x'), config.getCharObjectValue());
        assertEquals(Boolean.FALSE, config.getBooleanObjectValue());
        assertEquals("hello", config.getStringValue());
        assertEquals(getDate(2010, 4, 3), config.getDateValue());
        assertEquals(String.class, config.getClassValue());
        assertEquals("foo", config.getInterfaceValue().getFoo());
        assertEquals(new BigInteger("12345"), config.getBigIntegerValue());
        assertEquals(new BigDecimal("-12345.67"), config.getBigDecimalValue());
        TestSubConfig sub = config.getSub();
        int[] numbers = sub.getNumbers();
        assertEquals(2, numbers.length);
        assertEquals(1, numbers[0]);
        assertEquals(2, numbers[1]);
    }

    private Date getDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, day);
        return cal.getTime();
    }
}
