package com.mgensystems.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ToString {

    /**
	 * Instantiate ToString.
	 * 
	 * @param instance
	 *            An <code>Object</code>.
	 * @return A <code>ToString</code>.
	 */
    public static ToString newInstance(final Object instance) {
        return new ToString(instance);
    }

    /** An instance of the reference object. */
    private final Object instance;

    /** A list of member names. */
    private final List<String> names;

    /** A map of member names to their values. */
    private final Map<String, Object> values;

    /**
	 * Create ToStringBuilder.
	 * 
	 * @param instance
	 *            An <code>Object</code>.
	 */
    private ToString(final Object instance) {
        super();
        this.instance = instance;
        this.names = new ArrayList<String>(7);
        this.values = new HashMap<String, Object>(7, 1.0F);
    }

    /**
	 * Append a name/value pair.
	 * 
	 * @param name
	 *            A <code>String</code>.
	 * @param value
	 *            An <code>Object</code>.
	 * @return A <code>ToStringBuilder</code>.
	 */
    public ToString append(final String name, final Object value) {
        this.names.add(name);
        this.values.put(name, value);
        return this;
    }

    /**
	 * @see java.lang.Object#toString()
	 * 
	 */
    @Override
    public String toString() {
        final StringBuilder buffer = new StringBuilder(instance.getClass().getName()).append('$');
        for (int i = 0; i < names.size(); i++) {
            if (0 < i) {
                buffer.append(';');
            }
            buffer.append(names.get(i)).append(':').append(values.get(names.get(i)));
        }
        return buffer.toString();
    }
}
