package org.jargo;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * @author Leon van Zantvoort
 */
public interface InvocationFactory {

    Set<Method> getMethods();

    Set<Class<? extends Event>> getEventTypes();

    /**
     * Must returns {@code null} if event is not executable.
     */
    Invocation getInvocation(Event event);
}
