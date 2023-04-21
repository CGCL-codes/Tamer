package net.sf.kfgodel.bean2bean.population.conversion;

import java.lang.reflect.Type;

/**
 * Esta interfaz representa un exctrator del tipo deseado de una conversion, el cual sabe como
 * obtener el tipo esperado a partir de una instancia
 * 
 * @author D.Garcia
 * @since 15/01/2009
 */
public interface ExpectedTypeExtractor {

    /**
	 * Obtiene el tipo esperado a partir del objeto destino del valor a convertir
	 * 
	 * @param destination
	 *            Objeto sobre el que se asignara el valor
	 * @return El tipo esperado de la conversion para que sea compatible en la asignacion sobre el
	 *         objeto pasado
	 */
    Type extractExpectedTypeFrom(Object destination);
}
