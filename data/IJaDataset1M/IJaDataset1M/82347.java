package frsf.cidisi.faia.examples.search.robot;

import frsf.cidisi.faia.agent.Action;
import frsf.cidisi.faia.agent.Agent;
import frsf.cidisi.faia.agent.Perception;
import frsf.cidisi.faia.environment.Environment;

public class RobotEnvironment extends Environment {

    public RobotEnvironment() {
        this.environmentState = new RobotEnvironmentState();
    }

    @Override
    public Perception getPercept() {
        return null;
    }

    @Override
    public String toString() {
        return "";
    }
}
