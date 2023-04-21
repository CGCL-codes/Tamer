package org.state4j.sm;

import java.util.Map;

/**
 * @author andrea
 *
 */
public interface SmCompositeState extends SmState {

    SmState getState(String name);

    Map<String, SmState> getStates();

    boolean hasState(String name);

    boolean hasStates();

    void setStates(Map<String, SmState> states);

    SmState getActiveSubstate(SmContext cntx);

    boolean hasActiveSubstate(SmContext cntx);

    SmCompositeState getLCA(SmState state1, SmState state2);

    SmState getInitialState();

    void registerLCA(SmTransition transition);
}
