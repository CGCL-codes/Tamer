package org.openaion.gameserver.ai.state.handler;

import org.openaion.gameserver.ai.AI;
import org.openaion.gameserver.ai.state.AIState;

/**
 * @author ATracer
 *
 */
public abstract class StateHandler {

    public abstract AIState getState();

    public abstract void handleState(AIState state, AI<?> ai);
}
