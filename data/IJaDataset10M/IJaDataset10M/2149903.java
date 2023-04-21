package emergent;

import java.util.HashSet;
import java.util.LinkedList;

public abstract class World<AGENT> {

    private int maxColumns;

    private int maxRows;

    private int time = 0;

    private int numberOfAgents = 0;

    private AGENT[][] agents;

    protected Monitor supervisor;

    public Monitor getSupervisor() {
        return supervisor;
    }

    public AGENT[][] getAgents() {
        return agents;
    }

    LinkedList<Rule> localRules = new LinkedList<Rule>();

    LinkedList<Detector> detectors = new LinkedList<Detector>();

    public World(int maxRows, int maxColumns, boolean syncMode) {
        this.maxColumns = maxColumns;
        this.maxRows = maxRows;
        Monitor worldSupervisor = new Monitor();
        this.supervisor = worldSupervisor;
        agents = instantiateWorld();
        populateWorld(syncMode);
        worldSupervisor.setWorld(this);
        System.out.println("\n \n >>>>>>>>>>>> Initial state <<<<<<<<<<<");
        detect();
    }

    public abstract void vitilizeWorld();

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getMaxColumns() {
        return maxColumns;
    }

    public void setMaxColumns(int columns) {
        this.maxColumns = columns;
    }

    public int getMaxRows() {
        return maxRows;
    }

    public void setMaxRows(int rows) {
        this.maxRows = rows;
    }

    public void addRule(Rule rule) {
        localRules.add(rule);
    }

    public void addDetector(Detector detector) {
        detectors.add(detector);
    }

    public synchronized void act(AGENT agent) {
        for (Rule rule : localRules) {
            if (rule.verifyGuard(this, agent)) {
                rule.doAction(this, agent);
            }
        }
    }

    public void detect() {
        for (Detector detector : detectors) {
            FeedBack fb = detector.detect(this);
            detector.applyFeedBack(fb, this);
        }
    }

    public AGENT getAgent(int row, int col) {
        return (agents[row][col]);
    }

    public void setAgent(int row, int col, AGENT agent) {
        agents[row][col] = agent;
    }

    public abstract void populateWorld(boolean syncMode);

    public abstract AGENT[][] instantiateWorld();

    public void setNumberOfAgents(int numberOfAgents) {
        this.numberOfAgents = numberOfAgents;
    }

    public int getNumberOfAgents() {
        return numberOfAgents;
    }
}
