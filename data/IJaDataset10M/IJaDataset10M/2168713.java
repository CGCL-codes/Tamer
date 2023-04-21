package org.hswgt.teachingbox.learner;

import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;
import org.hswgt.teachingbox.env.Action;
import org.hswgt.teachingbox.env.State;
import org.hswgt.teachingbox.tabular.TabularValueFunction;

/**
 * Ordinary Tabular TD-Learning
 * TODO: Add eligibility traces
 */
public class TabularTdLearner implements Learner {

    private static final long serialVersionUID = 444487333654199521L;

    Logger log4j = Logger.getLogger("TabularTdLearner");

    /**
     * The default decay-rate parameter for eligibility traces
     */
    public static final double DEFAULT_LAMBDA = 0.9;

    /**
     * The default learning rate
     */
    public static final double DEFAULT_ALPHA = 0.2;

    /**
     * The default discount-rate
     */
    public static final double DEFAULT_GAMMA = 0.95;

    protected double alpha = DEFAULT_ALPHA;

    protected double gamma = DEFAULT_GAMMA;

    protected List<ErrorObserver> observers = new LinkedList<ErrorObserver>();

    protected TabularValueFunction V;

    /**
     * Constructor 
     * @param valueFunction The value function to learn
     */
    public TabularTdLearner(TabularValueFunction V) {
        this.V = V;
    }

    public void updateNewEpisode(State initialState) {
    }

    /**
     * This method will notify all observer of a new tderror
     * @param tderror The actual tderror
     */
    public void notify(double tderror) {
        for (ErrorObserver observer : observers) observer.update(tderror);
    }

    /**
     * Attaches an observer to this Learner
     * @param obs The observer to attach
     */
    public void addObserver(ErrorObserver obs) {
        log4j.info("New Observer added: " + obs.getClass());
        this.observers.add(obs);
    }

    public void update(State s, Action a, State sn, Action an, double r, boolean isTerminalState) {
        double tderror = (r + gamma * V.getValue(sn) - V.getValue(s));
        this.notify(tderror);
        V.setValue(s, (V.getValue(s) + alpha * tderror));
    }

    /**
     * @return the alpha
     */
    public double getAlpha() {
        return alpha;
    }

    /**
     * @param alpha the alpha to set
     */
    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    /**
     * @return the gamma
     */
    public double getGamma() {
        return gamma;
    }

    /**
     * @param gamma the gamma to set
     */
    public void setGamma(double gamma) {
        this.gamma = gamma;
    }
}
