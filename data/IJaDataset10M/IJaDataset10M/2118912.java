package net.sf.doolin.app.mt.service.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.doolin.app.mt.MTState;
import net.sf.doolin.app.mt.service.MTStateMachine;

public class DefaultMTStateMachine implements MTStateMachine {

    private final Map<MTState, List<MTState>> transitionMap = new HashMap<MTState, List<MTState>>();

    private final Map<MTState, MTState> revertMap = new HashMap<MTState, MTState>();

    public DefaultMTStateMachine() {
        this.transitionMap.put(MTState.OPENED, Arrays.asList(MTState.ONGOING, MTState.WAITING, MTState.CLOSED, MTState.CANCELLED));
        this.transitionMap.put(MTState.ONGOING, Arrays.asList(MTState.OPENED, MTState.WAITING, MTState.CLOSED, MTState.CANCELLED));
        this.transitionMap.put(MTState.WAITING, Arrays.asList(MTState.OPENED, MTState.ONGOING, MTState.CLOSED, MTState.CANCELLED));
        this.revertMap.put(MTState.ONGOING, MTState.OPENED);
        this.revertMap.put(MTState.WAITING, MTState.OPENED);
        this.revertMap.put(MTState.CLOSED, MTState.OPENED);
        this.revertMap.put(MTState.CANCELLED, MTState.OPENED);
    }

    @Override
    public boolean canRevertState(MTState state) {
        return this.revertMap.containsKey(state);
    }

    @Override
    public MTState getInitialState() {
        return MTState.OPENED;
    }

    @Override
    public List<MTState> getNextStates(MTState state) {
        List<MTState> list = this.transitionMap.get(state);
        if (list != null) {
            return list;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public boolean isFinalState(MTState state) {
        List<MTState> list = this.transitionMap.get(state);
        if (list != null) {
            return list.isEmpty();
        } else {
            return true;
        }
    }

    @Override
    public boolean isTransitionValid(MTState state1, MTState state2) {
        List<MTState> list = this.transitionMap.get(state1);
        if (list != null) {
            return list.contains(state2);
        } else {
            return false;
        }
    }

    @Override
    public MTState revertState(MTState state) {
        return this.revertMap.get(state);
    }
}
