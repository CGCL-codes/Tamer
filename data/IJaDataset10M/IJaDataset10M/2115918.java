package ar.com.fdvs.bean2bean.conversion;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Esta interfaz representa un conversor de tipos que puede tratar las dos operaciones de conversion
 * (es decir, en ambas direcciones). Y mas de un tipo a la vez.
 * 
 * @version 1.0
 * @since 29/12/2007
 * @author D. Garcia
 * @param <S>
 *            Tipo de la conversion S FROM D
 * @param <D>
 *            Tipo de la conversion S TO D
 */
public interface GeneralTypeConverter<S, D> {

    /**
	 * Indica si este conversor puede convertir un valor desde el tipo indicado como fuente al
	 * destino
	 * 
	 * @param sourceType
	 *            Tipo del objeto indicado
	 * @param expectedType
	 *            Tipo esperado como resultado
	 * @param sourceObject
	 *            Objeto desde el que se realizaria la conversion
	 * @return true si es seguro asignarle la conversion a este conversor
	 */
    boolean acceptsConversionFrom(Class<?> sourceType, Type expectedType, Object sourceObject);

    /**
	 * Indica si este conversor puede convertir un valor hacia el tipo esperado, desde el tipo
	 * fuente indicado
	 * 
	 * @param expectedType
	 *            Tipo esperado de la conversion
	 * @param sourceType
	 *            Tipo fuente de la conversion
	 * @param sourceObject
	 *            Objeto desde el que se realizaria la conversion
	 * @return true si es seguro asignarle la conversion a este conversor
	 */
    boolean acceptsConversionTo(Type expectedType, Class<?> sourceType, Object sourceObject);

    /**
	 * Convierte desde el tipo S el objeto pasado al tipo D
	 * 
	 * @param value
	 *            Objeto a convertir
	 * @param expectedType
	 *            tipo esperado de la conversion
	 * @param contextAnnotations
	 *            Annotations que sirven de contexto a la conversion Este parametro es necesario
	 *            solo para algunas conversiones y es opcional. Si no se encontraron annotations en
	 *            el contexto de conversion se pasara null.
	 * @return El objeto convertido
	 */
    D convertFrom(S value, Type expectedType, Annotation[] contextAnnotations);

    /**
	 * Convierte el objeto pasado desde el tipo D al tipo S
	 * 
	 * @param expectedType
	 *            Tipo esperado de la conversion
	 * @param value
	 *            Objeto original
	 * @param contextAnnotations
	 *            Annotations que sirven de contexto a la conversion Este parametro es necesario
	 *            solo para algunas conversiones y es opcional. Si no se encontraron annotations en
	 *            el contexto de conversion se pasara null.
	 * @return El objeto convertido
	 */
    S convertTo(Type expectedType, D value, Annotation[] contextAnnotations);
}
