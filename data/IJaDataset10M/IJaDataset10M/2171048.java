package org.apptools.data;

import java.util.Properties;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.converters.StringConverter;

public class DataHolderUtils {

    /**
   * Return a Converter for converting from a String to the 
   * DataDescriptor's data type.
   * @param dd
   * @return A Converter or null if none is found
   */
    public static Converter getToValueConverter(DataDescriptor dd) {
        Class type = dd.getDataType();
        if (dd instanceof ConvertibleDataDescriptor) {
            ConvertibleDataDescriptor cdd = (ConvertibleDataDescriptor) dd;
            Converter c = cdd.getToValueConverter();
            if (c != null) return c;
        }
        return ConvertUtils.lookup(type);
    }

    /**
   * Return a non-null Converter for converting from the 
   * DataDescriptor's data type to a String object. 
   * Defaults to StringConverter (toString) if no Converter specified 
   * by ConvertibleDataDescriptor
   * @param dd
   * @return A converter
   */
    public static Converter getToStringConverter(DataDescriptor dd) {
        if (dd instanceof ConvertibleDataDescriptor) {
            ConvertibleDataDescriptor cdd = (ConvertibleDataDescriptor) dd;
            Converter c = cdd.getToStringConverter();
            if (c != null) return c;
        }
        return new StringConverter();
    }

    public static Object[] getOrderedValues(DataHolder dh) {
        DataDescriptor[] dds = dh.getDataDescriptors();
        Object[] values = new Object[dds.length];
        for (int i = 0; i < dds.length; i++) {
            values[i] = dh.getData(dds[i].getKey());
        }
        return values;
    }

    public static void putOrderedData(Object[] values, DataHolder dh) {
        DataDescriptor[] dds = dh.getDataDescriptors();
        for (int i = 0; i < dds.length; i++) {
            Object value = values[i];
            if (value == null) dh.setData(dds[i].getKey(), null); else {
                Class type = dds[i].getDataType();
                if (type.isAssignableFrom(value.getClass())) dh.setData(dds[i].getKey(), value); else {
                    Converter toValueConverter = getToValueConverter(dds[i]);
                    if (toValueConverter == null) throw new RuntimeException("No Converter found for " + type);
                    dh.setData(dds[i].getKey(), toValueConverter.convert(type, value));
                }
            }
        }
    }

    public static Properties getProperties(DataHolder dh) {
        Properties p = new Properties();
        DataDescriptor[] dds = dh.getDataDescriptors();
        for (int i = 0; i < dds.length; i++) {
            String key = dds[i].getKey();
            Object value = dh.getData(key);
            String string = (String) getToStringConverter(dds[i]).convert(String.class, value);
            p.put(key, string);
        }
        return p;
    }

    public static DataDescriptor getDescriptorForKey(DataHolder dh, String key) {
        DataDescriptor[] dds = dh.getDataDescriptors();
        for (int i = 0; i < dds.length; i++) {
            if (dds[i].getKey().equals(key)) return dds[i];
        }
        return null;
    }
}
