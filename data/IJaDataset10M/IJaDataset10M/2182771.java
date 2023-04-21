package ar.com.fdvs.bean2bean.testconverter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import ar.com.fdvs.bean2bean.conversion.SpecializedTypeConverter;
import ar.com.fdvs.bean2bean.exceptions.CannotConvertException;

/**
 * Esta clase existe para testear el conversor especializado declarado con spring
 * 
 * @author Dario Garcia
 */
public class SpecializedTestConverter implements SpecializedTypeConverter<Object, Class<?>> {

    /**
	 * @see ar.com.fdvs.bean2bean.conversion.SpecializedTypeConverter#convertTo(java.lang.reflect.Type,
	 *      java.lang.Object, java.lang.annotation.Annotation[])
	 */
    public Class<?> convertTo(Type expectedType, Object sourceObject, Annotation[] contextAnnotations) throws CannotConvertException {
        return sourceObject.getClass();
    }
}
