package org.jnf.application.async;

import java.io.Serializable;

/**
 * Interfaz que es implementada para ejecuci�n de servicios asincronos.
 * 
 * @author Alfredo Lopez powered by GUCOBA Systems S.C.
 * @version 1.0
 */
public interface AsyncService extends Serializable {

    /**
	 * Metodo que contiene el codigo que se requiere ejecutar de manera asincrona.
	 * 
	 * @param parameters
	 *            Parametros de la ejecuci�n asincrona.
	 * @throws Throwable
	 *             En caso de error propaga una Excepci�n.
	 */
    void executeService(AsyncServiceParameters parameters);
}
