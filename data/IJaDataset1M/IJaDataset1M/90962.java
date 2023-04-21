package skycastle.model.conditions;

import skycastle.util.propertyaccess.condition.NotificationCondition;
import java.util.Collection;

/**
 * A condition for notifying a listener when a collection is not-empty.
 *
 * @author Hans H�ggstr�m
 */
public class NotEmptyCondition implements NotificationCondition {

    public boolean meetsCondition(Object value) {
        if (value instanceof Collection) {
            final Collection collection = (Collection) value;
            return !collection.isEmpty();
        } else {
            return true;
        }
    }
}
