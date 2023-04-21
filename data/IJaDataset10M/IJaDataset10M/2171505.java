package org.hswgt.teachingbox.experiment;

import org.hswgt.teachingbox.env.Action;
import org.hswgt.teachingbox.env.State;

/**
 * averages the rewards at each time step over episodes
 * @author tokicm
 */
public class RewardAverager extends DataAverager {

    private static final long serialVersionUID = 7963389815902355970L;

    /**
	 * The constructor
	 * @param maxSteps maximum steps per episode
	 * @param configString the config string for plotting
	 */
    public RewardAverager(int maxSteps, String configString) {
        super(maxSteps, configString);
    }

    public void update(State s, Action a, State sn, Action an, double r, boolean terminalState) {
        this.dataArray[t] = this.dataArray[t] + ((1.0 / ((double) episode)) * (r - this.dataArray[t]));
        t++;
    }
}
