package xuniversewizard.gui.param;

/**
 * A listener that observes value changes of a parameter.
 * 
 * @author Tobias Weigel
 * @date 06.04.2009
 * 
 */
public interface ParameterValueChangeListener {

    /**
	 * Called when the given parameter has changed its value.
	 * 
	 * @param parameter
	 */
    public void parameterChanged(Parameter parameter);

    /**
	 * Called before a new value (from either user interface or external call)
	 * is assigned to the given parameter. May not be supported by all
	 * parameters (i.e. by all possible input models).
	 * 
	 * @param parameter
	 * @return true if new parameter value should be accepted, false if new
	 *         value should be rejected
	 */
    public boolean parameterChanging(Parameter parameter, Object newValue);
}
