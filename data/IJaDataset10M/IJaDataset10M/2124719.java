package org.nexopenframework.ide.eclipse.commons.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>Context related to the execution thread</p>
 * 
 * @author Francesc Xavier Magdaleno
 * @version 1.0
 * @since 1.0
 */
public abstract class PersistentContext {

    private static final Map<String, Object> CONTEXT = Collections.synchronizedMap(new HashMap<String, Object>());

    private PersistentContext() {
    }

    public static void setAttribute(String name, Object value) {
        CONTEXT.put(name, value);
    }

    public static Object getAttribute(String name) {
        return CONTEXT.get(name);
    }

    public static Object removeAttribute(String name) {
        return CONTEXT.remove(name);
    }

    public static void reset() {
        CONTEXT.clear();
    }
}
