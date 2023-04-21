package xconsole.impl;

import java.util.HashMap;
import java.util.Map;
import xconsole.ExecutionContext;

/**
 * @author keke
 * @version $Revision: 15 $
 */
public class DefaultExecutionContext implements ExecutionContext {

    private Map<String, Object> properties = new HashMap<String, Object>();

    public Object getProperty(String name) {
        return properties.get(name);
    }

    public void setProperty(String name, Object obj) {
        properties.put(name, obj);
    }
}
