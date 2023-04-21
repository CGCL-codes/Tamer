package uchicago.src.sim.engine;

import java.util.ArrayList;
import uchicago.src.sim.util.Random;
import uchicago.src.sim.util.SimUtilities;

/**
 * Encapsulates some simple model behavoir and hides the scheduling
 * mechansim.
 *
 * Every time step of the simulation, SimpleModel will executes its
 * preStep(), step() and postStep() methods. Classes that extends this
 * class can provide the appropriate implementation of these methods.
 * In addition, SimpleModel will execute the atPause and atEnd methods
 * at a pause in or at the end of a simulation run.<p>
 *
 * If the protected ivar autoStep is true, then SimpleModel will executes
 * preStep(), autoStep(), and postStep(). SimpleModel defines autoStep and
 * child classes need only implement preStep() and postStep() if desired.
 * autoStep() will optionaly shuffle the list of agents, and then call
 * step() on each agent. <b>Note</b> that in the auto-step case, agents
 * must implement the <code>Stepable</code> interface.<p>
 *
 * Child classes can make use of the following protected instance variables:
 * <ul>
 * <li> name - the model's name.
 * <li> autoStep - whether the model should "autoStep" through the agents
 * as described above.
 * <li> shuffle - whether the model should shuffle the list of agents
 * before "autostepping".
 * <li> isGui - true if the model is running in gui mode, false if running
 * in batch mode.
 * </ul>
 *
 * @version $Revision: 1.12 $ $Date: 2004/11/03 19:50:57 $
 */
public class SimpleModel extends SimModelImpl {

    protected Schedule schedule;

    protected ArrayList agentList = new ArrayList();

    protected String name = "A Repast Model";

    protected String[] params = { "" };

    private double stoppingTime = Double.POSITIVE_INFINITY;

    private BasicAction stoppingAction;

    protected boolean autoStep = false;

    protected boolean shuffle = false;

    protected long seed = 1;

    protected boolean isGui;

    protected long startAt = 1;

    /**
   * Sets the tick at which this model will stop.
   */
    public void setStoppingTime(long time) {
        setStoppingTime((double) time);
    }

    /**
   * Sets the tick at which this model will stop.
   */
    public void setStoppingTime(double time) {
        stoppingTime = time;
        if (stoppingAction != null) schedule.removeAction(stoppingAction);
        if (schedule != null) setStopAction();
    }

    private void setStopAction() {
        stoppingAction = schedule.scheduleActionAt(stoppingTime, this, "stop", Schedule.LAST);
    }

    public void setup() {
        isGui = !(getController().isBatch());
        stoppingTime = Double.POSITIVE_INFINITY;
        stoppingAction = null;
        schedule = new Schedule();
        agentList = new ArrayList();
        setRngSeed(seed);
        Random.createUniform();
    }

    /**
   * Sets the random number seed for this model, and recreates a
   * uniform distribution with that seed.
   */
    public void setRngSeed(long seed) {
        this.seed = seed;
        super.setRngSeed(seed);
        Random.createUniform();
    }

    /**
   * Returns the next random integer between from and to, <b>inclusive</b>
   * of from and to.
   */
    public int getNextIntFromTo(int from, int to) {
        return Random.uniform.nextIntFromTo(from, to);
    }

    /**
   * Returns the next random double between from and to, <b>exclusive</b>
   * of from and to.
   */
    public double getNextDoubleFromTo(double from, double to) {
        return Random.uniform.nextDoubleFromTo(from, to);
    }

    public void begin() {
        buildModel();
        buildSchedule();
    }

    /**
   * Gets the name of this model.
   */
    public String getName() {
        return name;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public String[] getInitParam() {
        return params;
    }

    public void buildModel() {
    }

    public void buildSchedule() {
        if (autoStep) schedule.scheduleActionBeginning(startAt, this, "runAutoStep"); else schedule.scheduleActionBeginning(startAt, this, "run");
        schedule.scheduleActionAtEnd(this, "atEnd");
        schedule.scheduleActionAtPause(this, "atPause");
        setStopAction();
    }

    public void atPause() {
    }

    public void atEnd() {
    }

    public void runAutoStep() {
        preStep();
        autoStep();
        postStep();
    }

    public void run() {
        preStep();
        step();
        postStep();
    }

    private void autoStep() {
        if (shuffle) SimUtilities.shuffle(agentList);
        int size = agentList.size();
        for (int i = 0; i < size; i++) {
            Stepable agent = (Stepable) agentList.get(i);
            agent.step();
        }
    }

    protected void preStep() {
    }

    protected void step() {
    }

    protected void postStep() {
    }
}
