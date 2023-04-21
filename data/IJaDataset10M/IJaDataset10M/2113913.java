package de.fhg.igd.semoa.compat.tracy;

import de.fhg.igd.semoa.server.*;
import de.fhg.igd.semoa.security.*;
import java.util.*;

/**
 * Creates lifecycles for Tracy agents. 
 *
 * @author Ulrich Pinsdorf
 * @version "$Id: TracyLifecycleFactory.java 1913 2007-08-08 02:41:53Z jpeters $"
 */
public class TracyLifecycleFactory extends LifecycleFactory {

    public boolean isSupported(String systype, String type) {
        if (systype == null || type == null) {
            throw new NullPointerException("system type or agent type");
        }
        return AgentStructure.SYSTEM_TRACY.equalsIgnoreCase(systype) && AgentStructure.TYPE_JAVA.equalsIgnoreCase(type);
    }

    public Lifecycle createLifecycle(AgentContext ctx) throws LifecycleException {
        if (!isSupported(ctx)) {
            throw new LifecycleException("Unsupported system or agent type!");
        }
        return new TracyLifecycle(ctx);
    }

    public void register() {
        Lifecycles.registerFactory(AgentStructure.SYSTEM_TRACY, AgentStructure.TYPE_JAVA, this);
    }
}
