package easyaccept.util.bean.convert;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.beanutils.converters.BigIntegerConverter;
import org.apache.commons.beanutils.converters.BooleanArrayConverter;
import org.apache.commons.beanutils.converters.ByteArrayConverter;
import org.apache.commons.beanutils.converters.ByteConverter;
import org.apache.commons.beanutils.converters.CharacterArrayConverter;
import org.apache.commons.beanutils.converters.CharacterConverter;
import org.apache.commons.beanutils.converters.ClassConverter;
import org.apache.commons.beanutils.converters.DoubleArrayConverter;
import org.apache.commons.beanutils.converters.FileConverter;
import org.apache.commons.beanutils.converters.FloatArrayConverter;
import org.apache.commons.beanutils.converters.LongArrayConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.beanutils.converters.ShortArrayConverter;
import org.apache.commons.beanutils.converters.ShortConverter;
import org.apache.commons.beanutils.converters.URLConverter;
import easyaccept.util.bean.formatter.Formatter;

/**


 * <p>Standard {@link Converter} implementation that converts an incoming
 * String into a <code>java.lang.Boolean</code> object, optionally using a
 * default value or throwing a {@link ConversionException} if a conversion
 * error occurs.</p>
 *
 * @author Craig R. McClanahan
 * @version $Revision: 1.1 $ $Date: 2008/04/01 23:53:17 $
 * @since 1.3
 */
@SuppressWarnings("unchecked")
public final class BeanHelperConverterUtils extends ConvertUtilsBean {

    public BeanHelperConverterUtils() {
        super();
        super.deregister();
        deregister();
    }

    /**
    * Remove all registered {@link Converter}s, and re-establish the
    * standard Converters.
    */
    public void deregister() {
        boolean[] booleanArray = new boolean[0];
        byte[] byteArray = new byte[0];
        char[] charArray = new char[0];
        double[] doubleArray = new double[0];
        float[] floatArray = new float[0];
        int[] intArray = new int[0];
        Integer[] integerArray = new Integer[0];
        long[] longArray = new long[0];
        short[] shortArray = new short[0];
        String[] stringArray = new String[] { null };
        BigDecimal defaultBigDecimal = null;
        BigInteger defaultBigInteger = null;
        Boolean defaultBoolean = null;
        Byte defaultByte = null;
        Character defaultCharacter = null;
        Double defaultDouble = null;
        Float defaultFloat = null;
        Integer defaultInteger = new Integer(0);
        Long defaultLong = null;
        Short defaultShort = null;
        registerCustomConverter(BigDecimal.class, new BigDecimalConverter(defaultBigDecimal));
        registerCustomConverter(BigInteger.class, new BigIntegerConverter(defaultBigInteger));
        registerCustomConverter(booleanArray.getClass(), new BooleanArrayConverter(booleanArray));
        registerCustomConverter(Byte.TYPE, new ByteConverter(defaultByte));
        registerCustomConverter(Byte.class, new ByteConverter(defaultByte));
        registerCustomConverter(byteArray.getClass(), new ByteArrayConverter(byteArray));
        registerCustomConverter(Character.TYPE, new CharacterConverter(defaultCharacter));
        registerCustomConverter(Character.class, new CharacterConverter(defaultCharacter));
        registerCustomConverter(charArray.getClass(), new CharacterArrayConverter(charArray));
        registerCustomConverter(Class.class, new ClassConverter());
        registerCustomConverter(doubleArray.getClass(), new DoubleArrayConverter(doubleArray));
        registerCustomConverter(floatArray.getClass(), new FloatArrayConverter(floatArray));
        registerCustomConverter(Integer.TYPE, new IntegerConverter(defaultInteger));
        registerCustomConverter(Integer.class, new IntegerConverter(defaultInteger));
        registerCustomConverter(intArray.getClass(), new IntegerArrayConverter(intArray));
        registerCustomConverter(integerArray.getClass(), new IntegerArrayConverter(integerArray));
        registerCustomConverter(Long.TYPE, new LongConverter(defaultLong));
        registerCustomConverter(Long.class, new LongConverter(defaultLong));
        registerCustomConverter(longArray.getClass(), new LongArrayConverter(longArray));
        registerCustomConverter(Short.TYPE, new ShortConverter(defaultShort));
        registerCustomConverter(Short.class, new ShortConverter(defaultShort));
        registerCustomConverter(shortArray.getClass(), new ShortArrayConverter(shortArray));
        registerCustomConverter(stringArray.getClass(), new StringArrayConverter(stringArray));
        registerCustomConverter(File.class, new FileConverter());
        registerCustomConverter(URL.class, new URLConverter());
        registerCustomConverter(String.class, new StringConverter());
        registerCustomConverter(Double.TYPE, new DecimalConverter("java.lang.Double", defaultDouble));
        registerCustomConverter(Double.class, new DecimalConverter("java.lang.Double", defaultDouble));
        registerCustomConverter(java.sql.Date.class, new DateConverter(Formatter.DEFAULT_DATE_FORMAT, "java.sql.Date", null));
        registerCustomConverter(java.util.Date.class, new DateConverter(Formatter.DEFAULT_DATE_FORMAT, "java.util.Date", null));
        registerCustomConverter(java.sql.Timestamp.class, new DateConverter(Formatter.DEFAULT_TIMESTAMP_FORMAT, "java.sql.Timestamp", null));
        registerCustomConverter(java.sql.Time.class, new DateConverter(Formatter.DEFAULT_TIME_FORMAT, "java.sql.Time", null));
        registerCustomConverter(Float.class, new DecimalConverter("java.lang.Double", defaultFloat));
        registerCustomConverter(Float.TYPE, new DecimalConverter("java.lang.Double", defaultFloat));
        registerCustomConverter(Boolean.TYPE, new BooleanConverter("S", "N", defaultBoolean));
        registerCustomConverter(Boolean.class, new BooleanConverter("S", "N", defaultBoolean));
    }

    @Override
    public void register(Converter converter, Class clazz) {
    }

    public void registerCustomConverter(Class clazz, Converter converter) {
        super.register(converter, clazz);
    }
}
