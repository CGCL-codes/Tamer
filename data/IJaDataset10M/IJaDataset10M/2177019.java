package org.mushroomdb.jdbc.impl;

import org.mushroomdb.executionplan.ExecutionPlanElement;

/**
 * @author Matias
 *
 */
public interface PhysicalConnection {

    /**
	 * Ejecuta una operaci�n en el server y devuelve el ID de la operaci�n
	 * para poder recuperar los resultados.
	 * @param operation la operaci�n a ejecutar 
	 * @return un ID de operaci�n para recuperar los resultados
	 * @throws PhysicalConnectionException
	 */
    public int execute(String operation) throws PhysicalConnectionException;

    /**
	 * Recupera el siguiente resultado de una operaci�n.
	 * @param operationId el ID de la operaci�n.
	 * @return un objecto representando un resultado
	 * o null si no hay mas resultados.
	 * @throws PhysicalConnectionException
	 */
    public Object next(int operationId) throws PhysicalConnectionException;

    /**
	 * Recupera la metadata de una operaci�n.
	 * @param operationId
	 * @return
	 * @throws PhysicalConnectionException
	 */
    public Object getMetadata(int operationId) throws PhysicalConnectionException;

    /**
	 * Recupera el plan de ejecuci�n de una operaci�n.
	 * @param operationId
	 * @return
	 * @throws PhysicalConnectionException
	 */
    public ExecutionPlanElement getExecutionPlan(int operationId) throws PhysicalConnectionException;

    /**
	 * Libera la conexi�n.
	 * @throws PhysicalConnectionException
	 */
    public void release() throws PhysicalConnectionException;
}
