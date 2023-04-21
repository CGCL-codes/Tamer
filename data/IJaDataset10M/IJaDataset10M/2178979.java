package frsf.cidisi.faia.agent.search;

import frsf.cidisi.faia.state.AgentState;

/**
 * This class is used to define a function to determine when an agent state
 * is a goal state. It's used by the agent's internal search process, and by
 * the simulator, to know when to stop.
 */
public abstract class GoalTest {

    public abstract boolean isGoalState(AgentState agentState);
}
