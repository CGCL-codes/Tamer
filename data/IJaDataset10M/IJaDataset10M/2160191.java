package org.kineticsystem.commons.data.model.bean;

import java.util.regex.Pattern;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kineticsystem.commons.data.model.mapping.Filter;

/**
 *
 * @author Giovanni Remigi
 * @version $Revision: 147 $
 */
public class BeanRegExpFilter implements Filter<Object> {

    /** Apache log framework. */
    private static Log logger = LogFactory.getLog(BeanRegExpFilter.class);

    /** Bean property to be used by the comparator. */
    private String[] propertyNames;

    /**
     * The pattern to be matched by the regular expression predicate.
     * @see java.util.regex.Pattern
     */
    private Pattern pattern;

    /**
     * Constructor.
     * @param propertyName The object property to be check with the regular
     *     expression.
     * @param regExp The regular expression.
     * @throws PatternSyntaxException
     */
    public BeanRegExpFilter(String propertyName, String regExp) {
        this.propertyNames = new String[] { propertyName };
        this.pattern = Pattern.compile(regExp);
    }

    /**
     * Constructor.
     * @param propertyNames The array of object properties to be check with the 
     *     regular expression. In at least one property value satisfies the
     *     regular expression, the whole object is accepted.
     * @param regExp The regular expression.
     * @throws PatternSyntaxException
     */
    public BeanRegExpFilter(String[] propertyNames, String regExp) {
        this.propertyNames = propertyNames;
        this.pattern = Pattern.compile(regExp);
    }

    /** {@inheritDoc} */
    public boolean evaluate(Object obj) {
        boolean result = false;
        try {
            for (int i = 0; i < propertyNames.length; i++) {
                Object propertyValue = PropertyUtils.getProperty(obj, propertyNames[i]);
                if (propertyValue instanceof String) {
                    String value = (String) propertyValue;
                    result = pattern.matcher(value).lookingAt();
                    if (result) {
                        continue;
                    }
                }
            }
        } catch (Exception ex) {
            logger.error(ex);
        }
        return result;
    }
}
