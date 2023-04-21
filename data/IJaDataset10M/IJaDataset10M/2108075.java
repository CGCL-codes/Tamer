package com.wideplay.junitobjects.state;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import com.wideplay.junitobjects.ObjectsBackplane;
import com.wideplay.junitobjects.mocks.MockProxy;
import com.wideplay.junitobjects.testing.metaphors.InstanceDescriptor;
import com.wideplay.junitobjects.testing.metaphors.Trigger;
import com.wideplay.junitobjects.testing.metaphors.TriggerRegistry;
import com.wideplay.junitobjects.testing.metaphors.Triggerable;
import com.wideplay.junitobjects.testing.metaphors.Trigger.TriggerType;

/**
 * @author Dhanji
 *
 */
public class StateChain implements Triggerable {

    private Map<Integer, ObjectState> states = new TreeMap<Integer, ObjectState>();

    private Map<String, MockProxy> mockObjects = new HashMap<String, MockProxy>();

    private int stateCounter = FIRST_STATE;

    public String metaphor;

    public String name;

    public String className;

    public String targetClassName;

    public String targetMethodName;

    public TriggerType type;

    private static final Integer NO_MORE_STATES = -1;

    static final Integer FIRST_STATE = 111111;

    /**
     * Adds a new state to the state-chain
     * @param key The name of the state to add
     * @param descriptor The state being added
     * 
     */
    public void addState(String key, ObjectState descriptor) {
        states.put((stateCounter++), descriptor);
    }

    /**
     * Registers a new trigger for this state chain 
     */
    public void initializeTrigger(TriggerRegistry registry) {
        registry.register(Trigger.buildTrigger(this));
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append(" for metaphor: ");
        sb.append(metaphor);
        sb.append("\n");
        for (Integer key : states.keySet()) {
            sb.append(key);
            sb.append(" - ");
            sb.append(states.get(key));
            sb.append("\n");
        }
        return sb.toString();
    }

    public boolean trigger(InstanceDescriptor instance) {
        if (!ObjectsBackplane.getInstance().isActiveMetaphor(this.metaphor)) return true;
        StateChainMetadata meta = ((StateChainMetadata) instance.metadata.get(this.name));
        if (null == meta) {
            meta = new StateChainMetadata(FIRST_STATE);
            instance.metadata.put(this.name, meta);
        }
        if (NO_MORE_STATES.equals(meta.nextState)) return true;
        ObjectState toFire = states.get(meta.nextState);
        if (!toFire.assertPrecondition(instance.instance)) return true;
        System.out.print("chain " + this.name + "; ");
        toFire.assertState(instance.instance, toFire.name);
        meta.nextState++;
        if (null == states.get(meta.nextState)) meta.nextState = NO_MORE_STATES;
        for (String property : mockObjects.keySet()) {
            MockProxy proxy = mockObjects.get(property);
            proxy.inject(instance.instance, property);
        }
        return true;
    }

    public void addMockObject(String property, MockProxy proxy) throws StateChainBuildingException {
        if (mockObjects.get(property) != null) {
            throw new StateChainBuildingException("Property [" + property + "] is being set more than once in state chain [" + name + "]");
        }
        mockObjects.put(property, proxy);
    }

    public String getUniqueName() {
        return this.name;
    }

    public String getTargetClassName() {
        return this.targetClassName;
    }

    public String getTargetMethodName() {
        return this.targetMethodName;
    }

    public TriggerType getTargetType() {
        return this.type;
    }
}
