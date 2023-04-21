package net.sf.myra.framework;

/**
 * Default heuristic value implementation. Each vertex and edge can have an
 * heuristic value associated.
 * 
 * @author Fernando Esteban Barril Otero
 * @version $Revision: 486 $ $Date:: 2008-11-30 19:49:18#$
 */
public class HeuristicValue extends Weight {

    /**
	 * Creates a new heuristic value with the specified value.
	 * 
	 * @param value the heuristic value.
	 */
    public HeuristicValue(double value) {
        super(value);
    }
}
