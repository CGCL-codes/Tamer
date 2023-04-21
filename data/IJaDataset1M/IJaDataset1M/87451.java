package dataModels;

import interfaces.Vector3d;
import World.Lane;

public class DataLane_World extends Data {

    private Double t;

    private Lane lane;

    private Vector3d agentPosition;

    public DataLane_World() {
        this.t = 0.0;
        this.lane = null;
        this.agentPosition = null;
    }

    public DataLane_World(Double t, Lane lane, Vector3d agentPosition) {
        this.t = t;
        this.lane = lane;
        this.agentPosition = agentPosition;
    }

    public Vector3d getAgentPosition() {
        return agentPosition;
    }

    public void setAgentPosition(Vector3d agentPosition) {
        this.agentPosition = agentPosition;
    }

    public Double getT() {
        return t;
    }

    public void setT(Double t) {
        this.t = t;
    }

    public Lane getLane() {
        return lane;
    }

    public void setLane(Lane lane) {
        this.lane = lane;
    }
}
