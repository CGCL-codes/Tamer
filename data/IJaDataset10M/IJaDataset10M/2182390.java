package edu.wpi.first.wpilibj.templates;

import java.util.Vector;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PIDSource;

/**
 * Line tracker detects single and multiple lines. 
 * If multiple lines are detected, will automatically follow the left
 * or right line depending on what was passed in when contructed.
 * <p>
 * You can reverse the outputs for the sensor and limit switches.
 * <p>
 * A transition is from no line to a line, or a line to no line.
 *  
xo * @author joseph & lee
 */
public class LineTracker implements PIDSource {

    public static final int LEFT_TO_RIGHT = 0;

    public static final int RIGHT_TO_LEFT = 1;

    protected boolean debug = false;

    protected int dir = LEFT_TO_RIGHT;

    protected double linePos;

    protected int numTransitions = 0;

    protected Vector transitions = new Vector();

    protected DigitalInput lineSensor;

    protected DigitalInput leftLimit;

    protected DigitalInput rightLimit;

    protected boolean forkLeft = false;

    protected boolean forkReached = false;

    protected boolean lineFound = false;

    protected boolean enabled = true;

    protected boolean sensorRead = false;

    protected boolean prevSensorRead = false;

    protected long scanStartTime = 0;

    protected long swipeTime = 0;

    public LineTracker(boolean debug, DigitalInput left, DigitalInput right, DigitalInput sensor, boolean forkLeft) {
        this(left, right, sensor, forkLeft);
        this.debug = debug;
    }

    public LineTracker(DigitalInput left, DigitalInput right, DigitalInput sensor, boolean forkLeft) {
        leftLimit = left;
        rightLimit = right;
        this.lineSensor = sensor;
        this.forkLeft = forkLeft;
    }

    public int getDirection() {
        return dir;
    }

    public int getNumTransitions() {
        return numTransitions;
    }

    public Vector getTransitons() {
        return transitions;
    }

    public void tick(long now) {
        boolean left = getLeftLimit();
        boolean right = getRightLimit();
        if (left) {
            resetTransitions();
            scanStartTime = now;
            if (getSensor()) {
                sensorRead = getSensor();
                prevSensorRead = sensorRead;
                addTransition(sensorRead, now - scanStartTime);
            }
            dir = LEFT_TO_RIGHT;
        } else if (right) {
            processTransitions(now);
            resetTransitions();
            dir = RIGHT_TO_LEFT;
        } else {
            if (0 != scanStartTime) {
                readSensor(now);
            }
        }
    }

    protected void reverseDirection() {
        if (LEFT_TO_RIGHT == dir) dir = RIGHT_TO_LEFT; else dir = LEFT_TO_RIGHT;
    }

    public void readSensor(long now) {
        sensorRead = getSensor();
        if (sensorRead != prevSensorRead) {
            prevSensorRead = sensorRead;
            addTransition(sensorRead, now - scanStartTime);
        }
    }

    public void addTransition(boolean sensorRead, long time) {
        LineTransition trans = new LineTransition(sensorRead, time);
        transitions.addElement(trans);
    }

    /** Take all the transition data and try to find one or more lines. */
    protected void processTransitions(long now) {
        forkReached = false;
        numTransitions = transitions.size();
        swipeTime = now - scanStartTime;
        for (int i = 0; i < transitions.size(); i++) {
            LineTransition trans = (LineTransition) transitions.elementAt(i);
            trans.pos = (double) trans.time / (double) swipeTime;
        }
        if (transitions.size() % 2 != 0) {
            addTransition(sensorRead, swipeTime);
        }
        if (transitions.size() == 2) {
            processSingleLine(0);
        } else if (transitions.size() > 2) {
            forkReached = true;
            if (forkLeft) processSingleLine(0); else processSingleLine(transitions.size() - 2);
        }
        if (RIGHT_TO_LEFT == dir) linePos *= -1;
        if (debug) showTransitionInfo();
    }

    protected void processSingleLine(int offset) {
        LineTransition left = (LineTransition) transitions.elementAt(offset);
        LineTransition right = (LineTransition) transitions.elementAt(offset + 1);
        linePos = 2 * ((double) ((right.time + left.time) / 2) / (double) swipeTime) - 1;
    }

    protected void resetTransitions() {
        transitions.removeAllElements();
        scanStartTime = 0;
    }

    protected void showTransitionInfo() {
        System.out.println(toString());
    }

    public String toString() {
        StringBuffer buff = new StringBuffer("# trans=[" + transitions.size() + "] Line Position [" + linePos + "] lineFound=[" + lineFound + "]\n");
        for (int i = 0; i < transitions.size(); i++) {
            LineTransition trans = (LineTransition) transitions.elementAt(i);
            buff.append(trans + "\n");
        }
        return buff.toString();
    }

    public boolean isLineFound() {
        return numTransitions > 0;
    }

    public boolean isForkReached() {
        return forkReached;
    }

    public double pidGet() {
        return linePos;
    }

    public boolean getLeftLimit() {
        return leftLimit.get();
    }

    public boolean getRightLimit() {
        return rightLimit.get();
    }

    public boolean getSensor() {
        return lineSensor.get();
    }
}
