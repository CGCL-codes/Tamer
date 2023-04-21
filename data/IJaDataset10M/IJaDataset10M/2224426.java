package org.hswgt.teachingbox.core.rl.rlglue;

import org.apache.log4j.Logger;
import org.hswgt.teachingbox.core.rl.env.Action;
import org.hswgt.teachingbox.core.rl.env.Environment;
import org.hswgt.teachingbox.core.rl.env.State;
import org.rlcommunity.rlglue.codec.RLGlue;
import org.rlcommunity.rlglue.codec.types.Observation;
import org.rlcommunity.rlglue.codec.types.Observation_action;
import org.rlcommunity.rlglue.codec.types.Reward_observation_action_terminal;
import org.rlcommunity.rlglue.codec.types.Reward_observation_terminal;

/**
 * This adapter maps the remote environment from RL_GLUE (eg. from the RL-Library) to an TB environment 
 * @author tokicm
 *
 */
public class RLGlueRemoteEnvironment implements Environment {

    /** Logger */
    private static final Logger log4j = Logger.getLogger("RLGlueRemoteEnvironment");

    /** the state variable */
    State tbState;

    /** the terminalState variable */
    boolean terminalState;

    /** step counter */
    double episodicStepCounter = 0;

    /** reward counter */
    double episodicRewardCounter = 0;

    /** The task specification */
    String taskSpec;

    /** The serial version uid */
    private static final long serialVersionUID = 3890057121768725913L;

    /**
	 * This is a wrapper function that calls RLGlue.RL_step(). The actual action is provided by the agent.  
	 * If the agent is an Teachingbox agent, then the RLGlueAgent.agent_step() will be called by RL-Glue.  
	 * The action is then selected by the Agent.nextStep() function. 
	 *
	 */
    public double doAction(org.hswgt.teachingbox.core.rl.env.Action a) {
        Reward_observation_action_terminal rat_observation = RLGlue.RL_step();
        this.tbState = ObservationHandler.getTbState(rat_observation.getObservation());
        this.terminalState = rat_observation.isTerminal();
        if (this.terminalState) {
            System.out.println("----------episode summary----------");
            System.out.println("It ran for " + this.episodicStepCounter + " steps, total reward was: " + this.episodicRewardCounter + "\n\n");
        }
        this.episodicStepCounter++;
        this.episodicRewardCounter += rat_observation.r;
        return rat_observation.r;
    }

    public State getState() {
        return this.tbState;
    }

    public void init(State s) {
        this.initRandom();
    }

    /** 
	 * initializes the environment and does the first observation
	 */
    public void initRandom() {
        this.episodicRewardCounter = 0;
        this.episodicStepCounter = 0;
        Observation_action oa = RLGlue.RL_start();
        this.tbState = ObservationHandler.getTbState(oa.o);
        log4j.info("Initial state = " + this.tbState);
        this.terminalState = false;
    }

    public boolean isTerminalState() {
        return this.terminalState;
    }
}
