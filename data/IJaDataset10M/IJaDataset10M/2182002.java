package royere.cwi.framework;

import org.apache.log4j.*;

public class NotEqualToOp implements PropertyComparisonOperator {

    /** Debug object.  Logs data to various channels. */
    private static Logger logger = Logger.getLogger("royere.cwi.framework.NotEqualToOp");

    public boolean apply(Properties object, Object key, Comparable value) {
        Comparable actualValue = (Comparable) object.getProperty(key);
        logger.log(Priority.DEBUG, "apply(): " + actualValue + " vs. " + value);
        if (actualValue == null) {
            return false;
        } else {
            return ComparisonOperatorUtil.compare(actualValue, value) != 0;
        }
    }

    public String toString() {
        return "NOT-EQUAL";
    }
}
