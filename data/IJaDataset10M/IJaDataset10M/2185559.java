package actions;

import interfaces.ISensor;
import interfaces.Vector3d;

public class AgentActionRegulateSteering extends AgentAction {

    private double angle;

    public AgentActionRegulateSteering() {
        super("Action:RegulateSteering", new AgentActionFrameSteering());
    }

    public AgentActionRegulateSteering(double angle) {
        super("Action:RegulateSteering", new AgentActionFrameSteering());
        this.angle = angle;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void finish(ISensor world) {
        Vector3d oldDirection = world.getDirection();
        Vector3d newDirection = new Vector3d();
        double radians = (Math.PI / 180.0) * this.angle;
        newDirection.X = Math.cos(radians) * oldDirection.X - Math.sin(radians) * oldDirection.Z;
        newDirection.Y = 0.0;
        newDirection.Z = Math.sin(radians) * oldDirection.X + Math.cos(radians) * oldDirection.Z;
        world.setDirection(newDirection);
    }
}
