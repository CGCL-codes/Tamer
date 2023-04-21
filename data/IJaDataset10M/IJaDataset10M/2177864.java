package net.sourceforge.mpango.entity;

import java.util.List;

public class Player {

    private Position position;

    private List<Unit> units;

    public Position getPosition() {
        return this.position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public List<Unit> getUnits() {
        return this.units;
    }

    public void setUnits(List<Unit> units) {
        this.units = units;
    }
}
