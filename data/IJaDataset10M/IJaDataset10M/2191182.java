package net.sf.kfgodel.bean2bean.conversion.converters;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import net.sf.kfgodel.bean2bean.conversion.SpecializedTypeConverter;
import net.sf.kfgodel.bean2bean.exceptions.CannotConvertException;
import net.sf.kfgodel.dgarcia.lang.reflection.ReflectionUtils;
import com.opensymphony.xwork2.conversion.impl.XWorkBasicConverter;

/**
 * Esta clase sabe como convertir de enum a integer
 * 
 * @version 1.0
 * @since 29/12/2007
 * @author D. Garcia
 */
@SuppressWarnings("unchecked")
public class Enum2NumberConverter implements SpecializedTypeConverter<Enum, Number> {

    private XWorkBasicConverter basicConverter;

    /**
	 * @param expectedType
	 *            El tipo esperado
	 * @param sourceObject
	 *            El enum a convertir
	 * @param contextAnnotations
	 *            No usado
	 * @return La representacion numerica del enum
	 * @throws CannotConvertException
	 *             Si no se puedo convertir
	 * @see net.sf.kfgodel.bean2bean.conversion.SpecializedTypeConverter#convertTo(java.lang.reflect.Type,
	 *      java.lang.Object, java.lang.annotation.Annotation[])
	 */
    public Number convertTo(Type expectedType, Enum sourceObject, Annotation[] contextAnnotations) throws CannotConvertException {
        if (expectedType == null) {
            throw new CannotConvertException("Cannot make conversion. Expected type was not defined", sourceObject, expectedType);
        }
        int ordinal = sourceObject.ordinal();
        Class<?> expectedClass = ReflectionUtils.degenerify(expectedType);
        if (expectedClass == null) {
            throw new CannotConvertException("Expected type can not be treated as a number[" + expectedType + "]", sourceObject, expectedType);
        }
        Number converted = (Number) getBasicConverter().convertValue(ordinal, expectedClass);
        return converted;
    }

    public static Enum2NumberConverter create() {
        Enum2NumberConverter converter = new Enum2NumberConverter();
        return converter;
    }

    private XWorkBasicConverter getBasicConverter() {
        if (basicConverter == null) {
            basicConverter = new XWorkBasicConverter();
        }
        return basicConverter;
    }
}
