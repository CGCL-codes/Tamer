package org.python.javaagent;

import java.lang.instrument.Instrumentation;

/**
 * A foreign agent that provides access to the Bytecode instrumentation library.
 * 
 * @author Tobias Ivarsson
 */
public class DeferringAgent {

    private final Instrumentation instrumentation;

    private static DeferringAgent agent = null;

    private DeferringAgent(Instrumentation inst) {
        this.instrumentation = inst;
    }

    /**
     * Get the instrumentation this agent received upon attachment, or try to attach the agent by
     * invoking the provided attacher.
     * 
     * @param attacher
     *            A task that gets executed to attach the agent if it has not already been attached.
     * @return The instrumentation associated with this agent.
     */
    public static Instrumentation getInstrumentation(Runnable attacher) {
        if (agent == null) {
            attacher.run();
            if (agent == null) {
                synchronized (DeferringAgent.class) {
                    if (agent == null) {
                        agent = new DeferringAgent(null);
                    }
                }
            }
        }
        return agent.instrumentation;
    }

    /**
     * Attach the agent upon start of the JVM.
     * 
     * @param agentArgs
     *            Arguments are ignored.
     * @param inst
     *            The instrumentation that will be associated with this agent.
     */
    public static void premain(String agentArgs, Instrumentation inst) {
        agentmain(agentArgs, inst);
    }

    /**
     * Attach the agent upon remote arrival to the JVM.
     * 
     * @param agentArgs
     *            Arguments are ignored.
     * @param inst
     *            The instrumentation that will be associated with this agent.
     */
    public static void agentmain(String agentArgs, Instrumentation inst) {
        agent = new DeferringAgent(inst);
    }
}
