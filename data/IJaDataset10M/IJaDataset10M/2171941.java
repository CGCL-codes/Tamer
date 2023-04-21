package pathplanning.plan.actions;

import java.util.List;
import planning.model.CollisionWorld;
import planning.model.IConfiguration;
import planning.model.Robot;
import planning.model.RobotConfiguration;
import util.DoubleArrayMath;

/**
 * Coordinates robots to make them simultaneously reach to their next configurations in the path.
 * Does not make collision checks etc. A simplistic action..
 * 
 * Calculates the min time possible for the robots to reach to their targets simultaneously. Then 
 * uses this min time to calculate the velocities to be used.
 * 
 * @author phoad
 */
public class RobotsGoto implements IAction {

    private List<IConfiguration> targets;

    private CollisionWorld world;

    public double MAX_VELOCITY = 10;

    protected double simulationStepSize = 0.64;

    private DoubleArrayMath dam = DoubleArrayMath.newDoubleArrayMath();

    protected VelocityFollow velocityFollow;

    public RobotsGoto(CollisionWorld world) {
        this.world = world;
        velocityFollow = new VelocityFollow(world);
    }

    public void setSimulationStepSize(double simulationStepSize) {
        this.simulationStepSize = simulationStepSize;
    }

    public void setTarget(RobotConfiguration nextConfig) {
        targets = nextConfig.getConfigurations();
    }

    public boolean continues() {
        if (!velocityFollow.continues()) {
            velocityFollow.result();
            return false;
        }
        return true;
    }

    public void reset() {
        targets = null;
        velocityFollow.reset();
    }

    public int result() {
        return 1;
    }

    public void step() {
        velocityFollow.step();
    }

    public boolean initialize() {
        if (targets == null) {
            return false;
        }
        double maxDistance = 0;
        int i = 0;
        for (Robot robot : world.robots) {
            double[] target = targets.get(i).raw();
            double[] config = robot.getConfig();
            double distance = dam.euclidianDistance(target, config);
            if (maxDistance < distance) {
                maxDistance = distance;
            }
            i++;
        }
        int maxTime = (int) Math.ceil(maxDistance / (MAX_VELOCITY * simulationStepSize));
        velocityFollow.setInterval(maxTime);
        i = 0;
        for (Robot robot : world.robots) {
            double[] target = targets.get(i).raw();
            double[] config = robot.getConfig();
            double distance = dam.euclidianDistance(target, config);
            double speed = 0;
            if (distance > 0) speed = distance / ((double) maxTime * simulationStepSize);
            double[] velocity = dam.duplicate(target);
            dam.sub(velocity, config);
            dam.unitize(velocity);
            dam.mult(velocity, speed);
            velocityFollow.setVelocity(robot, velocity);
            i++;
        }
        return velocityFollow.initialize();
    }
}
