package simulation.robot.sensors;

import java.util.Arrays;
import simulation.Simulator;
import simulation.physicalobjects.GeometricInfo;
import simulation.physicalobjects.PhysicalObjectDistance;
import simulation.physicalobjects.checkers.AllowedObjectsChecker;
import simulation.robot.Robot;

/**
 * 
 * @author Fernando Silva
 *
 */
public class NearResourceSensor extends ConeTypeSensor {

    private static final long serialVersionUID = 472580101506272731L;

    protected final double OPENING_ANGLE;

    protected final double R;

    public NearResourceSensor(Simulator simulator, int id, Robot robot, int numbersensors, double orientation, double range, AllowedObjectsChecker allowedObjectsChecker, double absoluteAngle) {
        super(simulator, id, robot, numbersensors, orientation, range, allowedObjectsChecker);
        this.OPENING_ANGLE = absoluteAngle / 2;
        R = (Math.PI / 2) / OPENING_ANGLE;
    }

    @Override
    protected double calculateContributionToSensor(int sensorNumber, PhysicalObjectDistance source) {
        GeometricInfo sensorInfo = getSensorGeometricInfo(sensorNumber, source);
        if ((sensorInfo.getDistance() < getRange())) {
            return (getRange() - sensorInfo.getDistance()) / getRange();
        }
        return 0;
    }

    public String toString() {
        for (int i = 0; i < numberOfSensors; i++) getSensorReading(i);
        return "NearResource [readings=" + Arrays.toString(readings) + "]";
    }
}
