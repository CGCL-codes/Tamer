package piconode.tutorials;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import piconode.core.node.FeedForwardNeuralNetwork;
import piconode.factory.MultiLayerPerceptronFactory;
import simbad.gui.Simbatch;
import simbad.sim.Agent;
import simbad.sim.BallAgent;
import simbad.sim.EnvironmentDescription;
import simbad.sim.RangeSensorBelt;
import simbad.sim.RobotFactory;
import simbad.sim.Wall;
import contribs.maploader.SimpleImage;

/**
 * This class represents an Environment useable in Simbad but with the
 * specification of being defined by a PNG image. For the moment, the image is
 * defined as follows - 20x20 pixels (have to fit) - a red (rgb 255,0,0) pixel
 * means a box - a green (rgb 0,255,0) pixel represent a goal to reach (if any) -
 * a blue (rgb 0,0,255) pixel represents the starting position - dark gray (rgb
 * 60,60,60) pixel representing a simulated ball - the image have to be in a
 * PNG, JPG or GIF format.
 * 
 * TODO : - define more flexible constraints (20x20 -> ~ [10:50]x[10;50]) or
 * something like that (for now : ([0:20],[0:20]) values but it should be more
 * likely ([10:20],[10:20])) - add a niveler due to the rgb values for more
 * visual effects - add the specific (table) walls defined by some intermediary
 * color.
 * 
 * 
 * ADDED : - multi floor : example : if you load image.png, the app will search
 * for image-2.png for the second floor, image-3.png for the third, etc. -
 * Multi-robot instances
 * 
 * @author nicolas (+ cedric)
 * 
 */
public class Tutorial_6b_Robot_EvaluatingRandomControllers extends EnvironmentDescription {

    public Point3d _goal = new Point3d();

    public ArrayList _startingPoints = new ArrayList();

    ArrayList<Robot> robots = new ArrayList<Robot>();

    public Tutorial_6b_Robot_EvaluatingRandomControllers() {
        Wall w1 = new Wall(new Vector3d(9, 0, 0), 19, 1, this);
        w1.rotate90(1);
        add(w1);
        Wall w2 = new Wall(new Vector3d(-9, 0, 0), 19, 2, this);
        w2.rotate90(1);
        add(w2);
        Wall w3 = new Wall(new Vector3d(0, 0, 9), 19, 1, this);
        add(w3);
        Wall w4 = new Wall(new Vector3d(0, 0, -9), 19, 2, this);
        add(w4);
        Robot robot = new Robot(new Vector3d(0, 0, 0), "MapRobot");
        this.robots.add(robot);
        add(robot);
    }

    /**
	 * initialize the environment due to the file __filename. TODO : - create
	 * multi-objectives points - create balls or non static items.
	 * 
	 * @param __filename
	 */
    public Tutorial_6b_Robot_EvaluatingRandomControllers(String __filename) {
        int[] values;
        SimpleImage simpleImage = new SimpleImage(__filename, false);
        simpleImage.displayInformation();
        for (int y = 0; y != simpleImage.getHeight(); y++) {
            System.out.print(" ");
            for (int x = 0; x != simpleImage.getWidth(); x++) {
                values = simpleImage.getPixel(x, y);
                if (values[1] > 200 && values[2] < 50 && values[3] < 50) {
                    add(new Wall(new Vector3d(x - (simpleImage.getWidth() / 2), 0, y - (simpleImage.getHeight() / 2)), 1, 1, 1, this));
                    System.out.print("#");
                } else if (values[1] < 50 && values[2] > 200 && values[3] < 50) {
                    _goal.x = x;
                    _goal.z = y;
                    _goal.y = 0;
                    System.out.print("X");
                } else if (values[1] < 50 && values[2] < 50 && values[3] > 200) {
                    _startingPoints.add(new Point3d(x, 0, y));
                    System.out.print("!");
                } else if (values[1] < 100 && values[1] == values[2] && values[2] == values[3]) {
                    showAxis(false);
                    setUsePhysics(true);
                    add(new BallAgent(new Vector3d(x - (simpleImage.getWidth() / 2), 0, y - (simpleImage.getHeight() / 2)), "ball", new Color3f(200, 0, 0), 0.25f, 0.25f));
                } else System.out.print(" ");
            }
            System.out.print("\n");
        }
        String secondFloor = __filename;
        boolean hasNextFloor = true;
        int cpt = 2;
        while (hasNextFloor) {
            try {
                if (__filename.endsWith(".png")) secondFloor = __filename.replaceAll(".png", "-" + cpt + ".png");
                if (__filename.endsWith(".gif")) secondFloor = __filename.replaceAll(".gif", "-" + cpt + ".gif");
                if (__filename.endsWith(".jpg")) secondFloor = __filename.replaceAll(".jpg", "-" + cpt + ".jpg");
                new FileReader(secondFloor);
                simpleImage = new SimpleImage(secondFloor, false);
                simpleImage.displayInformation();
                for (int y = 0; y != simpleImage.getHeight(); y++) {
                    System.out.print(" ");
                    for (int x = 0; x != simpleImage.getWidth(); x++) {
                        values = simpleImage.getPixel(x, y);
                        if (values[1] > 200 && values[2] < 50 && values[3] < 50) {
                            add(new Wall(new Vector3d(x - (simpleImage.getWidth() / 2), cpt - 1, y - (simpleImage.getHeight() / 2)), 1, 1, 1, this));
                            System.out.print("#");
                        } else System.out.print(" ");
                    }
                    System.out.print("\n");
                }
                cpt++;
            } catch (FileNotFoundException fnfe) {
                if (cpt == 2) {
                    System.out.println("no second floor.");
                    System.out.println(" - to define a second floor, create a file called : " + secondFloor);
                    System.out.println("");
                }
                hasNextFloor = false;
            }
        }
        for (int i = 0; i < _startingPoints.size(); i++) {
            Robot robot = new Robot(new Vector3d(((Point3d) _startingPoints.get(i)).x - (simpleImage.getWidth() / 2), 0f, ((Point3d) _startingPoints.get(i)).z - (simpleImage.getHeight() / 2)), "robot n." + i, this);
            this.robots.add(robot);
            add(robot);
        }
    }

    public void resetRobots() {
        for (int i = 0; i != this.robots.size(); i++) {
            this.robots.get(i).moveToStartPosition();
            this.robots.get(i).setRunning(true);
        }
    }

    public Robot getRobot(int __index) {
        return this.robots.get(__index);
    }

    public class Robot extends Agent {

        EnvironmentDescription _environment;

        RangeSensorBelt sonars, bumpers;

        FeedForwardNeuralNetwork _ctl;

        boolean _running = true;

        public Robot(Vector3d position, String name) {
            super(position, name);
            sonars = RobotFactory.addSonarBeltSensor(this, 8);
            bumpers = RobotFactory.addBumperBeltSensor(this, 12);
        }

        public Robot(Vector3d position, String name, EnvironmentDescription env) {
            super(position, name);
            sonars = RobotFactory.addSonarBeltSensor(this, 8);
            bumpers = RobotFactory.addBumperBeltSensor(this, 12);
            this._environment = env;
        }

        /** Initialize Agent's Behavior */
        @Override
        public void initBehavior() {
        }

        /** set new controller */
        public void setController(FeedForwardNeuralNetwork __ctl) {
            if (__ctl == null) {
                System.err.println("[error] controller cannot be set");
                System.exit(-1);
            }
            this._ctl = __ctl;
        }

        /** Perform one step of Agent's Behavior */
        @Override
        public void performBehavior() {
            if (this._ctl == null) {
                System.err.println("[error] no controller");
                System.exit(-1);
            }
            if (bumpers.oneHasHit() || collisionDetected()) {
                setTranslationalVelocity(0.0);
                setRotationalVelocity(0);
                this._running = false;
            } else {
                double[] inputs = new double[4];
                inputs[0] = sonars.getFrontLeftQuadrantMeasurement();
                inputs[1] = sonars.getFrontRightQuadrantMeasurement();
                inputs[2] = sonars.getFrontQuadrantMeasurement();
                inputs[3] = sonars.getBackQuadrantMeasurement();
                this._ctl.step();
                setTranslationalVelocity(this._ctl.getOutputNeuronAt(0).getValue());
                setRotationalVelocity(this._ctl.getOutputNeuronAt(1).getValue());
            }
        }

        public void setRunning(boolean __flag) {
            this._running = __flag;
        }

        public boolean getRunning() {
            return this._running;
        }
    }

    public static void main(String[] args) {
        Tutorial_6b_Robot_EvaluatingRandomControllers environment = new Tutorial_6b_Robot_EvaluatingRandomControllers("piconode/tutorials/robotmap.png");
        Simbatch sim = new Simbatch(environment, true);
        FeedForwardNeuralNetwork ctl = MultiLayerPerceptronFactory.createPerceptron(4, 5, 2, false);
        double[] weights = new double[ctl.getNumberOfAllArcs()];
        environment.getRobot(0).setController(ctl);
        int k = 0;
        int maxEval = 1000;
        while (k < maxEval) {
            System.out.println("beginning of evaluation no." + (k + 1));
            sim.reset();
            environment.resetRobots();
            for (int i = 0; i != ctl.getNumberOfAllArcs(); i++) weights[i] = 2. * Math.random() - 1.;
            ctl.setAllArcsWeightValues(weights);
            int i = 0;
            while (i < 1000 && environment.getRobot(0).getRunning() == true) {
                sim.step();
                i++;
            }
            System.out.println("end of evaluation no." + (k + 1));
            k++;
        }
    }
}
