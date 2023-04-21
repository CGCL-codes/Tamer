package net.sf.kfgodel.bean2bean.annotations;

import java.lang.annotation.Annotation;

/**
 * Esta clase tiene algunos metodos de utilidad para trabajar con las los annotations de contexto
 * 
 * @version 1.0
 * @since 06/01/2008
 * @author D. Garcia
 */
public class ContextAnnotationUtil {

    /**
	 * A partir del array de annotations pasado a un conversor, devuelve el annotation del tipo
	 * indicado.
	 * 
	 * @param <T>
	 *            Tipo del annotation esperado
	 * @param contextAnnotations
	 *            Annotations pasadas al conversor
	 * @param annotationType
	 *            Tipo del annotation a devolver
	 * @return El annotation en el array o null si no existia ninguno
	 */
    @SuppressWarnings("unchecked")
    public static <T extends Annotation> T getAnnotationFrom(Annotation[] contextAnnotations, Class<T> annotationType) {
        if (contextAnnotations == null) {
            return null;
        }
        for (Annotation annotation : contextAnnotations) {
            if (annotation.annotationType().equals(annotationType)) {
                return (T) annotation;
            }
        }
        return null;
    }
}
