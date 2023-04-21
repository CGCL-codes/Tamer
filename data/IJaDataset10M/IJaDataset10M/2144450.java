package org.jaffa.rules.aop.interceptors;

import java.util.Properties;
import org.apache.log4j.Logger;
import org.jaffa.datatypes.DataTypeMapper;
import org.jaffa.datatypes.exceptions.BelowMinimumException;
import org.jaffa.util.StringHelper;
import org.jboss.aop.joinpoint.Invocation;
import org.jboss.aop.joinpoint.MethodInvocation;

/**
 * When applied to a property, prevents the setter from being executed if the property is below the minValue.
 *
 * @author PaulE
 */
public class MinValuePropertyRule extends PropertyRuleInterceptor {

    private static Logger log = Logger.getLogger(MinValuePropertyRule.class);

    private static String NAME = "MinValuePropertyRule";

    private String m_minValue = null;

    /**
     * Create minValue rule for a specified property.
     * @param propertyName The property name.
     */
    public MinValuePropertyRule(String propertyName) {
        super(propertyName);
    }

    /**
     * Returns the name of this Rule.
     * @return the name of this Rule.
     */
    public String getName() {
        return NAME;
    }

    /**
     * This will perform the minValue check on the property.
     * @param invocation The invoation object.
     * @throws Throwable if any error occurs
     * @return the next object to invoke in the interceptor chain.
     */
    public Object invoke(Invocation invocation) throws Throwable {
        if (log.isDebugEnabled()) log.debug("Invoked Rule on " + invocation.getTargetObject().getClass().getName() + ", " + getTargetPropertyName());
        if (isConditionTrue(invocation.getTargetObject())) {
            Object obj = ((MethodInvocation) invocation).getArguments()[0];
            if (obj != null) {
                if (obj instanceof Comparable) {
                    Comparable min = (Comparable) getParsedMinValue(obj.getClass());
                    if (min != null) {
                        if (min.compareTo(obj) > 0) throw new BelowMinimumException(getPropertyLabel(invocation.getTargetObject()), new Object[] { m_minValue });
                    }
                } else {
                    if (log.isDebugEnabled()) log.debug("The property '" + invocation.getTargetObject().getClass().getName() + ", " + getTargetPropertyName() + "' is of type '" + obj.getClass().getName() + "' and hence cannot be compared");
                }
            }
        }
        return invocation.invokeNext();
    }

    /**
     * Return methods to bind to. This will bind to any setter.
     * @param className The class name.
     * @param property The property name.
     * @return methods to bind to.
     */
    public String[] bindNames(String className, String property) {
        property = StringHelper.getUpper1(property);
        return new String[] { "execution(void " + className + "->set" + property + "(*))" };
    }

    /**
     * Returns the value for this Rule as defined in the rules file.
     * @return the value for this Rule as defined in the rules file.
     */
    public String getMinValue() {
        return m_minValue;
    }

    /** Setter for property minValue.
     * @param minValue New value of property minValue.
     */
    public void setMinValue(String minValue) {
        m_minValue = minValue;
    }

    /**
     * Returns the parsed value for this Rule as defined in the rules file.
     * If the input target is null, a String instance of the minValue will be returned.
     * The returned object will be an instance of the input Class.
     * @param target The desired type.
     * @return the parsed value for this Rule as defined in the rules file.
     */
    public Object getParsedMinValue(Class target) {
        try {
            return target != null ? DataTypeMapper.instance().map(m_minValue, target) : m_minValue;
        } catch (ClassCastException e) {
            throw new InvalidRuleParameterException(getTargetPropertyName(), getName(), "value", m_minValue);
        }
    }

    /** Sets the parameters as defined in the rules file.
     * The 'value' parameter is mandatory.
     * @param parameters The parameters.
     * @throws RuleParameterException If the value parameter is missing.
     */
    public void setParameters(Properties parameters) throws RuleParameterException {
        String value = parameters.getProperty("value");
        if (value == null) throw new MissingRuleParameterException(getTargetPropertyName(), getName(), "value");
        m_minValue = value;
        super.setParameters(parameters);
    }
}
