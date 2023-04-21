package com.schwidder.petrinet.spring.runtime;

import java.util.HashMap;
import org.springframework.context.ApplicationContext;
import com.schwidder.nucleus.objects.interfaces.INet;
import com.schwidder.nucleus.objects.interfaces.IObjectActive;
import com.schwidder.nucleus.runtime.ResolverListImpl;
import com.schwidder.nucleus.runtime.interfaces.IResolver;
import com.schwidder.nucleus.runtime.interfaces.ITask;

/**
 * @author kai@schwidder.com
 * @version 1.0
 */
public class PTSpringResolverListImpl extends ResolverListImpl implements IResolver {

    public PTSpringResolverListImpl() {
        super();
    }

    public PTSpringResolverListImpl(INet aNet) {
        super(aNet);
    }

    public ITask createTask(IObjectActive aObject) {
        ApplicationContext ctx = (ApplicationContext) ((HashMap) aObject.getNet().getNetGroup().getAttributes()).get("spring.ctx");
        ITask aTask = (ITask) ctx.getBean("task");
        aTask.setObjectActive(aObject);
        return aTask;
    }
}
