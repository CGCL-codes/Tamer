package simulation.robot.sensors;

import java.util.ArrayList;
import simulation.Simulator;
import simulation.SimulatorObject;
import simulation.physicalobjects.PhysicalObject;
import simulation.robot.Robot;

public abstract class Sensor extends SimulatorObject {

    protected int id;

    protected Robot robot;

    public Sensor(Simulator simulator, int id, Robot robot) {
        super(simulator);
        this.id = id;
        this.robot = robot;
    }

    public abstract double getSensorReading(int sensorNumber);

    public void update(int time, ArrayList<PhysicalObject> teleported) {
    }

    ;

    public int getId() {
        return id;
    }
}
