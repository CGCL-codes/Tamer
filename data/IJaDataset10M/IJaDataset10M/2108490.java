package org.modyna.modyna.simulation.parser;

import java.util.Iterator;
import org.modyna.modyna.model.elements.Quantity;

/**
 * Interface that has to be implemented by models in order to enable the parser
 * to access the model quantities in order to retrieve the equations and to come
 * to know the reserved words for variables (auxiliaries, levels, parameters).
 * 
 * @author Dr. Rupert Rebentisch
 * 
 */
public interface SetOfVariablesAsParserInput {

    /**
	 * Retrieve a named quantity from a model
	 * 
	 * @param name
	 *            name of the quantity to be retrieved
	 * @return the quantity beeing retrieved
	 */
    public abstract Quantity getModelQuantityByName(String name);

    /**
	 * Enable parser to systematically access all quantities
	 * 
	 * @return iterator for all quantities in the model (auxiliaries, levels and
	 *         parameters).
	 */
    public abstract Iterator getModelQuantityIterator();
}
