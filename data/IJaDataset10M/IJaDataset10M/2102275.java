package velosurf.context;

import java.util.Map;

/**
 * Implemented by context objects that do provide a default method handler. This is used
 * to let template designers provide extra external parameters to an attribute or an action,
 * like: <code>$myinstance.myattribute({'param1':'value1','param2':'value2'})</code>
 *
 */
public interface HasParametrizedGetter {

    /**
     * Default method handler, called by Velocity when it did not find the specified method
     *
     * @param key asked key
     * @param params passed parameters
     */
    public Object getWithParams(String key, Map params);
}
