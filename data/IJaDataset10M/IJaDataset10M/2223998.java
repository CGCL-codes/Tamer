package ar.com.fdvs.dgarcia.lang.closures;

/**
 * Una expresion es un bloque de codigo que puede ser evaluado en distintos objetos devolviendo un
 * valor resultado de la evaluacion
 * 
 * @param <T>
 *            Tipo del objeto usado como variable de entrada de esta expresion
 * @param <R>
 *            Tipo del objeto retornado
 * @version 1.0
 * @since 2006-03-23
 * @author D. Garcia
 */
public interface Expression<T, R> {

    /**
	 * Evalua esta expresion sobre el objeto pasado devolviendo el resultado
	 * 
	 * @param element
	 *            Objeto sobre el que evaluar esta instancia
	 * @return El resultado de la evaluacion o null si no se devolvio nada (null tambien puede ser
	 *         el resultado de la evaluacion)
	 */
    public R evaluateOn(T element);
}
