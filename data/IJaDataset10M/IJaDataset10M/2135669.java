package com.nhncorp.usf.core.ognl;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import ognl.OgnlContext;
import ognl.OgnlException;
import org.apache.commons.collections15.map.LRUMap;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import com.nhncorp.usf.core.exception.CallExecutorException;

/**
 * The Class OgnlCallParameters.
 * 
 * @author MiddleWare Platform Development Team
 */
class OgnlCallParameters {

    private static final ParameterNameDiscoverer PARAM_NAME_DISCOVER = new LocalVariableTableParameterNameDiscoverer();

    private static final Map<Method, String[]> PARAM_NAME_CACHE = Collections.synchronizedMap(new LRUMap<Method, String[]>(65536));

    /**
	 * Gets the parameter names.
	 * 
	 * @param method the method
	 * 
	 * @return the parameter names
	 */
    private static String[] getParameterNames(Method method) {
        String[] names = PARAM_NAME_CACHE.get(method);
        if (names == null) {
            names = PARAM_NAME_DISCOVER.getParameterNames(method);
            if (names == null) {
                throw new CallExecutorException("Invocation target has not been compiled with debug information: " + method.getDeclaringClass().getName());
            }
            PARAM_NAME_CACHE.put(method, names);
        }
        return names;
    }

    /** The parameters. */
    private final Map<String, Entry> parameters = new LinkedHashMap<String, Entry>();

    /**
	 * Instantiates a new ognl call parameters.
	 * 
	 * @param method the method
	 * @param paramTypes the param types
	 */
    OgnlCallParameters(Method method, Class<?>[] paramTypes) {
        String[] names = getParameterNames(method);
        for (int i = 0; i < names.length; i++) {
            parameters.put(names[i], new Entry(names[i], paramTypes[i]));
        }
    }

    /**
	 * Contains all parameter names.
	 * 
	 * @param paramNames the param names
	 * 
	 * @return true, if successful
	 */
    boolean containsAllParameterNames(Collection<String> paramNames) {
        if (paramNames.size() != parameters.size()) {
            return false;
        }
        for (String name : paramNames) {
            if (!parameters.containsKey(name)) {
                return false;
            }
        }
        return true;
    }

    /**
	 * Gets the parameter value.
	 * 
	 * @param name the name
	 * 
	 * @return the parameter value
	 */
    Object getParameterValue(String name) {
        Entry entry = parameters.get(name);
        return entry == null ? null : entry.getValue();
    }

    /**
	 * Sets the parameter value.
	 * 
	 * @param ognlContext the ognl context
	 * @param name the name
	 * @param value the value
	 * 
	 * @throws OgnlException the ognl exception
	 */
    void setParameterValue(Map<?, ?> ognlContext, String name, Object value) throws OgnlException {
        Entry entry = parameters.get(name);
        if (entry == null) {
            return;
        }
        entry.setValue(ognlContext, value);
    }

    /**
	 * Gets the parameter values.
	 * 
	 * @return the parameter values
	 */
    Object[] getParameterValues() {
        int i = 0;
        Object[] values = new Object[parameters.size()];
        for (Entry entry : parameters.values()) {
            values[i] = entry.getValue();
            i++;
        }
        return values;
    }

    /**
	 * The Class Entry.
	 */
    private final class Entry {

        private final String name;

        private final Class<?> type;

        private Object value;

        private Entry(String name, Class<?> type) {
            this.name = name;
            this.type = type;
        }

        public Object getValue() {
            if (value == null) {
                value = OgnlCallExecutor.getDefaultInstance(type);
            }
            return value;
        }

        public void setValue(Map<?, ?> ognlContext, Object value) throws OgnlException {
            if (value == null || type.isAssignableFrom(value.getClass())) {
                this.value = value;
            } else {
                this.value = ((OgnlContext) ognlContext).getTypeConverter().convertValue(ognlContext, OgnlCallParameters.this, null, name, value, type);
                if (this.value == null) {
                    throw new OgnlException("Failed to convert '" + value.getClass().getName() + "' to '" + type.getName() + "'.");
                }
            }
        }

        @SuppressWarnings("unused")
        public Class<?> getType() {
            return type;
        }
    }
}
