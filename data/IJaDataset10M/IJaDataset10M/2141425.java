package ar.com.fdvs.bean2bean.interpreters;

import ar.com.fdvs.bean2bean.exceptions.MissingPropertyException;
import ar.com.fdvs.bean2bean.instantiation.ObjectFactory;

/**
 * Esta interfaz representa un interprete de expresiones que puede evaluar Strings para obtener
 * valores de de otros objetos y tambien realizar la asginación del valor obtenido sobre un tercer
 * objteo
 * 
 * @author D.Garcia
 */
public interface ExpressionInterpreter {

    /**
	 * Genera un objeto intermedio con la expresion precompilada para ser ejecutada sobre el objeto
	 * final. Esta expresion precompilada debe ser utilizada para ejecutarse sobre el objeto para
	 * realizar la evaluacion de la expresion con este interprete. La expresion compilada es
	 * reutilizada para evaluar en cada objeto nuevo
	 * 
	 * @param expression
	 *            Expresion que representa una accion evaluable por este interprete
	 * @return El resultado de la compilación utilizando el lenguaje de este interprete
	 */
    Object precompile(String expression, ObjectFactory objectFactory);

    /**
	 * Genera un objeto que representa el contexto de evaluacion de la expresion. El objeto
	 * dependerá del lenguaje de este interprete
	 * 
	 * @param sourceObject
	 *            Objeto del que se tomaran los valores
	 * @return El objeto que representa el contexto
	 */
    Object generateGetterContextFrom(Object sourceObject);

    /**
	 * Evalua la expresion precompilada obtenida anteriormente con la precompilacion, sobre el
	 * objeto fuente para obtener el valor devuelto por la expresión pasandole el contexto indicado.
	 * Usado como getter
	 * 
	 * @param source
	 *            Objeto raiz sobre el que se evaluara la expresion
	 * @param expression
	 *            Expresion a evaluar por este interprete
	 * @param context
	 *            Contexto utilizado por la expresion a ser evaluada
	 * @return El valor devuelto por la expresión
	 * @throws MissingPropertyException
	 *             Si falta una propiedad para llegar al valor
	 */
    Object evaluateGetterOn(Object source, Object expression, Object context) throws MissingPropertyException;

    /**
	 * Evalua la expresion general pasada devolviendo su resultado. Este metodo es un punto de
	 * acceso para evaluar expresiones generales
	 * 
	 * @param expression
	 *            Expresion a evaluar sin un objeto raiz ni contexto
	 * @return El valor de la evaluacion
	 */
    Object evaluate(String expression);

    /**
	 * Genera un contexto que sera aplicado al momento de la asignacion del valor en el objeto
	 * destino, utilizando una expresion en el leguaje de este interprete
	 * 
	 * @param destination
	 *            Objeto destino de la asignacion
	 * @param value
	 *            Valor a asignar segun la expresino
	 * @return El objeto que representa el contexto
	 */
    Object generateSetterContextFor(Object destination, Object value);

    /**
	 * Realiza una asignacion usando la expresión pasada con el valor indicado
	 * 
	 * @param destination
	 *            Objeto sobre el que se realizara la asignacion
	 * @param expression
	 *            Expresion utilizada para evaluar la forma de la asignacion
	 * @param context
	 *            Contexto del lenguaje para utilizar por el interprete
	 * @param value
	 *            Valor a realizar la asignacion
	 */
    void makeAssignmentOn(Object destination, Object expression, Object context, Object value);
}
