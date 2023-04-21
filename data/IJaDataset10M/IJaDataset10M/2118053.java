package org.jete.model;

import org.springframework.util.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyValue;
import org.apache.log4j.Logger;
import java.util.Collection;
import java.util.Map;

/**
 * Simplify and concentrate the wrapper interface for use by result validators and actions.  Actions and
 * validators using the ResultWrapper should use this utility object to wrap or extract values from
 * the results.
 * <p/>
 * @version $Id: ResultWrapperUtils.java,v 1.2 2006/03/05 00:16:55 tdeast Exp $
 */
public class ResultWrapperUtils {

    private static final Logger logger = Logger.getLogger(ResultWrapperUtils.class);

    private static String defaultPropertyName = "value";

    /**
	 * Determine if, based on the rules, this invocation result should be wrapped into a ResultWrapper.
	 * The rules concerning wrapping are:
	 * - The result is primitive (Integer, Float, etc.)
	 * - The result is an object array
	 * - The result is a collection
	 * - Otherwise, the submitted result object
	 * <p/>
	 * If the result is a collection, the collection is simplified by calling toArray, for easier validation
	 *
	 * @param invocationResult
	 * @return Wrapped result object if primitive or collection
	 */
    public static Object wrapIfNecessary(Object invocationResult) {
        Object result = null;
        if (invocationResult == null) {
            result = null;
        } else if ((invocationResult.getClass().isArray()) || (invocationResult instanceof Boolean) || (invocationResult instanceof Character) || (invocationResult instanceof Byte) || (invocationResult instanceof Short) || (invocationResult instanceof Integer) || (invocationResult instanceof Long) || (invocationResult instanceof Float) || (invocationResult instanceof Double) || (invocationResult instanceof Void) || (invocationResult instanceof Map)) {
            result = new ResultWrapper(invocationResult);
        } else if (invocationResult instanceof Collection) {
            result = new ResultWrapper(((Collection) invocationResult).toArray());
        } else {
            result = invocationResult;
        }
        return result;
    }

    /**
	 * Retrieve the propertyValue from the bean wrapper given the specified rules.  The rules are:
	 * - if the wrapper is a ResultWrapper, and no property name is specified, use the default property name "value"
	 * - if the property name is specified, use it
	 * - otherwise, no property name is used (this should fail)
	 *
	 * @param wrapper
	 * @param propertyValue
	 * @return Retrieved result property
	 */
    public static Object getProperty(BeanWrapper wrapper, PropertyValue propertyValue) {
        Object result = null;
        String propertyName = null;
        if ((wrapper.getWrappedInstance() instanceof ResultWrapper) && (!StringUtils.hasText(propertyValue.getName()))) {
            propertyName = defaultPropertyName;
        } else if (StringUtils.hasText(propertyValue.getName())) {
            propertyName = propertyValue.getName();
        } else {
            propertyName = null;
        }
        logger.debug("Retrieving property named:" + propertyName);
        result = wrapper.getPropertyValue(propertyName);
        return result;
    }
}
