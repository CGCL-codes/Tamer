package de.fhg.igd.semoa.hsmagent;

import de.fhg.igd.amoa.AMoAInterpreter;
import de.fhg.igd.jhsm.CompositeState;
import de.fhg.igd.jhsm.Context;
import de.fhg.igd.jhsm.Event;
import de.fhg.igd.jhsm.EventBus;
import de.fhg.igd.jhsm.HSMContext;
import de.fhg.igd.jhsm.HSMEventBus;
import de.fhg.igd.jhsm.Interpreter;
import de.fhg.igd.jhsm.util.HSMValidator;
import de.fhg.igd.semoa.server.AgentLogger;

/**
 * This is an agent base class for mobile agents of the SeMoA plattform.
 * A hierarchical state machine is used for behavior modelling.
 *
 * @author Jan Haevecker
 * @version "$Id: HSMAgent.java 1913 2007-08-08 02:41:53Z jpeters $"
 */
public class HSMAgent extends AbstractAgent implements Context {

    /**
     * The state machine that defines the behavior of this agent.
     */
    private CompositeState hsm_;

    /**
     * The interpreter that runs the state machine.
     */
    private Interpreter interpreter_;

    /**
     * This has to be transient because it must be set up on each new host
     * anyway.
     */
    private transient EventReflectorAdapter adapter_;

    /**
     * The event dispatcher for this agent.
     */
    private EventBus bus_;

    /**
     * The context of the state machine.
     */
    private Context actionContext_;

    /** 
     * Creates a new instance of this class 
     */
    protected HSMAgent() {
    }

    /**
     * Runs the agent. This method will also be called after each migration to
     * restart the agent on the new server.
     */
    public void run() {
        AgentLogger.debug("starting agent...");
        init();
        interpreter_.run();
        adapter_.shutdown();
        adapter_ = null;
        AgentLogger.debug("stopping agent...");
    }

    /**
     * This is for initializations that have to be repeated after every
     * migration of the agent. All <code>transient</code> variables of the
     * agent class might be re-initialized here.
     */
    protected void init() {
        this.adapter_ = new EventReflectorAdapter(bus_);
        AgentLogger.debug("registered at event reflector");
    }

    /**
     * Returns the event bus that is associated with the agent's hsm.
     */
    protected EventBus getEventBus() {
        return bus_;
    }

    /**
     * Sets the hsm that defines the behavior of the agent.
     * Before the hsm is interpreted it is tested by the
     * {@linkplain HSMValidator}.
     * An appropriate context, interpreter and event bus is automatically
     * generated for the agent.
     *
     * @param hsm A composite state that contains a complete hsm.
     */
    public void setHSM(CompositeState hsm) {
        HSMValidator val;
        val = new HSMValidator();
        try {
            val.validate(hsm);
        } catch (Exception ex) {
            AgentLogger.error("failed to validate hsm");
            AgentLogger.caught(ex);
            return;
        }
        hsm_ = hsm;
        interpreter_ = new AMoAInterpreter(hsm_);
        bus_ = new HSMEventBus(interpreter_);
        actionContext_ = new HSMContext();
        actionContext_.setEventDispatcher(bus_);
        interpreter_.setContext(this);
    }

    public Object get(String key) {
        return actionContext_.get(key);
    }

    public EventBus getEventDispatcher() {
        return actionContext_.getEventDispatcher();
    }

    public Event getTriggerEvent() {
        return actionContext_.getTriggerEvent();
    }

    public void set(String key, Object value) {
        actionContext_.set(key, value);
    }

    public void setEventDispatcher(EventBus dispatcher) {
        actionContext_.setEventDispatcher(dispatcher);
    }

    public void setTriggerEvent(Event trigger) {
        actionContext_.setTriggerEvent(trigger);
    }
}
