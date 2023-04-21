package org.openjrpg.gameplay.entities;

import java.io.Serializable;
import java.util.UUID;
import org.openjrpg.ContextI;
import org.openjrpg.gameplay.state.StateMachineI;

public interface EntityI extends Serializable {

    ContextI getContext();

    UUID getId();

    String getName();

    void setName(String name);

    public StateMachineI getStateMachine();

    public void setStateMachine(StateMachineI stateMachine);
}
