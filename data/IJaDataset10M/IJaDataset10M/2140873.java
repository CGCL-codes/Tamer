package net.sf.doolin.gui;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Context management at application level.
 * 
 * @author Damien Coraboeuf
 * @version $Id: Context.java,v 1.3 2007/07/31 15:33:08 guinnessman Exp $
 */
public class Context {

    /**
	 * Log
	 */
    private static final Log log = LogFactory.getLog(Context.class);

    /**
	 * Attributes
	 */
    private Map<String, Object> attributes = Collections.synchronizedMap(new HashMap<String, Object>());

    /**
	 * Gets an attribute from the context
	 * 
	 * @param key
	 *            Attribute key
	 * @return Attribute value or <code>null</code> if not found
	 */
    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    /**
	 * Sets an attribute in the context
	 * 
	 * @param key
	 *            Attribute key
	 * @param value
	 *            Attribute value
	 */
    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    /**
	 * Initializes the context from a set of context attributes defined in the
	 * application configuration files.
	 * 
	 * @param contextAttributes
	 *            Context attributes
	 */
    public void init(ContextAttributes contextAttributes) {
        if (contextAttributes != null) {
            Properties properties = contextAttributes.getProperties();
            for (Map.Entry<Object, Object> property : properties.entrySet()) {
                String key = ObjectUtils.toString(property.getKey(), null);
                if (key != null) {
                    Object value = property.getValue();
                    if (value != null) {
                        log.info("Setting context attribute, key=" + key + ",value=" + value);
                        attributes.put(key, value);
                    }
                }
            }
        }
    }
}
