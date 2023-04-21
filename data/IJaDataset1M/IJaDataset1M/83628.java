package pteam;

import simulator.Actor;
import simulator.Robot;
import simulator.Sensor;
import simulator.Simulator;
import simulator.BumpSensor;

public class DecisionActor2 implements Actor {

    boolean bumped;

    boolean makingTurns;

    boolean rangefinder;

    boolean setServo = false;

    public static final int DISTANCE_FORWARD = 32;

    public static final int WALL_DISTANCE = 32;

    public boolean paused = false;

    double[][] histFL = new double[3][2];

    double[][] histFR = new double[3][2];

    double[][] histBL = new double[3][2];

    double[][] histBR = new double[3][2];

    public String getMaze() {
        return "Super";
    }

    public DecisionActor2() {
        Tree.root = new Node();
        Link l = new Link(Tree.root);
        l.selected = true;
        for (int i = 1; i < 4; i++) {
            Tree.root.set(i, null);
        }
        l.beginningDirection = Tree.NORTH;
        Tree.current = l;
        Tree.root.set(0, l);
        Simulator.out.println("Root:\n" + Tree.root);
    }

    public void act(Robot r) {
        if (setServo) {
            r.getServo().rotate(45);
            setServo = false;
            return;
        }
        boolean triggered = rangefinder || bumped;
        if (triggered) {
            SimpleNavigator.canAct = false;
            try {
                Simulator.out.println("triggered " + (rangefinder ? " rangefinder" : " !rangefinder") + (makingTurns ? " makingTurns" : " !makingTurns") + (bumped ? " bumped" : " !bumped"));
            } catch (Exception e) {
            }
            if (bumped) {
                Simulator.out.println("BUMPED => Makingturns");
                makingTurns = true;
                bumped = false;
                r.goBackward();
                r.waitDistance(10);
                rangefinder = true;
                return;
            } else if (rangefinder && !makingTurns) {
                Simulator.out.println("Rangefinder is true");
                enterHole(r);
                makingTurns = true;
                SimpleNavigator.canAct = false;
                return;
            }
        }
        if (rangefinder) {
            rangefinder = false;
        }
        if (makingTurns) {
            makingTurns(r);
            if (!bumped) exitHole(r);
            if (!makingTurns) {
                r.getServo().rotate(0);
                SimpleNavigator.canAct = true;
                try {
                } catch (Exception e) {
                }
            }
        }
        SimpleNavigator.canAct = true;
    }

    public void makingTurns(Robot r) {
        r.getServo().rotate(45);
        Simulator.out.println("new noding.");
        int dir;
        if (histBL[0][0] < WALL_DISTANCE && histFR[0][0] < WALL_DISTANCE && histFL[0][0] > WALL_DISTANCE) {
            Simulator.out.println("Cancelled NEWNODE");
            dir = Tree.FORWARD;
        } else dir = Tree.current.newNode(findHoles());
        Simulator.out.println("deliberating has completed: " + dir);
        SimpleNavigator.canAct = false;
        switch(dir) {
            case Tree.FORWARD:
                makingTurns = false;
                return;
            case Tree.LEFT:
                r.turnLeft();
                r.waitAngle(90);
                return;
            case Tree.RIGHT:
                r.turnRight();
                r.waitAngle(90);
                return;
            case Tree.BACKWARD:
                r.goBackward();
                r.waitDistance(32);
                r.turnRight();
                r.waitAngle(180);
                return;
        }
    }

    public boolean[] findHoles() {
        boolean options[] = new boolean[3];
        options[1] = (histFL[0][0] > WALL_DISTANCE);
        options[2] = (histFR[0][0] > WALL_DISTANCE);
        options[0] = (histBL[0][0] > WALL_DISTANCE);
        return options;
    }

    public void updateSensors(Sensor[] sensors) {
        if ((RangeFinderAnalysis.risingOrFalling(true, histFR) || RangeFinderAnalysis.risingOrFalling(true, histFL)) && !makingTurns) {
            SimpleNavigator.canAct = false;
            rangefinder = true;
            setServo = true;
        }
        if (sensors[0].getState()[0] == BumpSensor.PRESSED) {
            bumped = true;
        }
        histFL = shift(histFL);
        histFR = shift(histFR);
        histBL = shift(histBL);
        histBR = shift(histBR);
        histFL[0] = sensors[2].getState();
        histFR[0] = sensors[1].getState();
        histBR[0] = sensors[4].getState();
        histBL[0] = sensors[3].getState();
    }

    public void enterHole(Robot r) {
        r.goForward();
        r.waitDistance((int) (histFL[0][0] - 16));
        Simulator.out.println(histFL[0][0]);
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
        }
    }

    public void exitHole(Robot r) {
        Simulator.out.println("\n\n==============================     DECISION DONE ==============================\n\n");
        r.goForward();
        r.waitDistance(WALL_DISTANCE);
    }

    public double[][] shift(double[][] arr) {
        for (int i = 0; i < arr[0].length; i++) {
            arr[2][i] = arr[1][i];
            arr[1][i] = arr[0][i];
        }
        return arr;
    }
}
