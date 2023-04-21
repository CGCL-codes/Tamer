package model.states;

import model.utils.directions.DirectionHorizontal;
import model.planet.elements.Tunnel;
import model.planet.elements.Pooglin;
import model.planet.PlanetMap;
import model.*;

public class BuildingState extends State {

    private Position buildingFrom;

    public void setup(Pooglin pooglin) {
        Position pos = new Position(pooglin.getPosition().getX() + ((pooglin.getSize().getWidth() / 2)), pooglin.getPosition().getY() + pooglin.getSize().getHeight() - 1);
        DirectionHorizontal dir;
        dir = pooglin.getWalkingDirection().turn();
        Tunnel t = new Tunnel(pos, dir, 1);
        PlanetMap.getInstance().addTunnel(t);
    }

    public void process(Pooglin pooglin) {
        pooglin.addSkill(null);
        pooglin.walk();
    }

    public void dismiss(Pooglin pooglin) {
    }

    public String getCode() {
        return "building";
    }

    public boolean allowedNextState(int state) {
        switch(state) {
            case StateManager.TYPE_WALKING:
            case StateManager.TYPE_DYING:
                return true;
            default:
                return false;
        }
    }

    public String toString() {
        return "[BuildingState buildingFrom=" + buildingFrom + "]" + super.toString();
    }
}
