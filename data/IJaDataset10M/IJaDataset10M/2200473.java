package org.nexopenframework.web.vlh;

import java.util.HashMap;
import java.util.Map;
import net.mlw.vlh.ValueListAdapter;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>Holds the configuration for the value list handler. We have extended the original 
 *    class from vlh in order to provide a convinient method {@link #setAdapter(String, ValueListAdapter)}}
 *    for auto-registering</p>
 * 
 * @author Matthew L. Wilson
 * @author <a href="mailto:fme@nextret.net">Francesc Xavier Magdaleno</a>
 * @version 1.0
 * @since 1.0
 */
public class Configuration {

    /** Holds all the adapters. The name of the adapter is the key. */
    protected Map adapters;

    /** The default adapter to pass back if none are found. * */
    private ValueListAdapter defaultAdapter = null;

    /**
	 * Gets the adapter with the given name. Returns the defaultAdapter if the
	 * given name could not be found.
	 * 
	 * @param name
	 *            The name of the adapter.
	 * @return The adapter with the given name, or the default.
	 */
    public ValueListAdapter getAdapter(String name) {
        ValueListAdapter adapter = (ValueListAdapter) adapters.get(name);
        if (adapter == null) {
            adapter = getDefaultAdapter();
        }
        if (adapter == null) {
            throw new NullPointerException("Adapter named: " + name + ", not found, and no default was declared.");
        }
        return adapter;
    }

    /**
	 * Sets the Map of adapters.
	 * 
	 * @param adapters
	 *            The adapters to set.
	 */
    public void setAdapters(Map adapters) {
        this.adapters = adapters;
    }

    /**
	 * Sets an adapter with a name into the map 
	 * of adapters holded in this class
	 * 
	 * @param adapters
	 *            The adapters to set.
	 */
    public void setAdapter(String name, ValueListAdapter adapter) {
        if (this.adapters == null) {
            this.adapters = new HashMap();
        }
        this.adapters.put(name, adapter);
    }

    /**
	 * Gets the default adapter to pass back if none are found.
	 * 
	 * @return Returns the defaultAdapter.
	 */
    public ValueListAdapter getDefaultAdapter() {
        return defaultAdapter;
    }

    /**
	 * Sets the default adapter to pass back if none are found.
	 * 
	 * @param defaultAdapter
	 *            The defaultAdapter to set.
	 */
    public void setDefaultAdapter(ValueListAdapter defaultAdapter) {
        this.defaultAdapter = defaultAdapter;
    }
}
