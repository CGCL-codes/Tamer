package org.dbc.examples.stack;

import org.javadbc.attributes.DBC;
import org.javadbc.attributes.Ensure;
import org.javadbc.attributes.Invariant;
import org.javadbc.attributes.Require;

/**
 * Ejemplo de una clase Stack usando principios definidos en "Design by Contract
 * by Example" 
 * 
 * 1. Separar Comandos y Consultas Las consultas retornan un
 * resultado pero no modifican las propiedades visibles del objeto. Los comandos
 * pueden cambiar el objeto pero no retornar un resultado
 * 
 * 2. Separar Consultas basicas de consultas derivadas Las consultas derivadas
 * pueden ser especificadas en terminos de consultas basicas
 * 
 * 3. Para cada consulta derivada, escribir una postcondicion que especifique 
 * que el resultado sera retornado en terminos de uno o mas consultas basicas.
 * Con lo cual, si conocemos los valores de las consultas basicas, tambien conocemos
 * los valores de las consultas derivadas.
 * 
 * 4. Para cada comando, escribir una postcondicion que especifique los valores de toda consulta
 * basica
 * 
 * 5. Para cada consulta y comando, decidir una precondicion adecuada.
 * 
 * 6. Escribir invariantes para definir las propiedades que no cambian de los 
 * objetos.
 * @author jvelilla
 * 
 * @param <G>
 */
@DBC
@Invariant("getCount() >= 0")
public interface IStack {

    /**
	 * Cantidad de items que contiene actualmente el Stack
	 * 
	 * @return
	 */
    int getCount();

    /**
	 * Retorna el item que esta en el tope de la pila
	 * 
	 * @return
	 */
    @Require("getCount() > 0")
    public Object getItem();

    /**
	 * Determina si el Stack esta vacio
	 * 
	 * @return
	 */
    @Ensure("{result}==(getCount()==0)")
    public boolean isEmpty();

    /**
	 * Cada nuevo Stack se inicializa en vacio o para re-inicializar una Stack
	 * existente
	 */
    void initialize();

    /**
	 * Pone un item g sobre el tope del Stack
	 * 
	 * @param g
	 */
    @Ensure("#arg0.equals(getItem()) && ( getCount() == {old getCount()} + 1)")
    void put(Object g);

    /**
	 * Saca el elemento que esta en el tope del Stack
	 * 
	 * @return
	 */
    @Require("getCount() > 0 ")
    @Ensure("getCount() == {old getCount()} - 1 ")
    void remove();
}
