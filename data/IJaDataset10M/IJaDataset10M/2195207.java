package org.vizzini.game;

import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.vizzini.game.action.IAction;
import org.vizzini.game.event.ActionReceivedEvent;
import org.vizzini.game.event.IActionReceivedListener;
import org.vizzini.game.event.IGameListener;
import org.vizzini.util.Queue;

/**
 * Provides functionality for an engine where the agent's actions occur at any
 * time and any amount.
 *
 * @author   Jeffrey M. Thompson
 * @version  v0.4
 * @since    v0.1
 */
public class AsynchronousEngine implements IEngine, IActionReceivedListener {

    /** Queues of actions mapped by agent. */
    private Map<IAgent, Queue<IAction>> _actionQueues = new Hashtable<IAgent, Queue<IAction>>();

    /** Engine delegate. */
    private IEngine _engineDelegate;

    /**
     * Construct this object with the given parameters.
     *
     * @param  adjudicator   Adjudicator.
     * @param  environment   Environment.
     * @param  minCycleTime  Minimum cycle time (milliseconds).
     *
     * @since  v0.4
     */
    public AsynchronousEngine(IAdjudicator adjudicator, IEnvironment environment, long minCycleTime) {
        _engineDelegate = new DefaultEngine(this, adjudicator, environment, minCycleTime);
    }

    /**
     * Construct this object with the given parameters.
     *
     * @param  owner         Owner.
     * @param  adjudicator   Adjudicator.
     * @param  environment   Environment.
     * @param  minCycleTime  Minimum cycle time (milliseconds).
     *
     * @since  v0.4
     */
    public AsynchronousEngine(IEngine owner, IAdjudicator adjudicator, IEnvironment environment, long minCycleTime) {
        _engineDelegate = new DefaultEngine(owner, adjudicator, environment, minCycleTime);
    }

    /**
     * @see  org.vizzini.game.event.IActionReceivedListener#actionReceived(org.vizzini.game.event.ActionReceivedEvent)
     */
    public void actionReceived(ActionReceivedEvent event) {
        if (!isGameOver()) {
            IAction action = event.getAction();
            if (action != null) {
                IAgent agent = action.getAgent();
                Queue<IAction> queue = getQueue(agent);
                synchronized (queue) {
                    queue.add(action);
                }
            }
        }
    }

    /**
     * @see  org.vizzini.game.IEngine#addGameListener(org.vizzini.game.event.IGameListener)
     */
    public void addGameListener(IGameListener listener) {
        _engineDelegate.addGameListener(listener);
    }

    /**
     * @see  org.vizzini.game.IEngine#cycleSleep(long)
     */
    public void cycleSleep(long start) {
        _engineDelegate.cycleSleep(start);
    }

    /**
     * @see  org.vizzini.game.IEngine#getActionList()
     */
    public List<IAction> getActionList() {
        return _engineDelegate.getActionList();
    }

    /**
     * @see  org.vizzini.game.IEngine#getAdjudicator()
     */
    public IAdjudicator getAdjudicator() {
        return _engineDelegate.getAdjudicator();
    }

    /**
     * @see  org.vizzini.game.IEngine#getCurrentAgent()
     */
    public IAgent getCurrentAgent() {
        return _engineDelegate.getCurrentAgent();
    }

    /**
     * @see  org.vizzini.game.IEngine#getElapsedTime()
     */
    public long getElapsedTime() {
        return _engineDelegate.getElapsedTime();
    }

    /**
     * @see         org.vizzini.game.IEngine#getElapsedTimeString()
     * @deprecated  Use DateUtilities.getElapsedTimeString(elapsedTime) instead.
     */
    @Deprecated
    public String getElapsedTimeString() {
        return _engineDelegate.getElapsedTimeString();
    }

    /**
     * @see  org.vizzini.game.IEngine#getEndTime()
     */
    public Date getEndTime() {
        return _engineDelegate.getEndTime();
    }

    /**
     * @see  org.vizzini.game.IEngine#getEnvironment()
     */
    public IEnvironment getEnvironment() {
        return _engineDelegate.getEnvironment();
    }

    /**
     * @see  org.vizzini.game.IEngine#getMinCycleTime()
     */
    public long getMinCycleTime() {
        return _engineDelegate.getMinCycleTime();
    }

    /**
     * @see  org.vizzini.game.IEngine#getStartTime()
     */
    public Date getStartTime() {
        return _engineDelegate.getStartTime();
    }

    /**
     * @see  org.vizzini.game.IEngine#isFiringGameChanges()
     */
    public boolean isFiringGameChanges() {
        return _engineDelegate.isFiringGameChanges();
    }

    /**
     * @see  org.vizzini.game.IEngine#isGameOver()
     */
    public boolean isGameOver() {
        return _engineDelegate.isGameOver();
    }

    /**
     * @see  org.vizzini.game.IEngine#isPaused()
     */
    public boolean isPaused() {
        return _engineDelegate.isPaused();
    }

    /**
     * @see  org.vizzini.game.IEngine#pause()
     */
    public void pause() {
        _engineDelegate.pause();
    }

    /**
     * @see  org.vizzini.game.IEngine#removeGameListener(org.vizzini.game.event.IGameListener)
     */
    public void removeGameListener(IGameListener listener) {
        _engineDelegate.removeGameListener(listener);
    }

    /**
     * @see  org.vizzini.game.IEngine#reset()
     */
    public void reset() {
        _actionQueues.clear();
        _engineDelegate.reset();
    }

    /**
     * @see  org.vizzini.game.IEngine#resume()
     */
    public void resume() {
        _engineDelegate.resume();
    }

    /**
     * Perform the game loop activity.
     *
     * @since  v0.1
     */
    public void run() {
        IEnvironment environment = getEnvironment();
        IAdjudicator adjudicator = getAdjudicator();
        List<IAction> actionList = getActionList();
        long deltaTime = getMinCycleTime();
        while (!isGameOver()) {
            long start = System.currentTimeMillis();
            environment.incrementTurnNumber();
            actionList.clear();
            Iterator<IAgent> iter = environment.getAgentCollection().iterator();
            while (!isGameOver() && iter.hasNext()) {
                IAgent currentAgent = iter.next();
                setCurrentAgent(currentAgent);
                Queue<IAction> queue = getQueue(currentAgent);
                synchronized (queue) {
                    while (!queue.isEmpty()) {
                        IAction action = getQueue(currentAgent).remove();
                        if (adjudicator.isActionLegal(environment, action)) {
                            actionList.add(action);
                        }
                    }
                }
            }
            if (!isGameOver()) {
                environment.performActions(actionList);
                setGameOver(adjudicator.isGameOver(environment), adjudicator.getWinner());
            }
            if (!isGameOver()) {
                cycleSleep(start);
            }
            environment.update(deltaTime);
            if (isPaused()) {
                synchronized (this) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * @see  org.vizzini.game.IEngine#setCurrentAgent(org.vizzini.game.IAgent)
     */
    public void setCurrentAgent(IAgent agent) {
        _engineDelegate.setCurrentAgent(agent);
    }

    /**
     * @see  org.vizzini.game.IEngine#setEnvironment(org.vizzini.game.IEnvironment)
     */
    public void setEnvironment(IEnvironment environment) {
        _engineDelegate.setEnvironment(environment);
    }

    /**
     * @see  org.vizzini.game.IEngine#setFiringGameChanges(boolean)
     */
    public void setFiringGameChanges(boolean isFiring) {
        _engineDelegate.setFiringGameChanges(isFiring);
    }

    /**
     * @see  org.vizzini.game.IEngine#setGameOver(boolean, org.vizzini.game.IAgent)
     */
    public void setGameOver(boolean isGameOver, IAgent winner) {
        _engineDelegate.setGameOver(isGameOver, winner);
    }

    /**
     * @see  org.vizzini.game.IEngine#start()
     */
    public void start() {
        _engineDelegate.start();
    }

    /**
     * @see  org.vizzini.game.IEngine#step()
     */
    public void step() {
        _engineDelegate.step();
    }

    /**
     * @param   agent  Agent.
     *
     * @return  the queue for the given agent, creating it if necessary.
     *
     * @since   v0.1
     */
    protected Queue<IAction> getQueue(IAgent agent) {
        Queue<IAction> answer = _actionQueues.get(agent);
        if (answer == null) {
            answer = new Queue<IAction>();
            _actionQueues.put(agent, answer);
        }
        return answer;
    }
}
