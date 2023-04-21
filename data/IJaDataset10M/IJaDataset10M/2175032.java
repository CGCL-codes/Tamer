package org.gudy.azureus2.ui.swt.config;

/**
 * @author parg
 *
 */
public interface ParameterChangeListener {

    public void parameterChanged(Parameter p, boolean caused_internally);

    /**
	 * An int parameter is about to change.
	 * <p>
	 * Not called when parameter set via COConfigurationManager.setParameter
	 * 
	 * @param p
	 * @param toValue
	 */
    public void intParameterChanging(Parameter p, int toValue);

    /**
	 * A boolean parameter is about to change.
	 * <p>
	 * Not called when parameter set via COConfigurationManager.setParameter
	 * 
	 * @param p
	 * @param toValue
	 */
    public void booleanParameterChanging(Parameter p, boolean toValue);

    /**
	 * A String parameter is about to change.
	 * <p>
	 * Not called when parameter set via COConfigurationManager.setParameter
	 * 
	 * @param p
	 * @param toValue
	 */
    public void stringParameterChanging(Parameter p, String toValue);

    /**
	 * A double/float parameter is about to change.
	 * <p>
	 * Not called when parameter set via COConfigurationManager.setParameter
	 * 
	 * @param p
	 * @param toValue
	 */
    public void floatParameterChanging(Parameter owner, double toValue);
}
