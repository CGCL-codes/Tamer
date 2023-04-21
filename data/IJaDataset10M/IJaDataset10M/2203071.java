package hudson.zipscript.parser.context;

import hudson.zipscript.parser.template.element.lang.variable.adapter.RetrievalContext;
import java.util.Set;

public interface Context {

    /**
	 * Return the object matching the key
	 * 
	 * @param key
	 *            the key name
	 */
    public Object get(Object key, RetrievalContext retrievalContext, String contextHint);

    /**
	 * Remove a value from the context matching the key
	 * 
	 * @param key
	 *            the key name
	 */
    public Object remove(Object key);

    /**
	 * Put a value in the context
	 * 
	 * @param key
	 *            the key name
	 * @param value
	 *            the value
	 */
    public void put(Object key, Object value);

    /**
	 * Return all keys for this scoped context
	 */
    public Set getKeys();
}
