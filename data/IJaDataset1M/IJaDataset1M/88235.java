package nl.utwente.ewi.hmi.deira.iam.riam;

import java.util.ArrayList;

/**
 * This class is a general interface for any expressions that may be described
 * in the event generation rules. When interpreted it returns a float value
 * corresponding to the expression.
 */
public interface EventGenerationExpression {

    /**
	 * Interprets the expression.
	 * 
	 * @param raceState
	 *            The (current) raceState for which the expression needs to be
	 *            interpreted.
	 * @return The expression value.
	 */
    public float interpret(ArrayList<Participant> raceState);

    /**
	 * Function to create a String representation of this expression. Mainly for
	 * debugging purposes.
	 * 
	 * @return A String representation of this expression.
	 */
    public String toString();
}
