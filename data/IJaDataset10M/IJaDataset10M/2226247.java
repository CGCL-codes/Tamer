package net.sf.dozer.util.mapping.converters;

import java.lang.reflect.Constructor;
import org.apache.commons.beanutils.Converter;

/**
 * Internal class for converting String --> Complex Data Types with a String constructor. Only intended for internal
 * use.
 * 
 * @author tierney.matt
 */
public class StringConstructorConverter implements Converter {

    public Object convert(Class destClass, Object srcObj) {
        try {
            Constructor constructor = destClass.getConstructor(new Class[] { String.class });
            return constructor.newInstance(new Object[] { srcObj.toString() });
        } catch (NoSuchMethodException e) {
            return srcObj.toString();
        } catch (Exception e) {
            throw new ConversionException(e);
        }
    }
}
