package org.gvsig.symbology.fmap.rendering.filter.operations;

import java.util.ArrayList;
import java.util.Hashtable;
import com.hardcode.gdbms.engine.values.Value;

/**
 * Interface that all the operators of an expression to be parsed
 * has to implement
 *
 * @author Pepe Vidal Salvador - jose.vidal.salvador@iver.es
 */
public interface Expression {

    public String getName();

    /**
	 * Returns true if the expression is correctly evaluated using the
	 * HashTable for that
	 *
	 * @param symbol_table
	 * @return
	 * @throws ExpressionException
	 */
    public Object evaluate() throws ExpressionException;

    /**
	 * Returns an String containing the pattern for the operator. That is, the
	 * type of its arguments and its solution
	 *
	 * @return String with the pattern
	 */
    public String getPattern();

    /**
	 * Adds an argument (which is an Expression) to the operator in the
	 * ith position.
	 *
	 * @param i position where the argument will be added
	 * @param arg argument to be added
	 */
    public void addArgument(int i, Expression arg);

    /**
	 * Obtains the arguments of the Expression
	 *
	 * @return ArrayList with the arguments
	 */
    public ArrayList<Expression> getArguments();

    /**
	 * Sets the arguments of the Expression
	 *
	 * @param arguments
	 */
    public void setArguments(ArrayList<Expression> arguments);

    /**
	 * Checks if the semantic of the Expression is correct using for that
	 * the values of the HashTable if it is necessary
	 *
	 * @param symbol_table which contains the symbols
	 * @throws ExpressionException
	 */
    public void check() throws ExpressionException;
}
