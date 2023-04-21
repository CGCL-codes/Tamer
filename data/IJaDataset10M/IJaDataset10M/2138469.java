package pathplanning.algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import planning.collision.CollidableCircular;
import planning.collision.CollisionTreeNode;
import planning.model.Configuration;
import planning.model.Entity;
import planning.model.ModelObject;
import planning.model.PreDefinedEntities;
import planning.model.RboTWorld;
import planning.model.Robot;
import simulation.shell.DefinedConfiguration;
import simulation.shell.Shell;
import util.DoubleArrayMath;

public class RboT {

    Random rand;

    class RboTRobot {

        public double h;

        public double xscale, yscale;

        public Robot robot;

        public List<ModelObject> neighbors;

        public RboTRobot(Robot robot, double hi) {
            h = hi;
            this.robot = robot;
            neighbors = new LinkedList<ModelObject>();
        }
    }

    private static final double V_MAX = 20;

    private final double b21 = (double) 1 / 5;

    private final double b31 = (double) 3 / 40;

    private final double b32 = (double) 9 / 40;

    private final double b41 = (double) 3 / 10;

    private final double b42 = (double) -9 / 10;

    private final double b43 = (double) 6 / 5;

    private final double b51 = (double) -11 / 4;

    private final double b52 = (double) 5 / 2;

    private final double b53 = (double) -70 / 27;

    private final double b54 = (double) 35 / 27;

    private final double b61 = (double) 1631 / 55296;

    private final double b62 = (double) 175 / 512;

    private final double b63 = (double) 575 / 13824;

    private final double b64 = (double) 44275 / 110592;

    private final double b65 = (double) 253 / 4096;

    private final double c1 = (double) 37 / 378;

    private final double c2 = (double) 0;

    private final double c3 = (double) 250 / 621;

    private final double c4 = (double) 125 / 594;

    private final double c5 = (double) 0;

    private final double c6 = (double) 512 / 1771;

    private final double dc1 = c1 - (double) 2825 / 27648;

    private final double dc2 = c2 - (double) 0;

    private final double dc3 = c3 - (double) 18575 / 48384;

    private final double dc4 = c4 - (double) 13525 / 55296;

    private final double dc5 = c5 - (double) 277 / 14336;

    private final double dc6 = c6 - (double) 1 / 4;

    private double k1x, k2x, k3x, k4x, k5x, k6x;

    private double k1y, k2y, k3y, k4y, k5y, k6y;

    private final double eps = .00005;

    private final double tiny = Math.pow(10, -30);

    private final double safety = .9;

    private final double econ = 1.89 * Math.pow(10, -4);

    private final double hi = .5;

    private final double pgrow = -0.2;

    private final double pshrink = -0.25;

    protected double finishThreshold;

    DoubleArrayMath dam = DoubleArrayMath.newDoubleArrayMath();

    GradientCalculator2 gradientCalculator;

    private RboTWorld world;

    private List<ModelObject> neighbors = null;

    private Map<ModelObject, Map<Integer, CollidableCircular>> leaves = new HashMap<ModelObject, Map<Integer, CollidableCircular>>();

    private Map<Integer, ModelObject> generatedObjects = new HashMap<Integer, ModelObject>();

    private Shell shell;

    /**
	 * Checking for finish of the algorithm..
	 */
    private boolean cont;

    private Map<Robot, double[]> avgPos0 = new HashMap<Robot, double[]>();

    private Map<Robot, double[]> avgPos1 = new HashMap<Robot, double[]>();

    private int DEPTH = 5;

    private int step = 0;

    /**
	 * TODO Centralized can be achieved by increasing these values to too high values!
	 */
    public static float distanceToEnterGroup = 300;

    public static float distanceToLeaveGroup = 600;

    List<RboTRobot> robots = new ArrayList<RboTRobot>();

    public RboT(Shell shell, RboTWorld world, boolean isBounded, double navigationK, double finishThreshold) {
        this.shell = shell;
        this.world = world;
        gradientCalculator = new GradientCalculator2(world, isBounded, navigationK);
        this.finishThreshold = finishThreshold;
        cont = true;
        initializeAvgPos();
        initialize(world);
    }

    private void initialize(RboTWorld world) {
        for (Robot robot : world.robots) {
            RboTRobot rbotRobot = new RboTRobot(robot, hi);
            robots.add(rbotRobot);
        }
        List<ModelObject> allObstacles = new ArrayList<ModelObject>();
        allObstacles.addAll(world.dynamicObstacles);
        allObstacles.addAll(world.obstacles);
        leaves = new HashMap<ModelObject, Map<Integer, CollidableCircular>>();
        generatedObjects = new HashMap<Integer, ModelObject>();
        neighbors = new LinkedList<ModelObject>(world.robots);
        for (ModelObject mo : allObstacles) {
            CollisionTreeNode top = mo.getEntity().getCollisionTreeHead();
            List<CollisionTreeNode> parents = new LinkedList<CollisionTreeNode>();
            parents.add(top);
            Map<CollisionTreeNode, double[]> leafPositions = new HashMap<CollisionTreeNode, double[]>();
            leafPositions.put(top, mo.getConfig().clone());
            Map<Integer, CollidableCircular> leafs = new HashMap<Integer, CollidableCircular>();
            leaves.put(mo, leafs);
            while (parents.size() > 0) {
                CollisionTreeNode parent = parents.get(0);
                parents.remove(0);
                List<CollisionTreeNode> children = parent.getChildren();
                if (children.size() == 0) {
                    double[] childPos = new double[3];
                    Entity<?> ent = world.preDefinedEntities.getEntity(PreDefinedEntities.ModelEntityId.CIRCULAR_ENTITY);
                    ModelObject nmo = new ModelObject(ent, new DefinedConfiguration("" + generatedObjects.size(), -1, childPos));
                    CollidableCircular circular = parent.getValue();
                    leafs.put(generatedObjects.size(), circular);
                    generatedObjects.put(generatedObjects.size(), nmo);
                    neighbors.add(nmo);
                } else {
                    parents.addAll(children);
                }
            }
        }
        rand = new Random(System.currentTimeMillis());
    }

    private void initializeAvgPos() {
        avgPos0 = new HashMap<Robot, double[]>();
        avgPos1 = new HashMap<Robot, double[]>();
        for (Robot robot : world.robots) {
            double[] config = robot.getConfig();
            avgPos0.put(robot, new double[] { config[0], config[1], config[2] });
            avgPos1.put(robot, new double[] { 0, 0, 0 });
        }
    }

    private void checkFinish() {
        boolean moreToTargets = false;
        cont = false;
        for (Robot robot : world.robots) {
            double[] pos0 = avgPos0.get(robot);
            double[] pos1 = avgPos1.get(robot);
            double diff = dam.euclidianDistance(pos0, pos1);
            if (diff > finishThreshold) {
                cont = true;
            }
            double[] config = robot.getConfig();
            double[] target = robot.getTarget().configuration();
            double distance = Math.sqrt((config[0] - target[0]) * (config[0] - target[0]) + (config[1] - target[1]) * (config[1] - target[1]));
            if (distance > 3.0) {
                moreToTargets = true;
            }
        }
        if (!moreToTargets) {
            cont = false;
        }
        avgPos0 = avgPos1;
        avgPos1 = new HashMap<Robot, double[]>();
        for (Robot robot : world.robots) {
            avgPos1.put(robot, new double[] { 0, 0, 0 });
        }
    }

    public void next() {
        for (Map.Entry<ModelObject, Map<Integer, CollidableCircular>> children : leaves.entrySet()) {
            ModelObject mo = children.getKey();
            double[] topConfig = mo.getConfig();
            double scale = mo.getScaling();
            double topAngle = topConfig[2];
            double cos_topAngle = Math.cos(topAngle);
            double sin_topAngle = Math.sin(topAngle);
            for (Map.Entry<Integer, CollidableCircular> child : children.getValue().entrySet()) {
                Integer id = child.getKey();
                CollidableCircular circular = child.getValue();
                ModelObject generated = generatedObjects.get(id);
                double[] config = generated.getDefinedConfig().configuration();
                double x = circular.getX();
                double y = circular.getY();
                config[0] = topConfig[0] + (x * cos_topAngle - y * sin_topAngle) * scale;
                config[1] = topConfig[1] + (x * sin_topAngle + y * cos_topAngle) * scale;
                config[2] = topAngle;
            }
        }
        System.out.println("Neighbors count : " + neighbors.size());
        for (RboTRobot robot : robots) {
            double[] next = null;
            neighbors.remove(robot.robot);
            double[] robotConfig = robot.robot.getConfig();
            for (ModelObject neigh : neighbors) {
                double distance = dam.euclidianDistance(robotConfig, neigh.getConfig());
                distance -= neigh.getBoundingRadius() + robot.robot.getBoundingRadius();
                if (distance < RboT.distanceToEnterGroup) {
                    if (!robot.neighbors.contains(neigh)) robot.neighbors.add(neigh);
                }
                if (distance > RboT.distanceToLeaveGroup) {
                    robot.neighbors.remove(neigh);
                }
            }
            next = calculateControlInput(robot, robot.neighbors);
            robot.robot.getDriver().setControlInputs(next);
            neighbors.add(robot.robot);
            double[] average = avgPos1.get(robot.robot);
            double[] current = robot.robot.getConfig();
            average[0] += current[0] / DEPTH;
            average[1] += current[1] / DEPTH;
            average[2] += current[2] / DEPTH;
        }
        if (step >= DEPTH) {
            checkFinish();
            step = 0;
        }
        step++;
    }

    public boolean continues() {
        return cont;
    }

    /**
	 * World is composed of controlled and non-controlled robots and obstacles.
	 * All are circular, and robots are unidirectional. So compose control inputs
	 * for the Robots, and escape from obstacles and other robots and go to
	 * the target.
	 */
    public double[] calculateControlInput(RboTRobot rbotRobot, List<? extends ModelObject> neighbors) {
        Robot robot = rbotRobot.robot;
        double h = rbotRobot.h;
        double[] currentLoc = dam.duplicate(robot.getConfig());
        applyRungeKutta(rbotRobot, neighbors);
        rbotRobot.xscale = Math.sqrt(gradientCalculator.gUtil.squaredDistanceToTarget(robot, new Configuration(robot.getTarget().configuration()))) + Math.abs(k1x * h * V_MAX) + tiny;
        rbotRobot.yscale = Math.sqrt(gradientCalculator.gUtil.squaredDistanceToTarget(robot, new Configuration(robot.getTarget().configuration()))) + Math.abs(k1y * h * V_MAX) + tiny;
        double[] gradient = adaptStepSize(rbotRobot, neighbors);
        double dx = gradient[0] - currentLoc[0];
        double dy = gradient[1] - currentLoc[1];
        double Vx = dx / shell.simulator.simulation_step_size;
        double Vy = dy / shell.simulator.simulation_step_size;
        double Vval = Math.sqrt(Vx * Vx + Vy * Vy);
        if (Vval > V_MAX) {
            double ratio = V_MAX / Vval;
            Vx *= ratio;
            Vy *= ratio;
        }
        double[] controlInput = new double[] { Vx, Vy, 0 };
        return controlInput;
    }

    public double[] adaptStepSize(RboTRobot rbotRobot, List<? extends ModelObject> neighbors) {
        Robot robot = rbotRobot.robot;
        double emax = 0, htemp;
        double xout = robot.getConfig()[0], yout = robot.getConfig()[1];
        Double temp = new Double(Double.NaN);
        for (int i = 0; i < 1000; i++) {
            double[] result = applyRungeKutta(rbotRobot, neighbors);
            emax = 0;
            xout = result[0];
            yout = result[1];
            emax = Math.max(Math.abs(result[2] / rbotRobot.xscale), Math.abs(result[3] / rbotRobot.yscale));
            emax /= eps;
            if (temp.equals(new Double(emax))) {
                xout = robot.getConfig()[0];
                yout = robot.getConfig()[1];
                break;
            }
            if (emax <= 1) break;
            htemp = safety * rbotRobot.h * Math.pow(emax, pshrink);
            rbotRobot.h = Math.max(Math.abs(htemp), .25 * Math.abs(rbotRobot.h));
            if (rbotRobot.h >= 0) rbotRobot.h = Math.max(htemp, .1 * rbotRobot.h); else rbotRobot.h = Math.min(htemp, .1 * rbotRobot.h);
        }
        if (emax > econ) rbotRobot.h = safety * Math.pow(emax, pgrow) * rbotRobot.h; else rbotRobot.h = 5 * rbotRobot.h;
        return new double[] { xout, yout };
    }

    public double[] applyRungeKutta(RboTRobot rbotRobot, List<? extends ModelObject> neighbors) {
        Robot robot = rbotRobot.robot;
        double h = rbotRobot.h;
        double[] oldConfig = dam.duplicate(robot.getConfig());
        double x = oldConfig[0];
        double y = oldConfig[1];
        robot.setConfiguration(new Configuration(oldConfig));
        double[] gradient = gradientCalculator.calculateControlInputs(robot, neighbors);
        k1x = gradient[0];
        k1y = gradient[1];
        robot.setConfiguration(new Configuration(new double[] { x + (b21 * k1x * h) * V_MAX, y + (b21 * k1y * h) * V_MAX, 0 }));
        gradient = gradientCalculator.calculateControlInputs(robot, neighbors);
        k2x = gradient[0];
        k2y = gradient[1];
        robot.setConfiguration(new Configuration(new double[] { x + (b31 * k1x * h + b32 * k2x * h) * V_MAX, y + (b31 * k1y * h + b32 * k2y * h) * V_MAX, 0 }));
        gradient = gradientCalculator.calculateControlInputs(robot, neighbors);
        k3x = gradient[0];
        k3y = gradient[1];
        robot.setConfiguration(new Configuration(new double[] { x + (b41 * k1x * h + b42 * k2x * h + b43 * k3x * h) * V_MAX, y + (b41 * k1y * h + b42 * k2y * h + b43 * k3y * h) * V_MAX, 0 }));
        gradient = gradientCalculator.calculateControlInputs(robot, neighbors);
        k4x = gradient[0];
        k4y = gradient[1];
        robot.setConfiguration(new Configuration(new double[] { x + (b51 * k1x * h + b52 * k2x * h + b53 * k3x * h + b54 * k4x * h) * V_MAX, y + (b51 * k1y * h + b52 * k2y * h + b53 * k3y * h + b54 * k4y * h) * V_MAX, 0 }));
        gradient = gradientCalculator.calculateControlInputs(robot, neighbors);
        k5x = gradient[0];
        k5y = gradient[1];
        robot.setConfiguration(new Configuration(new double[] { x + (b61 * k1x * h + b62 * k2x * h + b63 * k3x * h + b64 * k4x * h + b65 * k5x * h) * V_MAX, y + (b61 * k1y * h + b62 * k2y * h + b63 * k3y * h + b64 * k4y * h + b65 * k5y * h) * V_MAX, 0 }));
        gradient = gradientCalculator.calculateControlInputs(robot, neighbors);
        k6x = gradient[0];
        k6y = gradient[1];
        double dx = h * (c1 * k1x + c2 * k2x + c3 * k3x + c4 * k4x + c6 * k6x) * V_MAX;
        double dy = h * (c1 * k1y + c2 * k2y + c3 * k3y + c4 * k4y + c6 * k6y) * V_MAX;
        robot.setConfiguration(new Configuration(new double[] { x, y, 0 }));
        double xout = x + dx;
        double yout = y + dy;
        double xerror = h * (dc1 * k1x + dc2 * k2x + dc3 * k3x + dc4 * k4x + dc5 * k5x + dc6 * k6x) * V_MAX;
        double yerror = h * (dc1 * k1y + dc2 * k2y + dc3 * k3y + dc4 * k4y + dc5 * k5y + dc6 * k6y) * V_MAX;
        return new double[] { xout, yout, xerror, yerror };
    }
}
