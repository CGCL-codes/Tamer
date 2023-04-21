package org.t2framework.commons.util;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import junit.framework.TestCase;

public class ConverterUtilTest extends TestCase {

    public void testStringConvert1() throws Exception {
        String convert = ConverterUtil.convert("aaaa", String.class);
        assertEquals("aaaa", convert);
    }

    public void testStringConvert2() throws Exception {
        Calendar c = Calendar.getInstance();
        c.set(2008, 4, 5);
        String convert = ConverterUtil.convert(c.getTime(), String.class, "yyyy/MM/dd");
        assertEquals("2008/05/05", convert);
        assertEquals("2008/05/05", ConverterUtil.convertAsString(c.getTime(), "yyyy/MM/dd"));
    }

    public void testAtomicInteger() throws Exception {
        AtomicInteger convert = ConverterUtil.convert("123", AtomicInteger.class);
        assertEquals(123, convert.get());
    }

    public void testAtomicLong() throws Exception {
        AtomicLong convert = ConverterUtil.convert("123", AtomicLong.class);
        assertEquals(123, convert.get());
    }

    public void testBigDecimalConvert1() throws Exception {
        BigDecimal convert = ConverterUtil.convert(new Integer(123), BigDecimal.class);
        assertNotNull(convert);
        assertEquals(new BigDecimal("123"), convert);
    }

    public void testBigDecimalConvert2() throws Exception {
        Object convert = ConverterUtil.convertAsBigDecimal("0.00000001");
        assertNotNull(convert);
        assertEquals(new BigDecimal("0.00000001"), convert);
    }

    public void testBigDecimalConvert3() throws Exception {
        Calendar c = Calendar.getInstance();
        c.set(2008, 4, 5);
        BigDecimal convert = ConverterUtil.convert(c.getTime(), BigDecimal.class, "yyyyMMdd");
        assertNotNull(convert);
        assertEquals(new BigDecimal("20080505"), convert);
    }

    public void testConvert() throws Exception {
        assertEquals(null, ConverterUtil.convertAsBigDecimal(""));
        assertEquals(new BigDecimal(1), ConverterUtil.convertAsBigDecimal(new Integer(1)));
        assertEquals(new BigDecimal(1.1), ConverterUtil.convertAsBigDecimal(new Double(1.1)));
        assertEquals(new BigDecimal(0.00000000123), ConverterUtil.convertAsBigDecimal(new Double(0.00000000123D)));
        assertEquals(new BigDecimal(100000), ConverterUtil.convertAsBigDecimal(new Long(100000L)));
        assertEquals(new BigDecimal(123), ConverterUtil.convertAsBigDecimal(new Byte((byte) 123)));
        assertEquals(new BigDecimal("0.000000123"), ConverterUtil.convertAsBigDecimal("1.23E-7"));
        assertEquals(new BigDecimal("1.23E-7"), ConverterUtil.convertAsBigDecimal("0.000000123"));
        assertEquals(new BigDecimal(123), ConverterUtil.convertAsBigDecimal(new Float(123f)));
        assertEquals(new BigDecimal("123223.333"), ConverterUtil.convertAsBigDecimal("$123,223.333"));
    }

    public void testPrimitiveIntegerConverter1() throws Exception {
        Integer ret = ConverterUtil.convertAsPrimitiveInteger(null);
        assertNotNull(ret);
        assertTrue(ret.intValue() == 0);
    }

    public void testIntegerConverter1() throws Exception {
        Integer ret = ConverterUtil.convertAsInteger(null);
        assertNull(ret);
    }

    public void testByteConvert1() throws Exception {
        Byte b = ConverterUtil.convertAsByte(new Integer(123));
        assertNotNull(b);
        assertEquals(new Byte("123"), b);
    }

    public void testByteConvert2() throws Exception {
        Byte b = ConverterUtil.convertAsByte("1,2,3");
        assertNotNull(b);
        assertEquals(new Byte("123"), b);
    }

    public void testConvertBoolean() throws Exception {
        assertEquals(Boolean.TRUE, ConverterUtil.convertAsBoolean(true));
        assertEquals(Boolean.FALSE, ConverterUtil.convertAsBoolean(Boolean.FALSE));
        assertEquals(null, ConverterUtil.convertAsBoolean(null));
        assertEquals(Boolean.TRUE, ConverterUtil.convertAsBoolean("true"));
        assertEquals(Boolean.TRUE, ConverterUtil.convertAsBoolean("Yes"));
        assertEquals(Boolean.FALSE, ConverterUtil.convertAsBoolean(0));
        assertEquals(Boolean.TRUE, ConverterUtil.convertAsBoolean(1));
    }

    public void testFindPattern() throws Exception {
        String p = DateUtil.findPattern(Locale.JAPAN);
        assertNotNull(p);
        assertEquals("yyyy/MM/dd", p);
        p = DateUtil.findPattern(Locale.US);
        assertNotNull(p);
        assertEquals("MM/dd/yyyy", p);
    }

    public void testGetSimpleDateFormat() throws Exception {
        SimpleDateFormat s = DateUtil.getSimpleDateFormat("yyyy-MM-dd", Locale.JAPAN);
        assertNotNull(s);
        assertEquals("yyyy-MM-dd", s.toPattern());
        SimpleDateFormat s2 = DateUtil.getSimpleDateFormat(null, Locale.JAPAN);
        assertNotNull(s2);
        assertEquals("yyyy/MM/dd", s2.toPattern());
    }

    public void testConvertDate1() throws Exception {
        Date date = ConverterUtil.convertAsDate("2001-11-11", "yyyy-MM-dd");
        assertNotNull(date);
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String format = f.format(date);
        assertEquals("2001-11-11", format);
    }

    public void testConvertDate2() throws Exception {
        Date d = new Date();
        Date date = ConverterUtil.convertAsDate(d, "yyyy-MM-dd");
        assertNotNull(date);
        assertEquals(d, date);
    }

    public void testConvert_url() throws Exception {
        String s = "file://" + ResourceUtil.getResourcePath(this.getClass());
        URL convert = ConverterUtil.convert(s, URL.class);
        assertNotNull(convert);
        assertEquals(s, convert.toExternalForm());
    }

    public void testConvert_urlFromString() throws Exception {
        String s = "file://" + ResourceUtil.getResourcePath(this.getClass());
        URL url = new URL(s);
        String convert = ConverterUtil.convert(url, String.class);
        assertNotNull(convert);
        assertEquals(s, convert);
    }

    public void testIntpuStreamConverter1() throws Exception {
        assertNull(ConverterUtil.INPUTSTREAM_CONVERTER.convert(null));
        DataInputStream is = new DataInputStream(null);
        assertEquals(is, ConverterUtil.INPUTSTREAM_CONVERTER.convert(is));
        File file = ResourceUtil.getResourceAsFile("logback.xml");
        FileInputStream fs = null;
        try {
            fs = (FileInputStream) ConverterUtil.INPUTSTREAM_CONVERTER.convert(file);
            assertNotNull(fs);
        } finally {
            CloseableUtil.close(fs);
        }
    }

    public void testConvertReader() throws Exception {
        File f = new File("reader.tmp");
        if (f.exists() == false) {
            f.createNewFile();
        }
        try {
            Reader r = ConverterUtil.convert(f, Reader.class);
            try {
                assertNotNull(r);
            } finally {
                r.close();
            }
            r = ConverterUtil.convert(f.toURI().toURL().toExternalForm(), Reader.class);
            try {
                assertNotNull(r);
            } finally {
                r.close();
            }
        } finally {
            f.delete();
        }
    }

    public void testConvertDouble() throws Exception {
        Double d = ConverterUtil.convertAsDouble(new Date());
        assertNotNull(d);
        d = ConverterUtil.convertAsDouble(new Date(), "yyyyMMdd");
        assertNotNull(d);
    }

    public void testConvertEnum1() throws Exception {
        Object e = ConverterUtil.convertAsEnum("A", HogeEnum.class);
        assertNotNull(e);
        assertTrue(e instanceof HogeEnum);
        assertEquals(HogeEnum.A, e);
    }

    public void testConvertEnum2() throws Exception {
        Object e = ConverterUtil.convert("A", HogeEnum.class);
        assertNotNull(e);
        assertTrue(e instanceof HogeEnum);
        assertEquals(HogeEnum.A, e);
    }

    public void testConvertEnum3() throws Exception {
        Object e = ConverterUtil.convert("A", HogeEnum.class, "no_such_pattern");
        assertNotNull(e);
        assertTrue(e instanceof HogeEnum);
        assertEquals(HogeEnum.A, e);
    }

    public void testConvertEnum4() throws Exception {
        Object e = ConverterUtil.convertAsEnum(HogeEnum.B, HogeEnum.class);
        assertNotNull(e);
        assertTrue(e instanceof HogeEnum);
        assertEquals(HogeEnum.B, e);
    }

    public void testConvertEnum5() throws Exception {
        Object e = ConverterUtil.convertAsEnum(null, HogeEnum.class);
        assertNull(e);
    }

    public void testPrimitiveByte_PrimitiveBoolean() throws Exception {
        byte b = 1;
        Boolean convert = ConverterUtil.convert(b, Boolean.TYPE);
        assertTrue(convert.booleanValue() == true);
    }

    public void testPrimitiveByte_PrimitiveShort() throws Exception {
        byte b = 1;
        Short convert = ConverterUtil.convert(b, Short.TYPE);
        assertTrue(convert.shortValue() == 1);
    }

    public void testPrimitiveByte_PrimitiveInt() throws Exception {
        byte b = 1;
        Integer convert = ConverterUtil.convert(b, Integer.TYPE);
        assertTrue(convert.intValue() == 1);
    }

    public void testPrimitiveByte_PrimitiveLong() throws Exception {
        byte b = 1;
        Long convert = ConverterUtil.convert(b, Long.TYPE);
        assertTrue(convert.longValue() == 1);
    }

    public void testPrimitiveByte_PrimitiveDouble() throws Exception {
        byte b = 1;
        Double convert = ConverterUtil.convert(b, Double.TYPE);
        assertTrue(convert.doubleValue() == 1.0);
    }

    /**
	 * TODO fix it.
	 */
    public void _testPrimitiveByte_PrimitiveChar() throws Exception {
        byte[] bytes = "a".getBytes();
        byte b = bytes[0];
        Character convert = ConverterUtil.convert(b, Character.TYPE);
        assertEquals('a', convert.charValue());
    }

    public void testPrimitiveByte_Byte() throws Exception {
        byte b = 1;
        Byte convert = ConverterUtil.convert(b, Byte.class);
        assertTrue(convert.byteValue() == b);
    }

    public void testPrimitiveByte_Boolean() throws Exception {
        byte b = 11;
        Boolean convert = ConverterUtil.convert(b, Boolean.class);
        assertTrue(convert.booleanValue());
    }

    public void testPrimitiveByte_AtomicBoolean() throws Exception {
        byte b = 1;
        AtomicBoolean convert = ConverterUtil.convert(b, AtomicBoolean.class);
        assertTrue(convert.get());
    }

    public void testPrimitiveByte_Short() throws Exception {
        byte b = 1;
        Short convert = ConverterUtil.convert(b, Short.class);
        assertTrue(convert.shortValue() == 1);
    }

    public void testPrimitiveByte_Integer() throws Exception {
        byte b = (byte) 0x123;
        Integer convert = ConverterUtil.convert(b, Integer.class);
        assertTrue(convert.intValue() == 35);
    }

    public void testPrimitiveByte_AtomicInteger() throws Exception {
        byte b = (byte) 0x123;
        AtomicInteger convert = ConverterUtil.convert(b, AtomicInteger.class);
        assertTrue(convert.get() == 35);
    }

    public void testPrimitiveByte_Long() throws Exception {
        byte b = 1;
        Long convert = ConverterUtil.convert(b, Long.class);
        assertTrue(convert.longValue() == 1);
    }

    public void testPrimitiveByte_AtomicLong() throws Exception {
        byte b = 1;
        AtomicLong convert = ConverterUtil.convert(b, AtomicLong.class);
        assertTrue(convert.get() == 1);
    }

    public void testPrimitiveByte_Float() throws Exception {
        byte b = (byte) 0x123;
        Float convert = ConverterUtil.convert(b, Float.class);
        assertTrue(convert.longValue() == 35);
    }

    public void testPrimitiveByte_Double() throws Exception {
        byte b = (byte) 0x123;
        Double convert = ConverterUtil.convert(b, Double.class);
        assertTrue(convert.doubleValue() == 35.0);
    }

    public void testPrimitiveByte_BigInteger() throws Exception {
        byte b = (byte) 0x123;
        BigInteger convert = ConverterUtil.convert(b, BigInteger.class);
        assertTrue(convert.byteValue() == b);
    }

    public void testPrimitiveByte_BigDecimal() throws Exception {
        byte b = (byte) 0x123;
        BigDecimal convert = ConverterUtil.convert(b, BigDecimal.class);
        assertTrue(convert.byteValue() == b);
    }

    /**
	 * TODO fix it.
	 */
    public void _testPrimitiveByte_Character() throws Exception {
        byte b = (byte) 0x123;
        Character convert = ConverterUtil.convert(b, Character.class);
        assertTrue(convert.charValue() == 'a');
    }

    public void testPrimitiveByte_String() throws Exception {
        byte b = (byte) 0x123;
        String convert = ConverterUtil.convert(b, String.class);
        assertEquals("35", convert);
    }

    public void testPrimitiveByte_java_util_Date() throws Exception {
        byte b = (byte) 0x123;
        Date convert = ConverterUtil.convert(b, Date.class);
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        assertNotNull(format.format(convert));
    }

    public void testPrimitiveByte_java_sql_Date() throws Exception {
        byte b = (byte) 0x123;
        java.sql.Date convert = ConverterUtil.convert(b, java.sql.Date.class);
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        String ret = format.format(convert);
        assertNotNull(ret);
    }

    public void testPrimitiveByte_java_sql_Time() throws Exception {
        byte b = (byte) 0x123;
        Time convert = ConverterUtil.convert(b, Time.class);
        assertNotNull(convert);
    }

    public void testPrimitiveByte_java_sql_Timestamp() throws Exception {
        byte b = (byte) 0x123;
        Timestamp convert = ConverterUtil.convert(b, Timestamp.class);
        assertNotNull(convert);
    }

    public void testPrimitiveByte_java_util_Calendar() throws Exception {
        byte b = (byte) 0x123;
        Calendar convert = ConverterUtil.convert(b, Calendar.class);
        assertNotNull(convert);
    }

    public void testPrimitiveByte_java_net_URL() throws Exception {
        byte b = (byte) 0x123;
        URL convert = ConverterUtil.convert(b, URL.class);
        assertNull(convert);
    }

    /**
	 * {@link URI} does not check anything at construction time so the result is
	 * not null.
	 */
    public void testPrimitiveByte_java_net_URI() throws Exception {
        byte b = (byte) 0x123;
        URI convert = ConverterUtil.convert(b, URI.class);
        assertNotNull(convert);
    }

    /**
	 * Need fix or not?
	 */
    public void _testPrimitiveByte_java_util_GregorianCalendar() throws Exception {
        byte b = (byte) 0x123;
        GregorianCalendar convert = ConverterUtil.convert(b, GregorianCalendar.class);
        assertNotNull(convert);
    }

    /**
	 * Need fix to use Enum.ordinal() to enum convert.
	 */
    public void _testPrimitiveByte_Enum() throws Exception {
        byte b = (byte) 0x123;
        Foo convert = ConverterUtil.convert(b, Foo.class);
        assertNotNull(convert);
    }

    public void testString_primitiveByte() throws Exception {
        String s = "100";
        byte ret = ConverterUtil.convert(s, Byte.TYPE);
        assertEquals(new Byte("100").byteValue(), ret);
    }

    public void testString_primitiveByteArray() throws Exception {
        String s = "aaa";
        byte[] ret = ConverterUtil.convert(s, byte[].class);
        assertTrue(ret.length == 3);
        byte[] decode = Base64Util.decode(s);
        assertEquals(decode[0], ret[0]);
        assertEquals(decode[1], ret[1]);
        assertEquals(decode[2], ret[2]);
    }

    public void testString_primitiveBoolean() throws Exception {
        String s = "Yes";
        boolean ret = ConverterUtil.convert(s, Boolean.TYPE);
        assertTrue(ret);
        ret = ConverterUtil.convert(null, Boolean.TYPE);
        assertFalse(ret);
    }

    public void testString_primitiveShort() throws Exception {
        String s = "12";
        short ret = ConverterUtil.convert(s, Short.TYPE);
        assertTrue(ret == 12);
    }

    public void testString_primitiveInteger() throws Exception {
        String s = Integer.toString(Integer.MAX_VALUE);
        int ret = ConverterUtil.convert(s, Integer.TYPE);
        assertTrue(ret == Integer.MAX_VALUE);
    }

    public void testString_primitiveLong() throws Exception {
        String s = Long.toString(Long.MAX_VALUE);
        long ret = ConverterUtil.convert(s, Long.TYPE);
        assertTrue(ret == Long.MAX_VALUE);
    }

    public void testString_primitiveFloat() throws Exception {
        String s = Float.toString(Float.MAX_VALUE);
        float ret = ConverterUtil.convert(s, Float.TYPE);
        assertTrue(ret == Float.MAX_VALUE);
    }

    public void testString_primitiveDouble() throws Exception {
        String s = Double.toString(Double.MAX_VALUE);
        double ret = ConverterUtil.convert(s, Double.TYPE);
        assertTrue(ret == Double.MAX_VALUE);
    }

    /**
	 * TODO : fix it
	 */
    public void _testString_primitiveChar() throws Exception {
        String s = "a";
        char ret = ConverterUtil.convert(s, Character.TYPE);
        assertTrue(ret == 'a');
    }

    public void testString_Byte() throws Exception {
        String s = "100";
        Byte ret = ConverterUtil.convert(s, Byte.class);
        assertEquals(new Byte("100"), ret);
    }

    public void testString_Boolean() throws Exception {
        String s = "YES";
        Boolean ret = ConverterUtil.convert(s, Boolean.class);
        assertTrue(ret.booleanValue());
    }

    public void testString_AtomicBoolean() throws Exception {
        String s = "YES";
        AtomicBoolean ret = ConverterUtil.convert(s, AtomicBoolean.class);
        assertTrue(ret.get());
    }

    public void testString_Short() throws Exception {
        String s = "100";
        Short ret = ConverterUtil.convert(s, Short.class);
        assertTrue(ret.shortValue() == 100);
    }

    public void testString_Integer() throws Exception {
        String s = "1000000";
        Integer ret = ConverterUtil.convert(s, Integer.class);
        assertTrue(ret.intValue() == 1000000);
    }

    public void testString_AtomicInteger() throws Exception {
        String s = "1000001";
        AtomicInteger ret = ConverterUtil.convert(s, AtomicInteger.class);
        assertTrue(ret.get() == 1000001);
    }

    public void testString_Long() throws Exception {
        String s = "1000002";
        Long ret = ConverterUtil.convert(s, Long.class);
        assertTrue(ret.longValue() == 1000002);
    }

    public void testString_AtomicLong() throws Exception {
        String s = "1,000,003";
        AtomicLong ret = ConverterUtil.convert(s, AtomicLong.class);
        assertTrue(ret.get() == 1000003);
    }

    public void testString_Float() throws Exception {
        String s = "1000002.123";
        Float ret = ConverterUtil.convert(s, Float.class);
        assertEquals(new Float(s), ret);
    }

    public void testString_Double() throws Exception {
        String s = "1000002.123123";
        Double ret = ConverterUtil.convert(s, Double.class);
        assertEquals(new Double(s), ret);
    }

    public void testString_BigInteger() throws Exception {
        String s = "10000023456789";
        BigInteger ret = ConverterUtil.convert(s, BigInteger.class);
        assertEquals(new BigInteger(s), ret);
    }

    public void testString_BigDecimal() throws Exception {
        String s = "10,000,023,456,789";
        BigDecimal ret = ConverterUtil.convert(s, BigDecimal.class);
        assertEquals(new BigDecimal("10000023456789"), ret);
    }

    /**
	 * TODO fix it.
	 * 
	 * @throws Exception
	 */
    public void _testString_Character() throws Exception {
        String s = "a";
        Character ret = ConverterUtil.convert(s, Character.class);
        assertEquals(new Character('a'), ret);
    }

    public void testString_String() throws Exception {
        String s = "abcdef";
        String ret = ConverterUtil.convert(s, String.class);
        assertEquals("abcdef", ret);
    }

    public void testString_java_util_Date() throws Exception {
        Locale org = Locale.getDefault();
        try {
            Locale.setDefault(Locale.JAPAN);
            String s = "2009/12/28";
            Date ret = ConverterUtil.convert(s, Date.class);
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            String retStr = format.format(ret);
            assertEquals(s, retStr);
        } finally {
            Locale.setDefault(org);
        }
    }

    /**
	 * TODO fix it.
	 * 
	 * @throws Exception
	 */
    public void _testString_java_util_Date2() throws Exception {
        String s = "2009/12/28 11:12:25";
        Date ret = ConverterUtil.convert(s, Date.class);
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String retStr = format.format(ret);
        assertEquals(s, retStr);
    }

    public void testString_java_sql_Date() throws Exception {
        Locale org = Locale.getDefault();
        try {
            Locale.setDefault(Locale.JAPAN);
            String s = "2009/12/28";
            java.sql.Date ret = ConverterUtil.convert(s, java.sql.Date.class);
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            String retStr = format.format(ret);
            assertEquals(s, retStr);
        } finally {
            Locale.setDefault(org);
        }
    }

    public void testString_Time() throws Exception {
        String s = "11:12:25";
        Time ret = ConverterUtil.convert(s, Time.class);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        String retStr = format.format(ret);
        assertEquals(s, retStr);
    }

    public void testString_java_sql_Timestamp() throws Exception {
        Locale org = Locale.getDefault();
        try {
            Locale.setDefault(Locale.JAPAN);
            String s = "2009/12/28";
            Timestamp ret = ConverterUtil.convert(s, Timestamp.class);
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            String retStr = format.format(ret);
            assertEquals(s, retStr);
        } finally {
            Locale.setDefault(org);
        }
    }

    public void testString_java_util_Calendar() throws Exception {
        Locale org = Locale.getDefault();
        try {
            Locale.setDefault(Locale.JAPAN);
            String s = "2009/12/28";
            Calendar ret = ConverterUtil.convert(s, Calendar.class);
            Date time = ret.getTime();
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            String retStr = format.format(time);
            assertEquals(s, retStr);
        } finally {
            Locale.setDefault(org);
        }
    }

    /**
	 * TODO need fix or not?
	 * 
	 * @throws Exception
	 */
    public void _testString_java_util_GregorianCalendar() throws Exception {
        Locale org = Locale.getDefault();
        try {
            Locale.setDefault(Locale.JAPAN);
            String s = "2009/12/28";
            GregorianCalendar ret = ConverterUtil.convert(s, GregorianCalendar.class);
            Date time = ret.getTime();
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            String retStr = format.format(time);
            assertEquals(s, retStr);
        } finally {
            Locale.setDefault(org);
        }
    }

    public void testString_java_net_URL() throws Exception {
        String s = "http://www.t2framework.org/";
        URL ret = ConverterUtil.convert(s, URL.class);
        assertNotNull(ret);
        assertEquals(s, ret.toExternalForm());
    }

    public void testString_java_net_URI() throws Exception {
        String s = "http://www.t2framework.org/";
        URI ret = ConverterUtil.convert(s, URI.class);
        assertNotNull(ret);
        assertEquals(s, ret.toURL().toExternalForm());
    }

    public void testString_enum() throws Exception {
        String s = "B";
        HogeEnum ret = ConverterUtil.convert(s, HogeEnum.class);
        assertNotNull(ret);
        assertEquals(HogeEnum.B, ret);
    }

    public static enum HogeEnum {

        A, B, C
    }

    public static enum Foo {

        A, B, C
    }
}
