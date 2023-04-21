package net.sf.ictalive.wc3;

public class BuildHouse extends Action {

    private Unit unit;

    private Unit building;

    public BuildHouse(Unit unit, Unit building) {
        this.unit = unit;
        this.building = building;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Unit getBuilding() {
        return building;
    }

    public void setBuilding(Unit building) {
        this.building = building;
    }

    @Override
    public String toString() {
        return "Buil|" + unit.getId() + "|" + building.getId() + "|";
    }
}
