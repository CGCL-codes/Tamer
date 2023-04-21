package net.sf.joyaop.impl;

import net.sf.joyaop.framework.RunnableAspect;
import net.sf.joyaop.framework.RunnableInstance;
import java.util.HashMap;
import java.util.Map;

/**
 * Not thread-safe. It ensures that the proxies contain no duplicate runtime aspect instance.  
 *
 * @author Shen Li
 */
public class RunnableAspectInstanceFactory {

    private final Map<Class, RunnableInstance> aspectInstances = new HashMap<Class, RunnableInstance>();

    private final Class originalClass;

    public RunnableAspectInstanceFactory(Class originalClass) {
        this.originalClass = originalClass;
    }

    public RunnableInstance getRunnableInstance(RunnableAspect aspect) {
        RunnableInstance runnableInstance = aspectInstances.get(aspect.getAspectClass());
        if (runnableInstance == null) {
            runnableInstance = aspect.newRunnableInstance(originalClass);
            aspectInstances.put(aspect.getAspectClass(), runnableInstance);
        }
        return runnableInstance;
    }
}
